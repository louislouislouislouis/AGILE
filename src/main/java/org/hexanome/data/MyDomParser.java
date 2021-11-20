package org.hexanome.data;

import org.hexanome.model.Intersection;
import org.hexanome.model.MapIF;
import org.hexanome.model.Segment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;

public class MyDomParser {

    public MyDomParser() {
    }

    public static MapIF parseMap(String fileURI) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(fileURI);

        NodeList intersections = document.getElementsByTagName("intersection");
        NodeList segments = document.getElementsByTagName("segment");

        HashMap<Long, Intersection> intersectionsHashMap = new HashMap<>();
        HashMap<String, Segment> segmentsHashMap = new HashMap<>();

        // creation des objets de type intersection
        for (int i = 0; i < intersections.getLength(); i++) {
            Node node = intersections.item(i);
            Element element = (Element) node;
            long id = Long.parseLong(element.getAttribute("id"));
            double longitude = Double.parseDouble(element.getAttribute("longitude"));
            double latitude = Double.parseDouble(element.getAttribute("latitude"));
            Intersection intersection = new Intersection(longitude, latitude, id);
            intersectionsHashMap.put(id, intersection);
        }

        // creation des objets de type segment
        for (int i = 0; i < segments.getLength(); i++) {
            Node node = segments.item(i);
            Element element = (Element) node;
            long originId = Long.parseLong(element.getAttribute("origin"));
            long destinationId = Long.parseLong(element.getAttribute("destination"));
            double length = Double.parseDouble(element.getAttribute("length"));
            String name = element.getAttribute("name");
            Intersection originIntersection = intersectionsHashMap.get(originId);
            Intersection destinationIntersection = intersectionsHashMap.get(destinationId);
            Segment segment = new Segment(originIntersection, destinationIntersection, name, length);
            segmentsHashMap.put(name, segment);
        }

        MapIF map = new MapIF(intersectionsHashMap, segmentsHashMap);

        return map;
    }

    public static void main(String args[]) {
        MyDomParser mydom = new MyDomParser();

        try {
            MapIF map = parseMap("src/main/java/org/hexanome/Model/testMap.xml");
            System.out.println(map.toString());
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }
}
