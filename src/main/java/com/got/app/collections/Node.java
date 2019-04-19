package com.got.app.collections;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

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

	/**
	 * Get all neighbouring edges and their reciprocal
	 *
	 * @return all connecting edges
	 */
	public Set<Edge> getEdgesAndReciprocals() {
		Set<Edge> edgesToBeReturned = new HashSet<>(getNeighbours().values());
		for(Edge edge : getNeighbours().values()){
			edgesToBeReturned.add(edge.getReciprocalEdge());
		}
		return edgesToBeReturned;
	}

	/**
	 * Get the x-coordinate of the node
	 * @return x-coordinate
	 */
	public double getX() {
		return x;
	}

	/**
	 * Get the y-coordinate of the node
	 * @return  y-coordinate
	 */
	public double getY() {
		return y;
	}

	/**
	 * Get the name of the node
	 *
	 * @return the name of the node
	 */
	public String getName() {
		return name;
	}

	/**
	 * Add a neighbour to the node
	 *
	 * @param destination node (neighbour)
	 * @param edge required to get to node
	 */
	public void addNeighbour(Node destination, Edge edge) {
		neighbours.put(destination, edge);
	}

	/**
	 * Gets all the neighbouring nodes and the edges
	 * required to get there
	 *
	 * @return all neighbouring nodes
	 */
	public HashMap<Node, Edge> getNeighbours() {
		return this.neighbours;
	}

	/**
	 * Get the edge required to get to a specific
	 * neighbouring node
	 *
	 * @param destination node
	 * @return edge to get to the node
	 */
	public Edge getEdgeByDestination(Node destination) {
		return neighbours.get(destination);
    }

	/**
	 * Custom equals method
	 *
	 * @param obj to compare to
	 * @return boolean
	 */
	@Override
    public boolean equals(Object obj) {
	    if(!(obj instanceof  Node)) return false;
	    Node node = (Node) obj;
	    return this.name.equals(node.getName());
    }

	/**
	 * String representation of the node
	 *
	 * @return the name of the node
	 */
	@Override
	public String toString() {
		return name;
	}

}
