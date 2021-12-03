package org.hexanome.controller;

import org.hexanome.data.ExceptionXML;
import org.hexanome.data.MapDeserializer;
import org.hexanome.model.MapIF;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class InitialState implements State {
    // initial state

    @Override
    public void loadMap(MainsScreenController controller, MapIF map, File selectedFile) {
        if (selectedFile != null) {
            // We initialize the deserializer
            MapDeserializer domMap = new MapDeserializer();
            try {
                // we load the map
                domMap.load(map, selectedFile);
                //controller.
            } catch (ParserConfigurationException | SAXException | IOException | ExceptionXML e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("aucun fichier choisi");
        }
    }
}
