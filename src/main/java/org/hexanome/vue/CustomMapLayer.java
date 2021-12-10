package org.hexanome.vue;

import com.gluonhq.maps.MapLayer;
import com.gluonhq.maps.MapPoint;

import javafx.animation.ScaleTransition;
import javafx.geometry.Point2D;
import javafx.scene.control.TableView;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.util.Duration;
import javafx.util.Pair;
import org.hexanome.controller.MainsScreenController;
import org.hexanome.model.MapIF;
import org.hexanome.model.Point;

import java.util.HashMap;
import java.util.Objects;

/**
 * Affiche un point rouge sur la carte
 */
public class CustomMapLayer extends MapLayer {

    private final HashMap<Long, MapPoint> pointList;
    private final HashMap<Long, Shape> shapeList;
    private final HashMap<Long, Polyline> polylineList;
    private final HashMap<Long, Pair<MapPoint, MapPoint>> segmentList;

    /**
     * @see com.gluonhq.maps.MapPoint
     */
    public CustomMapLayer() {
        pointList = new HashMap<>();
        shapeList = new HashMap<>();
        segmentList = new HashMap<>();
        polylineList = new HashMap<>();
    }

    /**
     * add a Point to the layer
     *
     * @param id       id of the point
     * @param mapPoint mapPoint that will be added
     */
    public void addPoint(Long id, MapPoint mapPoint) {
        Circle circle = new Circle(2, Color.FIREBRICK);

        pointList.put(id, mapPoint);
        shapeList.put(id, circle);

        /* Ajoute le cercle au MapLayer */
        this.getChildren().add(circle);
    }

    public void resetAll() {
        pointList.clear();
        segmentList.clear();
        shapeList.clear();
        polylineList.clear();
        this.getChildren().clear();
    }

    /**
     * add a special Point to the layer
     * with a circle shape
     *
     * @param id       id of the point
     * @param mapPoint mapPoint that will be added
     * @param color    color of the circle
     */
    public void addSpecialPointCircle(Long id, MapPoint mapPoint, Color color) {
        Circle circle = new Circle(10, color);

        addShape(circle, mapPoint, id);
    }

    /**
     * add a special Point to the layer
     * with a rectangle shape
     *
     * @param id       id of the point
     * @param mapPoint mapPoint that will be added
     * @param color    color of the rectangle
     */
    public void addSpecialPointRectangle(Long id, MapPoint mapPoint, Color color) {
        Rectangle rec = new Rectangle(20, 20, color);
        addShape(rec, mapPoint, id);
    }

    private void addShape(Shape shape, MapPoint mapPoint, Long id) {
        shape.setId(id.toString());
        DropShadow e = new DropShadow();

        e.setRadius(12);
        shape.setEffect(e);

        pointList.put(id, mapPoint);
        shapeList.put(id, shape);

        /* Ajoute le cercle au MapLayer */
        this.getChildren().add(shape);
    }

    /**
     * add a red segment to the layer
     *
     * @param id            id of the point
     * @param mapPointStart start mapPoint of the segment
     * @param mapPointEnd   end mapPoint of the segment
     */
    public void addSegment(MapPoint mapPointStart, MapPoint mapPointEnd, Long id) {
        Polyline polyline = new Polyline();
        polyline.setStroke(Color.DODGERBLUE);
        polyline.setStrokeWidth(5);
        polyline.setOpacity(0.5);
        polyline.setId(id.toString());

        segmentList.put(id, new Pair<>(mapPointStart, mapPointEnd));
        polylineList.put(id, polyline);

        this.getChildren().add(polyline);
    }

    /**
     * this method create event for each shape of the map that enable to scroll to the
     * element in the table view. It scrolls if you click on one of the shape.
     *
     * @param tableView
     */
    public void scrollToPointEvent(TableView<Point> tableView) {
        shapeList.forEach((id, shape) -> {
            shape.setOnMouseClicked(mouseEvent -> {

                for (int i = 0; i < tableView.getItems().size(); i++) {
                    Point point = (Point) tableView.getItems().get(i);
                    if (Objects.equals(point.getId(), id)) {
                        tableView.scrollTo(i);
                        tableView.getSelectionModel().select(i);
                        break;
                    }
                }
            });
        });
    }

    public void animateOnePoint(Long Id) {
        System.out.println("IN ANIMATE");
        //Creating a rotate transition
        //Setting the node for the transition
        System.out.println(shapeList.get(Id));

        ScaleTransition st = new ScaleTransition(Duration.millis(400), shapeList.get(Id));
        st.setFromX(1);
        st.setToX(1.5);
        st.setFromY(1);
        st.setToY(1.5);
        st.setCycleCount(4);
        st.setAutoReverse(true);

        st.play();


        //Playing the animation

    }

    /**
     * This method create events for each intersection of the map
     * The first event is a hover event that change the size of the shape
     * The second event trigger the method leftClick or rightClick of the controller when we click on them
     *
     * @param c   main controller
     * @param map param that contains all the segments and all the intersections of the current map
     */
    public void intersectionEvent(MainsScreenController c, MapIF map) {
        shapeList.forEach((id, shape) -> {
            // hover pour changer la taille des points
            shape.hoverProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    Circle circle = (Circle) shape;
                    circle.setRadius(6);
                } else {
                    Circle circle = (Circle) shape;
                    circle.setRadius(2);
                }
            });

            // event sur le click de la souris
            shape.setOnMouseClicked(mouseEvent -> {
                if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                    c.leftClick(map.getIntersections().get(id));
                } else if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                    c.rightClick();
                }
            });
        });
    }

    /**
     * This method create event for each segment of the layer
     * This help the user to know the path that has been taken to arrive to this segment
     */
    public void tourLineHover() {
        polylineList.forEach((aLong, polyline) -> {
            polyline.hoverProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    polylineList.forEach((id, poly) -> {
                        if (id <= aLong) {
                            poly.setStrokeWidth(7);
                            poly.setStroke(Color.DODGERBLUE);
                            DropShadow e = new DropShadow();
                            e.setColor(Color.BLUE);
                            e.setRadius(9);
                            poly.setEffect(e);
                        }
                    });
                } else {
                    polylineList.forEach((id, poly) -> {
                        if (id <= aLong) {
                            poly.setStrokeWidth(5);
                            poly.setStroke(Color.DODGERBLUE);
                            poly.setEffect(null);
                        }
                    });
                }
            });
        });
    }

    public HashMap<Long, MapPoint> getPointList() {
        return pointList;
    }

    public HashMap<Long, Shape> getShapeList() {
        return shapeList;
    }

    public HashMap<Long, Polyline> getPolylineList() {
        return polylineList;
    }

    public HashMap<Long, Pair<MapPoint, MapPoint>> getSegmentList() {
        return segmentList;
    }

    /**
     * This public method is used to force the render of this layer
     */
    public void forceReRender() {
        layoutLayer();
    }

    /* La fonction est appelée à chaque rafraichissement de la carte */
    @Override
    protected void layoutLayer() {

        /* Conversion des MapPoint vers Point2D */
        pointList.forEach((id, mapPoint) -> {

            Shape shape = shapeList.get(id);
            Point2D point2d = this.getMapPoint(mapPoint.getLatitude(), mapPoint.getLongitude());

            if (shape instanceof Circle) {
                /* Déplace le cercle selon les coordonnées du point */
                shape.setTranslateX(point2d.getX());
                shape.setTranslateY(point2d.getY());
            } else {
                /* Déplace le cercle selon les coordonnées du point */
                shape.setTranslateX(point2d.getX() - shape.getLayoutBounds().getWidth() / 2);
                shape.setTranslateY(point2d.getY() - shape.getLayoutBounds().getWidth() / 2);
            }
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
