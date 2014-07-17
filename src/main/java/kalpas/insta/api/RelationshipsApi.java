package kalpas.insta.api;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import kalpas.insta.api.domain.RelationshipsResponse;
import kalpas.insta.api.domain.UserData;
import kalpas.insta.api.domain.base.InstagramApiException;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RelationshipsApi {

    protected final Log         logger = LogFactory.getLog(getClass());

    @Autowired
    private ApiBase             api;

    private static final String PATH   = "/users";

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
        long elapsed = System.nanoTime();

        List<UserData> list = new ArrayList<>();
        try {
            RelationshipsResponse apiResponse = api.executeRequest(buildRequestString(userId, access_token, METHOD),
                    RelationshipsResponse.class);
            do {
                if (apiResponse == null) {
                    logger.error("null api response, check request above");
                } else if (apiResponse.meta != null && API.CODE_SUCCESS.equals(apiResponse.meta.code)) {
                    Collections.addAll(list, apiResponse.data);

                    if (apiResponse.pagination != null && apiResponse.pagination.next_url != null) {
                        apiResponse = api.executeRequest(apiResponse.pagination.next_url, RelationshipsResponse.class);
                    } else {
                        apiResponse = null;
                    }
                }

            } while (apiResponse != null);
            logger.debug(String.format("all data received for user %s", userId.toString()));
        } catch (InstagramApiException e) {
            logger.error(e);
        }
        elapsed = System.nanoTime() - elapsed;
        logger.debug(String.format("%s method returned %d users for %s id (took %s)", METHOD, list.size(), userId,
                DurationFormatUtils.formatDurationHMS(elapsed)));
        return list;
    }

    private String buildRequestString(Long userId, String access_token, String METHOD) {
        URIBuilder builder = new URIBuilder();
        builder.setScheme("https").setHost(API.HOST)
                .setPath("/" + API.VERSION + PATH + "/" + userId.toString() + "/" + METHOD)
                .addParameter("access_token", access_token);
        String string = null;
        try {
            string = builder.build().toString();
        } catch (URISyntaxException e) {
            logger.error(e);
        }
        return string;
    }

}
