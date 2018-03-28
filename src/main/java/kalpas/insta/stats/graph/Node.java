package kalpas.insta.stats.graph;

import java.util.HashMap;
import java.util.Map;

public class Node {
    public final String id;
    public Map<String, String> properties = new HashMap<>();

    public Node(Map<String, String> properties) {
        this.id = properties.get("id");
        this.properties = properties;
    }

    public Node(String id) {
        this.id = id;
    }
}