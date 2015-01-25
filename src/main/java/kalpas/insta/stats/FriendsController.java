package kalpas.insta.stats;

import java.util.ArrayList;
import java.util.Collection;
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
	public Collection<UserDataWrapper> getMutual(HttpSession session, ModelMap map,
	        @RequestParam Map<String, String> params) throws Exception {
		String accessToken = (String) session.getAttribute(AppConsts.ACCESS_TOKEN_ATTRIBUTE);
		if (accessToken == null) {
			return null;
		}
		String user1name = params.get("firstUser");
		String user2name = params.get("secondUser");

		UserData user1, user2 = null;

		UserData[] found = userApi.search(user1name, 1, accessToken);
		user1 = found.length > 0 ? found[0] : null;
		found = userApi.search(user2name, 1, accessToken);
		user2 = found.length > 0 ? found[0] : null;

		List<UserData> user1followedBy = relationshipsApi.getFollowedBy(user1.id, accessToken);
		List<UserData> user1follows = relationshipsApi.getFollows(user1.id, accessToken);

		List<UserData> user2followedBy = relationshipsApi.getFollowedBy(user2.id, accessToken);
		List<UserData> user2follows = relationshipsApi.getFollows(user2.id, accessToken);



		Set<UserData> user1Connections = new HashSet<>(user1followedBy);
		user1Connections.addAll(user1follows);

		Set<UserData> user2connections = new HashSet<>(user2followedBy);
		user2connections.addAll(user2follows);

		Set<UserData> intersection = Sets.intersection(user1Connections, user2connections);

		ArrayList<UserDataWrapper> result = new ArrayList<>();
		for (UserData entry : intersection) {

			Boolean[] flags = new Boolean[4];
			flags[0] = user1followedBy.contains(entry);
			flags[1] = user1follows.contains(entry);
			flags[2] = user2followedBy.contains(entry);
			flags[3] = user2follows.contains(entry);

			UserDataWrapper wrapper = new UserDataWrapper();
			wrapper.user = entry;
			wrapper.flags = flags;
			result.add(wrapper);
		}

		// Map<String, Collection<UserData>> result = new HashMap<>();
		// result.put("bothFollow", Sets.intersection(new
		// HashSet<>(user1follows), new HashSet<>(user2follows)));
		// result.put("bothFollowedBy", Sets.intersection(new
		// HashSet<>(user1followedBy), new HashSet<>(user2followedBy)));
		// result.put("1followsXfollows2", Sets.intersection(new
		// HashSet<>(user1follows), new HashSet<>(user2followedBy)));
		// result.put("2followsXfollows1", Sets.intersection(new
		// HashSet<>(user2follows), new HashSet<>(user1followedBy)));



		return result;
	}

	public class UserDataWrapper {
		public UserData  user;
		public Boolean[] flags;
	}

}
