package kalpas.insta.persist.domain;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multiset;
import kalpas.insta.api.domain.UserData;
import org.neo4j.graphdb.*;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class SimpleMultimapNeo4jFacade implements Multimap<UserData, UserData> {

    private final GraphDatabaseService dbService;
    private Multimap<UserData, UserData> graph = ArrayListMultimap.create();

    public SimpleMultimapNeo4jFacade(GraphDatabaseService graphDb) {
        this.dbService = graphDb;
    }

    public int size() {
        return graph.size();
    }

    public boolean isEmpty() {
        return graph.isEmpty();
    }

    public boolean containsKey(Object key) {
        return graph.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return graph.containsValue(value);
    }

    public boolean containsEntry(Object key, Object value) {
        return graph.containsEntry(key, value);
    }

    public boolean put(UserData key, UserData value) {
        try (Transaction tx = dbService.beginTx()) {
            createPair(key, value, tx);
            tx.success();
        }

        return graph.put(key, value);
    }

    public boolean remove(Object key, Object value) {
        throw new UnsupportedOperationException("not implemented yet");
        // return graph.remove(key, value);
    }

    public boolean putAll(UserData key, Iterable<? extends UserData> values) {
        try (Transaction tx = dbService.beginTx()) {
            for (UserData entry : values) {
                createPair(key, entry, tx);
            }
            tx.success();
        }

        return graph.putAll(key, values);
    }

    public boolean putAll(Multimap<? extends UserData, ? extends UserData> multimap) {
        throw new UnsupportedOperationException("not implemented yet");
        // return graph.putAll(multimap);
    }

    public Collection<UserData> replaceValues(UserData key, Iterable<? extends UserData> values) {
        throw new UnsupportedOperationException("not implemented yet");
        // return graph.replaceValues(key, values);
    }

    public Collection<UserData> removeAll(Object key) {
        throw new UnsupportedOperationException("not implemented yet");
        // return graph.removeAll(key);
    }

    public void clear() {
        throw new UnsupportedOperationException("not implemented yet");
        // graph.clear();
    }

    public Collection<UserData> get(UserData key) {
        return graph.get(key);
    }

    public Set<UserData> keySet() {
        return graph.keySet();
    }

    public Multiset<UserData> keys() {
        return graph.keys();
    }

    public Collection<UserData> values() {
        return graph.values();
    }

    public Collection<Entry<UserData, UserData>> entries() {
        return graph.entries();
    }

    public Map<UserData, Collection<UserData>> asMap() {
        return graph.asMap();
    }

    public boolean equals(Object obj) {
        return graph.equals(obj);
    }

    public int hashCode() {
        return graph.hashCode();
    }

    private Node createUserNode(UserData user, Transaction tx) {
        Label label = DynamicLabel.label("User");
        Node userNode = dbService.createNode(label);

        userNode.setProperty("username", user.username);

        userNode.setProperty("first_name", user.first_name == null ? "null" : user.first_name);
        userNode.setProperty("full_name", user.full_name == null ? "null" : user.full_name);
        userNode.setProperty("profile_picture", user.profile_picture == null ? "null" : user.profile_picture);
        userNode.setProperty("id", user.id == null ? "null" : user.id);
        userNode.setProperty("last_name", user.last_name == null ? "null" : user.last_name);
        userNode.setProperty("bio", user.bio == null ? "null" : user.bio);
        userNode.setProperty("website", user.website == null ? "null" : user.website);

        return userNode;
    }

    private void createPair(UserData key, UserData value, Transaction tx) {
        Node from = createUserNode(key, tx);
        Node to = createUserNode(value, tx);
        from.createRelationshipTo(to, RelationshipsType.FOLLOWS);
    }

}
