package kalpas.insta.persist;

import kalpas.insta.AppConsts;

import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.ResourceIterable;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.event.KernelEventHandler;
import org.neo4j.graphdb.event.TransactionEventHandler;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.index.IndexManager;
import org.neo4j.graphdb.schema.IndexDefinition;
import org.neo4j.graphdb.schema.Schema;
import org.neo4j.graphdb.traversal.BidirectionalTraversalDescription;
import org.neo4j.graphdb.traversal.TraversalDescription;

public class Neo4jDBServiceFactory {

	public static GraphDatabaseService getNeo4jService(String db) {
		GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(AppConsts.ROOT_PATH + db);
		registerShutdownHook(graphDb);

		IndexDefinition indexDefinition;
		try (Transaction tx = graphDb.beginTx()) {
			Schema schema = graphDb.schema();
			Iterable<IndexDefinition> indexes = schema.getIndexes(DynamicLabel.label("User"));
			if (!indexes.iterator().hasNext()) {
				indexDefinition = schema.indexFor(DynamicLabel.label("User")).on("username").create();
			}
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

	public static GraphDatabaseService getMockNeo4jService() {
		return new GraphDatabaseService() {

			@Override
			public <T> TransactionEventHandler<T> unregisterTransactionEventHandler(TransactionEventHandler<T> handler) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public KernelEventHandler unregisterKernelEventHandler(KernelEventHandler handler) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public TraversalDescription traversalDescription() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void shutdown() {
				// TODO Auto-generated method stub

			}

			@Override
			public Schema schema() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public <T> TransactionEventHandler<T> registerTransactionEventHandler(TransactionEventHandler<T> handler) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public KernelEventHandler registerKernelEventHandler(KernelEventHandler handler) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public boolean isAvailable(long timeout) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public IndexManager index() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Iterable<RelationshipType> getRelationshipTypes() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Relationship getRelationshipById(long id) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Node getNodeById(long id) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Iterable<Node> getAllNodes() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public ResourceIterable<Node> findNodesByLabelAndProperty(Label label, String key, Object value) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Node createNode(Label... labels) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Node createNode() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public BidirectionalTraversalDescription bidirectionalTraversalDescription() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Transaction beginTx() {
				// TODO Auto-generated method stub
				return null;
			}
		};

	}

}
