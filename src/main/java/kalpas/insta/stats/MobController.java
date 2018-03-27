package kalpas.insta.stats;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpSession;

import kalpas.insta.AppConsts;
import kalpas.insta.api.UsersApi;
import kalpas.insta.api.domain.Comment;
import kalpas.insta.api.domain.Media;
import kalpas.insta.api.domain.UserData;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;

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

		if (start == null) {
			model.addAttribute("error", "invalid user profile passed");
			return "error";
		}

		Media[] media = usersApi.getMedia(start.id, accessToken);

		Media startingPost = null;

		for (Media post : media) {
			String captionText = post.caption.text;
			if (!Strings.isNullOrEmpty(captionText) && captionText.contains("5") && captionText.contains("TODO")) {
				startingPost = post;
				break;
			} else {
				for (Comment comment : post.comments.data) {
					String commentText = comment.text;
					if (!Strings.isNullOrEmpty(commentText) && commentText.contains("5") && commentText.contains("TODO")) {
						startingPost = post;
						break;
					}
				}
				if (startingPost != null) {
					break;
				}
			}
		}

		if (startingPost == null) {
			model.addAttribute("error", "no post with 5 ��� found");
			return "error";
		}

		String caption = startingPost.caption.text;
		System.out.println(caption);

		Iterable<String> parts = Splitter.on(" ").trimResults().split(caption);
		Iterator<String> iterator = parts.iterator();
		String word = null;
		List<String> mentions = new ArrayList<>();
		while (iterator.hasNext()) {
			word = iterator.next();
			if (!Strings.isNullOrEmpty(word) && word.startsWith("@")) {
				mentions.add(word.substring(1));
			}
		}
		// TODO it is undirected graph

		for (String mention : mentions) {
			System.out.println(mention);
		}

		return "mob";
	}
}
