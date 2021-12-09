package org.hexanome.data;

import javafx.scene.paint.Color;
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
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * This class is used to parse xml files containing a representation of a planning with requests
 *
 * @author Gastronom'if
 */
public class RequestDeserializer {
    /**
     * Open an XML file and create a map from this file
     *
     * @param planning the planning to create from the file
     * @param xml      file that contain the xml
     * @param map      object that contains map's data
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws ExceptionXML
     */
    public void load(PlanningRequest planning, File xml, MapIF map) throws ParserConfigurationException, SAXException, IOException, ExceptionXML {
        DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = docBuilder.parse(xml);
        Element root = document.getDocumentElement();
        if (root.getNodeName().equals("planningRequest")) {
            buildFromDOMXML(root, planning, map);
        } else
            throw new ExceptionXML("Wrong format the first node has this name : " + root.getNodeName());
    }

    private void buildFromDOMXML(Element noeudDOMRacine, PlanningRequest planning, MapIF map) throws ExceptionXML, NumberFormatException {
        NodeList warehouses = noeudDOMRacine.getElementsByTagName("depot");
        NodeList requests = noeudDOMRacine.getElementsByTagName("request");

        for (int i = 0; i < requests.getLength(); i++) {
            planning.addRequest(createRequest((Element) requests.item(i), map, i));
        }

        if (warehouses.getLength() != 1)
            throw new ExceptionXML("Error when reading file: xml must contain one and only one warehouse");

        planning.setWarehouse(createWarehouse((Element) warehouses.item(0), map));
    }

    private Request createRequest(Element elt, MapIF map, int i) throws ExceptionXML {

        Color color = ColorEnum.values()[i].color;

        int pickupDuration = Integer.parseInt(elt.getAttribute("pickupDuration"));
        if (pickupDuration < 0)
            throw new ExceptionXML("Error when reading file: pickup duration must be positive");

        int deliveryDuration = Integer.parseInt(elt.getAttribute("deliveryDuration"));
        if (deliveryDuration < 0)
            throw new ExceptionXML("Error when reading file: delivery duration must be positive");

        Long pickupAddress = Long.parseLong(elt.getAttribute("pickupAddress"));
        if (pickupAddress < 0)
            throw new ExceptionXML("Error when reading file: pickupAddress must be positive");

        if (map.getIntersections().get(pickupAddress) == null)
            throw new ExceptionXML("Error when reading file: point out of the map");

        PickupPoint pickupPoint = new PickupPoint(map.getIntersections().get(pickupAddress), pickupDuration, color);

        Long deliveryAddress = Long.parseLong(elt.getAttribute("deliveryAddress"));
        if (deliveryAddress < 0)
            throw new ExceptionXML("Error when reading file: deliveryAddress must be positive");

        if (map.getIntersections().get(deliveryAddress) == null)
            throw new ExceptionXML("Error when reading file: point out of the map");

        DeliveryPoint deliveryPoint = new DeliveryPoint(map.getIntersections().get(deliveryAddress), deliveryDuration, color);

        return new Request(pickupPoint, deliveryPoint);
    }

    private Warehouse createWarehouse(Element elt, MapIF map) throws ExceptionXML {
        long address = Long.parseLong(elt.getAttribute("address"));
        if (address < 0)
            throw new ExceptionXML("Error when reading file: address must be positive");

        Intersection departureAddress = map.getIntersections().get(address);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:m:s");

        LocalTime departureTime = LocalTime.parse(elt.getAttribute("departureTime"), formatter);

        return new Warehouse(departureTime, departureAddress, Color.BLACK);
    }
}
