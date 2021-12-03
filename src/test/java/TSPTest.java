import org.hexanome.model.Intersection;
import org.hexanome.model.MapIF;
import org.hexanome.model.PlanningRequest;
import org.hexanome.model.Segment;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.*;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

import static org.junit.Assert.*;

public class TSPTest {
    protected static HashMap<Long, Intersection> intersections;
    protected static HashMap<UUID, Segment> segments;
    protected static MapIF map;
    protected static PlanningRequest planning

    @BeforeClass
    public static void setUp() {
        String path = "src/test/tsplib/a280.tsp";
        File a280 = new File(path);
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
            intersections.forEach((id1, intersection1) -> {
                intersections.forEach((id2, intersection2) -> {
                    if (!Objects.equals(id1, id2)) {
                        segments.put(UUID.randomUUID(), new Segment(intersection1, intersection2, "Rue de Clignancourt", 42.0));
                    }
                });
            });

            map = new MapIF(intersections, segments);

            // Génération du planning


        } catch (IOException e) {
            e.printStackTrace();
            fail("Could not open " + path);
        }
    }

    @Test
    public void testTSP() {
        System.out.println("[TSPTest] fumier");
    }

}
