package com.got.app.collections;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.util.*;
import java.util.List;

public class Path implements Comparable {

    private int index;
    private List<Node> nodes;
    private List<Edge> edges;
    private boolean reversed;
    private Double cost = 0.0;

    public Path() {
        this.index = 1;
        nodes = new ArrayList<>();
        edges = new ArrayList<>();
    }

    public Path(List<Node> nodes, List<Edge> edges) {
        this.index = 1;
        this.nodes = new ArrayList<>(nodes);
        this.edges = new ArrayList<>(edges);
    }

    /**
     * Add node and edge to path
     *
     * @param node to be added
     * @param edge corresponding edge
     */
    public void add(Node node, Edge edge) {
        add(node);
        add(edge);
        calculateCost();
    }

    /**
     * Get the total cost of the path
     *
     * @return the cost
     */
    public Double getCost() {
        if(cost == 0.0) calculateCost();
        return cost;
    }

    /**
     * Calculate the cost of the path
     *
     */
    private void calculateCost() {
        Double cost = 0.0;
        for(Edge edge : edges) {
            if(edge != null) {
                cost += edge.getDistance();
            }
        }
        this.cost = cost;
    }

    /**
     * Updates the index (used for the rank/order of the route)
     */
    public void updateIndex(int index) {
        this.index = index;
    }

    /**
     * Add a node to the path
     *
     * @param node to be added
     */
    public void add(Node node) {
        nodes.add(node);
    }

    /**
     * Add an edge to the path
     *
     * @param edge to be added
     */
    public void add(Edge edge) {
        edges.add(edge);
    }

    /**
     * Merge two paths
     *
     * @param path to be merged into the current path
     */
    public void merge(Path path) {
        nodes.addAll(path.getNodes());
        edges.addAll(path.getEdges());
        reversed = true;
    }

    /**
     * Get all the nodes in the path
     *
     * @return list of nodes in path
     */
    public List<Node> getNodes() {
        if(!reversed) reversePath();
        return nodes;
    }

    /**
     * Get all the edges in the path
     *
     * @return list of edges in the path
     */
    public List<Edge> getEdges() {
        if(!reversed) reversePath();
        edges.remove(null);
        return edges;
    }

    /**
     * Construct visual representation of the nodes for the map
     *
     * @param color of the circle
     * @return list of nodes as circles
     */
    public List<Circle> drawCircles(Color color) {
        if(!reversed) reversePath();
        List<Circle> circles = new ArrayList<>();
        for(Node node : nodes) {
            if(node != null) {
                circles.add(new Circle(node.getX(), node.getY(), 8, color));
            }
        }
        return circles;
    }

    /**
     * Construct visual representation of the edges for the map
     *
     * @param color of the edge
     * @return list of edges as lines
     */
    public List<Line> drawLines(Color color) {
        if(!reversed) reversePath();
        List<Line> lines = new ArrayList<>();
        for(Edge edge : edges) {
            if(edge != null) {
                Node source = edge.getSource();
                Node dest = edge.getDestination();
                Line line = new Line(
                    source.getX(), source.getY(),
                    dest.getX(), dest.getY()
                );
                line.setStroke(color);
                line.setStrokeWidth(3);
                lines.add(line);
            }
        }
        return lines;
    }

    /**
     * Check if a route is valid or not
     *
     * @return boolean
     */
    public boolean isValid(Node start, Node end) {
        if(!(getNodes().contains(start) && getNodes().contains(end))) return false;
        for(Node node : getNodes()) {
            int count = 0;
            for(Node neighbour : node.getNeighbours().keySet()) {
                if(getNodes().contains(neighbour)) count++;
            }
            if(count == 0) return false;
        }
        return true;
    }

    /**
     * Reverse the path
     *
     */
    private void reversePath() {
        Collections.reverse(nodes);
        Collections.reverse(edges);
        reversed = true;
    }

    /**
     * Custom compare method for comparable implementation
     *
     * @param o object being compared to
     * @return whether larger or not
     */
    @Override
    public int compareTo(Object o) {
        if(o == null) return 0;
        if(!(o instanceof Path)) return -1;
        return (int) (cost - ((Path) o).getCost());
    }

    /**
     * Custom equals method
     *
     * @param o object being compared to
     * @return whether the object is equal to the path or not
     */
    @Override
    public boolean equals(Object o) {
        if(o == null) return false;
        if(!(o instanceof Path)) return false;
        Path p = (Path) o;
        List<Node> temp1 = new ArrayList<>(this.nodes);
        List<Node> temp2 = new ArrayList<>(p.getNodes());
        return (temp1.containsAll(temp2) && (temp1.size() == temp2.size()));
    }

    /**
     * String representation of object
     *
     * @return route name and index
     */
    @Override
    public String toString() {
        return "Route " + index;
    }
}
