package kalpas.insta.api;

public class API {

	public final String  AUTH_PATH                 = "/oauth/access_token/";
	public final String  HOST                      = "api.instagram.com";
	public final String  VERSION                   = "v1";
	public final Integer CODE_SUCCESS              = 200;
	public final Integer HTTP_SC_TOO_MANY_REQUESTS = 429;

	public String        authResponseType;
	public String        redirectUri;
	public String        clientSecret;
	public String        clientId;

	public void setAuthResponseType(String authResponseType) {
		this.authResponseType = authResponseType;
	}

	public void setRedirectUri(String resirectUri) {
		this.redirectUri = resirectUri;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
}
