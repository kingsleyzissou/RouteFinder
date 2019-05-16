package com.got.app.collections;

import java.util.*;

public class Yen {

    private Graph graph;
    private String type;
    private Node start;
    private Node end;

    public Yen(Graph graph, String type) {
        this.graph = graph;
        this.type = type;
    }


    /**
     * Find the k shortest paths from starting point to end point
     *
     * References:
     * https://en.wikipedia.org/wiki/Yen%27s_algorithm
     * https://github.com/yan-qi/k-shortest-paths-java-version/blob/master/src/main/java/edu/asu/emit/algorithm/graph/shortestpaths/YenTopKShortestPathsAlg.java
     *
     * @param from starting node
     * @param to ending node
     * @param K number of paths to be found
     * @param avoidPoints paths to avoid
     * @return list of K shortest paths
     */
    public List<Path> kShortestPaths(Node from, Node to, int K, List<Node> avoidPoints) {
        if(start == null) start = from; // if the start node has not been set, set it now
        if(end == null) end = to; // if the end node has not been set, set it now
        List<Path> A = new ArrayList<>();
        Queue<Path> B = new PriorityQueue<>();
        // Calculate the shortest path first
        Path shortest = new Dijkstra(graph,type).shortestPath(from, to, avoidPoints);
        A.add(shortest);
        // Loop through K times
        for(int k = 1; k < K; k++) {
            // Get the previous path
            Path previous = A.get(k-1);
            // Loop through each node in the previous path to get
            // spur nodes and spur paths
            for(int i = 0; i < previous.getNodes().size() - 2; i++) {
                // Get spur path
                Node spurNode = previous.getNodes().get(i);
                // Get root from starting point to spur node
                Path rootPath = createSubPath(previous, i);
                // Temporarily remove edges from the graph
                // in order to alter the route slightly from Dijkstra
                removeEdges(A, rootPath, i);
                // Temporarily remove nodes from the graph
                // in order to alter the route slightly from Dijkstra
                removeNodes(rootPath, spurNode);
                // Get shortest path from spur node to sought after node
                Path spurPath = new Dijkstra(graph, type).shortestPath(spurNode, to, avoidPoints);
                // Merge the spur path to the root path
                mergeSpurToRoot(spurPath, rootPath, B);

                // Restore nodes and edges back to graph
                graph.restore();
            }
            if(B.isEmpty()) break;
            Path candidate = B.remove();
            candidate.updateIndex(A.size() + 1); // Set the index to show the rank of the route
            A.add(k, candidate);
        }
        return A;
    }

    /**
     * Overloaded method of the above, which includes way point support
     *
     * @param from starting node
     * @param to destination node
     * @param K number of shortest paths
     * @param wayPoints way points
     * @param avoidPoints nodes to avoid
     * @return K shortest paths
     */
    public List<Path> kShortestPaths(Node from, Node to, int K, List<Node> wayPoints, List<Node> avoidPoints) {
        if(wayPoints.isEmpty()) return kShortestPaths(from, to, K, avoidPoints);
        start = from; end = to; // set global start and end nodes used for completeness check
        List<Path> paths = new ArrayList<>(kShortestPaths(from, wayPoints.get(0), K, avoidPoints));
        wayPoints.add(to);
        for(int index = 0; index < wayPoints.size() - 1; index++) {
            List<Path> result = kShortestPaths(wayPoints.get(index), wayPoints.get(index + 1), K, avoidPoints);
            for(int k = 0; k < K; k++) {
                if(result.size() > k && paths.size() > k)
                    paths.get(k).merge(result.get(k));
            }
        }
        return paths;
    }

    /**
     * Removes edges from the graph
     *
     * @param A list of K possible paths
     * @param rootPath the path from spur node to destination node
     * @param i index of path to remove
     */
    private void removeEdges(List<Path> A, Path rootPath, int i) {
        for(Path p : A) {
            // Temporary  sub path for comparison
            Path temp = null;
            if(p.getEdges().size() > i)
                temp = createSubPath(p, i);
            // If the temp path matches root path, remove the first edge
            if(rootPath.equals(temp)) {
                Edge edge = p.getEdges().get(i);
                // Only remove an edge if there is at least 2 remaining
                // edges
                if(!isLastEdge(edge)) {
                    graph.removeEdge(edge);
                }
            }
        }
    }

    /**
     * Remove nodes from the graph
     *
     * @param rootPath from which to gather the nodes
     * @param spurNode for the check
     */
    private void removeNodes(Path rootPath, Node spurNode) {
        for(Node n : rootPath.getNodes()) {
            // Remove all nodes but spur node
            if(!n.equals(spurNode)) {
                graph.removeNode(n);
            }
        }
    }

    /**
     * Create a sub path
     *
     * @param path path for sub path
     * @param index end point for sub path
     * @return sub path
     */
    private Path createSubPath(Path path, int index) {
        return new Path(
            path.getNodes().subList(0,index),
            path.getEdges().subList(0,index)
        );
    }

    /**
     *
     * @param spurPath spur path from spur node to destination
     * @param rootPath  root path from start to spur node
     * @param B list of candidates
     */
    private void mergeSpurToRoot(Path spurPath, Path rootPath, Queue<Path> B) {
        if(isComplete(spurPath)) {
            B.add(spurPath);
            return;
        }
        rootPath.merge(spurPath);
        rootPath.getNodes();
        if(rootPath.isValid(start, end))
            B.add(rootPath);
    }

    /**
     * Checks if the spur path goes from the start to end point,
     * if so, add this path to list of candidates, instead
     * of merging it
     *
     * @param spurPath the path being checked for completeness
     * @return if the path is complete or not
     */
    private boolean isComplete(Path spurPath) {
        return (spurPath.getNodes().contains(start) && spurPath.getNodes().contains(end));
    }

    /**
     * Check if all of the edges from a node have already
     * been removed
     *
     * @param edge needed for the check
     * @return boolean
     */
    private boolean isLastEdge(Edge edge) {
        if(edge == null) return true;
        // Get the source node
        Node source = edge.getSource();
        // Count the number of nodes
        int count = source.getNeighbours().values().size();
        for(Edge e : source.getNeighbours().values()) {
            if(graph.getRemovedEdges().contains(e)) count--;
        }
        // if only 1 node is left, return false, we don't
        // want to remove the last edge
        return (count < 2);
    }

}
