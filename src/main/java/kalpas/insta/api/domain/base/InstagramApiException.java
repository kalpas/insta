package kalpas.insta.api.domain.base;

public class InstagramApiException extends Exception {

    private static final long serialVersionUID = 1L;

    public InstagramApiException(String error_message) {
        super(error_message);
    }

}
