package com.got.helpers;

import com.got.collections.Graph;

import java.io.*;

public class DataLoader {

    /**
     * Static helper method to save graph object to file
     *
     * @param graph element containing all the nodes and edges
     * @throws IOException
     */
    public static void save(Graph graph) throws IOException {
        FileOutputStream fos = new FileOutputStream(("graph.dat"));
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(graph);
        oos.close();
    }

    /**
     * Static helper method to load graph object from file
     *
     * @return graph element containing all the nodes and edges
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Graph load() throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream("graph.dat");
        ObjectInputStream ois = new ObjectInputStream(fis);
        Graph graph = (Graph) ois.readObject();
        fis.close();
        return graph;
    }

}
