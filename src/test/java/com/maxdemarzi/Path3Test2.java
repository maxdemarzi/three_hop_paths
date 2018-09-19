package com.maxdemarzi;

import org.junit.Rule;
import org.junit.Test;
import org.neo4j.harness.junit.Neo4jRule;
import org.neo4j.test.server.HTTP;

import java.util.Map;

import static java.util.Collections.singletonList;
import static java.util.Collections.singletonMap;
import static junit.framework.TestCase.assertEquals;

public class Path3Test2 {

    @Rule
    public final Neo4jRule neo4j = new Neo4jRule()
            .withFixture(MODEL_STATEMENT)
            .withProcedure(Procedures.class);

    @Test
    public void testTraversal() throws Exception {
        Procedures.relProperties.invalidateAll();
        Procedures.nodeProperties.invalidateAll();
        Procedures.graph = null;

        HTTP.Response response = HTTP.POST(neo4j.httpURI().resolve("/db/data/transaction/commit").toString(), QUERY1);
        int count = response.get("results").get(0).get("data").size();
        assertEquals(1, count);

    }

    private static final Map QUERY1 =
            singletonMap("statements", singletonList(singletonMap("statement",
                    "CALL com.maxdemarzi.three_hop_paths(['4297089638'], ['5000527109'], {RELTYPE1: 0.50, RELTYPE2: 0.40})")));

    private static final String MODEL_STATEMENT =
            "CREATE (n1:Node { id: '4297089638' })" +
            "CREATE (n2:Node { id: '4298546138' })" +
            "CREATE (n3:Node { id: '5037473967' })" +
            "CREATE (n4:Node { id: '5000558529' })" +
            "CREATE (n5:Node { id: '5000527109' })" +
            "CREATE (n1)-[:RELTYPE1 {weight: 0.2}]->(n2)" +
            "CREATE (n1)-[:RELTYPE1 {weight: 0.3}]->(n3)" +
            "CREATE (n3)-[:RELTYPE1 {weight: 0.2}]->(n2)" +
            "CREATE (n3)-[:RELTYPE1 {weight: 0.6}]->(n4)" +
            "CREATE (n4)-[:RELTYPE2 {weight: 0.5}]->(n5)";

}
