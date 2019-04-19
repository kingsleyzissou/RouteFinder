package com.got.app.collections;

public class DijkstraCost {

    private Double cost;
    private Node previous;
    private String type;

    public DijkstraCost(String type) {
        this.cost = Double.MAX_VALUE;
        this.previous = null;
        this.type = type;
    }

    public DijkstraCost(Double cost, Node previous, String type) {
        this.previous = previous;
        this.cost = cost;
        this.type = type;
    }

    /**
     * Get the cost required to get to a node
     *
     * @return the cost of the node
     */
    public Double getCost() {
        return cost;
    }

    /**
     * The node previous to this node (i.e. the node that has the cheapest route to the current node)
     *
     * @return the previous node
     */
    public Node getPrevious() {
        return previous;
    }


}
