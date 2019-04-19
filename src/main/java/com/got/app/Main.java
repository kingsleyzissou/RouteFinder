package com.got.app;

import java.io.*;

import com.got.app.collections.Graph;
import com.got.app.helpers.DataLoader;
import com.got.app.helpers.ExcelReader;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		Parent root = FXMLLoader.load(new File("src/main/java/com/got/app/resources/Scene.fxml").toURL());
		Scene scene = new Scene(root);
		stage.setTitle("GoT");
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
