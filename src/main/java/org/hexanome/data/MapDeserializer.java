package org.hexanome.data;

import org.hexanome.controller.tsp.CompleteGraph;
import org.hexanome.controller.tsp.Graph;
import org.hexanome.model.*;
import org.hexanome.controller.Dijkstra;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.lang.*;

public class MapDeserializer {
    /**
     * Open an XML file and create plan from this file
     *
     * @param map the plan to create from the file
     * @param xml file that contain the xml
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws ExceptionXML
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
    }

    private Intersection createIntersection(Element elt) throws ExceptionXML {
        double longitude = Double.parseDouble(elt.getAttribute("longitude"));
        if (longitude < 0)
            throw new ExceptionXML("Error when reading file: Longitude must be positive");

        double latitude = Double.parseDouble(elt.getAttribute("latitude"));
        if (latitude < 0)
            throw new ExceptionXML("Error when reading file: Latitude must be positive");

        Long idIntersection = Long.parseLong(elt.getAttribute("id"));
        if (idIntersection < 0)
            throw new ExceptionXML("Error when reading file: Id must be positive");

        return new Intersection(longitude, latitude, idIntersection);
    }

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

    /**
     * @param intersections intersections in the map
     * @param segments      amount of all segments in the map
     * @return map with key: startIntersection,
     * value: map with key: neighbourIntersection,
     * value: segment between startIntersection and neighbourIntersection
     */
    private Map<Intersection, Map<Intersection, Segment>> getAdj(Map<Long, Intersection> intersections, Map<UUID, Segment> segments) {
        Map<Intersection, Map<Intersection, Segment>> adj = new HashMap<>();
        for (Intersection i : intersections.values()) {
            Map<Intersection, Segment> emptyMap = new HashMap<>();
            adj.put(i, emptyMap);
        }
        for (Segment s : segments.values()) {
            adj.get(s.getOriginIntersection()).put(s.getDestinationIntersection(), s);
        }
        return adj;
    }

    public static void main(String args[]) {
        MapDeserializer mydom = new MapDeserializer();
        RequestDeserializer mydomrequest = new RequestDeserializer();
        PlanningRequest planning = new PlanningRequest();
        MapIF map = new MapIF();

        try {
            File fileXml = new File("src/main/resources/org/hexanome/model/testMap.xml");
            mydom.load(map, fileXml);

            File requestFile = new File("src/main/resources/org/hexanome/model/testRequest.xml");
            mydomrequest.load(planning, requestFile, map);

            Map<Intersection, Map<Intersection, Segment>> adj = mydom.getAdj(map.getIntersections(), map.getSegments());

            Set<Intersection> destinations = new HashSet<>();
            destinations.add(planning.getWarehouse().getAddress());
            for (Request r : planning.getRequests()) {
                destinations.add(r.getPickupPoint().getAddress());
                destinations.add(r.getDeliveryPoint().getAddress());
            }

            int nbVerticesDijkstra = map.getIntersections().size();
            int nbVerticesTSP = destinations.size();

            Double[][] costTSP = new Double[nbVerticesTSP][nbVerticesTSP];
            int iOrigin = 0;
            for (Intersection origin : destinations) {
                System.out.println("Calculating shortest paths for Origin: " + origin.getIdIntersection());
                Dijkstra dijkstra = new Dijkstra(nbVerticesDijkstra);
                dijkstra.dijkstra(map.getIntersections(), adj, origin, destinations);
                System.out.println(dijkstra.getPath());
                System.out.println(dijkstra.getDist());

                Map<Long, Double> distTSP = dijkstra.getResultDistForTSP(destinations);
                int j = 0;
                for (Double c : distTSP.values()) {
                    costTSP[iOrigin][j++] = c;
                }
                iOrigin++;
            }

            for (int i = 0; i < nbVerticesTSP; i++) {
                for (int j = 0; j < nbVerticesTSP; j++) {
                    System.out.print(costTSP[i][j] + " ");
                }
                System.out.println();
            }


        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ExceptionXML exceptionXML) {
            exceptionXML.printStackTrace();
        }
    }


}
