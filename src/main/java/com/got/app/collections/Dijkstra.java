package com.got.app.collections;

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
     * @param avoidPoints nodes to avoid
     * @return shortest path
     */
    public Path shortestPath(Node start, Node needle, List<Node> avoidPoints) {
        this.avoid = avoidPoints;
        Set<Node> visited = new HashSet<>();
        Set<Node> unvisited = new HashSet<>();
        cost.put(start, new DijkstraCost(0.0 , null, this.type));
        unvisited.add(start);

        while(!unvisited.isEmpty()) {
            Node smallest = smallestUnsettled(unvisited);
            unvisited.remove(smallest);
            updateNeighbourCosts(visited, unvisited, smallest);
            visited.add(smallest);
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
     * @param wayPoints way points
     * @param avoidPoints nodes to avoid
     * @return shortest path
     */
    public Path shortestPath(Node start, Node needle, List<Node> wayPoints, List<Node> avoidPoints) {
        this.avoid = avoidPoints;
        wayPoints.add(0, start);
        wayPoints.add(needle);
        Path path = new Path();
        for(int index = 0; index < wayPoints.size() - 1; index++) {
            path.merge(shortestPath(wayPoints.get(index), wayPoints.get(index + 1), avoidPoints));
        }

        return path;
    }

    /**
     * Get each neighbour node and update the cost to get to that
     * node
     *
     * @param visited list of nodes already visited
     * @param unvisited list of nodes not yet visited
     * @param smallest smallest node
     */
    private void updateNeighbourCosts(Set<Node> visited, Collection<Node> unvisited, Node smallest) {
        for(Map.Entry<Node, Edge> entry : smallest.getNeighbours().entrySet()) {
            // Get the node and the the edge to get to that node
            Node node = entry.getKey(); Edge edge = entry.getValue();
            if(!visited.contains(node) // if  the node has not yet been visited
                    && !avoid.contains(node) // check that the node is not a node to be avoided
                    && graph.getNodes().contains(node) // ensure that the node has not been removed from the graph
                    && graph.getEdges().contains(edge))  // ensure the edge has not been removed from the graph
            {
                unvisited.add(node);
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
     * @param unvisited list of unvisited nodes
     * @return the smallest unvisited node
     */
    private Node smallestUnsettled(Set<Node> unvisited) {
        return Collections.min(unvisited, (n1, n2) -> (int) (cost.get(n1).getCost() - cost.get(n2).getCost()));
    }


}
