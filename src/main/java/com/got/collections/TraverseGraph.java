package com.got.collections;

import java.util.*;

public class TraverseGraph {

    public static void traverse(Node from, ArrayList<Node> encountered, double totalDistance, int count) {
        System.out.println("Total nodes: " + count +  "\nTotal distance: " + totalDistance + "\n" + from);
        if(encountered == null) encountered = new ArrayList<>();
        encountered.add(from);
        for(Map.Entry<Node, Edge> entry : from.getNeighbours().entrySet()) {
            Edge link = entry.getValue();
            if(!encountered.contains(link.getDestination())) {
                traverse(link.getDestination(), encountered, totalDistance + link.getDistance(), count + 1);
            }
        }
    }

    public static RouteCost search(Node from, ArrayList<Node> encountered, double totalCost, int difficulty, int danger, Node needle) {

        if(from.equals(needle)) {
            RouteCost cost = new RouteCost();
            cost.nodeList.add(from);
            cost.distance = totalCost;
            return cost;
        }

        if(encountered == null) encountered = new ArrayList<>();

        encountered.add(from);
        List<RouteCost> allPaths = new ArrayList<>();

        for(Map.Entry<Node, Edge> entry : from.getNeighbours().entrySet()) {
            Node destination = entry.getKey();
            Edge edge = entry.getValue();
            if(!encountered.contains(destination)) {
                RouteCost temp = search(
                        destination,
                        encountered,
                        totalCost + edge.getDistance(),
                        difficulty + edge.getDifficulty(),
                        danger + edge.getDanger(),
                        needle
                );
                if(temp == null) continue;
                temp.nodeList.add(0,from);
                allPaths.add(temp);
            }
        }

        return allPaths.isEmpty() ? null : Collections.min(allPaths, (p1,p2) -> (int) (p1.distance - p2.distance));

    }

//    public static List<List<Node>> findAll(Node from, ArrayList<Node> encountered, Node needle) {
//        List<List<Node>> result = null, temp2;
//
//        if(from.equals(needle)) {
//            List<Node> temp = new ArrayList<>();
//            temp.add(from);
//            result = new ArrayList<>();
//            result.add(temp);
//            return result;
//        }
//
//        if(encountered == null) encountered = new ArrayList<>();
//        encountered.add(from);
//
//        for(Map.Entry<Node, Edge> entry : from.getNeighbours().entrySet()) {
//            Node destination = entry.getKey();
//            Edge edge = entry.getValue();
//            if(!encountered.contains((entry.getKey()))) {
//                temp2 = findAll(destination, new ArrayList<>(encountered), needle);
//                if(temp2 != null) {
//                    for (List<Node> x : temp2) {
//                        x.add(0, from);
//                    }
//                    if (result == null) result = temp2;
//                    else result.addAll(temp2);
//                }
//            }
//        }
//
//        return result;
//
//    }



}
