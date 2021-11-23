import org.hexanome.data.MapDeserializer;
import org.hexanome.model.Intersection;
import org.hexanome.model.MapIF;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class AppTest {
    @Test
    public void testXML() {
        MapDeserializer md = new MapDeserializer();
        MapIF map = new MapIF();

        try {
            File fileXml = new File("src/test/xml/testMap.xml");
            md.load(map, fileXml);

            // Test quantitatif
            assertEquals("Nombre de segments incorrect", map.getSegments().size(), 2);
            assertEquals("Nombre d'intersections incorrect", map.getIntersections().size(), 4);

            //Test des segments
            map.getSegments().forEach(((uuid, segment) -> {
                if (segment.getName().equals("Rue Danton")) {
                    assertEquals("Longueur de la Rue Danton", segment.getLength(), 69.979805, 0.01);
                    assertEquals("Destination de la Rue Danton", segment.getDestinationIntersection().getIdIntersection(), 26086130);
                    assertEquals("Origine de la Rue Danton", segment.getOriginIntersection().getIdIntersection(), 25175791);
                } else if (segment.getName().equals("Rue de l'Abondance")) {
                    assertEquals("Longueur de la Rue de l'Abondance", segment.getLength(), 136.00636, 0.01);
                    assertEquals("Destination de la Rue de l'Abondance", segment.getDestinationIntersection().getIdIntersection(), 2129259178);
                    assertEquals("Origine de la Rue de l'Abondance", segment.getOriginIntersection().getIdIntersection(), 25175791);
                }
            }));

            //Test des intersections
            double delta = 0.00001;
            Intersection i1 = map.getIntersections().get((long) 25175791);
            assertNotEquals("Intersection 25175791 is null ", i1, null);
            assertEquals("Intersection 25175791 latitude", i1.getLatitude(), 45.75406, delta);
            assertEquals("Intersection 25175791 longitude", i1.getLongitude(), 4.857418, delta);
            Intersection i2 = map.getIntersections().get((long) 2129259178);
            assertNotEquals("Intersection 2129259178 is null", i2, null);
            assertEquals("Intersection 2129259178 latitude", i2.getLatitude(), 45.750404, delta);
            assertEquals("Intersection 2129259178 longitude", i2.getLongitude(), 4.8744674, delta);
            Intersection i3 = map.getIntersections().get((long) 26086130);
            assertNotEquals("Intersection 26086130 is null", i3, null);
            assertEquals("Intersection 26086130 latitude", i3.getLatitude(), 45.75871, delta);
            assertEquals("Intersection 26086130 longitude", i3.getLongitude(), 4.8704023, delta);
            Intersection i4 = map.getIntersections().get((long) 2129259176);
            assertNotEquals("Intersection 2129259176 is null", i4, null);
            assertEquals("Intersection 2129259176 latitude", i4.getLatitude(), 45.75171, delta);
            assertEquals("Intersection 2129259176 longitude", i4.getLongitude(), 4.8718166, delta);

        } catch (Exception e) {
            fail("An exception occurred");
            System.err.println(e);
        }
    }
}
