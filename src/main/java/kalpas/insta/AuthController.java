package kalpas.insta;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import kalpas.insta.api.API;
import kalpas.insta.api.Relationships;
import kalpas.insta.api.domain.base.AuthResponse;
import kalpas.insta.api.domain.base.ErrorResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/auth")
public class AuthController {
    protected final Log         logger        = LogFactory.getLog(getClass());

    @RequestMapping(method = RequestMethod.GET)
    public String getCode(@RequestParam(value = "code", required = false) String code, ModelMap model)
            throws URISyntaxException, ClientProtocolException, IOException {
        // model.addAttribute("code", code);

        URIBuilder builder = new URIBuilder();
        builder.setScheme("https").setHost(API.HOST).setPath(API.AUTH_PATH);

        CloseableHttpClient httpclient = HttpClients.createDefault();
        String URI = builder.build().toString();
        logger.info(URI);
        HttpPost post = new HttpPost(URI);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("client_id", API.CLIENT_ID));
        params.add(new BasicNameValuePair("client_secret", API.CLIENT_SECRET));
        params.add(new BasicNameValuePair("grant_type", "authorization_code"));
        params.add(new BasicNameValuePair("redirect_uri", API.REDIRECT_URI));
        params.add(new BasicNameValuePair("code", code));
        post.setEntity(new UrlEncodedFormEntity(params, Consts.UTF_8));
        post.addHeader("Content-Type", "application/x-www-form-urlencoded");
        CloseableHttpResponse response = httpclient.execute(post);

        String entityString;
        try {
            entityString = IOUtils.toString(response.getEntity().getContent());
            logger.info(entityString);
        } finally {
            response.close();
        }
        ObjectMapper mapper = new ObjectMapper();
        AuthResponse authResponse = null;
        ErrorResponse error = null;
        try {
            authResponse = mapper.readValue(entityString, AuthResponse.class);
            model.addAttribute("image", authResponse.user.profile_picture);
            model.addAttribute("name", authResponse.user.username);
        } catch (JsonParseException | JsonMappingException e) {
            error = mapper.readValue(entityString, ErrorResponse.class);
            logger.error(error);
            model.addAttribute("error", error.toString());
        }

        if (authResponse != null) {
            Relationships relationships = new Relationships();
            relationships.getFollows(authResponse.user.id, authResponse.access_token);
            relationships.getFollowedBy(authResponse.user.id, authResponse.access_token);
        }

        return "auth";
    }
}
