package kalpas.insta.api.domain.base;

public class Meta {
    public Integer code;
    public String  error_type;
    public String  error_message;

    @Override
    public String toString() {
        return code + " " + error_type + ": " + error_message;
    }
}