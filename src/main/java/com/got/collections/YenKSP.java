package com.got.collections;

import java.util.*;

public class YenKSP {

    private Graph graph;

    public YenKSP(Graph graph) {
        this.graph = graph;
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
     * @param avoid paths to avoid
     * @return list of K shortest paths
     */
    public List<Path> kShortestPaths(Node from, Node to, int K, List<Node> avoid) {
        List<Path> A = new ArrayList<>();
        Queue<Path> B = new PriorityQueue<>();
        // Calculate the shortest path first
        Path shortest = new Dijkstra(graph,"distance").shortestPath(from, to, avoid);
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
                Path spurPath = new Dijkstra(graph, "distance").shortestPath(spurNode, to, new ArrayList<>());
                // Merge the spur path to the root path
                mergeSpurToRoot(spurPath, rootPath, B);
                // Restore nodes and edges back to graph
                graph.restore();
            }
            if(B.isEmpty()) break;
            A.add(k, B.remove());
        }
        return A;
    }

    /**
     * Overloaded method of the above, which includes waypoint support
     *
     * @param from starting node
     * @param to destination node
     * @param K number of shortest paths
     * @param wanted waypoints
     * @param avoid nodes to avoid
     * @return K shortest paths
     */
    public List<Path> kShortestPaths(Node from, Node to, int K, List<Node> wanted, List<Node> avoid) {
        List<Path> paths = new ArrayList<>(kShortestPaths(from, wanted.get(0), K, avoid));
        wanted.add(to);
        for(int index = 0; index < wanted.size() - 1; index++) {
            List<Path> result = kShortestPaths(wanted.get(index), wanted.get(index + 1), K, avoid);
            for(int k = 0; k < K; k++) {
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
            Path temp = createSubPath(p, i);
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
        if(spurPath != null) {
            Path total = new Path(rootPath.getNodes(), rootPath.getEdges());
            total.merge(spurPath);
            total.getNodes();
            B.add(total);
        }
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
