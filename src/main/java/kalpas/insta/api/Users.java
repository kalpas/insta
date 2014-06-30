package kalpas.insta.api;

import java.io.IOException;
import java.net.URISyntaxException;

import kalpas.insta.api.domain.UserData;
import kalpas.insta.api.domain.UsersResponse;

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

public class Users {

    private static final String PATH       = "/users";

    private final Log           logger     = LogFactory.getLog(getClass());

    private CloseableHttpClient httpClient = HttpClients.createDefault();

    private ObjectMapper        mapper     = new ObjectMapper();

    public UserData get(String id, String access_token) {
        UserData userData = null;
        URIBuilder builder = new URIBuilder();
        builder.setScheme("https").setHost(API.HOST).setPath("/" + API.VERSION + PATH + "/" + id + "/")
                .addParameter("access_token", access_token);
        try {
            HttpGet request = new HttpGet(builder.build().toString());
            UsersResponse apiResponse = executeRequest(request);
            if (apiResponse.meta != null && API.CODE_SUCCESS.equals(apiResponse.meta.code)) {
                userData = apiResponse.data;
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            logger.error(e);
        }

        return userData;
    }

    private UsersResponse executeRequest(HttpGet request) throws IOException, ClientProtocolException,
            JsonParseException, JsonMappingException {
        CloseableHttpResponse response = httpClient.execute(request);
        String entityString;
        try {
            entityString = IOUtils.toString(response.getEntity().getContent());
            logger.info(entityString);
        } finally {
            response.close();
        }
        UsersResponse apiResponse = mapper.readValue(entityString, UsersResponse.class);
        return apiResponse;
    }

}
