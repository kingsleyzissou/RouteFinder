package com.got.app;

import com.got.collections.*;
import com.got.helpers.DataLoader;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
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


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            graph = DataLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }


        ArrayList<Color> colours = new ArrayList<>();

        Collections.addAll(colours,
                Color.BLUE, Color.RED, Color.PALEGREEN, Color.VIOLET,
                Color.FIREBRICK, Color.TURQUOISE, Color.TOMATO,
                Color.YELLOW,  Color.INDIGO, Color.LIME,
                Color.RED, Color.TEAL, Color.BEIGE, Color.AZURE,
                Color.HONEYDEW, Color.CHOCOLATE, Color.CORAL,
                Color.THISTLE, Color.TAN, Color.ORANGERED, Color.ORCHID,
                Color.SEAGREEN, Color.SEASHELL
        );

        Node from = graph.getNodes().get(0);
        Node needle = graph.getNodes().get(27);

        ArrayList<Node> contains = new ArrayList<>();
        contains.add(graph.getNodes().get(8));
        contains.add(graph.getNodes().get(26));
        contains.add(graph.getNodes().get(17));
        contains.add(graph.getNodes().get(25));

        HashMap map = new HashMap();
        map.put("contains", contains);
        map.put("avoid", new ArrayList());

        ArrayList<Line> lines = new ArrayList<>();
        ArrayList<Circle> circles = new ArrayList<>();



        Dijkstra d = new Dijkstra(graph);

        List<Node> path = d.search(from, needle, map);

        System.out.println(path.size());

        if(!path.isEmpty()) {
            Node current = null;
            for(Node next : path) {
                if(current != null) {
                    Line line = new Line(
                            current.getX(), current.getY(),
                            next.getX(), next.getY()
                    );
                    line.setStroke(Color.RED);
                    lines.add(line);
                }
                circles.add(new Circle(next.getX(),  next.getY(), 5, Color.BLUE));
                current = next;
            }
        }

        Group content = new Group();
        zoomGroup = new Group();
        content.getChildren().add(zoomGroup);
        zoomGroup.getChildren().add(scrollPane.getContent());
        scrollPane.setContent(content);
        pane.getChildren().addAll(circles);
        pane.getChildren().addAll(lines);
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
