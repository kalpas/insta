package kalpas.insta.stats.graph;

import java.util.Map;

public class Edge {
    public String              from;
    public String              to;
    public Map<String, String> properties;

    public Edge(String from, String to) {
        this.from = from;
        this.to = to;
    }

}
