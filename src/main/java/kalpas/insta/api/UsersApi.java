package kalpas.insta.api;

import java.net.URISyntaxException;

import kalpas.insta.api.domain.UserData;
import kalpas.insta.api.domain.UsersResponse;
import kalpas.insta.api.domain.base.InstagramApiException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UsersApi {

    protected final Log         logger = LogFactory.getLog(getClass());

    @Autowired
    private ApiBase             api;

    private static final String PATH   = "/users";

    public UserData get(String id, String access_token) {
        logger.info(String.format("getting info for id %s", id));
        UserData userData = null;
        try {
            UsersResponse apiResponse = api.executeRequest(buildRequestString(id, access_token), UsersResponse.class);
            if (apiResponse != null) {
                userData = apiResponse.data;
            } else {
                logger.error("null api response, check request above");
            }
        } catch (InstagramApiException e) {
            // FIXME some code here to handle APINotAllowedError
        }

        return userData;
    }

    private String buildRequestString(String id, String access_token) {
        URIBuilder builder = new URIBuilder();
        builder.setScheme("https").setHost(API.HOST).setPath("/" + API.VERSION + PATH + "/" + id + "/")
                .addParameter("access_token", access_token);
        String requestUri = null;
        try {
            requestUri = builder.build().toString();
        } catch (URISyntaxException e) {
            logger.error(e);
        }
        return requestUri;
    }
}
