package com.maxdemarzi;

import com.maxdemarzi.schema.Schema;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.neo4j.harness.Neo4j;
import org.neo4j.harness.Neo4jBuilders;
import org.neo4j.test.server.HTTP;

import java.util.Map;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonMap;
import static org.junit.Assert.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class IngestDocumentTest {

    private Neo4j neo4j;
    @BeforeAll
    void initializeNeo4j() {
        neo4j = Neo4jBuilders.newInProcessBuilder()
                .withProcedure(Procedures.class)
                .withProcedure(Schema.class)
                .build();
    }

    @AfterAll
    void shutdownNeo4j() {
        neo4j.close();
    }

    @Test
    public void testIngest() throws Exception {
        HTTP.POST(neo4j.httpURI().resolve("/db/neo4j/tx/commit").toString(), SCHEMA);
        HTTP.Response response = HTTP.POST(neo4j.httpURI().resolve("/db/neo4j/tx/commit").toString(), QUERY);
        assertEquals(5, response.get("results").get(0).get("data").get(0).get("row").get(0).size());
        assertEquals(4, response.get("results").get(0).get("data").get(0).get("row").get(1).size());
    }

    @Test
    public void testSpanishIngest() throws Exception {
        HTTP.POST(neo4j.httpURI().resolve("/db/neo4j/tx/commit").toString(), SCHEMA);
        HTTP.Response response = HTTP.POST(neo4j.httpURI().resolve("/db/neo4j/tx/commit").toString(), QUERY2);
        assertEquals(2, response.get("results").get(0).get("data").get(0).get("row").get(0).size());
        assertEquals(1, response.get("results").get(0).get("data").get(0).get("row").get(1).size());
    }

    private static final Map SCHEMA =
            singletonMap("statements",asList(singletonMap("statement",
                    "CALL com.maxdemarzi.schema.generate()")));

    private static final Map QUERY =
            singletonMap("statements",asList(singletonMap("statement",
                    "CALL com.maxdemarzi.en.ingest('data/en_sample.txt')")));

    private static final Map QUERY2 =
            singletonMap("statements",asList(singletonMap("statement",
                    "CALL com.maxdemarzi.es.ingest('data/es_sample.txt')")));

}
