import com.sun.javafx.geom.Line2D;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.scenario.effect.Effect;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.*;

public class MotionSpace extends Canvas {

    private int numOfConnections = 5;

    private int RRTMultiplier = 234;
    private int optimiseDistance = 1000;

    private final float scale = 1.3f;

    List<Node> RRTPoints = new ArrayList<>();

    List<Node> generatedPoint = new ArrayList<>();
    List<Point2D> PRMPoints = new ArrayList<>();
    List<Node> StartAndTargetNode = new ArrayList<>();

    List<RectBounds> obstacles = new ArrayList<>();

    List<List<RectBounds>> obstacleSets = new ArrayList<>();

    List<NodeAStar> listAStar = new ArrayList<>();

    private int size;

    public MotionSpace(int size) {
        super(size, size);
        this.size = size;

        List<RectBounds> r1 = new ArrayList<>();
        r1.add(new RectBounds(300, 250, 400, 350));
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
        g.setLineWidth(1.0);
        g.setLineDashes(1);
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

    public void addPRM() {
        GraphicsContext g = getGraphicsContext2D();
        int b = 2;
        paintBackground();
        g.setLineWidth(1.0);
        g.setLineDashes(1);

        addTarget(g);

        generatedPoint = UserPanel.CoordonateAlgorithms.getGeneratedPoint();

        int j = 0;
        while (j < 5) {

            boolean free = true;
            int x = (int) generatedPoint.get(j).point.x;
            int y = (int) generatedPoint.get(j).point.y;

            for (RectBounds rect : obstacles) {
                if (rect.contains(new Point2D(x, y))) {
                    free = false;
                    break;
                }
            }

            if (free) {
                g.setFill(Color.BLUE);
                g.fillOval(x - b, y - b, 2 * b, 2 * b);
                PRMPoints.add(new Point2D(x, y));
            }

            j++;
        }

        connect();
        aStar(listAStar.get(0), listAStar.get(listAStar.size() - 1));
        paintPRM(listAStar.get(listAStar.size() - 1));

        PRMPoints.clear();
        generatedPoint.clear();
        listAStar.clear();
    }
    public void addPRMforCase2a() {
        GraphicsContext g = getGraphicsContext2D();
        int b = 2;
        paintBackground();
        g.setLineWidth(1.0);
        g.setLineDashes(1);

        addTarget(g);

        generatedPoint = UserPanel.CoordonateAlgorithms.getGeneratedPoint();

        int j = 0;
        while (j < 5) {

            boolean free = true;
            int x = (int) generatedPoint.get(j).point.x;
            int y = (int) generatedPoint.get(j).point.y;

            for (RectBounds rect : obstacles) {
                if (rect.contains(new Point2D(x, y))) {
                    free = false;
                    break;
                }
            }

            if (free) {
                g.setFill(Color.BLUE);
                g.fillOval(x - b, y - b, 2 * b, 2 * b);
                PRMPoints.add(new Point2D(x, y));
            }

            j++;
        }

        connect();
        aStar(listAStar.get(0), listAStar.get(listAStar.size() - 1));
        paintPRM(listAStar.get(listAStar.size() - 1));

        PRMPoints.clear();
        generatedPoint.clear();
        listAStar.clear();
    }

    // creare punct de start si target
    public void addTarget(GraphicsContext g) {
        int b = 6;
        g.setFill(Color.HOTPINK);
        g.fillOval(240 - b, 260 - b, 2 * b, 2 * b);

        g.setFill(Color.GOLD);
        g.fillOval(370 - b, 450 - b, 2 * b, 2 * b);

        StartAndTargetNode.add(new Node(null, new Point2D(240, 260)));
        StartAndTargetNode.add(new Node(null, new Point2D(370, 450)));
    }

    public void addRRT() {
        // se creaza background = se seteaza daca e cazul obstacolele
        GraphicsContext g = getGraphicsContext2D();
        paintBackground();

        g.setLineWidth(1.0);
        g.setLineDashes(1);

        Map<Integer, Double> historyRoad = new HashMap<>();
        List<Double> distanceList = new ArrayList<>();

//        Node intermediate = new Node(null); // for what purpose or must be deleted?

        // se pun punctele de start si target- dar ast dupa ce background-ul e setat
        addTarget(g);

        generatedPoint = UserPanel.CoordonateAlgorithms.getGeneratedPoint();

        RRTPoints.add(new Node(new Point2D(StartAndTargetNode.get(0).point.x, StartAndTargetNode.get(0).point.y)));

        int j = 0;
        while (j < 4) {
            historyRoad.clear();
            distanceList.clear();
            //generare sample
            int x = (int) generatedPoint.get(j).point.x;
            int y = (int) generatedPoint.get(j).point.y;

            boolean tooClose = false;

            for (int i = 0; i < RRTPoints.size(); i++) {
                Node node = RRTPoints.get(i);
                double dist = Math.sqrt((node.point.x - x) * (node.point.x - x) + (node.point.y - y) * (node.point.y - y));

                if (dist < 10.0) {
                    tooClose = true;
                }
                // keep the history of all roads
                historyRoad.put(i, dist);
                distanceList.add(dist);
            }

            if (tooClose) {
                continue;
            }

            historyRoad.entrySet().stream().sorted(
                    new Comparator<Map.Entry<Integer, Double>>() {
                        @Override
                        public int compare(Map.Entry<Integer, Double> o1, Map.Entry<Integer, Double> o2) {
                            return (int) (o1.getValue() - o2.getValue());
                        }
                    }
            );

            Collections.sort(distanceList);

            boolean found = false;
            int index = 0;
            while (!found && index < distanceList.size()) {
                //calculam noul punct
                int pozClosest = positionByDistance(distanceList.get(index), historyRoad);
                double disClosest = distanceList.get(index);
                double delX = RRTMultiplier * ((x - RRTPoints.get(pozClosest).point.x) / disClosest);
                double delY = RRTMultiplier * ((y - RRTPoints.get(pozClosest).point.y) / disClosest);

                float newX = (float) delX + RRTPoints.get(pozClosest).point.x;
                float newY = (float) delY + RRTPoints.get(pozClosest).point.y;

                boolean collision = false;

                Line2D line = new Line2D(RRTPoints.get(pozClosest).point.x, RRTPoints.get(pozClosest).point.y, newX, newY);
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
                    this.RRTPoints.add(new Node(RRTPoints.get(pozClosest), new Point2D(newX, newY)));
                    g.setStroke(Color.BROWN);
                    g.strokeLine((double) RRTPoints.get(pozClosest).point.x, (double) RRTPoints.get(pozClosest).point.y, (double) newX, (double) newY);
                    j++;
                    found = true;
                } else {
                    index++;
                }
            }
        }

        RRTPoints.clear();
        generatedPoint.clear();
    }
 public void addRRTforCase2a() {
        // se creaza background = se seteaza daca e cazul obstacolele
        GraphicsContext g = getGraphicsContext2D();
        paintBackground();

        g.setLineWidth(1.0);
        g.setLineDashes(1);

        Map<Integer, Double> historyRoad = new HashMap<>();
        List<Double> distanceList = new ArrayList<>();

//        Node intermediate = new Node(null); // for what purpose or must be deleted?

        // se pun punctele de start si target- dar ast dupa ce background-ul e setat
        addTarget(g);

        generatedPoint = UserPanel.CoordonateAlgorithms.getGeneratedPoint();

        RRTPoints.add(new Node(new Point2D(StartAndTargetNode.get(0).point.x, StartAndTargetNode.get(0).point.y)));

        int j = 0;
        while (j < 3) {
            historyRoad.clear();
            distanceList.clear();
            //generare sample
            int x = (int) generatedPoint.get(j).point.x;
            int y = (int) generatedPoint.get(j).point.y;

            boolean tooClose = false;

            for (int i = 0; i < RRTPoints.size(); i++) {
                Node node = RRTPoints.get(i);
                double dist = Math.sqrt((node.point.x - x) * (node.point.x - x) + (node.point.y - y) * (node.point.y - y));

                if (dist < 10.0) {
                    tooClose = true;
                }
                // keep the history of all roads
                historyRoad.put(i, dist);
                distanceList.add(dist);
            }

            if (tooClose) {
                continue;
            }

            historyRoad.entrySet().stream().sorted(
                    new Comparator<Map.Entry<Integer, Double>>() {
                        @Override
                        public int compare(Map.Entry<Integer, Double> o1, Map.Entry<Integer, Double> o2) {
                            return (int) (o1.getValue() - o2.getValue());
                        }
                    }
            );

            Collections.sort(distanceList);

            boolean found = false;
            int index = 0;
            while (!found && index < distanceList.size()) {
                //calculam noul punct
                int pozClosest = positionByDistance(distanceList.get(index), historyRoad);
                double disClosest = distanceList.get(index);
                double delX = RRTMultiplier * ((x - RRTPoints.get(pozClosest).point.x) / disClosest);
                double delY = RRTMultiplier * ((y - RRTPoints.get(pozClosest).point.y) / disClosest);

                float newX = (float) delX + RRTPoints.get(pozClosest).point.x;
                float newY = (float) delY + RRTPoints.get(pozClosest).point.y;

                boolean collision = false;

                Line2D line = new Line2D(RRTPoints.get(pozClosest).point.x, RRTPoints.get(pozClosest).point.y, newX, newY);
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
                    this.RRTPoints.add(new Node(RRTPoints.get(pozClosest), new Point2D(newX, newY)));
                    g.setStroke(Color.BROWN);
                    g.strokeLine((double) RRTPoints.get(pozClosest).point.x, (double) RRTPoints.get(pozClosest).point.y, (double) newX, (double) newY);
                    j++;
                    found = true;
                } else {
                    index++;
                }
            }
        }

        RRTPoints.clear();
        generatedPoint.clear();
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

    public void addRRTStar() {
        GraphicsContext g = getGraphicsContext2D();
        paintBackground();
        g.setLineWidth(1.0);
        g.setLineDashes(1);

        addTarget(g);

        generatedPoint = UserPanel.CoordonateAlgorithms.getGeneratedPoint();

        RRTPoints.add(new Node(new Point2D(StartAndTargetNode.get(0).point.x, StartAndTargetNode.get(0).point.y)));

        int j = 0;
        while (j < 4) {

            //generare sample
            int x = (int) generatedPoint.get(j).point.x;
            int y = (int) generatedPoint.get(j).point.y;

            double closestDistance = 99999;
            boolean tooClose = false;

            Node closestNode = null;
            boolean isContinuity;

            for (Node node : RRTPoints) {

                double dist = Math.sqrt((node.point.x - x) * (node.point.x - x) + (node.point.y - y) * (node.point.y - y));

                if (dist < 10) {
                    tooClose = true;
                }

                if (dist < closestDistance) {
                    isContinuity = true;
                    if (node.point.x == StartAndTargetNode.get(1).point.x &&
                            node.point.y == StartAndTargetNode.get(1).point.y) {
                        isContinuity = checkContinuity(new Node(null, new Point2D(x, y)));

                    }

                    if (isContinuity) {
                        closestDistance = dist;
                        closestNode = node;
                    }
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
                    isContinuity = true;
                    if (node.point.x == StartAndTargetNode.get(1).point.x &&
                            node.point.y == StartAndTargetNode.get(1).point.y) {
                        isContinuity = checkContinuity(new Node(null, new Point2D(x, y)));

                    }
                    if (isContinuity) {
                        node.helper = dist;
                        closeNodes.add(node);
                    }
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

            g.setFill(Color.BLUE);
            g.fillOval(newX - d, newY - d, 2 * d, 2 * d);

            toAdd = new Node(closestNode, new Point2D(newX, newY));
            RRTPoints.add(toAdd);
            g.setStroke(Color.BLACK);
            g.strokeLine(closestNode.point.x, closestNode.point.y, newX, newY);

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

            j++;
        }

        RRTPoints.clear();
        generatedPoint.clear();
    }
    public void addRRTStarforCase2a() {
        GraphicsContext g = getGraphicsContext2D();
        paintBackground();
        g.setLineWidth(1.0);
        g.setLineDashes(1);

        addTarget(g);

        generatedPoint = UserPanel.CoordonateAlgorithms.getGeneratedPoint();

        RRTPoints.add(new Node(new Point2D(StartAndTargetNode.get(0).point.x, StartAndTargetNode.get(0).point.y)));

        int j = 0;
        while (j < 3) {

            //generare sample
            int x = (int) generatedPoint.get(j).point.x;
            int y = (int) generatedPoint.get(j).point.y;

            double closestDistance = 99999;
            boolean tooClose = false;

            Node closestNode = null;
            boolean isContinuity;

            for (Node node : RRTPoints) {

                double dist = Math.sqrt((node.point.x - x) * (node.point.x - x) + (node.point.y - y) * (node.point.y - y));

                if (dist < 10) {
                    tooClose = true;
                }

                if (dist < closestDistance) {
                    isContinuity = true;
                    if (node.point.x == StartAndTargetNode.get(1).point.x &&
                            node.point.y == StartAndTargetNode.get(1).point.y) {
                        isContinuity = checkContinuity(new Node(null, new Point2D(x, y)));

                    }

                    if (isContinuity) {
                        closestDistance = dist;
                        closestNode = node;
                    }
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
                    isContinuity = true;
                    if (node.point.x == StartAndTargetNode.get(1).point.x &&
                            node.point.y == StartAndTargetNode.get(1).point.y) {
                        isContinuity = checkContinuity(new Node(null, new Point2D(x, y)));

                    }
                    if (isContinuity) {
                        node.helper = dist;
                        closeNodes.add(node);
                    }
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

            g.setFill(Color.BLUE);
            g.fillOval(newX - d, newY - d, 2 * d, 2 * d);

            toAdd = new Node(closestNode, new Point2D(newX, newY));
            RRTPoints.add(toAdd);
            g.setStroke(Color.BLACK);
            g.strokeLine(closestNode.point.x, closestNode.point.y, newX, newY);

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

            j++;
        }

        RRTPoints.clear();
        generatedPoint.clear();
    }

    public void connect() {

        for (int j = 0; j < PRMPoints.size(); j++) {
            NodeAStar intermediar;
            if (j < 1) {
                intermediar = new NodeAStar(3.0, j);
            } else if (j < 2) {
                intermediar = new NodeAStar(2.0, j);
            } else {
                intermediar = new NodeAStar(1.0, j);
            }

            listAStar.add(intermediar);
        }

        for (int j = 0; j < PRMPoints.size(); j++) {
            Point2D point = PRMPoints.get(j);

            List<Point2D> closest = new ArrayList<>();
            List<Double> closestDist = new ArrayList<>();

            for (int i = 0; i < numOfConnections; i++) {
                closest.add(null);
                closestDist.add(99999.0);
            }


            for (int k = 0; k < PRMPoints.size(); k++) {
                Point2D p = PRMPoints.get(k);
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
                    if (closestDist.get(i) > dist && dist != 0) {
                        closestDist.add(k, dist);
                        closest.add(k, p);
                        break;
                    }
                }
            }

            //deseneaza linile
            List<Point2D> edges = new ArrayList<>();
            for (int i = 0; i < numOfConnections; i++) {

                Point2D close = closest.get(i);
                if (close != null) {
                    GraphicsContext g = getGraphicsContext2D();
                    g.setStroke(Color.ORANGERED);
                    g.setLineWidth(4);
                    g.strokeLine(close.x, close.y, point.x, point.y);

                    double dist = Math.sqrt((close.x - point.x) * (close.x - point.x) + (close.y - point.y) * (close.y - point.y));

                    listAStar.get(j).addBranch((int) dist, listAStar.get(i));
                    edges.add(close);
                }
            }
        }

    }

    public void reset() {
        RRTPoints.clear();
        PRMPoints.clear();
        paintBackground();
    }

    public void setObstacles(int n) {
        obstacles = obstacleSets.get(n);
        reset();
    }

    public void setRRTMultiplier(int m) {
        RRTMultiplier = m;
    }


    public boolean checkContinuity(Node currentNode) {
        boolean ok = false;
        int i = 0;
        while (ok == false && i < RRTPoints.size()) {
            Node node = RRTPoints.get(i);
            if (node.parent != null && node.parent.point.x == currentNode.point.x &&
                    node.parent.point.y == currentNode.point.y) {
                ok = true;
            }
            i++;
        }
        return ok;
    }

    public int positionByDistance(Double distance, Map<Integer, Double> map) {
        int poz = 0;

        for (Integer key : map.keySet()) {
            if (map.get(key).equals(distance))
                poz = key;
        }

        return poz;
    }

    private class NodeAStar implements Comparable<NodeAStar> {
        // Id for readability of result purposes
        public int id;

        // Parent in the path
        public NodeAStar parent = null;

        public List<Edge> neighbors;

        // Evaluation functions
        public double f = Double.MAX_VALUE;
        public double g = Double.MAX_VALUE;
        // Hardcoded heuristic
        public double h;

        NodeAStar(double h, int id) {
            this.h = h;
            this.id = id;
            this.neighbors = new ArrayList<>();
        }

        @Override
        public int compareTo(NodeAStar n) {
            return Double.compare(this.f, n.f);
        }

        public class Edge {
            Edge(int weight, NodeAStar node) {
                this.weight = weight;
                this.node = node;
            }

            public int weight;
            public NodeAStar node;
        }

        public void addBranch(int weight, NodeAStar node) {
            Edge newEdge = new Edge(weight, node);
            neighbors.add(newEdge);
        }

        public double calculateHeuristic(NodeAStar target) {
            return this.h;
        }
    }

    public NodeAStar aStar(NodeAStar start, NodeAStar target) {
        PriorityQueue<NodeAStar> closedList = new PriorityQueue<>();
        PriorityQueue<NodeAStar> openList = new PriorityQueue<>();

        start.f = start.g + start.calculateHeuristic(target);
        openList.add(start);

        while (!openList.isEmpty()) {
            NodeAStar n = openList.peek();
            if (n == target) {
                return n;
            }

            for (NodeAStar.Edge edge : n.neighbors) {
                NodeAStar m = edge.node;
                double totalWeight = n.g + edge.weight;

                if (!openList.contains(m) && !closedList.contains(m)) {
                    m.parent = n;
                    m.g = totalWeight;
                    m.f = m.g + m.calculateHeuristic(target);
                    openList.add(m);
                } else {
                    if (totalWeight < m.g) {
                        m.parent = n;
                        m.g = totalWeight;
                        m.f = m.g + m.calculateHeuristic(target);

                        if (closedList.contains(m)) {
                            closedList.remove(m);
                            openList.add(m);
                        }
                    }
                }
            }

            openList.remove(n);
            closedList.add(n);
        }
        return null;
    }

    public void paintPRM(NodeAStar target) {
        GraphicsContext g = getGraphicsContext2D();

        NodeAStar n = target;

        if (n == null)
            return;

        while (n.parent != null) {
            float currentX = PRMPoints.get(n.id).x;
            float currentY = PRMPoints.get(n.id).y;

            n = n.parent;
            float parentX = PRMPoints.get(n.id).x;
            float parentY = PRMPoints.get(n.id).y;
            g.setLineDashes(45);
            g.setLineWidth(7.0);
            g.setStroke(Color.BLUEVIOLET);
            g.strokeLine(currentX, currentY, parentX, parentY);
        }
    }

}
