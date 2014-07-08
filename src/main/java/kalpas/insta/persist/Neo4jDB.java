package kalpas.insta.persist;

import kalpas.insta.AppConsts;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

public class Neo4jDB {
    private GraphDatabaseService graphDb;

    public Neo4jDB(String db) {
        graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(AppConsts.ROOT_PATH + db);
        registerShutdownHook(graphDb);
    }

    public GraphDatabaseService getGraphDatabaseService() {
        return graphDb;
    }


    private static void registerShutdownHook(final GraphDatabaseService graphDb) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                graphDb.shutdown();
            }
        });
    }

}
