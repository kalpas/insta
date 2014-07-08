package kalpas.insta.stats;

import java.util.HashSet;
import java.util.Set;

import kalpas.insta.api.RelationshipsApi;
import kalpas.insta.api.UsersApi;
import kalpas.insta.api.domain.UserData;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

@Component
public class GraphBuilder {

    private final Log        logger           = LogFactory.getLog(getClass());

    @Autowired
    private UsersApi         usersApi;

    @Autowired
    private RelationshipsApi relationshipsApi;

    public Multimap<UserData, UserData> buildGraph(UserData center, String access_token) {
        Set<UserData> followedBy = Sets.newHashSet(relationshipsApi.getFollowedBy(center.id, access_token));
        Set<UserData> follows = Sets.newHashSet(relationshipsApi.getFollows(center.id, access_token));

        Set<UserData> friends = Sets.union(followedBy, follows);

        Multimap<UserData, UserData> graph = ArrayListMultimap.create();

        graph.putAll(center, follows);

        for (UserData follower : followedBy) {
            graph.put(follower, center);// putting relations with user
        }

        for (UserData friend : friends) {

            friend = usersApi.get(friend.id.toString(), access_token);

            // check to avoid too much work with popular usersApi and null
            // usersApi
            if (friend != null && (friend.counts.followed_by > 1000 || friend.counts.follows > 1000)) {
                logger.info(String.format("User has too many connections %s", friend.username));
                continue;
            } else if (friend == null) {
                logger.error("user returned is null");
                continue;
            }

            Set<UserData> set = Sets.newHashSet(relationshipsApi.getFollowedBy(friend.id, access_token));
            Set<UserData> intersection = Sets.intersection(set, friends);
            for (UserData user : intersection) {
                graph.put(user, friend);
            }

            set = Sets.newHashSet(relationshipsApi.getFollows(friend.id, access_token));
            intersection = Sets.intersection(set, friends);
            graph.putAll(friend, intersection);
        }

        return graph;

    }

    public Multimap<UserData, UserData> buildGraphGrade2(UserData center, String access_token) {
        Set<UserData> followedBy = Sets.newHashSet(relationshipsApi.getFollowedBy(center.id, access_token));
        Set<UserData> follows = Sets.newHashSet(relationshipsApi.getFollows(center.id, access_token));

        Set<UserData> firstCircle = Sets.union(followedBy, follows);

        Multimap<UserData, UserData> graph = ArrayListMultimap.create();

        graph.putAll(center, follows);

        for (UserData follower : followedBy) {
            graph.put(follower, center);// putting relations with center
        }

        Set<UserData> secondCircle = new HashSet<>();

        int total = firstCircle.size();
        int progress = 0;

        for (UserData friend : firstCircle) {

            friend = usersApi.get(friend.id.toString(), access_token);

            // check to avoid too much work with popular usersApi and null
            // usersApi
            if (friend != null && (friend.counts.followed_by > 1000 || friend.counts.follows > 1000)) {
                logger.info(String.format("User has too many connections %s", friend.username));
                continue;
            } else if (friend == null) {
                logger.error("user returned is null");
                continue;
            }

            Set<UserData> set = Sets.newHashSet(relationshipsApi.getFollowedBy(friend.id, access_token));
            secondCircle.addAll(set);
            for (UserData user : set) {
                graph.put(user, friend);
            }

            set = Sets.newHashSet(relationshipsApi.getFollows(friend.id, access_token));
            secondCircle.addAll(set);
            graph.putAll(friend, set);

            progress++;
            logger.info(String.format("getting first circle %.2f%% (%d/%d)", progress * 100. / total, progress, total));
        }

        progress = 0;
        total = secondCircle.size();
        for (UserData friend : secondCircle) {

            friend = usersApi.get(friend.id.toString(), access_token);

            // check to avoid too much work with popular usersApi and null
            // usersApi
            if (friend != null && (friend.counts.followed_by > 1000 || friend.counts.follows > 1000)) {
                logger.info(String.format("User has too many connections %s", friend.username));
                continue;
            } else if (friend == null) {
                logger.error("user returned is null");
                continue;
            }

            Set<UserData> set = Sets.newHashSet(relationshipsApi.getFollowedBy(friend.id, access_token));
            Set<UserData> intersection = Sets.intersection(set, secondCircle);
            for (UserData user : intersection) {
                graph.put(user, friend);
            }

            set = Sets.newHashSet(relationshipsApi.getFollows(friend.id, access_token));
            intersection = Sets.intersection(set, secondCircle);
            graph.putAll(friend, intersection);

            progress++;
            logger.info(String.format("getting second circle %.2f%% (%d/%d)", progress * 100. / total, progress, total));
        }

        return graph;

    }

}
