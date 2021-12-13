package org.hexanome.data;

import org.hexanome.model.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.lang.*;

/**
 * This class is used to parse xml files containing a representation of a map with segments and intersections
 *
 * @author Gastronom'if
 */
public class MapDeserializer {
    /**
     * Open an XML file and create plan from this file
     *
     * @param map the plan to create from the file
     * @param xml file that contain the xml
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws ExceptionXML                 exceptions that come from a malformed xml file
     */
    public void load(MapIF map, File xml) throws ParserConfigurationException, SAXException, IOException, ExceptionXML {
        DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = docBuilder.parse(xml);
        Element root = document.getDocumentElement();
        if (root.getNodeName().equals("map")) {
            buildFromDOMXML(root, map);
        } else
            throw new ExceptionXML("Wrong format");
    }

    private void buildFromDOMXML(Element noeudDOMRacine, MapIF map) throws ExceptionXML, NumberFormatException {
        NodeList intersections = noeudDOMRacine.getElementsByTagName("intersection");
        NodeList segments = noeudDOMRacine.getElementsByTagName("segment");

        for (int i = 0; i < intersections.getLength(); i++) {
            map.addIntersection(createIntersection((Element) intersections.item(i)));
        }

        for (int i = 0; i < segments.getLength(); i++) {
            UUID uuid = UUID.randomUUID();
            map.addSegment(uuid, createSegment((Element) segments.item(i), map.getIntersections()));
        }

        map.setAdj();
    }

    /**
     * This private method create an intersection from an element
     *
     * @param elt current element
     * @return it returns an Intersection
     */
    private Intersection createIntersection(Element elt) throws ExceptionXML {
        double longitude = Double.parseDouble(elt.getAttribute("longitude"));
        if (longitude < 0)
            throw new ExceptionXML("Error when reading file: Longitude must be positive");

        double latitude = Double.parseDouble(elt.getAttribute("latitude"));
        if (latitude < 0)
            throw new ExceptionXML("Error when reading file: Latitude must be positive");

        long idIntersection = Long.parseLong(elt.getAttribute("id"));
        if (idIntersection < 0)
            throw new ExceptionXML("Error when reading file: Id must be positive");

        return new Intersection(longitude, latitude, idIntersection);
    }

    /**
     * This private method create a segment from an element and a list of intersections
     *
     * @param elt           current element
     * @param intersections list of intersection composing the segment
     * @return it returns a segment
     */
    private Segment createSegment(Element elt, HashMap<Long, Intersection> intersections) throws ExceptionXML {
        long originId = Long.parseLong(elt.getAttribute("origin"));
        if (originId < 0)
            throw new ExceptionXML("Error when reading file: origin Id must be positive");

        Intersection originIntersection = intersections.get(originId);

        long destinationId = Long.parseLong(elt.getAttribute("destination"));
        if (destinationId < 0)
            throw new ExceptionXML("Error when reading file: destination Id must be positive");

        Intersection destinationIntersection = intersections.get(destinationId);

        double length = Double.parseDouble(elt.getAttribute("length"));
        if (length < 0)
            throw new ExceptionXML("Error when reading file: length Id must be positive");

        String name = elt.getAttribute("name");

        return new Segment(originIntersection, destinationIntersection, name, length);
    }
}
