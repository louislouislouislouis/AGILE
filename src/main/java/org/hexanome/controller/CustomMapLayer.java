package org.hexanome.controller;

import com.gluonhq.maps.MapLayer;
import com.gluonhq.maps.MapPoint;

import javafx.geometry.Point2D;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.util.Pair;

import java.util.HashMap;

/**
 * Affiche un point rouge sur la carte
 */
public class CustomMapLayer extends MapLayer {

    private final HashMap<Long, MapPoint> pointList;
    private final HashMap<Long, Circle> circleList;
    private final HashMap<Long, Polyline> polylineList;
    private final HashMap<Long, Pair<MapPoint, MapPoint>> segmentList;

    /**
     * @see com.gluonhq.maps.MapPoint
     */
    public CustomMapLayer() {
        pointList = new HashMap<>();
        circleList = new HashMap<>();
        segmentList = new HashMap<>();
        polylineList = new HashMap<>();
    }

    public void addPoint(Long id, MapPoint mapPoint) {
        Circle circle = new Circle(1.5, Color.FIREBRICK);

        pointList.put(id, mapPoint);
        circleList.put(id, circle);

        /* Ajoute le cercle au MapLayer */
        this.getChildren().add(circle);
    }

    public void addSpecialPoint(Long id, MapPoint mapPoint, Color color) {
        Circle circle = new Circle(10, color);

        circle.setId(id.toString());
        DropShadow e = new DropShadow();

        e.setRadius(12);

        circle.setEffect(e);

        pointList.put(id, mapPoint);
        circleList.put(id, circle);

        /* Ajoute le cercle au MapLayer */
        this.getChildren().add(circle);
    }

    public void addSegment(MapPoint mapPointStart, MapPoint mapPointEnd, Long id) {
        Polyline polyline = new Polyline();
        polyline.setStroke(Color.RED);
        polyline.setStrokeWidth(5);
        polyline.setOpacity(0.5);
        polyline.setId(id.toString());

        segmentList.put(id, new Pair<>(mapPointStart, mapPointEnd));
        polylineList.put(id, polyline);

        this.getChildren().add(polyline);
    }

    public HashMap<Long, MapPoint> getPointList() {
        return pointList;
    }

    public HashMap<Long, Circle> getCircleList() {
        return circleList;
    }

    public HashMap<Long, Polyline> getPolylineList() {
        return polylineList;
    }

    public HashMap<Long, Pair<MapPoint, MapPoint>> getSegmentList() {
        return segmentList;
    }

    public void forceReRender() {
        System.out.println("forceRender is calleds");
        layoutLayer();
    }

    /* La fonction est appelée à chaque rafraichissement de la carte */
    @Override
    protected void layoutLayer() {
        /* Conversion des MapPoint vers Point2D */
        System.out.println("LayoutLayer is Called");
        pointList.forEach((id, mapPoint) -> {

            Shape shape = circleList.get(id);

            Point2D point2d = this.getMapPoint(mapPoint.getLatitude(), mapPoint.getLongitude());
            /* Déplace le cercle selon les coordonnées du point */
            shape.setTranslateX(point2d.getX());
            shape.setTranslateY(point2d.getY());
        });

        segmentList.forEach((id, pair) -> {
            MapPoint mapPointStart = pair.getKey();
            MapPoint mapPointEnd = pair.getValue();

            Point2D point2dStart = this.getMapPoint(mapPointStart.getLatitude(), mapPointStart.getLongitude());
            Point2D point2dEnd = this.getMapPoint(mapPointEnd.getLatitude(), mapPointEnd.getLongitude());

            Polyline polyline = polylineList.get(id);

            polyline.getPoints().clear();

            polyline.getPoints().addAll(
                    point2dStart.getX(), point2dStart.getY(),
                    point2dEnd.getX(), point2dEnd.getY()
            );
        });
    }
}
