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
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UserData other = (UserData) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }


}
