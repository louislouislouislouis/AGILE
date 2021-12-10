import javafx.scene.paint.Color;
import org.hexanome.controller.MainsScreenController;
import org.hexanome.controller.tsp.GraphAPI;
import org.hexanome.model.*;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.*;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

import static java.lang.Long.parseLong;
import static org.junit.Assert.*;

public class TSPTest {
    protected static HashMap<Long, Intersection> intersections;
    protected static HashMap<UUID, Segment> segments;
    protected static MapIF map;
    protected static PlanningRequest planning;
    protected static Long[] solution;

    /**
     * @param i1 première intersection
     * @param i2 seconde intersection
     * @return distance euclidienne entre les deux intersections
     */
    public static double distanceEuclidienne(Intersection i1, Intersection i2) {
        double lat1 = i1.getLatitude();
        double lat2 = i2.getLatitude();
        double long1 = i1.getLongitude();
        double long2 = i2.getLongitude();

        return Math.sqrt((lat1 - lat2) * (lat1 - lat2) + (long1 - long2) * (long1 - long2));
    }

    @BeforeClass
    public static void setUp() {
        String path = "src/test/tsplib/ulysses16.tsp";
        int nbInter = 11;
        int nbReq = 5;
        try {
            FileReader fr = new FileReader(path);
            BufferedReader br = new BufferedReader(fr);

            // Lecture des intersections à partir du fichier
            intersections = new HashMap<>();
            for (int i = 1; i <= nbInter; i++) {
                String[] line = br.readLine().split(" ");
                assertEquals("Problème de formatage du fichier " + path, line.length, 3);
                intersections.put((long) i, new Intersection(Double.parseDouble(line[2]), Double.parseDouble(line[1]), i));
            }

            // Relier toutes les intersections
            segments = new HashMap<>();
            intersections.forEach((id1, intersection1) -> intersections.forEach((id2, intersection2) -> {
                if (!Objects.equals(id1, id2)) {
                    double distance = distanceEuclidienne(intersection1, intersection2);
                    Segment s = new Segment(intersection1, intersection2, "", distance);
                    segments.put(UUID.randomUUID(), s);
                }
            }));

            map = new MapIF(intersections, segments);
            map.setAdj();

            // Génération du planning
            planning = new PlanningRequest();
            Intersection warehouseIntersection = intersections.get(1L);
            Warehouse warehouse = new Warehouse(LocalTime.MIDNIGHT, warehouseIntersection, Color.BLACK);
            planning.setWarehouse(warehouse);
            PickupPoint[] pickupList = new PickupPoint[nbReq];
            DeliveryPoint[] deliveryList = new DeliveryPoint[nbReq];
            int iPickup = 0;
            int iDelivery = 0;
            for (long i = 2; i <= nbInter; i++) {
                Intersection curr = intersections.get(i);
                if (curr.getIdIntersection() <= 6) {
                    pickupList[iPickup++] = new PickupPoint(curr, 0, Color.RED);
                } else if (curr.getIdIntersection() > 6) {
                    deliveryList[iDelivery++] = new DeliveryPoint(curr, 0, Color.RED);
                }
            }

            for (int i = 0; i < nbInter / 2; ++i) {
                planning.addRequest(new Request(pickupList[i], deliveryList[i]));
            }

            // Lecture du fichier solution
            String pathSol = path.replace(".tsp", ".opt.tour");
            FileReader frSol = new FileReader(pathSol);
            BufferedReader brSol = new BufferedReader(frSol);
            solution = new Long[nbInter];
            for (int i = 0; i < nbInter; ++i) {
                solution[i] = parseLong(brSol.readLine());
            }
            assertEquals((long) solution[0], 1);
            assertEquals((long) solution[6], 11);

        } catch (IOException e) {
            e.printStackTrace();
            fail("Could not open " + path);
        }
    }

    @Test(timeout = 120000L)
    public void testTSP() {
        GraphAPI ga = new GraphAPI();
        Tour t = new Tour();
        t.setDepartureTime(LocalTime.MIDNIGHT);
        MainsScreenController controller = new MainsScreenController();
        controller.setAllowcalculation(true);
        ga.V1_TSP(planning, map, t, controller);
        assertEquals(t.getIntersections().size(), 12);

        for (int i = 0; i < solution.length; ++i) {
            System.out.println("Solution : " + solution[i] + " Actual : " + t.getIntersections().get(i).getIdIntersection());
            //assertEquals((long) solution[i], t.getIntersections().get(i).getIdIntersection());
        }
    }

}
