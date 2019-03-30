package com.got.collections;

import java.util.*;

public class Dijkstra {

    private Graph graph;
    private Map<Node, DijkstraCost> cost;

    public Dijkstra(Graph graph)  {
        this.graph =  graph;
        cost = new HashMap<>();
        for(Node node : graph.getNodes()) {
            this.cost.put(node, new DijkstraCost());
        }
    }

//    public List<Node> search(Node start, Node needle, HashMap<String) {
//        List<Node> allPaths = new ArrayList<>();
//        List<Node> avoid = waypoints.get("avoid");
//        List<Node> wanted = waypoints.get("contains");
//        wanted.add(0, start); wanted.add(wanted.size(), needle);
//        allPaths.addAll(search(start, wanted.get(0), avoid));
//        for(int index = 0; index < wanted.size() - 1; index++) {
//            if(allPaths.contains(wanted.get(index))) continue;
//            allPaths.addAll(search(wanted.get(index), wanted.get(index + 1), avoid));
//            avoid.addAll(allPaths);
//        }
//        return allPaths;
//    }

    public List<Node> search(Node start, Node needle, HashMap<String, List<Node>> waypoints) {
        List<Node> wanted = waypoints.get("contains"), avoid = waypoints.get("avoid");
        Set<Node> encountered = new HashSet<>();
        Set<Node> unencountered = new HashSet<>();
        cost.put(start, new DijkstraCost(0.0 , null));
        unencountered.add(start);

        while(!unencountered.isEmpty() && wayPointsVisited(encountered, wanted)) {
            filterOutUnwantedRoutes(unencountered, avoid);
            Node smallest = smallestUnsettled(unencountered);
            unencountered.remove(smallest);
            updateNeighbourCosts(encountered, unencountered, smallest);
            encountered.add(smallest);
        }

        List<Node> path = new ArrayList<>();

        for(Node n = needle; n != null; n = cost.get(n).getPrevious()) {
            path.add(n);
        }

        Collections.reverse(path);
        return path;
    }

    private boolean wayPointsVisited(Set<Node> encountered, List<Node> wanted) {
        for(Node w : wanted) {
            if(!encountered.contains(w)) return false;
        }
        return true;
    }

    private void filterOutUnwantedRoutes(Set<Node> unencountered, List<Node> avoid) {
        for(Node unwanted : avoid) {
            unencountered.remove(unwanted);
        }
    }

    private void updateNeighbourCosts(Set<Node> encountered, Set<Node> unencountered, Node smallest) {
        for(Map.Entry<Node, Edge> entry : smallest.getNeighbours().entrySet()) {
            Node node = entry.getKey(); Edge edge = entry.getValue();
            if(!encountered.contains(node)) {
                unencountered.add(node);
                Double smallestCost = cost.get(smallest).getCost();
                Double currentCost = cost.get(node).getCost();
                Double cumulativeCost = smallestCost + edge.getDistance();
                if(currentCost > cumulativeCost) {
                    cost.put(entry.getKey(), new DijkstraCost(cumulativeCost, smallest));
                }
            }
        }
    }

//    public Node getSmallestNeighbour(Set<Node> unencountered) {
//        Set<Map.Entry<Node, Edge>> smallestNeighbourSet = new HashSet<>();
//        for(Node n : unencountered) {
//            Map.Entry<Node, Edge> entry = smallestEntry(n.getNeighbours().entrySet());
//            smallestNeighbourSet.add(entry);
//        }
//        return getSmallestUnencountered(smallestNeighbourSet);
//    }

//    private Node getSmallestUnencountered(Set<Map.Entry<Node, Edge>> smallestNeighbourSet) {
//        Map.Entry<Node, Edge> entry = smallestEntry(smallestNeighbourSet);
//        return entry.getKey();
//    }

    private Map.Entry<Node, Edge> smallestEntry(Set<Map.Entry<Node, Edge>> entrySet) {
        return Collections.min(entrySet, (e1, e2) -> {
            return (int) (e1.getValue().getDistance() - e2.getValue().getDistance());
        });
    }

    private Node smallestUnsettled(Set<Node> unencountered) {
        return Collections.min(unencountered, (n1, n2) -> {
            return (int) (cost.get(n1).getCost() - cost.get(n2).getCost());
        });
    }


}
