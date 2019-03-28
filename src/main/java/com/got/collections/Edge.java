package com.got.collections;

import java.io.Serializable;

public class Edge implements Serializable {
	
	private Node source;
    private Node destination;
	private int difficulty, danger;
	private double distance;
	
	public Edge(Node source, Node destination, int difficulty, int danger) {
		if(!source.equals(destination)) {
			this.source = source;
			this.destination = destination;
			this.difficulty = difficulty;
			this.danger = danger;
			this.distance = euclideanDistance(source.getX(), destination.getX(), source.getY(), destination.getY());
			source.addNeighbour(destination, this);
			destination.addNeighbour(source, this);
		}
	}
	
	private double euclideanDistance(double x1, double x2, double y1, double y2) {
		double r2 = Math.pow((x1 - x2), 2) + Math.pow(y1 - y2, 2);
		return Math.round((Math.sqrt(r2) * 100.00)) / 100.00;
	}
	
	@Override
	public String toString() {
		return "From: " + source.getName() +
				"\nTo: " + destination.getName() +
				"\nDifficulty: " + difficulty +
				"\nDanger: " + danger +
				"\nDistance: " + distance + "\n";
	}

	public Node getSource() {
		return source;
	}

	public Node getDestination() {
		return destination;
	}

	public double getDistance() {
		return distance;
	}

	public int getDifficulty() {
		return difficulty;
	}

	public int getDanger() {
		return danger;
	}


}
