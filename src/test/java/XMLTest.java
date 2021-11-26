import org.hexanome.data.ExceptionXML;
import org.hexanome.data.MapDeserializer;
import org.hexanome.data.RequestDeserializer;
import org.hexanome.model.Intersection;
import org.hexanome.model.MapIF;
import org.hexanome.model.PlanningRequest;
import org.hexanome.model.Warehouse;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class XMLTest {
    protected static MapIF map;
    protected static MapDeserializer md;
    protected static RequestDeserializer rd;

    @BeforeClass
    public static void loadMap() {
        File mapXml = new File("src/test/xml/testMap.xml");
        map = new MapIF();
        md = new MapDeserializer();
        try {
            md.load(map, mapXml);
        } catch (ParserConfigurationException | SAXException | IOException | ExceptionXML e) {
            e.printStackTrace();
            fail("[loadMap] An exception occured");
        }
    }

    @Test
    public void mapParsing() {
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
        assertNotNull("Intersection 25175791 is null", i1);
        assertEquals("Intersection 25175791 latitude", i1.getLatitude(), 45.75406, delta);
        assertEquals("Intersection 25175791 longitude", i1.getLongitude(), 4.857418, delta);
        Intersection i2 = map.getIntersections().get((long) 2129259178);
        assertNotNull("Intersection 2129259178 is null", i2);
        assertEquals("Intersection 2129259178 latitude", i2.getLatitude(), 45.750404, delta);
        assertEquals("Intersection 2129259178 longitude", i2.getLongitude(), 4.8744674, delta);
        Intersection i3 = map.getIntersections().get((long) 26086130);
        assertNotNull("Intersection 26086130 is null", i3);
        assertEquals("Intersection 26086130 latitude", i3.getLatitude(), 45.75871, delta);
        assertEquals("Intersection 26086130 longitude", i3.getLongitude(), 4.8704023, delta);
        Intersection i4 = map.getIntersections().get((long) 2129259176);
        assertNotNull("Intersection 2129259176 is null", i4);
        assertEquals("Intersection 2129259176 latitude", i4.getLatitude(), 45.75171, delta);
        assertEquals("Intersection 2129259176 longitude", i4.getLongitude(), 4.8718166, delta);

    }

    @Test
    public void requestsParsing() {
        File xml = new File("src/test/xml/testReq.xml");
        RequestDeserializer rd = new RequestDeserializer();
        MapIF mapIF = new MapIF();
        PlanningRequest planning = new PlanningRequest();

        try {
            planning.clearPlanning();
            rd.load(planning, xml, mapIF);

            Warehouse w = planning.getWarehouse();
            assertEquals("Warehouse", w.toString(), "Warehouse{departureTime=8:0:0Point=");
        } catch (ParserConfigurationException | SAXException | ExceptionXML | IOException e) {
            e.printStackTrace();
            fail("[requestsParsing] An exception occured");
        }
    }

    @Test(expected = ExceptionXML.class)
    public void negativeIntersectionId() throws ExceptionXML, ParserConfigurationException, IOException, SAXException {
        File idNeg = new File("src/test/xml/corrupt/idNeg.xml");
        MapDeserializer md = new MapDeserializer();
        MapIF map = new MapIF();
        md.load(map, idNeg);
    }

    @Test(expected = ExceptionXML.class)
    public void negativeLatitude() throws ExceptionXML, ParserConfigurationException, IOException, SAXException {
        File negLat = new File("src/test/xml/corrupt/negLat.xml");
        MapDeserializer md = new MapDeserializer();
        MapIF map = new MapIF();
        md.load(map, negLat);
    }

    @Test(expected = ExceptionXML.class)
    public void negativeLongitude() throws ExceptionXML, ParserConfigurationException, IOException, SAXException {
        File negLong = new File("src/test/xml/corrupt/negLong.xml");
        MapDeserializer md = new MapDeserializer();
        MapIF map = new MapIF();
        md.load(map, negLong);
    }
}
