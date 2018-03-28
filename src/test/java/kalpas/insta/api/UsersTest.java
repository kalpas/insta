package kalpas.insta.api;

import kalpas.insta.api.domain.Media;
import kalpas.insta.api.domain.UserData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class UsersTest {

    @Autowired
    UsersApi api;

    @Test
    public void test() {
        UsersApi usersApi = new UsersApi();
        UserData data = usersApi.get(0l, "");

    }

    @Test
    public void test2() {
        Media[] data = api.getMedia(3l, "307563973.cdcfc3e.877e29e67e784f2b99c4dc599da51d7d");
        System.out.println(data);

    }

}
