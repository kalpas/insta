package kalpas.insta.api;

import kalpas.insta.api.domain.UserData;

import org.junit.Test;

public class UsersTest {

    @Test
    public void test() {
        UsersApi usersApi = new UsersApi();
        UserData data = usersApi.get(0l, "");

    }

}
