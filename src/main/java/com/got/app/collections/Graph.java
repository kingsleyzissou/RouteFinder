package com.got.app.collections;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Graph implements Serializable {

    private ArrayList<Node> nodes = new ArrayList<>();
    private ArrayList<Edge> edges = new ArrayList<>();
    private Set<Node> removedNodes = new HashSet<>();
    private Set<Edge> removedEdges = new HashSet<>();

    public Graph() {}

    /**
     * Add a node to the graph
     *
     * @param node to be added
     */
    public void addNode(Node node) {
        nodes.add(node);
    }

    /**
     * Add en edge to the graph
     *
     * @param edge to be added
     */
    public void addEdge(Edge edge) {
        edges.add(edge);
    }

    /**
     * Remove a node from the graph and
     * store in the temporary removed nodes set
     *
     * @param node to be removed
     */
    public void  removeNode(Node node) {
        nodes.remove(node);
        removedNodes.add(node);
        edges.removeAll(node.getEdgesAndReciprocals());
        removedEdges.addAll(node.getEdgesAndReciprocals());
    }

    /**
     * Remove a edge from the graph and
     * store in the temporary removed edges set
     *
     * @param edge to be removed
     */
    public void  removeEdge(Edge edge) {
        if(edge != null) {
            removedEdges.add(edge.getReciprocalEdge());
            removedEdges.add(edge);
            edges.remove(edge.getReciprocalEdge());
            edges.remove(edge);
        }
    }

    /**
     * Restore the removed nodes back into the graph
     *
     */
    public void  restore() {
        nodes.addAll(removedNodes);
        edges.addAll(removedEdges);
        removedNodes.clear();
        removedEdges.clear();
    }

    /**
     * Get all the nodes from the graph
     *
     * @return list of nodes
     */
    public ArrayList<Node> getNodes() {
        return nodes;
    }

    /**
     * Get all the edges in the graph
     *
     * @return list of edges
     */
    public ArrayList<Edge> getEdges() {
        return edges;
    }

    /**
     * Get list of removed edges
     *
     * @return set of removed edges
     */
    public Set<Edge> getRemovedEdges() {
        return removedEdges;
    }
}
