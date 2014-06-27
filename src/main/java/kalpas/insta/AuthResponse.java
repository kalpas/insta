package kalpas.insta;

public class AuthResponse {

    public String access_token;
    public User   user;

    public class User {

        public Long   id;
        public String bio;
        public String website;
        public String username;
        public String full_name;
        public String profile_picture;

    }

}
