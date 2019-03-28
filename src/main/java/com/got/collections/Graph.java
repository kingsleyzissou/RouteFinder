package com.got.collections;

import java.io.Serializable;
import java.util.ArrayList;

public class Graph implements Serializable {

    ArrayList<Node> nodes = new ArrayList();

    public Graph() {}

    public void addNode(Node node) {
        nodes.add(node);
    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }
}
