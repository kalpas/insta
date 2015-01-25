package kalpas.insta.persist;

import kalpas.insta.persist.domain.RelationshipsType;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "testAppContext.xml" })
public class Neo4JTest {

	@Autowired
	private GraphDatabaseService graphDb;

	@Test
	public void testNeo4j() {
		Node firstNode;
		Node seconNode;

		Relationship relationship;

		try (Transaction tx = graphDb.beginTx()) {

			firstNode = graphDb.createNode();
			firstNode.setProperty("message", "Hello, ");

			seconNode = graphDb.createNode();
			seconNode.setProperty("message", "World!");

			relationship = firstNode.createRelationshipTo(seconNode, RelationshipsType.FOLLOWS);
			relationship.setProperty("message", "brave Neo4j ");

			tx.success();
		}

	}

}
