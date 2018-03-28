package kalpas.insta.controller;

import kalpas.insta.AppConsts;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/home")
public class BaseController {

    @RequestMapping(method = RequestMethod.GET)
    public String defaut(ModelMap model, HttpSession session) {

        String image = (String) session.getAttribute(AppConsts.IMAGE_ATTRIBUTE);
        String name = (String) session.getAttribute(AppConsts.NAME_ATTRIBUTE);
        String access_token = (String) session.getAttribute(AppConsts.ACCESS_TOKEN_ATTRIBUTE);
        String id = (String) session.getAttribute(AppConsts.ID_ATTRIBUTE);

        populateModel(model, image, name, access_token, id);
        return "home";
    }

    @RequestMapping(value = "/experiments", method = RequestMethod.GET)
    public String experiments(ModelMap model, HttpSession session) {

        String image = (String) session.getAttribute(AppConsts.IMAGE_ATTRIBUTE);
        String name = (String) session.getAttribute(AppConsts.NAME_ATTRIBUTE);
        String access_token = (String) session.getAttribute(AppConsts.ACCESS_TOKEN_ATTRIBUTE);
        String id = (String) session.getAttribute(AppConsts.ID_ATTRIBUTE);

        populateModel(model, image, name, access_token, id);
        return "experiments";
    }

    private void populateModel(ModelMap model, String image, String name, String access_token, String id) {
        model.addAttribute(AppConsts.IMAGE_ATTRIBUTE, image);
        model.addAttribute(AppConsts.NAME_ATTRIBUTE, name);
        model.addAttribute(AppConsts.ACCESS_TOKEN_ATTRIBUTE, access_token);
        model.addAttribute(AppConsts.ID_ATTRIBUTE, id);
    }

}
