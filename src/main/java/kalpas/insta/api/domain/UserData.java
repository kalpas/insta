package kalpas.insta.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserData {

    public String username;
    public String first_name;
    public String full_name;
    public String profile_picture;
    public Long id;
    public String last_name;
    public String bio;
    public String website;
    public String type;           // "user",
    public Boolean is_business;

    public Counts counts;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserData userData = (UserData) o;
        return Objects.equals(username, userData.username) &&
                Objects.equals(first_name, userData.first_name) &&
                Objects.equals(full_name, userData.full_name) &&
                Objects.equals(profile_picture, userData.profile_picture) &&
                Objects.equals(id, userData.id) &&
                Objects.equals(last_name, userData.last_name) &&
                Objects.equals(bio, userData.bio) &&
                Objects.equals(website, userData.website) &&
                Objects.equals(type, userData.type) &&
                Objects.equals(is_business, userData.is_business) &&
                Objects.equals(counts, userData.counts);
    }

    @Override
    public int hashCode() {

        return Objects.hash(username, first_name, full_name, profile_picture, id, last_name, bio, website, type, is_business, counts);
    }

    @Override
    public String toString() {
        return String.format("%s(%d) [ < %d| > %d ]", username, id, counts == null ? -1 : counts.followed_by,
                counts == null ? -1 : counts.follows);

    }

}
