package kalpas.insta.persist;

import kalpas.insta.AppConsts;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

public class Neo4jDBServiceFactory {

    public static GraphDatabaseService getNeo4jService(String db) {
        GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(AppConsts.ROOT_PATH + db);
        registerShutdownHook(graphDb);
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
