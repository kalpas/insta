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

    private final Log        logger = LogFactory.getLog(getClass());

    @Autowired
    private UsersApi         usersApi;

    @Autowired
    private RelationshipsApi relationshipsApi;

    public Multimap<UserData, UserData> buildGraph(UserData center, String access_token) {

        // TODO change it to HashMultimap when things will be all right
        Multimap<UserData, UserData> graph = ArrayListMultimap.create();

        Set<UserData> group = wireFirstLayer(access_token, center, graph);

        // putting connection backwards and sideways
        wireLastLayer(access_token, group, graph);

        return graph;

    }

    public Multimap<UserData, UserData> buildGraphLevel2(UserData center, String access_token) {

        Multimap<UserData, UserData> graph = ArrayListMultimap.create();

        // building the first level of star
        Set<UserData> layer_1 = wireFirstLayer(access_token, center, graph);

        Set<UserData> layer_2 = wireMiddleLayer(access_token, layer_1, graph);

        wireLastLayer(access_token, layer_2, graph);

        return graph;

    }

    private Set<UserData> wireFirstLayer(String access_token, UserData center, Multimap<UserData, UserData> graph) {
        Set<UserData> followedBy = Sets.newHashSet(relationshipsApi.getFollowedBy(center.id, access_token));
        Set<UserData> follows = Sets.newHashSet(relationshipsApi.getFollows(center.id, access_token));

        graph.putAll(center, follows);
        for (UserData follower : followedBy) {
            graph.put(follower, center);// putting relations with center
        }
        Set<UserData> level_1 = Sets.union(followedBy, follows);

        return level_1;
    }

    private Set<UserData> wireMiddleLayer(String access_token, Set<UserData> group, Multimap<UserData, UserData> graph) {
        Set<UserData> nextLayer = new HashSet<>();

        int total = group.size();
        int progress = 0;

        for (UserData friend : group) {

            friend = usersApi.get(friend.id.toString(), access_token);

            if (friend == null) {
                logger.error("user returned is null");
                continue;
            }

            Set<UserData> set = Sets.newHashSet(relationshipsApi.getFollows(friend.id, access_token));
            nextLayer.addAll(Sets.difference(set, group));
            graph.putAll(friend, set);

            set = Sets.newHashSet(relationshipsApi.getFollowedBy(friend.id, access_token));
            set = Sets.difference(set, group);
            nextLayer.addAll(set);
            for (UserData user : set) {
                graph.put(user, friend);
            }

            progress++;
            logger.info(String.format("got %.2f%% of connections (%d/%d)", progress * 100. / total, progress, total));
        }
        return nextLayer;
    }

    private void wireLastLayer(String access_token, Set<UserData> group, Multimap<UserData, UserData> graph) {

        int total = group.size();
        int progress = 0;

        for (UserData friend : group) {

            logger.info(String.format("getting info for %s user", friend.username));
            friend = usersApi.get(friend.id.toString(), access_token);

            if (friend == null) {
                logger.error("user returned is null");
                continue;
            }

            Set<UserData> set = Sets.newHashSet(relationshipsApi.getFollows(friend.id, access_token));
            // excluding any connections outside of the group
            set = Sets.intersection(set, group);
            graph.putAll(friend, set);

            progress++;
            logger.info(String.format("got %.2f%% of connections (%d/%d)", progress * 100. / total, progress, total));
        }
    }

}
