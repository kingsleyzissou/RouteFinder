
import com.got.app.collections.*;
import com.got.app.helpers.DataLoader;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class YenTest {

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
    public void kShortestPathsAllContainStartNode() {
        Yen yen = new Yen(graph, "distance");
        List<Path> pathList = yen.kShortestPaths(start, end, 3, new ArrayList<>());
        boolean test = true;
        for(Path path : pathList)
            test = (path.getNodes().contains(start) && test);
        assertTrue(test);
    }

    @Test
    public void shortestPathContainsEndNode() {
        Yen yen = new Yen(graph, "distance");
        List<Path> pathList = yen.kShortestPaths(start, end, 3, new ArrayList<>());
        boolean test = true;
        for(Path path : pathList)
            test = (path.getNodes().contains(end) && test);
        assertTrue(test);
    }

    @Test
    public void shortestPathWithAvoidNodeDoesNotContainThatNode() {
        Yen yen = new Yen(graph, "distance");
        List<Node> avoidList = new ArrayList<>();
        Node avoid = graph.getNodes().get(3);
        avoidList.add(avoid);
        List<Path> pathList = yen.kShortestPaths(start, end, 3, avoidList);
        boolean test = true;
        for(Path path : pathList)
            test = (path.getNodes().contains(avoid) && test);
        assertFalse(test);
    }

    @Test
    public void shortestPathWithWaypointContainThatNode() {
        Yen yen = new Yen(graph, "distance");
        List<Node> waypointList = new ArrayList<>();
        Node waypoint = graph.getNodes().get(3);
        waypointList.add(waypoint);
        List<Path> pathList = yen.kShortestPaths(start, end, 3, waypointList, new ArrayList<>());
        boolean test = true;
        for(Path path : pathList)
            test = (path.getNodes().contains(waypoint) && test);
        assertTrue(test);
    }


}
