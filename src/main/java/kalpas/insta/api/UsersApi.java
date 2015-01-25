package kalpas.insta.api;

import java.net.URISyntaxException;

import kalpas.insta.api.domain.GetMediaResponse;
import kalpas.insta.api.domain.Media;
import kalpas.insta.api.domain.UserData;
import kalpas.insta.api.domain.UsersResponse;
import kalpas.insta.api.domain.UsersSearchResponse;
import kalpas.insta.api.domain.base.InstagramApiException;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UsersApi {

    protected final Log         logger = LogFactory.getLog(getClass());

    @Autowired
	private API                 API;

	@Autowired
    private ApiBase             api;

    private static final String PATH   = "/users";

    /**
     * populates give user object with data
     * 
     * @param user
     * @param access_token
     * @return
     */
    public UserData get(UserData user, String access_token) {
        UserData details = get(user.id, access_token);

        if (details == null) {
            logger.error(String.format("null reponse for GET %s user", user));
            return user;
        }

        if (user.username == null) {
            user.username = details.username;
        }
        if (user.first_name == null) {
            user.first_name = details.first_name;
        }
        if (user.full_name == null) {
            user.full_name = details.full_name;
        }
        if (user.profile_picture == null) {
            user.profile_picture = details.profile_picture;
        }
        if (user.id == null) {
            user.id = details.id;
        }
        if (user.last_name == null) {
            user.last_name = details.last_name;
        }
        if (user.bio == null) {
            user.bio = details.bio;
        }
        if (user.website == null) {
            user.website = details.website;
        }
        if (user.counts == null) {
            user.counts = details.counts;
        }

        return user;
    }

    public UserData get(Long id, String access_token) {
        logger.debug(String.format("getting info for id %s", id));

        long elapsed = System.nanoTime();
        UserData userData = null;
        try {
            UsersResponse apiResponse = api.executeRequest(buildRequestString(id.toString(), access_token),
                    UsersResponse.class);
            if (apiResponse != null) {
                userData = apiResponse.data;
            } else {
                logger.error("null api response, check request above");
            }
        } catch (InstagramApiException e) {
            // FIXME some code here to handle APINotAllowedError
        }
        elapsed = System.nanoTime() - elapsed;
        logger.debug(String.format("took %s for GET USER id=%d to complete",
                DurationFormatUtils.formatDurationHMS(elapsed), id));

        return userData;
    }

    public UserData[] search(String userName, int count, String accessToken) {
        UserData[] result = null;

        URIBuilder builder = new URIBuilder();
        builder.setScheme("https").setHost(API.HOST).setPath("/" + API.VERSION + PATH + "/search")
                .addParameter("q", userName).addParameter("count", String.valueOf(count))
                .addParameter("access_token", accessToken);
        String requestUri = null;
        try {
            requestUri = builder.build().toString();
        } catch (URISyntaxException e) {
            logger.error(e);
        }

        try {
            UsersSearchResponse apiResponse = api.executeRequest(requestUri, UsersSearchResponse.class);
            if (apiResponse != null) {
                result = apiResponse.data;
            } else {
                logger.error("null api response, check request above");
            }
        } catch (InstagramApiException e) {
            // FIXME some code here to handle APINotAllowedError
        }

        return result;

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

    public Media[] getMedia(Long id, String access_token) {
        Media[] result = null;

        URIBuilder builder = new URIBuilder();
        builder.setScheme("https").setHost(API.HOST).setPath("/" + API.VERSION + PATH + "/" + id + "/media/recent")
                .addParameter("access_token", access_token);
        String requestUri = null;
        try {
            requestUri = builder.build().toString();
        } catch (URISyntaxException e) {
            logger.error(e);
        }

        try {
            GetMediaResponse apiResponse = api.executeRequest(requestUri, GetMediaResponse.class);
            if (apiResponse != null) {
                result = apiResponse.data;
            } else {
                logger.error("null api response, check request above");
            }
        } catch (InstagramApiException e) {
            // FIXME some code here to handle APINotAllowedError
        }

        return result;
    }
}
