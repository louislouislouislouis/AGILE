package org.hexanome.controller;

import com.gluonhq.maps.MapLayer;
import com.gluonhq.maps.MapPoint;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.util.Pair;

import java.util.HashMap;

/**
 * Affiche un point rouge sur la carte
 */
public class CustomMapLayer extends MapLayer {

    private final HashMap<Long, Pair<MapPoint, Shape>> pointList;
    private final HashMap<Polyline, Pair<MapPoint, MapPoint>> segmentList;

    /**
     * @see com.gluonhq.maps.MapPoint
     */
    public CustomMapLayer() {
        pointList = new HashMap<>();
        segmentList = new HashMap<>();
    }

    public void addPoint(Long id, MapPoint mapPoint) {
        Circle circle = new Circle(1.5, Color.FIREBRICK);

        pointList.put(id, new Pair<>(mapPoint, circle));

        /* Ajoute le cercle au MapLayer */
        this.getChildren().add(circle);
    }

    public void addPointWarehouse(Long id, MapPoint mapPoint, Color color) {
        Circle circle = new Circle(6, color);

        pointList.put(id, new Pair<>(mapPoint, circle));

        /* Ajoute le cercle au MapLayer */
        this.getChildren().add(circle);
    }

    public void addPointDelivery(Long id, MapPoint mapPoint, Color color) {
        Circle circle = new Circle(5, color);


        SVGPath icon = new SVGPath();
        icon.setContent("M 45 0 C 25.463 0 9.625 15.838 9.625 35.375 c 0 8.722 3.171 16.693 8.404 22.861 L 45 90 l 26.97 -31.765 c 5.233 -6.167 8.404 -14.139 8.404 -22.861 C 80.375 15.838 64.537 0 45 0 z M 45 48.705 c -8.035 0 -14.548 -6.513 -14.548 -14.548 c 0 -8.035 6.513 -14.548 14.548 -14.548 s 14.548 6.513 14.548 14.548 C 59.548 42.192 53.035 48.705 45 48.705 z");
        icon.setFill(color);
        icon.setScaleX(0.25);
        icon.setScaleY(0.25);

        pointList.put(id, new Pair<>(mapPoint, circle));

        /* Ajoute le cercle au MapLayer */
        this.getChildren().add(circle);
    }

    public void addPointPickup(Long id, MapPoint mapPoint, Color color) {
        Circle circle = new Circle(4, color);

        pointList.put(id, new Pair<>(mapPoint, circle));

        /* Ajoute le cercle au MapLayer */
        this.getChildren().add(circle);
    }

    public void addSegment(MapPoint mapPointStart, MapPoint mapPointEnd) {
        Polyline polyline = new Polyline();
        polyline.setStroke(Color.RED);
        polyline.setStrokeWidth(2.5);

        segmentList.put(polyline, new Pair<>(mapPointStart, mapPointEnd));
        this.getChildren().add(polyline);
    }

    public HashMap<Long, Pair<MapPoint, Shape>> getPointList() {
        return pointList;
    }

    /* La fonction est appelée à chaque rafraichissement de la carte */
    @Override
    protected void layoutLayer() {
        /* Conversion des MapPoint vers Point2D */

        pointList.forEach((key, value) -> {
            MapPoint mapPoint = value.getKey();

            Shape shape = value.getValue();

            Point2D point2d = this.getMapPoint(mapPoint.getLatitude(), mapPoint.getLongitude());
            /* Déplace le cercle selon les coordonnées du point */
            shape.setTranslateX(point2d.getX());
            shape.setTranslateY(point2d.getY());
        });

        segmentList.forEach((polyline, pair) -> {
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
