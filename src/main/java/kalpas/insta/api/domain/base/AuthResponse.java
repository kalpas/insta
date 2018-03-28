package kalpas.insta.api.domain.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import kalpas.insta.api.domain.UserData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthResponse {

    public String access_token;
    public UserData user;

}
