package org.hexanome.controller;

import com.gluonhq.maps.MapLayer;
import com.gluonhq.maps.MapPoint;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Pair;

import java.util.HashMap;

/** Affiche un point rouge sur la carte */
public class CustomCircleMarkerLayer extends MapLayer {

    //private final MapPoint mapPoint;
    //private final Circle circle;
    private final HashMap<Long, Pair<MapPoint, Circle>> pointList;

    /**
     * @see com.gluonhq.maps.MapPoint
     */
    public CustomCircleMarkerLayer(){
        pointList = new HashMap<>();
    }

    public void addPoint(Long id, MapPoint mapPoint){
        Circle circle = new Circle(2.5, Color.RED);

        pointList.put(id, new Pair<>(mapPoint, circle));

        /* Ajoute le cercle au MapLayer */
        this.getChildren().add(circle);
    }

    public void addPointDelivery(Long id, MapPoint mapPoint, Color color){
        Circle circle = new Circle(4, color);

        pointList.put(id, new Pair<>(mapPoint, circle));

        /* Ajoute le cercle au MapLayer */
        this.getChildren().add(circle);
    }

    public void addPointPickup(Long id, MapPoint mapPoint, Color color){
        Circle circle = new Circle(4, color);

        pointList.put(id, new Pair<>(mapPoint, circle));

        /* Ajoute le cercle au MapLayer */
        this.getChildren().add(circle);
    }

    /* La fonction est appelée à chaque rafraichissement de la carte */
    @Override
    protected void layoutLayer() {
        /* Conversion des MapPoint vers Point2D */

        pointList.forEach( (key, value) -> {
            MapPoint mapPoint = value.getKey();
            Circle circle = value.getValue();
            Point2D point2d = this.getMapPoint(mapPoint.getLatitude(), mapPoint.getLongitude());

            /* Déplace le cercle selon les coordonnées du point */
            circle.setTranslateX(point2d.getX());
            circle.setTranslateY(point2d.getY());
        });
    }
}
