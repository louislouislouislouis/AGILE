import org.hexanome.data.MapDeserializer;
import org.hexanome.model.MapIF;
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
            assertEquals(map.getSegments().size(), 2);
            assertEquals(map.getIntersections().size(), 4);

            //Test qualitatif

        } catch (Exception e) {
            System.err.println(e);
        }
    }

}
