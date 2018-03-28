package kalpas.insta.controller;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kalpas.insta.AppConsts;
import kalpas.insta.api.API;
import kalpas.insta.api.domain.base.AuthResponse;
import kalpas.insta.api.domain.base.Meta;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/auth")
public class AuthController {

    protected final Log logger = LogFactory.getLog(getClass());
    @Autowired
    private API API;

    @RequestMapping(method = RequestMethod.GET, value = "/login")
    public String login(ModelMap model, HttpSession session) throws URISyntaxException, ClientProtocolException,
            IOException {

        model.addAttribute("insta_client_id", API.clientId);
        model.addAttribute("insta_redirect_uri", API.redirectUri);
        model.addAttribute("insta_response_type", API.authResponseType);

        return "login";

    }

    @RequestMapping(method = RequestMethod.GET, value = "/code")
    public String getCode(@RequestParam(value = "code", required = false) String code, ModelMap model,
                          HttpSession session) throws URISyntaxException, ClientProtocolException, IOException {

        boolean autorised = session.getAttribute(AppConsts.SESSION_AUTH_STATE) != null;
        if (autorised) {
            return "redirect:/home";
        }

        URIBuilder builder = new URIBuilder();
        builder.setScheme("https").setHost(API.HOST).setPath(API.AUTH_PATH);

        CloseableHttpClient httpclient = HttpClients.createDefault();
        String URI = builder.build().toString();
        logger.debug(URI);
        HttpPost post = new HttpPost(URI);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("client_id", API.clientId));
        params.add(new BasicNameValuePair("client_secret", API.clientSecret));
        params.add(new BasicNameValuePair("grant_type", "authorization_code"));
        params.add(new BasicNameValuePair("redirect_uri", API.redirectUri));
        params.add(new BasicNameValuePair("code", code));
        post.setEntity(new UrlEncodedFormEntity(params, Consts.UTF_8));
        post.addHeader("Content-Type", "application/x-www-form-urlencoded");
        CloseableHttpResponse response = httpclient.execute(post);

        String entityString;
        try {
            entityString = IOUtils.toString(response.getEntity().getContent());
            logger.debug(entityString);
        } finally {
            response.close();
        }
        ObjectMapper mapper = new ObjectMapper();
        AuthResponse authResponse = null;
        Meta error = null;
        try {
            authResponse = mapper.readValue(entityString, AuthResponse.class);
            String profile_picture = authResponse.user.profile_picture;
            String username = authResponse.user.username;
            String access_token = authResponse.access_token;
            Long id = authResponse.user.id;

            populateSession(session, profile_picture, username, access_token, id.toString());

            return "redirect:/home";
        } catch (JsonParseException | JsonMappingException e) {
            error = mapper.readValue(entityString, Meta.class);
            logger.error(error);
            model.addAttribute("error", error.toString());
            return "error";
        }

        // if (authResponse != null) {
        // RelationshipsApi relationships = new RelationshipsApi();
        // relationships.getFollows(authResponse.user.id,
        // authResponse.access_token);
        // relationships.getFollowedBy(authResponse.user.id,
        // authResponse.access_token);
        // }

    }

    private void populateSession(HttpSession session, String profile_picture, String username, String access_token,
                                 String id) {
        session.setAttribute(AppConsts.SESSION_AUTH_STATE, "success");
        session.setAttribute(AppConsts.IMAGE_ATTRIBUTE, profile_picture);
        session.setAttribute(AppConsts.NAME_ATTRIBUTE, username);
        session.setAttribute(AppConsts.ACCESS_TOKEN_ATTRIBUTE, access_token);
        session.setAttribute(AppConsts.ID_ATTRIBUTE, id);
    }
}
