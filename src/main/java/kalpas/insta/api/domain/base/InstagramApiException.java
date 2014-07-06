package kalpas.insta.api.domain.base;

public class InstagramApiException extends Exception {

    public InstagramApiException(String error_message) {
        super(error_message);
    }

    private static final long serialVersionUID = 1L;

}
