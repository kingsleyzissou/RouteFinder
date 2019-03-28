package com.got.collections;

import java.io.Serializable;
import java.util.HashMap;

public class Node implements Serializable {

	private int id;
	private String name;
	private double x, y;
	private HashMap<Node, Edge> neighbours = new HashMap<>();
	
	public Node(int id, String name, double x, double y) {
		this.id = id;
		this.name = name;
		this.x = x;
		this.y = y;
	}
	
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	public String getName() {
		return name;
	}

	public void addNeighbour(Node destination, Edge e) {
		neighbours.put(destination, e);
	}

	public HashMap<Node, Edge> getNeighbours() {
		return this.neighbours;
	}

	public Edge getEdgeByDestination(Node node) {
		return neighbours.get(node);
    }

    @Override
    public boolean equals(Object obj) {
	    if(!(obj instanceof  Node)) return false;
	    Node node = (Node) obj;
	    return this.name.equals(node.getName());
    }

	@Override
	public String toString() {
		return "Index: " + id +
				"\nName: " + name +
				"\nX: " + x +
				"\nY: " + y + "\n";
	}

}
