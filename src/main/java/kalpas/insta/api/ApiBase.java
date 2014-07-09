package kalpas.insta.api;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import kalpas.insta.api.domain.base.APINotAllowedError;
import kalpas.insta.api.domain.base.ApiResponse;
import kalpas.insta.api.domain.base.Meta;
import kalpas.insta.api.domain.base.OAuthRateLimitException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ApiBase {

    private static final int    MAX_RETRIES = 3;

    private final Log           logger      = LogFactory.getLog(getClass());

    @Autowired
    private CloseableHttpClient httpClient;

    @Autowired
    private ObjectMapper        mapper;

    public <T extends ApiResponse> T executeRequest(String requestString, Class<T> cls) throws APINotAllowedError {
        String jsonResponse = executeHttpRequest(requestString);
        T apiResponse = null;

        if (jsonResponse == null) {
            return apiResponse;
        }

        try {
            apiResponse = parseJson(jsonResponse, cls);
        } catch (OAuthRateLimitException e) {
            try {
                logger.info("sleeping for 60 min");
                TimeUnit.MINUTES.sleep(60);
                return executeRequest(requestString, cls);
            } catch (InterruptedException e2) {
                logger.error("sleep interrupted: " + e2.toString());
            }
        }
        return apiResponse;
    }

    private <T extends ApiResponse> T parseJson(String entityString, Class<T> cls) throws OAuthRateLimitException,
            APINotAllowedError {
        T apiResponse = null;
        try {
            apiResponse = mapper.readValue(entityString, cls);
            if (!API.CODE_SUCCESS.equals(apiResponse.meta.code)) {
                handleApiError(apiResponse.meta);
            }
        } catch (JsonParseException | JsonMappingException e) {
            extractApiError(entityString);
        } catch (IOException e) {
            logger.equals(e);
        }
        return apiResponse;
    }

    private String executeHttpRequest(String requestString) {
        return executeHttpRequest(requestString, MAX_RETRIES);
    }

    // FIXME rewrite using response handler (ResponseHandler)
    private String executeHttpRequest(String requestString, int retries) {
        logger.debug(requestString);
        HttpGet request = new HttpGet(requestString);
        String entityString = null;
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();
            if (HttpStatus.SC_OK == statusCode) {
                entityString = IOUtils.toString(response.getEntity().getContent());
                logger.debug(entityString);
            } else if (HttpStatus.SC_SERVICE_UNAVAILABLE == statusCode) {
                // retry after 3s
                logger.info(String.format("service unavailable, waiting for 3s untill next retry (%d retries left)",
                        retries));
                TimeUnit.SECONDS.sleep(3);

                if (retries != 0) {
                    // making retries in recursion
                    return executeHttpRequest(requestString, retries - 1);
                }
            } else {
                logger.error(String.format("status code is not OK: %s", statusCode));
            }
        } catch (IllegalStateException | IOException e1) {
            logger.error(e1);
        } catch (InterruptedException e) {
            logger.error("sleep interrupted");
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    logger.error(e);
                }
            }
        }

        return entityString;
    }

    private void extractApiError(String entityString) throws OAuthRateLimitException, APINotAllowedError {
        Meta error = null;
        try {
            error = mapper.readValue(entityString, Meta.class);
            logger.error(String.format("API responded with an error %s (%d)", error.error_message, error.code));
            handleApiError(error);
        } catch (IOException e) {
            logger.error(e);
        }
    }

    private void handleApiError(Meta error) throws OAuthRateLimitException, APINotAllowedError {
        // TODO
        switch (error.code) {
        case 429:
            throw new OAuthRateLimitException(error.error_message);
        case 400:
            throw new APINotAllowedError(error.error_message);
        default:
            break;
        }
    }

}