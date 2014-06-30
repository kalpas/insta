package kalpas.insta.api;

import kalpas.insta.api.domain.UserData;

import org.junit.Test;

public class UsersTest {

    @Test
    public void test() {
        Users users = new Users();
        UserData data = users.get("", "");

    }

}
