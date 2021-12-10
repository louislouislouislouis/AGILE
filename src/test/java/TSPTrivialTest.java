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

        Segment s1 = new Segment(i1, i2, "Rue fumier", 42.0);
        Segment s2 = new Segment(i3, i1, "Rue Onche", 21.0);
        Pair<UUID, Segment> p1 = new Pair<>(UUID.randomUUID(), s1);
        Pair<UUID, Segment> p2 = new Pair<>(UUID.randomUUID(), s2);
        segments.put(p1.getKey(), p1.getValue());
        segments.put(p2.getKey(), p2.getValue());

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
        assertEquals("Erreur de calcul du coût", 126.0, t.getCost(), delta);
        assertEquals(i1, t.getIntersections().get(0));
        assertEquals(i2, t.getIntersections().get(1));
    }
}
