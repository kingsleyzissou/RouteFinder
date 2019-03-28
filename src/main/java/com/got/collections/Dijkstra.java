package com.got.collections;

import java.util.*;

public class Dijkstra {

    private Graph graph;
    private RouteCost cost;
    private Map<Node, Integer> nodeValues;

    public Dijkstra(Graph graph)  {
        this.graph =  graph;
        cost = new RouteCost();
        nodeValues = new HashMap<>();
        for(Node node : graph.getNodes()) {
            this.nodeValues.put(node, Integer.MAX_VALUE);
        }
    }

    public void search(Node start, Node needle) {
        Map<Node, Double> cost = new HashMap<>();
        Map<Node, Node> previous = new HashMap<>();
        Set<Node> encountered = new HashSet<>();
        Set<Node> unencountered = new HashSet<>();
        nodeValues.put(start, 0);
        unencountered.add(start);


        while(!unencountered.isEmpty()) {
            Node smallest = getSmallestNeighbour(unencountered);
            unencountered.remove(smallest);

            for(Node neighbour : smallest.getNeighbours().keySet()) {

                if(!encountered.contains(neighbour)) {

                    // Essentially we need to update the value of each of the neighbour nodes
                    // in the nodeValues map

                    // Instead of infinity, their value should be updated to their actual cost
                    // There are a number of things we could do here.
                    // We could  change the nodeValues map to Map<Node, RouteCost> or
                    // maybe Map<Node, Edge>

                    // Once we've decided on that, we calculate the total cost required to get to that
                    // particular node with the lowest possible value  found

                }

            }
        }


    }

    public Node getSmallestNeighbour(Set<Node> unencountered) {
        Set<Map.Entry<Node, Edge>> smallestNeighbourSet = new HashSet<>();
        for(Node n : unencountered) {
            Map.Entry<Node, Edge> entry = smallestEntry(n.getNeighbours().entrySet());
            smallestNeighbourSet.add(entry);
        }
        return getSmallestUnencountered(smallestNeighbourSet);
    }

    private Node getSmallestUnencountered(Set<Map.Entry<Node, Edge>> smallestNeighbourSet) {
        Map.Entry<Node, Edge> entry = smallestEntry(smallestNeighbourSet);
        return entry.getKey();
    }

    private Map.Entry<Node, Edge> smallestEntry(Set<Map.Entry<Node, Edge>> entrySet) {
        return Collections.min(entrySet, (e1, e2) -> {
            return (int) (e1.getValue().getDistance() - e2.getValue().getDistance());
        });
    }


}
