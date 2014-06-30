package kalpas.insta.api.domain;

public class UserData {

    public String username;
    public String first_name;
    public String full_name;
    public String profile_picture;
    public Long   id;
    public String last_name;
    public String bio;
    public String website;

    public Counts counts;

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return this.id.equals(obj);
    }

}
