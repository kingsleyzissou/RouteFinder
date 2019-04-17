package com.got.collections;

import java.util.*;

public class Dijkstra {

    private Graph graph;
    private String type;
    private List<Node> avoid;
    private Map<Node, DijkstraCost> cost;

    public Dijkstra(Graph graph, String type)  {
        this.graph =  graph;
        cost = new HashMap<>();
        this.type = type;
        for(Node node : graph.getNodes()) {
            this.cost.put(node, new DijkstraCost(type));
        }
    }

    /**
     * Dijkstra's shortest path algorithm
     *
     * @param start node
     * @param needle destination node
     * @param avoid nodes to avoid
     * @return shortest path
     */
    public Path shortestPath(Node start, Node needle, List<Node> avoid) {
        this.avoid = avoid;
        Set<Node> encountered = new HashSet<>();
        Set<Node> unencountered = new HashSet<>();
        cost.put(start, new DijkstraCost(0.0 , null, this.type));
        unencountered.add(start);

        while(!unencountered.isEmpty()) {
            Node smallest = smallestUnsettled(unencountered);
            unencountered.remove(smallest);
            updateNeighbourCosts(encountered, unencountered, smallest);
            encountered.add(smallest);
        }


        Path path = new Path();

        Node n = needle;
        while(cost.get(n) != null) {
            Edge e = n.getEdgeByDestination(cost.get(n).getPrevious());
            if(e != null) e = e.getReciprocalEdge();
            path.add(n, e);
            n = cost.get(n).getPrevious();
        }

        return path;
    }

    /**
     * Overloaded method of the shortest path algorithm which
     * includes way point support
     * @param start node
     * @param needle destination node
     * @param wanted way points
     * @param avoid nodes to avoid
     * @return shortest path
     */
    public Path shortestPath(Node start, Node needle, List<Node> wanted, List<Node> avoid) {
        this.avoid = avoid;
        wanted.add(0, start);
        wanted.add(needle);
        Path path = new Path();
        for(int index = 0; index < wanted.size() - 1; index++) {
            path.merge(shortestPath(wanted.get(index), wanted.get(index + 1), avoid));
        }

        return path;
    }

    /**
     * Get each neighbour node and update the cost to get to that
     * node
     *
     * @param encountered list of nodes already encountered
     * @param unencountered list of nodes not yet encountered
     * @param smallest smallest node
     */
    private void updateNeighbourCosts(Set<Node> encountered, Collection<Node> unencountered, Node smallest) {
        for(Map.Entry<Node, Edge> entry : smallest.getNeighbours().entrySet()) {
            // Get the node and the the edge to get to that node
            Node node = entry.getKey(); Edge edge = entry.getValue();
            if(!encountered.contains(node) // if  the node has not yet been encountered
                    && !avoid.contains(node) // check that the node is not a node to be avoided
                    && graph.getNodes().contains(node) // ensure that the node has not been removed from the graph
                    && graph.getEdges().contains(edge))  // ensure the edge has not been removed from the graph
            {
                unencountered.add(node);
                // Get the cost of the smallest neighbour node
                Double smallestCost = cost.get(smallest).getCost();
                // Get the cost required currently to get to the neighbour node
                Double currentCost = cost.get(node).getCost();
                // The cumulative cost of reaching the neighbour node
                Double cumulativeCost = smallestCost + edge.getCostByType(this.type);
                // if the cumulative cost is smaller than the cost currently required,
                // update the cost to get to the node
                if(currentCost > cumulativeCost) {
                    cost.put(entry.getKey(), new DijkstraCost(cumulativeCost, smallest, this.type));
                }
            }
        }
    }

    /**
     * Returns the smallest node note yet visited
     *
     * @param unencountered list of unencountered nodes
     * @return the smallest unencountered node
     */
    private Node smallestUnsettled(Set<Node> unencountered) {
        return Collections.min(unencountered, (n1, n2) -> (int) (cost.get(n1).getCost() - cost.get(n2).getCost()));
    }


}
