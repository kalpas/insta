package kalpas.insta.api;

import java.io.IOException;
import java.net.URISyntaxException;

import kalpas.insta.api.domain.RelationshipsResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Relationships {

    private final Log           logger     = LogFactory.getLog(getClass());

    private static final String PATH       = "/users";

    private CloseableHttpClient httpClient = HttpClients.createDefault();

    private ObjectMapper        mapper     = new ObjectMapper();

    public void getFollows(Long userId, String access_token) {
        URIBuilder builder = new URIBuilder();
        String METHOD = "follows";
        builder.setScheme("https").setHost(API.HOST)
                .setPath("/" + API.VERSION + PATH + "/" + userId.toString() + "/" + METHOD)
                .addParameter("access_token", access_token);
        try {
            HttpGet request = new HttpGet(builder.build().toString());
            CloseableHttpResponse response = httpClient.execute(request);
            String entityString;
            try {
                entityString = IOUtils.toString(response.getEntity().getContent());
                logger.info(entityString);
            } finally {
                response.close();
            }
            RelationshipsResponse apiResponse = mapper.readValue(entityString, RelationshipsResponse.class);
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


    }

}
