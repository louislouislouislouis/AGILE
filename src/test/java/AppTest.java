import org.hexanome.data.MapDeserializer;
import org.hexanome.model.Intersection;
import org.hexanome.model.MapIF;
import org.hexanome.model.Segment;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;

public class AppTest {
    // TODO : Test du parser XML
    @Test
    public void testXML() {
        MapDeserializer md = new MapDeserializer();
        MapIF map = new MapIF();

        try {
            File fileXml = new File("src/main/resources/org/hexanome/model/testMap.xml");
            md.load(map, fileXml);

            // Test quantitatif
            assertEquals("Nombre de segments incorrect", map.getSegments().size(), 2);
            assertEquals("Nombre d'intersections incorrect", map.getIntersections().size(), 4);

            //Test qualitatif
            Segment rueDanton = map.getSegments().get("Rue Danton");
            assertEquals("Longueur de la Rue Danton", rueDanton.getLength(), 69.979805, 0.01);
            assertEquals("Destination de la Rue Danton", rueDanton.getDestinationIntersection().getIdIntersection(), 26086130);
            assertEquals("Origine de la Rue Danton", rueDanton.getOriginIntersection().getIdIntersection(), 25175791);
            Segment rueAbondance = map.getSegments().get("Rue de l'Abondance");
            assertEquals("Longueur de la Rue de l'Abondance", rueAbondance.getLength(), 136.00636, 0.01);
            assertEquals("Destination de la Rue de l'Abondance", rueAbondance.getDestinationIntersection().getIdIntersection(), 2129259178);
            assertEquals("Origine de la Rue de l'Abondance", rueAbondance.getOriginIntersection().getIdIntersection(), 25175791);


        } catch (Exception e) {
            System.err.println(e);
        }
    }

}
