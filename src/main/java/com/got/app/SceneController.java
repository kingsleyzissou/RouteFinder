package com.got.app;

import com.got.collections.*;
import com.got.helpers.DataLoader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.*;

public class SceneController implements Initializable {

    private Graph graph;
    private Group zoomGroup;
    private double zoomValue = 0.2161;
    private List<Node> wayPoints = new ArrayList<>();
    private List<Node> avoidPoints = new ArrayList<>();

    private Path selectedPath;
    private List<Path> suitablePaths = new ArrayList<>();



    @FXML private Pane pane;
    @FXML private ScrollPane scrollPane;
    @FXML private ComboBox<Node> fromSelection;
    @FXML private ComboBox<Node> toSelection;
    @FXML private ComboBox<Node> wayPointSelection;
    @FXML private ComboBox<Node> avoidPointSelection;
    @FXML private ComboBox<String> measurementSelection;
    @FXML private ListView<Node> wayPointList;
    @FXML private ListView<Node> avoidPointList;
    @FXML private ListView<Node> nodeList;
    @FXML private ListView<Path> routeList;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadGraph();
        initializeNodeCombos();
        initialiseMeasurementCombo("Distance", "Danger", "Difficulty");
        initializeScrollPane();
        zoom(0.2161);
    }

    private void initialiseMeasurementCombo(String ...strings) {
        for(String string : strings)
            measurementSelection.getItems().add(string);
    }

    private void initializeScrollPane() {
        Group content = new Group();
        zoomGroup = new Group();
        content.getChildren().add(zoomGroup);
        zoomGroup.getChildren().add(scrollPane.getContent());
        scrollPane.setContent(content);
    }

    private void loadGraph() {
        try {
            graph = DataLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeNodeCombos() {
        List<ComboBox<Node>> comboBoxes = new ArrayList<>();
        Collections.addAll(comboBoxes, fromSelection, toSelection, wayPointSelection, avoidPointSelection);
        for(ComboBox<Node> comboBox : comboBoxes) {
            comboBox.getItems().addAll(graph.getNodes());
        }
    }

    private void zoom(Double value) {
        double h = scrollPane.getHvalue(), v = scrollPane.getVvalue();
        zoomGroup.setScaleX(value);
        zoomGroup.setScaleY(value);
        scrollPane.setHvalue(h);
        scrollPane.setVvalue(v);
    }

    private void updateListViews() {
        ObservableList<Node> wanted = FXCollections.observableArrayList(wayPoints);
        ObservableList<Node> avoid = FXCollections.observableArrayList(avoidPoints);
        wayPointList.setItems(wanted);
        avoidPointList.setItems(avoid);
    }

    private void updateDesiredNodes(ComboBox<Node> combo, List<Node> wanted, List<Node> opposite) {
        Node selection = combo.getSelectionModel().getSelectedItem();
        combo.getSelectionModel().clearSelection();
        opposite.remove(selection);
        if(!wanted.contains(selection) && selection != null) wanted.add(selection);
        updateListViews();
    }

    private void clear() {
        pane.getChildren().clear();
        updateListViews();
    }

    @FXML
    private void go() {
        Yen yen = new Yen(graph);
        Node from = fromSelection.getSelectionModel().getSelectedItem();
        Node to = toSelection.getSelectionModel().getSelectedItem();
        String type = measurementSelection.getSelectionModel().getSelectedItem();
        if(!valid(from, to, type)) return;
        suitablePaths = yen.kShortestPaths(from, to, 3, type, wayPoints, avoidPoints);
        selectedPath = suitablePaths.remove(0);
        drawSelectedRoute();
        setSelectedRouteNodes();
        updateRouteList();
    }

    private boolean valid(Node from, Node to, String type) {
        if(from == null || to == null || type == null) {
            showAlert("No empty selections allowed");
            return false;
        }
        if(from.equals(to)) {
            showAlert("Start and end points cannot be the same");
            return false;
        }
        return true;
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    @FXML
    private void updateSelectedRoute() {
        suitablePaths.add(selectedPath);
        selectedPath = routeList.getSelectionModel().getSelectedItem();
        suitablePaths.remove(selectedPath);
        updateRouteList();
        setSelectedRouteNodes();
        drawSelectedRoute();
    }

    private void setSelectedRouteNodes() {
        if(selectedPath != null) {
            ObservableList<Node> nodes = FXCollections.observableArrayList(selectedPath.getNodes());
            nodeList.setItems(nodes);
        }
    }

    private void updateRouteList() {
        suitablePaths.sort((p1, p2) -> (int) (p1.getCost() - p2.getCost()));
        ObservableList<Path> paths = FXCollections.observableArrayList(suitablePaths);
        routeList.setItems(paths);
    }

    private void drawSelectedRoute() {
        pane.getChildren().clear();
        pane.getChildren().addAll(selectedPath.drawCircles(Color.RED));
        pane.getChildren().addAll(selectedPath.drawLines(Color.RED));
    }

    @FXML
    private void addWayPoint() {
        updateDesiredNodes(wayPointSelection, wayPoints, avoidPoints);
    }

    @FXML
    private void removeWayPoint() {
        Node selection = wayPointList.getSelectionModel().getSelectedItem();
        wayPoints.remove(selection);
        updateListViews();
    }

    @FXML
    private void addAvoidPoint() {
        updateDesiredNodes(avoidPointSelection, avoidPoints, wayPoints);
    }

    @FXML
    private void removeAvoidPoint() {
        Node selection = avoidPointList.getSelectionModel().getSelectedItem();
        avoidPoints.remove(selection);
        updateListViews();
    }

    @FXML
    private void zoomIn() {
        double step = 0.1;
        double zoomMax = 1.5;
        zoomValue = (zoomValue + step > zoomMax) ? zoomMax : zoomValue + step;
        zoom(zoomValue);
    }

    @FXML
    private void zoomOut() {
        double step = 0.1;
        double zoomMin = 0.2161;
        zoomValue = (zoomValue - step < zoomMin) ? zoomMin : zoomValue - step;
        zoom(zoomValue);
    }

    //        Node source = graph.getNodes().get(0);
//        Node destination = graph.getNodes().get(3);
//        Node destination = graph.getNodes().get(44);

//        ArrayList<Node> contains = new ArrayList<>();
//        contains.add(graph.getNodes().get(8));
//        contains.add(graph.getNodes().get(17));
//        contains.add(graph.getNodes().get(26));

//        ArrayList<Node> avoidPoints = new ArrayList<>();
//
//        HashMap<String, List<Node>> map = new HashMap<>();
//        map.put("avoidPoints", avoidPoints);
//        map.put("contains", contains);
//

//        Dijkstra d = new Dijkstra(graph, "distance");
//        Path path = d.shortestPath(source, destination, map);

//        Yen yen = new Yen(graph);
//        List<Path> paths = yen.kShortestPaths(source, destination, 3, contains, avoidPoints);
//        List<Path> paths = yen.kShortestPaths(source, destination, 3, avoidPoints);

//        System.out.println(paths.size());


//        for(Path path : paths) {
//        pane.getChildren().addAll(path.drawCircles(Color.RED));
//        pane.getChildren().addAll(path.drawLines(Color.RED));
//        pane.getChildren().addAll(paths.get(0).drawCircles(Color.RED));
//        pane.getChildren().addAll(paths.get(0).drawLines(Color.RED));
//        pane.getChildren().addAll(paths.get(1).drawCircles(Color.BLUE));
//        pane.getChildren().addAll(paths.get(1).drawLines(Color.BLUE));
//        pane.getChildren().addAll(paths.get(2).drawCircles(Color.BLACK));
//        pane.getChildren().addAll(paths.get(2).drawLines(Color.BLACK));
//        }

//


}
