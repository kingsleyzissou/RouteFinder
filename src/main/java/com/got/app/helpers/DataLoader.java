package com.got.app.helpers;

import com.got.app.collections.Graph;

import java.io.*;

public class DataLoader {

    /**
     * Static helper method to save graph object to file
     *
     * @param graph element containing all the nodes and edges
     * @throws IOException
     */
    public static void save(Graph graph) throws IOException {
        FileOutputStream fos = new FileOutputStream(("src/main/java/com/got/app/database/graph.dat"));
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
        FileInputStream fis = new FileInputStream("src/main/java/com/got/app/database/graph.dat");
        ObjectInputStream ois = new ObjectInputStream(fis);
        Graph graph = (Graph) ois.readObject();
        fis.close();
        return graph;
    }

}
