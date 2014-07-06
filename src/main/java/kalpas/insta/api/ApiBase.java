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
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ApiBase {

    protected final Log           logger     = LogFactory.getLog(getClass());

    protected CloseableHttpClient httpClient = HttpClients.createDefault();

    protected ObjectMapper        mapper     = new ObjectMapper();

    protected <T extends ApiResponse> T executeRequest(String requestString, Class<T> cls) throws APINotAllowedError {
        String jsonResponse = executeHttpRequest(requestString);
        T apiResponse = null;
        try {
            apiResponse = parseJson(jsonResponse, cls);
        } catch (OAuthRateLimitException e) {
            try {
                logger.info("sleeping for 60s");
                TimeUnit.MINUTES.sleep(60);
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
        HttpGet request = new HttpGet(requestString);
        String entityString = null;
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(request);
            try {
                entityString = IOUtils.toString(response.getEntity().getContent());
                logger.info(entityString);
            } finally {
                response.close();
            }
        } catch (IllegalStateException | IOException e1) {
            logger.error(e1);
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