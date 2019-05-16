
import com.got.app.collections.Dijkstra;
import com.got.app.collections.Graph;
import com.got.app.collections.Node;
import com.got.app.collections.Path;
import com.got.app.helpers.DataLoader;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class DijsktraTest {

    private Graph graph;
    private Node start;
    private Node end;

    @Before
    public void setup() throws Exception{
        graph = DataLoader.load();
        start = graph.getNodes().get(0);
        end = graph.getNodes().get(27);
    }

    @Test
    public void shortestPathContainsStartNode() {
        Dijkstra d = new Dijkstra(graph, "distance");
        Path path = d.shortestPath(start, end, new ArrayList<>());
        assertTrue(path.getNodes().contains(start));
    }

    @Test
    public void shortestPathContainsEndNode() {
        Dijkstra d = new Dijkstra(graph, "distance");
        Path path = d.shortestPath(start, end, new ArrayList<>());
        assertTrue(path.getNodes().contains(end));
    }

    @Test
    public void shortestPathWithAvoidNodeDoesNotContainThatNode() {
        Dijkstra d = new Dijkstra(graph, "distance");
        List<Node> avoidList = new ArrayList<>();
        Node avoid = graph.getNodes().get(3);
        avoidList.add(avoid);
        Path path = d.shortestPath(start, end, avoidList);
        assertFalse(path.getNodes().contains(avoid));
    }

    @Test
    public void shortestPathWithWaypointContainThatNode() {
        Dijkstra d = new Dijkstra(graph, "distance");
        List<Node> waypointList = new ArrayList<>();
        Node waypoint = graph.getNodes().get(3);
        waypointList.add(waypoint);
        Path path = d.shortestPath(start, end, waypointList, new ArrayList<>());
        assertTrue(path.getNodes().contains(waypoint));
    }


}
