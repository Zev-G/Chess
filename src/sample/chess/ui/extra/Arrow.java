package sample.chess.ui.extra;

import javafx.scene.Group;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;

/**
 * @author kn on stackoverflow + changes by me
 */
public class Arrow extends Group {
    private static final double defaultArrowHeadSize = 5.0;

    public Arrow(double startX, double startY, double endX, double endY, double arrowHeadSize, double lineWidth){
        super();
        setMouseTransparent(true);

        //ArrowHead
        double angle = Math.atan2((endY - startY), (endX - startX)) - Math.PI / 2.0;
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);
        //point1
        double x1 = (- 1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * arrowHeadSize + endX;
        double y1 = (- 1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * arrowHeadSize + endY;
        //point2
        double x2 = (1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * arrowHeadSize + endX;
        double y2 = (1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * arrowHeadSize + endY;


        Polygon arrowHead = new Polygon(endX, endY,
                                        x1, y1,
                                        x2, y2,
                                        endX, endY);
        Line line = new Line(startX, startY, x1 - (x1 - x2) / 2, y1 - (y1 - y2) / 2);
        line.setStrokeWidth(lineWidth);
        line.getStyleClass().add("arrow-line");
        arrowHead.getStyleClass().add("arrow-cap");
        getChildren().addAll(arrowHead, line);


//        getElements().add(new LineTo(x1, y1));
//        getElements().add(new LineTo(x2, y2));
//        getElements().add(new LineTo(endX, endY));
    }

//    public Arrow(double startX, double startY, double endX, double endY){
//        this(startX, startY, endX, endY, defaultArrowHeadSize);
//    }
}