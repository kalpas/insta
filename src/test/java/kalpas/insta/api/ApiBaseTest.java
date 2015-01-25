package kalpas.insta.api;

import kalpas.insta.api.domain.UsersResponse;
import kalpas.insta.api.domain.base.APINotAllowedError;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class ApiBaseTest {

	@Autowired
	private ApiBase base;

	@Test
	public void test() throws APINotAllowedError {

		String requestString = "https://api.instagram.com/v1/users/374051522/?access_token=307563973.cdcfc3e.877e29e67e784f2b99c4dc599da51d7d";
		UsersResponse executeRequest = base.executeRequest(requestString, UsersResponse.class);

	}

}
