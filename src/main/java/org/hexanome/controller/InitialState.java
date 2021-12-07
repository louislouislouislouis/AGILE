package org.hexanome.controller;

import com.gluonhq.maps.MapPoint;
import org.hexanome.data.ExceptionXML;
import org.hexanome.data.MapDeserializer;
import org.hexanome.model.MapIF;
import org.hexanome.vue.CustomMap;
import org.hexanome.vue.CustomMapLayer;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class InitialState implements State {
    // initial state

    @Override
    public void loadMap(MainsScreenController controller, MapIF map, File selectedFile
    ) throws ExceptionXML, ParserConfigurationException, IOException, SAXException {
        // We initialize the deserializer
        MapDeserializer domMap = new MapDeserializer();

        // we load the map
        domMap.load(map, selectedFile);

        // we change the state of the controller
        controller.setCurrentState(controller.mapState);
    }

    @Override
    public void enableButton(MainsScreenController controller) {
        controller.getBtnLoadRequest().setDisable(true);
        controller.getBtnLoadMap().setDisable(false);
        controller.getBtnValidateRoute().setDisable(true);
        controller.getBtnAddRequest().setDisable(true);
        controller.getBtnRedo().setDisable(true);
        controller.getBtnUndo().setDisable(true);
    }
}
