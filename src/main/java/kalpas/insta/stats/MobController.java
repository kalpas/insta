package kalpas.insta.stats;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpSession;

import kalpas.insta.AppConsts;
import kalpas.insta.api.UsersApi;
import kalpas.insta.api.domain.UserData;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/mob")
public class MobController {

    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private UsersApi  usersApi;

    @RequestMapping(method = RequestMethod.GET)
    public String build(@RequestParam(value = "target_id", required = true) String target_id, ModelMap model,
            HttpSession session) {
        String accessToken = (String) session.getAttribute(AppConsts.ACCESS_TOKEN_ATTRIBUTE);
        UserData start = null;

        target_id.trim();
        if (target_id.startsWith("http") || target_id.startsWith("instagr")) {
            Pattern p = Pattern.compile("(.*)instagram.com/(.*)");
            Matcher m = p.matcher(target_id);
            String id = null;
            if (m.find()) {
                id = m.group(2);
            }
            start = usersApi.search(id, 1, accessToken)[0];
        } else {
            try {
                Long id = Long.parseLong(target_id);
                start = usersApi.get(id, accessToken);
            } catch (NumberFormatException ex) {
                model.addAttribute("error", ex.toString());
                return "error";
            }
        }

        if(start==null){
            model.addAttribute("error", "invalid user profile passed");
            return "error";
        }



        return "mob";
    }
}
