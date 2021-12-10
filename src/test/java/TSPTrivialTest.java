import javafx.scene.paint.Color;
import javafx.util.Pair;
import org.hexanome.controller.MainsScreenController;
import org.hexanome.controller.tsp.GraphAPI;
import org.hexanome.model.*;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.time.LocalTime;
import java.util.UUID;

public class TSPTrivialTest {
    private static MapIF map;
    private static PlanningRequest planning;
    private static Intersection i1;
    private static Intersection i2;
    private static Intersection i3;

    @BeforeClass
    public static void setUp() {
        HashMap<Long, Intersection> intersections = new HashMap<>();
        HashMap<UUID, Segment> segments = new HashMap<>();
        i1 = new Intersection(0.0, 1.0, 1);
        i2 = new Intersection(1.0, 1.0, 2);
        i3 = new Intersection(0.0, 0.0, 3);

        intersections.put(1L, i1);
        intersections.put(2L, i2);
        intersections.put(3L, i3);

        Segment s1 = new Segment(i1, i2, "Rue fumier", 5.0);
        Segment s3 = new Segment(i2, i1, "Rue fumier", 5.0);
        Segment s2 = new Segment(i3, i1, "Rue Onche", 10.0);
        Segment s4 = new Segment(i1, i3, "Rue Onche", 10.0);
        Segment s5 = new Segment(i2, i3, "Rue Schlagetto", 9.0);
        Segment s6 = new Segment(i3, i2, "Rue Schlagetto", 9.0);
        Pair<UUID, Segment> p1 = new Pair<>(UUID.randomUUID(), s1);
        Pair<UUID, Segment> p2 = new Pair<>(UUID.randomUUID(), s2);
        Pair<UUID, Segment> p3 = new Pair<>(UUID.randomUUID(), s3);
        Pair<UUID, Segment> p4 = new Pair<>(UUID.randomUUID(), s4);
        Pair<UUID, Segment> p5 = new Pair<>(UUID.randomUUID(), s5);
        Pair<UUID, Segment> p6 = new Pair<>(UUID.randomUUID(), s6);
        segments.put(p1.getKey(), p1.getValue());
        segments.put(p2.getKey(), p2.getValue());
        segments.put(p3.getKey(), p3.getValue());
        segments.put(p4.getKey(), p4.getValue());
        segments.put(p5.getKey(), p5.getValue());
        segments.put(p6.getKey(), p6.getValue());

        map = new MapIF(intersections, segments);

        map.setAdj();

        PickupPoint pp = new PickupPoint(i1, 10, Color.RED);
        DeliveryPoint dp = new DeliveryPoint(i2, 15, Color.RED);
        Request r = new Request(pp, dp);
        Warehouse w = new Warehouse(LocalTime.NOON, i3, Color.BLACK);

        planning = new PlanningRequest();
        planning.setWarehouse(w);
        planning.addRequest(r);
    }

    @Test
    public void testTSP() {
        double delta = 0.0000001;
        GraphAPI ga = new GraphAPI();
        Tour t = new Tour();
        MainsScreenController controller = new MainsScreenController();
        t.setDepartureTime(LocalTime.MIDNIGHT);
        ga.V1_TSP(planning, map, t, controller);
        t.calculateCost(map);
        assertEquals("Erreur de calcul du coût", 24.0, t.getCost(), delta);
        assertEquals(i3, t.getIntersections().get(0));
        assertEquals(i1, t.getIntersections().get(1));
        assertEquals(i2, t.getIntersections().get(2));
        assertEquals(i3, t.getIntersections().get(3));
    }
}
