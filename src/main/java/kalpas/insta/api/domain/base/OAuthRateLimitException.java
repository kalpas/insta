package kalpas.insta.api.domain.base;

public class OAuthRateLimitException extends InstagramApiException {

    private static final long serialVersionUID = 1L;

    public OAuthRateLimitException(String error_message) {
        super(error_message);
    }

}
