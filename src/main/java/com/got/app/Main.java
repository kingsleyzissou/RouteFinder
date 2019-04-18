package com.got.app;

import java.io.*;

import com.got.collections.Dijkstra;
import com.got.collections.Edge;
import com.got.collections.Graph;
import com.got.collections.Node;
import com.got.helpers.DataLoader;
import com.got.helpers.ExcelReader;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		Graph graph = new Graph();
		ExcelReader.loadNodes(graph);
		ExcelReader.loadEdges(graph);
		DataLoader.save(graph);
		System.out.println("Save successful");
		Parent root = FXMLLoader.load(new File("src/main/java/com/got/app/Scene.fxml").toURL());
		Scene scene = new Scene(root);
		stage.setTitle("GoT");
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
