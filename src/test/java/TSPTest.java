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
        String path = "src/test/tsplib/a280.tsp";
        try {
            FileReader fr = new FileReader(path);
            BufferedReader br = new BufferedReader(fr);
            for (int i = 0; i < 6; ++i) {
                br.readLine();
            }

            // Lecture des intersections à partir du fichier
            intersections = new HashMap<>();
            for (int i = 1; i <= 280; i++) {
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
                    if ((id1 == 1 && id2 == 2) || (id1 == 2 && id2 == 1)) {
                        assertEquals(distance, 20.0, 0.0001);
                    } else if ((id1 == 1 && id2 == 4) || (id1 == 4 && id2 == 1)) {
                        assertEquals(distance, 32.984845004941285, 0.01);
                    }
                }
            }));

            map = new MapIF(intersections, segments);
            map.setAdj();

            // Génération du planning
            planning = new PlanningRequest();
            Intersection warehouseIntersection = intersections.get(1L);
            System.out.println(warehouseIntersection);
            Warehouse warehouse = new Warehouse(LocalTime.MIDNIGHT, warehouseIntersection, Color.BLACK);
            planning.setWarehouse(warehouse);
            intersections.forEach((id, intersection) -> {
                PickupPoint pp = new PickupPoint(warehouseIntersection, 0, Color.RED);
                DeliveryPoint dp = new DeliveryPoint(intersection, 0, Color.RED);
                planning.addRequest(new Request(pp, dp));
            });

            // Lecture du fichier solution
            String pathSol = path.replace(".tsp", ".opt.tour");
            FileReader frSol = new FileReader(pathSol);
            BufferedReader brSol = new BufferedReader(frSol);
            for (int i = 0; i < 4; ++i) {
                brSol.readLine();
            }
            solution = new Long[280];
            for (int i = 0; i < 280; ++i) {
                solution[i] = parseLong(brSol.readLine());
            }
            assertEquals((long) solution[0], 1);
            assertEquals((long) solution[17], 245);
            assertEquals((long) solution[279], 280);
            assertEquals((long) solution[195], 36);
            assertEquals((long) solution[141], 115);
            assertEquals((long) solution[100], 162);
            assertEquals((long) solution[11], 235);

        } catch (IOException e) {
            e.printStackTrace();
            fail("Could not open " + path);
        }
    }

    @Test
    public void testTSP() {
        GraphAPI ga = new GraphAPI();
        Tour t = new Tour();
        t.setDepartureTime(LocalTime.MIDNIGHT);
        MainsScreenController controller = new MainsScreenController();
        ga.V1_TSP(planning, map, t, controller);
        assertEquals(t.getIntersections().size(), 281);

        for (int i = 0; i < solution.length; ++i) {
            assertEquals((long) solution[i], t.getIntersections().get(i).getIdIntersection());
        }
    }

}
