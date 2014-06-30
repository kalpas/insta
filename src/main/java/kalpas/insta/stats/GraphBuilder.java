package kalpas.insta.stats;

import java.util.Set;

import kalpas.insta.api.Relationships;
import kalpas.insta.api.Users;
import kalpas.insta.api.domain.UserData;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

public class GraphBuilder {

    private final Log     logger        = LogFactory.getLog(getClass());

    private Users         users         = new Users();

    private Relationships relationships = new Relationships();

    public Multimap<UserData, UserData> buildGraph(UserData center, String access_token) {
        Set<UserData> followedBy = Sets.newHashSet(relationships.getFollowedBy(center.id, access_token));
        Set<UserData> follows = Sets.newHashSet(relationships.getFollows(center.id, access_token));

        Set<UserData> friends = Sets.union(followedBy, follows);

        Multimap<UserData, UserData> graph = ArrayListMultimap.create();

        graph.putAll(center, follows);

        for (UserData follower : followedBy) {
            graph.put(follower, center);// putting relations with user
        }

        for (UserData friend : friends) {

            // getting followers of the followers
            friend = users.get(friend.id.toString(), access_token);
            if (friend != null && (friend.counts.followed_by > 1000 || friend.counts.follows > 1000)) {
                logger.info(String.format("User has too many connections %s", friend.username));
                continue;
            } else if (friend == null) {
                logger.error("user returned is null");
                continue;
            }

            Set<UserData> set = Sets.newHashSet(relationships.getFollowedBy(friend.id, access_token));
            Set<UserData> intersection = Sets.intersection(set, friends);
            for (UserData user : intersection) {
                graph.put(user, friend);
            }

            set = Sets.newHashSet(relationships.getFollows(friend.id, access_token));
            intersection = Sets.intersection(set, friends);
            graph.putAll(friend, intersection);
        }

        return graph;

    }

}
