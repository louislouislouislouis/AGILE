import javafx.scene.paint.Color;
import org.hexanome.controller.MainsScreenController;
import org.hexanome.controller.tsp.GraphAPI;
import org.hexanome.model.*;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.UUID;

import static org.junit.Assert.*;

public class TSPTest {
    protected static HashMap<Long, Intersection> intersections;
    protected static HashMap<UUID, Segment> segments;
    protected static MapIF map;
    protected static PlanningRequest planning;

    @BeforeClass
    public static void setUp() {
        intersections = new HashMap<>();
        intersections.put(1L, new Intersection(0.0, 0.0, 1));
        intersections.put(2L, new Intersection(0.0, 0.0, 2));
        intersections.put(3L, new Intersection(0.0, 0.0, 3));
        intersections.put(4L, new Intersection(0.0, 0.0, 4));

        segments.put(UUID.randomUUID(), new Segment(intersections.get(1L), intersections.get(2L), "", 10.0));
        segments.put(UUID.randomUUID(), new Segment(intersections.get(2L), intersections.get(1L), "", 10.0));

        segments.put(UUID.randomUUID(), new Segment(intersections.get(1L), intersections.get(4L), "", 20.0));
        segments.put(UUID.randomUUID(), new Segment(intersections.get(4L), intersections.get(1L), "", 20.0));

        segments.put(UUID.randomUUID(), new Segment(intersections.get(1L), intersections.get(3L), "", 15.0));
        segments.put(UUID.randomUUID(), new Segment(intersections.get(3L), intersections.get(1L), "", 15.0));

        segments.put(UUID.randomUUID(), new Segment(intersections.get(2L), intersections.get(4L), "", 25.0));
        segments.put(UUID.randomUUID(), new Segment(intersections.get(4L), intersections.get(2L), "", 25.0));

        segments.put(UUID.randomUUID(), new Segment(intersections.get(2L), intersections.get(3L), "", 35.0));
        segments.put(UUID.randomUUID(), new Segment(intersections.get(3L), intersections.get(2L), "", 35.0));

        segments.put(UUID.randomUUID(), new Segment(intersections.get(3L), intersections.get(4L), "", 30.0));
        segments.put(UUID.randomUUID(), new Segment(intersections.get(4L), intersections.get(3L), "", 30.0));

        map = new MapIF(intersections, segments);
        map.setAdj();

        PickupPoint pp1 = new PickupPoint(intersections.get(1L), 0, Color.RED);
        PickupPoint pp2 = new PickupPoint(intersections.get(2L), 0, Color.RED);
        DeliveryPoint dp1 = new DeliveryPoint(intersections.get(3L), 0, Color.RED);
        DeliveryPoint dp2 = new DeliveryPoint(intersections.get(4L), 0, Color.RED);
        Request r1 = new Request(pp1, dp1);
        Request r2 = new Request(pp2, dp2);
        Warehouse w = new Warehouse(LocalTime.NOON, intersections.get(1L), Color.BLACK);

        planning = new PlanningRequest();
        planning.setWarehouse(w);
        planning.addRequest(r1);
        planning.addRequest(r2);

    }

    @Test(timeout = 120000L)
    public void testTSP() {
        GraphAPI ga = new GraphAPI();
        Tour t = new Tour();
        t.setDepartureTime(LocalTime.MIDNIGHT);
        MainsScreenController controller = new MainsScreenController();
        controller.setAllowcalculation(true);
        ga.V1_TSP(planning, map, t, controller);
        t.calculateCost(map);
        assertEquals(t.getIntersections().size(), 5);
        assertEquals(t.getCost(), 80.0, 0.001);
        assertEquals(t.getIntersections().get(1), intersections.get(1L));
        assertEquals(t.getIntersections().get(2), intersections.get(2L));
        assertEquals(t.getIntersections().get(3), intersections.get(4L));
        assertEquals(t.getIntersections().get(4), intersections.get(3L));
        assertEquals(t.getIntersections().get(5), intersections.get(1L));
    }

}
