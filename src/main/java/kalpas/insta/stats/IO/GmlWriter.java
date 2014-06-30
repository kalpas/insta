package kalpas.insta.stats.IO;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.Set;

import kalpas.insta.stats.graph.Edge;
import kalpas.insta.stats.graph.GmlGraph;
import kalpas.insta.stats.graph.Node;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GmlWriter {

    private static final String ID  = "id";
    private Log                 log = LogFactory.getLog(getClass());

    public void saveGraphToFile(String fileName, GmlGraph graph) {
        HashSet<String> hashSet = new HashSet<>();
        hashSet.add(ID);// FIXME
        hashSet.add("username");
        saveGraphToFile(fileName, graph, hashSet);
    }

    public void saveGraphToFile(String fileName, GmlGraph graph, Set<String> nodeProps) {
        BufferedWriter bw = null;
        try {
            OutputStreamWriter fileWriter = new OutputStreamWriter(new FileOutputStream(fileName + ".gml", false),
                    "UTF-8");
            bw = new BufferedWriter(fileWriter);
            save(bw, graph, checkProps(nodeProps));
            bw.flush();
            bw.close();
        } catch (IOException e) {
            log.error("IO exception", e);
        } finally {
            try {
                bw.close();
            } catch (Exception e) {
            }
        }
    }

    private Set<String> checkProps(Set<String> nodeProps) {
        if (nodeProps == null) {
            nodeProps = new HashSet<>();
        }
        nodeProps.add(ID);
        return nodeProps;
    }

    private void save(BufferedWriter bw, GmlGraph graph, Set<String> nodeProps) throws IOException {
        addHeading(bw);
        for (Node node : graph.nodes) {
            startNode(bw);
            for (String prop : nodeProps) {
                addProp(bw, node, prop);
            }
            endNode(bw);
        }
        for (Edge entry : graph.edges) {
            startEdge(bw);
            addSourceTarget(bw, entry.from, entry.to);
            endEdge(bw);
        }
        endFile(bw);

    }

    private void endFile(BufferedWriter bw) throws IOException {
        bw.write("]");

    }

    private void endEdge(BufferedWriter bw) throws IOException {
        bw.write("\t]");
        bw.newLine();

    }

    private void addSourceTarget(BufferedWriter bw, String source, String target) throws IOException {
        bw.write("\t\tsource " + source);
        bw.newLine();
        bw.write("\t\ttarget " + target);
        bw.newLine();
    }

    private void startEdge(BufferedWriter bw) throws IOException {
        bw.write("\tedge [");
        bw.newLine();
    }

    private void endNode(BufferedWriter bw) throws IOException {
        bw.write("\t]");
        bw.newLine();

    }

    private void startNode(BufferedWriter bw) throws IOException {
        bw.write("\tnode [");
        bw.newLine();

    }

    private void addProp(BufferedWriter bw, Node node, String prop) throws IOException {
        try {
            String value = node.properties.get(prop);
            bw.write("\t\t" + prop + " " + value);
            bw.newLine();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private void addHeading(BufferedWriter bw) throws IOException {
        bw.write("graph [");
        bw.newLine();
        bw.write("\tdirected 1");
        bw.newLine();
        bw.write("\tid 1");
        bw.newLine();
    }

}
