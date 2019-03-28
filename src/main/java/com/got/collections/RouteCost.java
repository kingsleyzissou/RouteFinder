package com.got.collections;

import java.util.ArrayList;
import java.util.List;

public class RouteCost {
    public double distance = 0;
    public int difficulty = 0;
    public int danger = 0;
    public List<Node> nodeList = new ArrayList<>();

    public void addAll(List<Node> nodes) {
        Node current = null;
        for(Node next : nodes) {

            if(current != null) {
                Edge edge = current.getEdgeByDestination(next);
                this.distance += edge.getDistance();
                this.danger += danger;
                this.difficulty += difficulty;
            }

            current = next;
        }
        nodeList.addAll(nodes);
    }

}
