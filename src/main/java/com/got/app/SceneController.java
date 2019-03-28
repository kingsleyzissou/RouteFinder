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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

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
        Node needle = graph.getNodes().get(20);
        RouteCost cost = TraverseGraph.search(from, null, 0, 0,0, needle);

//        List<List<Node>> allPaths = TraverseGraph.findAll(from, null, needle);

        ArrayList<Line> lines = new ArrayList<>();
        ArrayList<Circle> circles = new ArrayList<>();

//        List<RouteCost> routes = new ArrayList<>();
//
//        for(List<Node> match : allPaths) {
//            RouteCost cost = new RouteCost();
//            cost.addAll(match);
//            routes.add(cost);
//        }
//
//        RouteCost cost = Collections.min(routes, (r1, r2) -> (int) (r1.distance - r2.distance));



//        if(!allPaths.isEmpty()) {
//            int i = 0;
//            for(List<Node> path : allPaths) {
//                Node current = null;
//                for(Node next : path) {
//                    if(current != null) {
//                        Line line = new Line(
//                                current.getX(), current.getY(),
//                                next.getX(), next.getY()
//                        );
//                        line.setStroke(colours.get(i%colours.size()));
//                        lines.add(line);
//                    }
//                    current = next;
//                    circles.add(new Circle(current.getX(), current.getY(), 5, colours.get(i%colours.size())));
//                }
//                i++;
//            }
//        }


        Dijkstra dijkstra = new Dijkstra(graph);

//        cost = dijkstra.search(from, needle);


        if(cost != null) {
            Node current = null;
            for(Node node : cost.nodeList) {
                if(current != null) {
                    Line line = new Line(
                            current.getX(), current.getY(),
                            node.getX(), node.getY()
                    );
                    line.setStroke(Color.BLUE);
                    lines.add(line);
                }
                current = node;
                circles.add(new Circle(current.getX(), current.getY(), 5, Color.RED));
            }
        }


//        for(Node node : graph.getNodes()) {
//            circles.add(new Circle(node.getX(), node.getY(), 5, Color.RED));
//
//            for(Edge edge : node.getNeighbours()) {
//                Line line = new Line(
//                        node.getX(), node.getY(),
//                        edge.getDestination().getX(), edge.getDestination().getY()
//                );
//                line.setStroke(Color.BLUE);
////                line.setStrokeWidth(10);
//                lines.add(line);
//            }
//
//        }




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
