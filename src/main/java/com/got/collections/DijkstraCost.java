package com.got.collections;

public class DijkstraCost {

    private Double cost;
    private Node previous;

    public DijkstraCost() {
        this.cost = Double.MAX_VALUE;
        this.previous = null;
    }

    public DijkstraCost(Double cost, Node previous) {
        this.previous = previous;
        this.cost = cost;
    }

    public Double getCost() {
        return cost;
    }

    public Node getPrevious() {
        return previous;
    }
}
