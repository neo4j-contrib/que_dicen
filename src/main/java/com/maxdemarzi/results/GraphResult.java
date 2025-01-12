package com.maxdemarzi.results;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import java.util.List;

public class GraphResult {
    public final List<Node> nodes;
    public final List<Relationship> relationships;

    public GraphResult(List<Node> nodes, List<Relationship> relationships) {
        this.nodes = nodes;
        this.relationships = relationships;
    }
}