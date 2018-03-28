package kalpas.insta.controller;

import com.google.common.collect.Sets;
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

import javax.servlet.http.HttpSession;
import java.util.*;

@RestController
@RequestMapping(value = "/friends")
public class FriendsController {

    @Autowired
    private UsersApi userApi;

    @Autowired
    private RelationshipsApi relationshipsApi;

    @RequestMapping(method = RequestMethod.POST, value = "/user")
    public Collection<UserDataWrapper> getConnections(HttpSession session, @RequestParam("name") String name)
            throws Exception {
        String accessToken = (String) session.getAttribute(AppConsts.ACCESS_TOKEN_ATTRIBUTE);
        if (accessToken == null) {
            return null;
        }

        Set<UserDataWrapper> result = new HashSet<>();

        UserData[] searchResult = userApi.search(name, 1, accessToken);
        if (searchResult.length == 1) {
            UserData user = searchResult[0];
            List<UserData> followedBy = relationshipsApi.getFollowedBy(user.id, accessToken);
            List<UserData> follows = relationshipsApi.getFollows(user.id, accessToken);

            Set<UserData> connections = new HashSet<>(followedBy);
            connections.addAll(follows);

            for (UserData connection : connections) {
                result.add(new UserDataWrapper(connection, new Boolean[]{follows.contains(connection),
                        followedBy.contains(connection)}));
            }

        }

        return result;

    }

    @RequestMapping(method = RequestMethod.POST, value = "/mutual")
    public Collection<UserDataWrapper> getMutual(HttpSession session, ModelMap map,
                                                 @RequestParam Map<String, String> params) throws Exception {
        String accessToken = (String) session.getAttribute(AppConsts.ACCESS_TOKEN_ATTRIBUTE);
        if (accessToken == null) {
            return null;
        }
        String user1name = params.get("firstUser");
        if (user1name.contains("http")) {
            String[] parts = user1name.split("/");
            user1name = parts[parts.length - 1];
        }

        String user2name = params.get("secondUser");
        if (user2name.contains("http")) {
            String[] parts = user2name.split("/");
            user2name = parts[parts.length - 1];
        }

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

        return result;
    }

    public class UserDataWrapper {
        public UserData user;
        public Boolean[] flags;

        public UserDataWrapper(UserData user, Boolean[] flags) {
            this.user = user;
            this.flags = flags;
        }

        public UserDataWrapper() {
        }
    }

}
