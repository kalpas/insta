package kalpas.insta.api.domain.base;

public class APINotAllowedError extends InstagramApiException {

    public APINotAllowedError(String error_message) {
        super(error_message);
    }

    private static final long serialVersionUID = 1L;


}
