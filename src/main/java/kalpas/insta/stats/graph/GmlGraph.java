package kalpas.insta.stats.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import kalpas.insta.api.domain.UserData;

import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

public class GmlGraph {

	public List<Node> nodes = new ArrayList<>();
	public List<Edge> edges = new ArrayList<>();

	public static GmlGraph build(Multimap<UserData, UserData> data) {
		GmlGraph gmlGraph = new GmlGraph();
		Set<UserData> users = Sets.newHashSet(data.keySet());
		for (Entry<UserData, UserData> entry : data.entries()) {// FIXME
			users.add(entry.getValue());
		}
		for (UserData user : users) {
			gmlGraph.nodes.add(new Node(asMap(user)));
		}

		for (Entry<UserData, UserData> entry : data.entries()) {
			gmlGraph.edges.add(new Edge(entry.getKey().id.toString(), entry.getValue().id.toString()));
		}

		return gmlGraph;
	}

	private static Map<String, String> asMap(UserData user) {
		Map<String, String> result = new HashMap<>();
		result.put("username", user.username);
		result.put("first_name", user.first_name);
		result.put("full_name", user.full_name);
		result.put("id", user.id.toString());
		result.put("last_name", user.last_name);

		return result;
	}

}
