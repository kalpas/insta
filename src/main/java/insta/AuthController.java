package insta;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @RequestMapping(method = RequestMethod.GET)
    public String getCode(String code, ModelMap model) {
        model.addAttribute("code", code);
        return "auth";
    }

}
