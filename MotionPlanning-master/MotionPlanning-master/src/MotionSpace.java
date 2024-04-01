import com.sun.javafx.geom.Line2D;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.*;

public class MotionSpace extends Canvas {

    private int numOfConnections = 5;

    private int RRTMultiplier = 1;
    private int optimiseDistance = 40;

    private final float scale = 1.3f;

    List<Node> RRTPoints = new ArrayList<>();

    List<Node> generatedPoint = new ArrayList<>();
    List<Point2D> PRMPoints = new ArrayList<>();
    List<Node> StartAndTargetNode = new ArrayList<>();

    List<RectBounds> obstacles = new ArrayList<>();

    List<List<RectBounds>> obstacleSets = new ArrayList<>();

    private int size;

    public MotionSpace(int size) {
        super(size, size);
        this.size = size;

        List<RectBounds> r1 = new ArrayList<>();
        r1.add(new RectBounds(300, 250, 400, 300));
        r1.add(new RectBounds(300, 50, 400, 100));

        List<RectBounds> r2 = new ArrayList<>();
        r2.add(new RectBounds(300, 0, 400, 400));
        r2.add(new RectBounds(50, 350, 250, 450));

        List<RectBounds> r3 = new ArrayList<>();
        r3.add(new RectBounds(50, 50, 450, 100));
        r3.add(new RectBounds(50, 400, 450, 450));
        r3.add(new RectBounds(50, 105, 100, 450));
        r3.add(new RectBounds(400, 50, 450, 395));

        obstacleSets.add(r1);
        obstacleSets.add(r2);
        obstacleSets.add(r3);
        obstacleSets.add(new ArrayList<>());

        reset();
    }

    private void paintBackground() {
        GraphicsContext g = getGraphicsContext2D();
        getGraphicsContext2D().setFill(Color.WHITE);
        getGraphicsContext2D().fillRect(0, 0, size, size);

        int b = 2;
        Random rand = new Random();
        for (RectBounds r : obstacles) {
            g.setFill(Color.BLACK);
            g.fillRect(r.getMinX(), r.getMinY(), r.getWidth(), r.getHeight());
            g.setFill(Color.INDIANRED);

            g.fillRect(r.getMinX() + b, r.getMinY() + b, r.getWidth() - 2 * b, r.getHeight() - 2 * b);
        }


    }

    private void repaintRRT() {
        paintBackground();

        GraphicsContext g = getGraphicsContext2D();
        int d = 2;

        g.setFill(Color.BLUE);
        for (Node n : RRTPoints) {
            g.fillOval(n.point.x - d, n.point.y - d, 2 * d, 2 * d);
        }

        g.setStroke(Color.BLACK);
        for (Node n : RRTPoints) {
            if (n.parent != null) {
                g.strokeLine(n.point.x, n.point.y, n.parent.point.x, n.parent.point.y);
            }
        }

    }

    public void addPRM(int n) {
        GraphicsContext g = getGraphicsContext2D();
        int b = 20;

        paintBackground();

//        for(int i = 0; i < PRMPoints.size(); i++){
//            double x = PRMPoints.get(i).x;
//            double y = PRMPoints.get(i).y;
//
//            g.setFill(Color.BLUE);
//            g.fillOval(x-b, y-b, 2*b, 2*b);
//        }

        for (int i = 0; i < n; i++) {
            boolean free = false;
            int x = (int) StartAndTargetNode.get(0).point.x;
            int y = (int) StartAndTargetNode.get(0).point.y;

            while (!free) {
                Random r = new Random();
                x = r.nextInt((int) getWidth());
                y = r.nextInt((int) getHeight());

                free = true;
                for (RectBounds rect : obstacles) {
                    if (rect.contains(new Point2D(x, y))) {
                        free = false;
                        break;
                    }
                }
            }

            g.setFill(Color.BLUE);
            g.fillOval(x - b, y - b, 2 * b, 2 * b);
            PRMPoints.add(new Point2D(x, y));
        }
    }


    // creare punct de start si target
    public void addTarget(GraphicsContext g) {
        int b = 6;


//        int x = 0;
//        int y = 0;
//        Random r = new Random();
//        x = r.nextInt((int) getWidth());
//        y = r.nextInt((int) getHeight());

        g.setFill(Color.HOTPINK);
        //g.fillOval(x - b, y - b, 2 * b, 2 * b);
        g.fillOval(240 - b, 260 - b, 2 * b, 2 * b);
//        PRMPoints.add(new Point2D(x, y));

//        int x1 = 0;
//        int y1 = 0;
//        Random r1 = new Random();
//        x1 = r1.nextInt((int) getWidth());
//        y1 = r1.nextInt((int) getHeight());

        //g.setFill(Color.GOLD);
        //g.fillOval(x1 - b, y1 - b, 2 * b, 2 * b);


        g.setFill(Color.GOLD);
        g.fillOval(240 - b, 450 - b, 2 * b, 2 * b);
//        StartAndTargetNode.add(new Node(null, new Point2D(x, y)));
//        StartAndTargetNode.add(new Node(null, new Point2D(x1, y1)));

        StartAndTargetNode.add(new Node(null, new Point2D(240, 260)));
        StartAndTargetNode.add(new Node(null, new Point2D(240, 450)));
    }

    public void addRRT(int n) {
        // se creaza background = se seteaza daca e cazul obstacolele
        GraphicsContext g = getGraphicsContext2D();
        paintBackground();

        // se pun puctele de start si target- dar ast dupa ce background-ul e setat
        addTarget(g);

//        generatedPoint.add(new Node(null, new Point2D((float)240.0, (float)260.0)));
        generatedPoint.add(new Node(null, new Point2D((float) 700 , (float)  -140)));
        generatedPoint.add(new Node(null, new Point2D((float) 700  , (float) 400)));
//        generatedPoint.add(new Node(null, new Point2D((float)240.0, (float)270.0)));
        Random rand = new Random();
        boolean gasit = false;
        RRTPoints.add(new Node(new Point2D(StartAndTargetNode.get(0).point.x, StartAndTargetNode.get(0).point.y)));
        RRTPoints.add(new Node(new Point2D(StartAndTargetNode.get(1).point.x, StartAndTargetNode.get(1).point.y)));

        int j = 0;
        while (j < 2) {

            System.out.println("jjjjjj" + j);


            //generare sample
            int x = (int) generatedPoint.get(j).point.x;
            int y = (int) generatedPoint.get(j).point.y;

            double closestDistance = 999999;
            Node closestNode = null;
            boolean tooClose = false;
            boolean distanceOk = true;

            for (Node node : RRTPoints) {

                //calcul distanta
//                double distTrget = Math.sqrt((StartAndTargetNode.get(1).point.x - x) * (StartAndTargetNode.get(1).point.x - x) + (StartAndTargetNode.get(1).point.y - y) * (StartAndTargetNode.get(1).point.y - y));
//                double distTargetStart = Math.sqrt((StartAndTargetNode.get(1).point.x - StartAndTargetNode.get(0).point.x) * (StartAndTargetNode.get(1).point.x - StartAndTargetNode.get(0).point.x) + (StartAndTargetNode.get(1).point.y - StartAndTargetNode.get(0).point.y) * (StartAndTargetNode.get(1).point.y - StartAndTargetNode.get(0).point.y));
//                if (distTargetStart > distTrget ) {
//
//                    int d = 2;
//
//                    //g.setFill(Color.BLUE);
//                    //g.fillOval(StartAndTargetNode.get(1).point.x - d, StartAndTargetNode.get(1).point.y - d, 2 * d, 2 * d);
//
//                    //RRTPoints.add(new Node(closestNode, new Point2D(StartAndTargetNode.get(1).point.x, StartAndTargetNode.get(1).point.y)));
//
//                } else {
//                    distanceOk = false;
//                    break;
//                }

                double dist = Math.sqrt((node.point.x - x) * (node.point.x - x) + (node.point.y - y) * (node.point.y - y));


                if (dist < 10.0) {
                    tooClose = true;
                }


                // sa nu fie prea aproape
                if (dist < closestDistance) {
                    closestDistance = dist;
                    closestNode = node;
                }

            }
            if (!distanceOk || tooClose || closestNode == null) {
                continue;
            }

            //calculam noul punct
            double delX = RRTMultiplier * ((x - closestNode.point.x) / closestDistance) ;
            double delY = RRTMultiplier * ((y - closestNode.point.y) / closestDistance) ;

            double dist_norm = Math.sqrt(delX * delX + delY * delY);

            float newX;
            float newY;
            if (dist_norm < closestDistance) {
                newX = (float) delX + closestNode.point.x;
                newY = (float) delY + closestNode.point.y;
            }
            else {
                newX = closestNode.point.x;
                newY = closestNode.point.y;
            }


            boolean collision = false;

            Line2D line = new Line2D(closestNode.point.x, closestNode.point.y, newX, newY);
            Iterator var19 = this.obstacles.iterator();

            RectBounds r;
            while (var19.hasNext()) {
                r = (RectBounds) var19.next();
                if (line.intersects(r)) {
                    collision = true;
                    break;
                }
            }

            byte d;
            if (!collision) {
                d = 2;
                g.setFill(Color.BLUE);
                g.fillOval((double) (newX - (float) d), (double) (newY - (float) d), (double) (2 * d), (double) (2 * d));
                this.RRTPoints.add(new Node(closestNode, new Point2D(newX, newY)));
                g.setStroke(Color.BLACK);
                g.strokeLine((double) closestNode.point.x, (double) closestNode.point.y, (double) newX, (double) newY);
                j++;
            } else {
                collision = false;
                newX = (float) delX / 2.0F + closestNode.point.x;
                newY = (float) delY / 2.0F + closestNode.point.y;
                line = new Line2D(closestNode.point.x, closestNode.point.y, newX, newY);
                var19 = this.obstacles.iterator();

                while (var19.hasNext()) {
                    r = (RectBounds) var19.next();
                    if (line.intersects(r)) {
                        collision = true;
                        break;
                    }
                }

                if (!collision) {
                    d = 2;
                    g.setFill(Color.BLUE);
                    g.fillOval((double) (newX - (float) d), (double) (newY - (float) d), (double) (2 * d), (double) (2 * d));
                    this.RRTPoints.add(new Node(closestNode, new Point2D(newX, newY)));
                    g.setStroke(Color.BLACK);
                    g.strokeLine((double) closestNode.point.x, (double) closestNode.point.y, (double) newX, (double) newY);
                    j++;
                }
            }
        }

        Collections.sort(RRTPoints, (a, b) ->  (int)b.point.y - (int)a.point.y);
        g.setStroke(Color.BLACK);
        g.strokeLine((double) RRTPoints.get(1).point.x, (double) RRTPoints.get(1).point.y, (double) RRTPoints.get(0).point.x, (double) RRTPoints.get(0).point.y);
    }

    public void colorPathRRT() {
        Node node = StartAndTargetNode.get(1);
        while (node != StartAndTargetNode.get(0)) {
            if (node.parent != null) {
                GraphicsContext g = getGraphicsContext2D();
                g.setStroke(Color.HOTPINK);
                g.strokeLine(node.point.x, node.point.y, node.parent.point.x, node.parent.point.y);
            }
            node = node.parent;
        }
    }

    public void addRRTStar(int n) {
        Random rand = new Random();
        GraphicsContext g = getGraphicsContext2D();

        for (int j = 0; j < n; j++) {

            int x = rand.nextInt(((int) getWidth()));
            int y = rand.nextInt(((int) getHeight()));

            double closestDistance = 99999;
            boolean tooClose = false;

            Node closestNode = null;

            for (Node node : RRTPoints) {

                double dist = Math.sqrt((node.point.x - x) * (node.point.x - x) + (node.point.y - y) * (node.point.y - y));

                if (dist < 10) {
                    tooClose = true;
                }

                if (dist < closestDistance) {
                    closestDistance = dist;
                    closestNode = node;
                }

            }

            if (tooClose || closestNode == null) {
                continue;
            }

            double delX = RRTMultiplier * ((x - closestNode.point.x) / closestDistance);
            double delY = RRTMultiplier * ((y - closestNode.point.y) / closestDistance);

            float newX = (float) delX + closestNode.point.x;
            float newY = (float) delY + closestNode.point.y;

            List<Node> closeNodes = new ArrayList<>();
            int maxDist = optimiseDistance;

            for (Node node : RRTPoints) {

                double dist = Math.sqrt((node.point.x - newX) * (node.point.x - newX) + (node.point.y - newY) * (node.point.y - newY));

                if (dist < maxDist) {
                    node.helper = dist;
                    closeNodes.add(node);
                }

            }

            closestNode = null;
            double smallestDist = 9999;

            for (Node node : closeNodes) {

                boolean collision = false;
                Line2D line = new Line2D(node.point.x, node.point.y, newX, newY);
                for (RectBounds r : obstacles) {
                    if (line.intersects(r)) {
                        collision = true;
                        break;
                    }
                }
                if (!collision) {
                    if (node.distance + node.helper < smallestDist) {
                        smallestDist = node.distance + node.helper;
                        closestNode = node;
                    }
                }
            }

            if (closestNode == null) {
                continue;
            }

            Node toAdd;

            int d = 2;

            int r = rand.nextInt(255);
            int ge = rand.nextInt(255);
            int b = rand.nextInt(255);

            g.setFill(Color.rgb(r, ge, b));
            g.setFill(Color.BLUE);
            g.fillOval(newX - d, newY - d, 2 * d, 2 * d);

            toAdd = new Node(closestNode, new Point2D(newX, newY));
            RRTPoints.add(toAdd);
            g.setStroke(Color.BLACK);
            g.strokeLine(closestNode.point.x, closestNode.point.y, newX, newY);

            if (closestDistance < 15) {
                x = (int) StartAndTargetNode.get(1).point.x;
                y = (int) StartAndTargetNode.get(1).point.y;
            }

            boolean changed = false;
            for (Node node : closeNodes) {

                if (node.helper + toAdd.distance < node.distance) {

                    boolean canConnect = true;
                    Line2D line = new Line2D(node.point.x, node.point.y, newX, newY);

                    for (RectBounds rect : obstacles) {
                        if (line.intersects(rect)) {
                            canConnect = false;
                            break;
                        }
                    }

                    if (canConnect) {
                        node.parent = toAdd;
                        node.distance = node.parent.distance + node.helper;
                        changed = true;
                    }
                }

            }

            if (changed) {
                repaintRRT();
            }

        }

    }

    public void connect() {
        for (int j = 0; j < PRMPoints.size(); j++) {
            Point2D point = PRMPoints.get(j);

            List<Point2D> closest = new ArrayList<>();
            List<Double> closestDist = new ArrayList<>();

            for (int i = 0; i < numOfConnections; i++) {
                closest.add(null);
                closestDist.add(99999.0);
            }


            for (Point2D p : PRMPoints) {
                if (p != point) {

                    boolean intersects = false;
                    Line2D l = new Line2D(p.x, p.y, point.x, point.y);
                    for (RectBounds r : obstacles) {
                        if (l.intersects(r)) {
                            intersects = true;
                        }
                    }

                    if (intersects) {
                        continue;
                    }

                    //distanta euclidiana
                    double dist = Math.sqrt((p.x - point.x) * (p.x - point.x) + (p.y - point.y) * (p.y - point.y));

                    for (int i = 0; i < numOfConnections; i++) {
                        //adauga ditanta punctului si de cine e apropiat
                        if (closestDist.get(i) > dist) {
                            closestDist.add(i, dist);
                            closest.add(i, p);
                            break;
                        }
                    }
                }
            }

            //deseneaza linile
            List<Point2D> edges = new ArrayList<>();
            for (int i = 0; i < numOfConnections; i++) {

                Point2D close = closest.get(i);
                if (close != null) {
                    GraphicsContext g = getGraphicsContext2D();
                    g.setStroke(Color.BLACK);
                    g.strokeLine(close.x, close.y, point.x, point.y);
                    edges.add(close);
                }
            }
        }

    }

    public void reset() {
        RRTPoints.clear();
        PRMPoints.clear();
//        RRTPoints.add(new Node(new Point2D(250,250)));
        paintBackground();
    }

    public void setObstacles(int n) {
        obstacles = obstacleSets.get(n);
        reset();
    }

    public void setRRTMultiplier(int m) {
        RRTMultiplier = m;
    }
}
