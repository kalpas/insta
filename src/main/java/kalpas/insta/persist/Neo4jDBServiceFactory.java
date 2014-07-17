package kalpas.insta.persist;

import kalpas.insta.AppConsts;

import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.schema.IndexDefinition;
import org.neo4j.graphdb.schema.Schema;

public class Neo4jDBServiceFactory {

    public static GraphDatabaseService getNeo4jService(String db) {
        GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(AppConsts.ROOT_PATH + db);
        registerShutdownHook(graphDb);

        IndexDefinition indexDefinition;
        try (Transaction tx = graphDb.beginTx()) {
            Schema schema = graphDb.schema();
            indexDefinition = schema.indexFor(DynamicLabel.label("User")).on("username").create();
            tx.success();
        }

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
