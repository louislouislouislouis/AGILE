package org.hexanome.controller;

import com.gluonhq.maps.MapLayer;
import com.gluonhq.maps.MapPoint;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polyline;
import javafx.util.Pair;

import java.util.HashMap;

/** Affiche un point rouge sur la carte */
public class CustomCircleMarkerLayer extends MapLayer {

    private final HashMap<Long, Pair<MapPoint, Circle>> pointList;
    private final HashMap<Polyline,Pair<MapPoint, MapPoint>> segmentList;

    /**
     * @see com.gluonhq.maps.MapPoint
     */
    public CustomCircleMarkerLayer(){
        pointList = new HashMap<>();
        segmentList = new HashMap<>();
    }

    public void addPoint(Long id, MapPoint mapPoint){
        Circle circle = new Circle(1.5, Color.FIREBRICK);

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

    public void addSegment(MapPoint mapPointStart, MapPoint mapPointEnd){
        Polyline polyline = new Polyline();
        polyline.setStroke( Color.FIREBRICK);
        polyline.setStrokeWidth(1.25);

        segmentList.put(polyline, new Pair<>(mapPointStart, mapPointEnd));
        this.getChildren().add(polyline);
    }

    public void clear(){

    }

    public HashMap<Long, Pair<MapPoint, Circle>> getPointList() {
        return pointList;
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

        segmentList.forEach((polyline,pair) -> {
            MapPoint mapPointStart = pair.getKey();
            MapPoint mapPointEnd = pair.getValue();

            Point2D point2dStart = this.getMapPoint(mapPointStart.getLatitude(), mapPointStart.getLongitude());
            Point2D point2dEnd = this.getMapPoint(mapPointEnd.getLatitude(), mapPointEnd.getLongitude());

            polyline.getPoints().clear();

            polyline.getPoints().addAll(
                    point2dStart.getX(), point2dStart.getY(),
                    point2dEnd.getX(), point2dEnd.getY()
            );
        });
    }
}
