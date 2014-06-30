package kalpas.insta.stats;

import java.util.Set;

import kalpas.insta.api.Relationships;
import kalpas.insta.api.domain.UserData;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

public class GraphBuilder {

    private Relationships relationships = new Relationships();

    public Multimap<UserData, UserData> buildGraph(UserData center, String access_token) {
        Set<UserData> followedBy = Sets.newHashSet(relationships.getFollowedBy(center.id, access_token));
        Set<UserData> follows = Sets.newHashSet(relationships.getFollows(center.id, access_token));

        Set<UserData> friends = Sets.union(followedBy, follows);

        Multimap<UserData, UserData> graph = ArrayListMultimap.create();
        graph.keySet().addAll(friends);

        graph.putAll(center, follows);

        for (UserData follower : followedBy) {
            graph.put(follower, center);// putting relations with user
        }

        for (UserData friend : friends) {

            // getting followers of the followers
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
