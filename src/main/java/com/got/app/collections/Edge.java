package com.got.app.collections;

import java.io.Serializable;

public class Edge implements Serializable {
	
	private Node source;
    private Node destination;
	private int difficulty, danger;
	private double distance;
	
	public Edge(Graph graph, Node source, Node destination, int difficulty, int danger) {
		if(!source.equals(destination)) {
			this.source = source;
			this.destination = destination;
			this.difficulty = difficulty;
			this.danger = danger;
			this.distance = euclideanDistance(source.getX(), destination.getX(), source.getY(), destination.getY());
			source.addNeighbour(destination, this);
			graph.addEdge(this);
		}
	}

	/**
	 * Get the euclidean distance between two nodes
	 *
	 * @param x1 x-coordinate of node 1
	 * @param x2 x-coordinate of node 2
	 * @param y1 y-coordinate of node 1
	 * @param y2 y-coordinate of node 2
	 * @return the distance as the crow flies
	 */
	private double euclideanDistance(double x1, double x2, double y1, double y2) {
		double r2 = Math.pow((x1 - x2), 2) + Math.pow(y1 - y2, 2);
		return Math.round((Math.sqrt(r2) * 100.00)) / 100.00;
	}

	/**
	 * Get the edge going in the opposite direction
	 *
	 * @return the reciprocal edge
	 */
	public Edge getReciprocalEdge() {
		return destination.getEdgeByDestination(source);
	}

	/**
	 * Get the source node
	 *
	 * @return source node
	 */
	public Node getSource() {
		return source;
	}

	/**
	 * Get the destination node
	 *
	 * @return destination node
	 */
	public Node getDestination() {
		return destination;
	}

	/**
	 * Get the distance of the edge
	 *
	 * @return the distance
	 */
	public double getDistance() {
		return distance;
	}

	/**
	 * Get the difficulty required to traverse the edge
	 *
	 * @return the difficulty
	 */
	public int getDifficulty() {
		return difficulty;
	}

	/**
	 * Get the danger required to traverse the edge
	 *
	 * @return the danger
	 */
	public int getDanger() {
		return danger;
	}

	/**
	 * Get the cost of the edge by it's type,
	 * this enables a polymorphic Dijsktra Cost
	 *
	 * @param type desired type
	 * @return the cost based on the type
	 */
    public Double getCostByType(String type) {
		type = type.toLowerCase();
		if(type.equals("distance")) return this.getDistance();
		if(type.equals("danger")) return (double) this.getDanger();
		if(type.equals("difficulty")) return (double) this.getDifficulty();
		return this.getDistance();
    }

	/**
	 * String representation of the object
	 *
	 * @return prettified string represention
	 *
	 */
	@Override
	public String toString() {
		return "From: " + source.getName() +
				"\nTo: " + destination.getName() +
				"\nDifficulty: " + difficulty +
				"\nDanger: " + danger +
				"\nDistance: " + distance + "\n";
	}

}
