package com.got.app;

import com.got.collections.*;
import com.got.helpers.DataLoader;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.net.URL;
import java.util.*;

public class SceneController implements Initializable {

    private Group zoomGroup;
    private Graph graph;

    @FXML private Pane pane;
    @FXML private  Slider slider;
    @FXML private ScrollPane scrollPane;
    @FXML private ComboBox<Node> from;
    @FXML private ComboBox<Node> to;
    @FXML private Button go;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            graph = DataLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }


//        from.getItems().addAll(graph.getNodes());
//        to.getItems().addAll(graph.getNodes());

        Node source = graph.getNodes().get(0);
//        Node destination = graph.getNodes().get(3);
        Node destination = graph.getNodes().get(44);

        ArrayList<Node> contains = new ArrayList<>();
        contains.add(graph.getNodes().get(8));
        contains.add(graph.getNodes().get(17));
        contains.add(graph.getNodes().get(26));

        ArrayList<Node> avoid = new ArrayList<>();
//
        HashMap<String, List<Node>> map = new HashMap<>();
        map.put("avoid", avoid);
        map.put("contains", contains);
//

        Dijkstra d = new Dijkstra(graph, "distance");
//        Path path = d.shortestPath(source, destination, map);

        YenKSP yen = new YenKSP(graph);
        List<Path> paths = yen.kShortestPaths(source, destination, 3, contains, avoid);
//        List<Path> paths = yen.kShortestPaths(source, destination, 3, avoid);

//        System.out.println(paths.size());


//        for(Path path : paths) {
//        pane.getChildren().addAll(path.drawCircles(Color.RED));
//        pane.getChildren().addAll(path.drawLines(Color.RED));
        pane.getChildren().addAll(paths.get(0).drawCircles(Color.RED));
        pane.getChildren().addAll(paths.get(0).drawLines(Color.RED));
        pane.getChildren().addAll(paths.get(1).drawCircles(Color.BLUE));
        pane.getChildren().addAll(paths.get(1).drawLines(Color.BLUE));
        pane.getChildren().addAll(paths.get(2).drawCircles(Color.BLACK));
        pane.getChildren().addAll(paths.get(2).drawLines(Color.BLACK));
//        }

//

        Group content = new Group();
        zoomGroup = new Group();
        content.getChildren().add(zoomGroup);
        zoomGroup.getChildren().add(scrollPane.getContent());
        scrollPane.setContent(content);

        slider.valueProperty().addListener((ov, old_val, new_val) -> zoom(new_val.doubleValue()));
        zoom(0.2161);
    }

    private void zoom(Double value) {
        double h = scrollPane.getHvalue(), v = scrollPane.getVvalue();
        zoomGroup.setScaleX(value);
        zoomGroup.setScaleY(value);
        scrollPane.setHvalue(h);
        scrollPane.setVvalue(v);
    }


}
