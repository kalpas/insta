package kalpas.insta.stats;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import kalpas.insta.AppConsts;
import kalpas.insta.api.RelationshipsApi;
import kalpas.insta.api.UsersApi;
import kalpas.insta.api.domain.UserData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Sets;

@RestController
@RequestMapping(value = "/friends")
public class FriendsController {

	@Autowired
	private UsersApi         userApi;

	@Autowired
	private RelationshipsApi relationshipsApi;

	@RequestMapping(method = RequestMethod.POST)
	public Set<UserData> getMutual(HttpSession session, ModelMap map, @RequestParam Map<String, String> params) {
		String accessToken = (String) session.getAttribute(AppConsts.ACCESS_TOKEN_ATTRIBUTE);
		String user1name = params.get("firstUser");
		String user2name = params.get("secondUser");

		UserData user1, user2 = null;

		UserData[] found = userApi.search(user1name, 1, accessToken);
		user1 = found.length > 0 ? found[0] : null;
		found = userApi.search(user2name, 1, accessToken);
		user2 = found.length > 0 ? found[0] : null;

		List<UserData> user1followedBy = relationshipsApi.getFollowedBy(user1.id, accessToken);
		List<UserData> user1follows = relationshipsApi.getFollows(user1.id, accessToken);

		List<UserData> user2followedby = relationshipsApi.getFollowedBy(user2.id, accessToken);
		List<UserData> user2follows = relationshipsApi.getFollows(user2.id, accessToken);

		Set<UserData> user1Connections = new HashSet<>(user1followedBy);
		user1Connections.addAll(user1follows);

		Set<UserData> user2connections = new HashSet<>(user2followedby);
		user2connections.addAll(user2follows);

		Set<UserData> intersection = Sets.intersection(user1Connections, user2connections);

		return intersection;
	}

}
