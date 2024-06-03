import com.sun.javafx.geom.Point2D;

public class Node {

    public Node parent;
    public Point2D point;
    public double distance;

    public double helper = 0;
    // TODO: 01/06/2024 ADD number CRITERIA for algorithms , like n!!

    public Node(Node parent, Point2D point){
        this.parent = parent;
        this.point = point;

        if(parent!= null)
        this.distance = parent.distance + Math.sqrt((parent.point.x-point.x)*(parent.point.x-point.x) +
                (parent.point.y-point.y)*(parent.point.y-point.y));
    }

    public Node(Point2D point){
        this.point = point;
        this.parent = null;
        this.distance = 0;
    }

}

