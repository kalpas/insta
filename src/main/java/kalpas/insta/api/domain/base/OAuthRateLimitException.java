package kalpas.insta.api.domain.base;

public class OAuthRateLimitException extends InstagramApiException {

    public OAuthRateLimitException(String error_message) {
        super(error_message);
    }

    private static final long serialVersionUID = 1L;

}
