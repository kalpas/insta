package kalpas.insta.api;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import kalpas.insta.api.domain.RelationshipsResponse;
import kalpas.insta.api.domain.UserData;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Relationships {

    private final Log           logger     = LogFactory.getLog(getClass());

    private static final String PATH       = "/users";

    private CloseableHttpClient httpClient = HttpClients.createDefault();

    private ObjectMapper        mapper     = new ObjectMapper();

    public List<UserData> getFollows(Long userId, String access_token) {
        String METHOD = "follows";
        return getUsers(userId, access_token, METHOD);
    }

    public List<UserData> getFollowedBy(Long userId, String access_token) {
        String METHOD = "followed-by";
        return getUsers(userId, access_token, METHOD);
    }

    public List<UserData> getRequestedBy(Long userId, String access_token) {
        String METHOD = "requested-by";
        return getUsers(userId, access_token, METHOD);
    }

    private List<UserData> getUsers(Long userId, String access_token, String METHOD) {
        List<UserData> follows = new ArrayList<>();
        URIBuilder builder = new URIBuilder();
        builder.setScheme("https").setHost(API.HOST)
                .setPath("/" + API.VERSION + PATH + "/" + userId.toString() + "/" + METHOD)
                .addParameter("access_token", access_token);
        try {
            HttpGet request = new HttpGet(builder.build().toString());
            RelationshipsResponse apiResponse = executeRequest(request);
            do {
                if (apiResponse.meta != null && API.CODE_SUCCESS.equals(apiResponse.meta.code)) {
                    Collections.addAll(follows, apiResponse.data);
                }

                if (apiResponse.pagination != null && apiResponse.pagination.next_url != null) {
                    request = new HttpGet(apiResponse.pagination.next_url);
                    apiResponse = executeRequest(request);
                } else {
                    apiResponse = null;
                }
            } while (apiResponse != null);
            logger.info("Success");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            logger.error(e);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            logger.error(e);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e);
        }
        logger.info("returned users: " + follows.size());
        return follows;
    }

    private RelationshipsResponse executeRequest(HttpGet request) throws IOException, ClientProtocolException,
            JsonParseException, JsonMappingException {
        CloseableHttpResponse response = httpClient.execute(request);
        String entityString;
        try {
            entityString = IOUtils.toString(response.getEntity().getContent());
            logger.info(entityString);
        } finally {
            response.close();
        }
        RelationshipsResponse apiResponse = mapper.readValue(entityString, RelationshipsResponse.class);
        return apiResponse;
    }

}
