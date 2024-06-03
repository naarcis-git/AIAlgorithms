import com.sun.javafx.geom.Line2D;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.*;

public class MotionSpace extends Canvas {

//    private static final DecimalFormat decfor = new DecimalFormat("0.00");

    private int numOfConnections = 10;

    private int optimiseDistance = 1000;

    private int RRTMultiplier = 8;

    List<Node> RRTPoints = new ArrayList<>();

    List<Node> generatedPoint = new ArrayList<>();
    List<Point2D> PRMPoints = new ArrayList<>();
    List<Node> StartAndTargetNode = new ArrayList<>();

    List<RectBounds> obstacles = new ArrayList<>();

    List<List<RectBounds>> obstacleSets = new ArrayList<>();

    List<NodeAStar> listAStar = new ArrayList<>();

    private int size;
    // TODO: 01/06/2024 ADD number CRITERIA for algorithms , like n!!

    public MotionSpace(int size) {
        super(size + 250, 390);
        this.size = size;

        // Add mouse click listener
        setOnMouseClicked(new EventHandler<MouseEvent>() {
            // TODO: 01/06/2024 ADD number CRITERIA for algorithms , like n!!

            @Override
            public void handle(MouseEvent mouseEvent) {
                // Get click coordinates relative to the canvas
                double clickX = mouseEvent.getX();
                double clickY = mouseEvent.getY();
                if (StartAndTargetNode.size() == 0) {
                    StartAndTargetNode.add(new Node(null, new Point2D((float) clickX, (float) clickY)));
                } else {
                    StartAndTargetNode.add(new Node(null, new Point2D((float) clickX, (float) clickY)));
                }

                // Print or store the coordinates
                System.out.println("Clicked at: (" + clickX + ", " + clickY + ")");
            }
        });

        List<RectBounds> r1 = new ArrayList<>();
        r1.add(new RectBounds(300, 250, 400, 350));
        r1.add(new RectBounds(300, 50, 400, 100));

        List<RectBounds> r2 = new ArrayList<>();
        r2.add(new RectBounds(300, 0, 400, 400));
        r2.add(new RectBounds(50, 350, 250, 450));

        //set3
        List<RectBounds> r3 = new ArrayList<>();
        r3.add(new RectBounds(50, 50, 450, 100));
        r3.add(new RectBounds(50, 400, 450, 450));
        r3.add(new RectBounds(50, 105, 100, 450));
        r3.add(new RectBounds(400, 50, 450, 395));

        List<RectBounds> r4 = new ArrayList<>();
        r4.add(new RectBounds(300, 250, 400, 350));
        r4.add(new RectBounds(300, 50, 400, 100));
        r4.add(new RectBounds(650, 110, 750, 400));
        r4.add(new RectBounds(650, 110, 750, 400));


        obstacleSets.add(r1);
        obstacleSets.add(r2);
        obstacleSets.add(r3);
        obstacleSets.add(r4);
        obstacleSets.add(new ArrayList<>());

        reset();
    }

    public void createObstacles() {
        GraphicsContext g = getGraphicsContext2D();
        getGraphicsContext2D().setFill(Color.PERU);
        getGraphicsContext2D().fillRect(0, 0, size + 250, size);

        int b = 2;
        for (RectBounds r : obstacles) {
            g.setFill(Color.BLACK);
            g.fillRect(r.getMinX(), r.getMinY(), r.getWidth(), r.getHeight());
            g.setFill(Color.DARKOLIVEGREEN);

            g.fillRect(r.getMinX() + b, r.getMinY() + b, r.getWidth() - 2 * b, r.getHeight() - 2 * b);
        }
    }

    public long addPRM(List<String> listConnectedPoints) {
        GraphicsContext g = getGraphicsContext2D();

        g.setLineWidth(1.0);
        g.setLineDashes(1);
        byte width = 6;  // width for point

        generateStartAndTarget();

        generatedPoint = UserPanel.CoordonateAlgorithms.getGeneratedPoint();

        long start = System.nanoTime();
        long end;
        int j = 0;
        while (j < generatedPoint.size()) {

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
                drawPoint(g, width, x, y);
                PRMPoints.add(new Point2D(x, y));
            }

            j++;
        }

        connect(listConnectedPoints);
        aStar(listAStar.get(0), listAStar.get(listAStar.size() - 1));
        paintShortestRoadPRM(listAStar.get(listAStar.size() - 1));

        // colorare puncte start si target
        drawStartAndTargetPoints(width);

        // color points
        for (int i = 0; i < PRMPoints.size(); i++) {
            setMarkers(PRMPoints.get(i).x, PRMPoints.get(i).y, i);
        }

        end = System.nanoTime();

        // clean
        PRMPoints.clear();
        generatedPoint.clear();
        listAStar.clear();
        StartAndTargetNode.clear();

        long result = (end - start) / 1000;
        System.out.println("PRM " + result);

        return result;
    }

    public long addPRMRandom(List<String> listConnectedPoints, List<Node> randomPoints) {
        GraphicsContext g = getGraphicsContext2D();
        g.setLineWidth(1.0);
        g.setLineDashes(1);

        int n = 7;
        int b = 6;

        long start = System.nanoTime();
        long end;

        for (int i = 0; i < n; i++) {
            boolean free = false;
            int x = 0;
            int y = 0;

            while (!free) {
                if (i != 0) {
                    x = randomInRange(27, (int) getWidth() - 27);
                    y = randomInRange(27, (int) getHeight() - 27);
                } else {
                    x = (int) StartAndTargetNode.get(0).point.x;
                    y = (int) StartAndTargetNode.get(0).point.y;
                }

                free = true;
                for (RectBounds rect : obstacles) {
                    if (rect.contains(new Point2D(x, y))) {
                        free = false;
                        break;
                    }
                }
            }

            g.setFill(Color.BLACK);
            g.fillOval(x - b, y - b, 2 * b, 2 * b);
            PRMPoints.add(new Point2D(x, y));
            randomPoints.add(new Node(null, new Point2D(x, y)));
        }

        connectForPRM(listConnectedPoints, randomPoints);
        aStar(listAStar.get(0), listAStar.get(listAStar.size() - 1));
        paintShortestRoadPRM(listAStar.get(listAStar.size() - 1));

        // setare initiala//letter pentru noduri
        for (int i = 0; i < PRMPoints.size(); i++) {
            setMarkers(PRMPoints.get(i).x, PRMPoints.get(i).y, i);
        }

        // colorare puncte start si target
        drawStartAndTargetPoints(6);

        PRMPoints.clear();
        listAStar.clear();
        StartAndTargetNode.clear();
        end = System.nanoTime();

        long result = (end - start) / 1000;
        System.out.println("RRT STAR " + result);
        return result;
    }


    public long addRRT(List<String> listConnectedPoints) {
        // se creaza background = se seteaza daca e cazul obstacolele
        GraphicsContext g = getGraphicsContext2D();

        g.setLineWidth(1.0);
        g.setLineDashes(1);
        byte width = 6;

        Map<Integer, Double> possibleRoadByDistance = new HashMap<>();
        List<Double> distanceList = new ArrayList<>();

        // se pun punctele de start si target- dar ast dupa ce background-ul e setat
        generateStartAndTarget();

        generatedPoint = UserPanel.CoordonateAlgorithms.getGeneratedPoint();

        RRTPoints.add(new Node(new Point2D(StartAndTargetNode.get(0).point.x, StartAndTargetNode.get(0).point.y)));

        long start = System.nanoTime();
        long end;
        int j = 0;
        while (j < generatedPoint.size()) {
            possibleRoadByDistance.clear();
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
                possibleRoadByDistance.put(i, dist);
                distanceList.add(dist);
            }

            if (tooClose) {
                continue;
            }

            possibleRoadByDistance.entrySet().stream().sorted(
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
                int pozClosest = positionByDistance(distanceList.get(index), possibleRoadByDistance);
                double disClosest = distanceList.get(index);

                Point2D newPoint = generatedPoint2D(x, y, RRTPoints.get(pozClosest).point.x, RRTPoints.get(pozClosest).point.y, disClosest);

                Line2D line = new Line2D(RRTPoints.get(pozClosest).point.x, RRTPoints.get(pozClosest).point.y, newPoint.x, newPoint.y);

                if (isNotIntersected(line)) {
                    this.RRTPoints.add(new Node(RRTPoints.get(pozClosest), newPoint));
                    drawPoint(g, width, newPoint.x, newPoint.y);

                    drawLine(g, new Point2D(RRTPoints.get(pozClosest).point.x, RRTPoints.get(pozClosest).point.y),
                            newPoint, 6, Color.ROYALBLUE);
                    found = true;
                    listConnectedPoints.add("" + (char) ((int) 'A' + pozClosest) + (char) ((int) 'A' + j + 1));
                    j++;
                } else {
                    index++;
                }
            }
        }
        // creare  drum optim
        paintShortestRoadRRT(RRTPoints.get(RRTPoints.size() - 1));

        // colorare puncte start si target
        drawStartAndTargetPoints(width);

        // color points
        for (int i = 0; i < RRTPoints.size(); i++) {
            setMarkers(RRTPoints.get(i).point.x, RRTPoints.get(i).point.y, i);
        }

        end = System.nanoTime();

        RRTPoints.clear();
        generatedPoint.clear();
        StartAndTargetNode.clear();

        long result = (end - start) / 1000;
        System.out.println("RRT " + result);

        return result;
    }

    public long addRRTRandom(List<String> listConnectedPoints, List<Node> randomPoints) {
        GraphicsContext g = getGraphicsContext2D();
        g.setLineWidth(1.0);
        g.setLineDashes(1);

        long start = System.nanoTime();
        long end;

        RRTPoints.add(StartAndTargetNode.get(0));
        randomPoints.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x, StartAndTargetNode.get(0).point.y)));

        int n = 10;
        int x;
        int y;
        int d = 6;

        // draw the target point
        g.setFill(Color.GOLD);
        g.fillOval(StartAndTargetNode.get(1).point.x - d, StartAndTargetNode.get(1).point.y - d, 2 * d, 2 * d);

        int j = 0;
        while (j < n) {

            if (j != 0) {
                x = randomInRange(27, (int) getWidth() - 27);
                y = randomInRange(27, (int) getHeight() - 27);
            } else {
                x = (int) StartAndTargetNode.get(0).point.x;
                y = (int) StartAndTargetNode.get(0).point.y;
            }


            double closestDistance = 99999;
            Node closestNode = null;

            boolean tooClose = false;

            // case for start -> draw the start point
            if (j == 0) {
                g.setFill(Color.RED);
                g.fillOval(x - d, y - d, 2 * d, 2 * d);
            }

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
                if (j == 0) {
                    j = j + 1;
                }
                continue;
            }

            double delX = RRTMultiplier * ((x - closestNode.point.x) / closestDistance);
            double delY = RRTMultiplier * ((y - closestNode.point.y) / closestDistance);

            float newX = (float) delX + closestNode.point.x;//norma euclidiana = x/ distanta euclidiana
            float newY = (float) delY + closestNode.point.y;

            if (newX > (getWidth() - 27) || newX < 27 || newY > (getHeight() - 27) || newY < 27) {
                continue;
            }
            boolean collision = false;

            Line2D line = new Line2D(closestNode.point.x, closestNode.point.y, newX, newY);
            for (RectBounds r : obstacles) {
                if (line.intersects(r)) {
                    collision = true;
                    break;
                }
            }

            if (collision) {
                collision = false;

                newX = (float) delX / 2 + closestNode.point.x;
                newY = (float) delY / 2 + closestNode.point.y;

                if (newX > (getWidth() - 27) || newX < 27 || newY > (getHeight() - 27) || newY < 27) {
                    continue;
                }

                line = new Line2D(closestNode.point.x, closestNode.point.y, newX, newY);

                for (RectBounds r : obstacles) {
                    if (line.intersects(r)) {
                        collision = true;
                        break;
                    }
                }

                // daca nu se intersecteaza cu obstacolul
                if (!collision) {
                    g.setFill(Color.BLACK);
                    g.fillOval(newX - d, newY - d, 2 * d, 2 * d);

                    Node currentNode = new Node(closestNode, new Point2D(newX, newY));
                    RRTPoints.add(currentNode);
                    randomPoints.add(currentNode);
                    g.setStroke(Color.ROYALBLUE);
                    g.setLineWidth(6);
                    g.strokeLine(closestNode.point.x, closestNode.point.y, newX, newY);
                    listConnectedPoints.add("" + (char) ((int) 'A' + positionForClosestPoint(RRTPoints, closestNode)) + (char) ((int) 'A' + j));


                    //calcul si setari pentru punct final
                    if (isGoal(new Node(StartAndTargetNode.get(1), new Point2D(newX, newY))) && randomPoints.stream().noneMatch(e -> e.point == StartAndTargetNode.get(1).point)) {
                        Node finalBound = new Node(currentNode, new Point2D(StartAndTargetNode.get(1).point.x, StartAndTargetNode.get(1).point.y));
                        RRTPoints.add(finalBound);
                        randomPoints.add(StartAndTargetNode.get(1));
                        g.setStroke(Color.ROYALBLUE);
                        g.setLineWidth(6);
                        g.strokeLine(StartAndTargetNode.get(1).point.x, StartAndTargetNode.get(1).point.y, newX, newY);
                        listConnectedPoints.add("" + (char) ((int) 'A' + j) + (char) ((int) 'A' + positionForClosestPoint(RRTPoints, finalBound)));
                        break;
                    }
                } else {
                    continue;
                }

            }
            // daca din prima nu avem intersectie cu obstacolele
            else {
                g.setFill(Color.BLACK);
                g.fillOval(newX - d, newY - d, 2 * d, 2 * d);

                Node currentNode = new Node(closestNode, new Point2D(newX, newY));
                RRTPoints.add(currentNode);
                randomPoints.add(currentNode);
                g.setStroke(Color.ROYALBLUE);
                g.setLineWidth(6);
                g.strokeLine(closestNode.point.x, closestNode.point.y, newX, newY);
                listConnectedPoints.add("" + (char) ((int) 'A' + positionForClosestPoint(RRTPoints, closestNode)) + (char) ((int) 'A' + j));

                if (isGoal(new Node(StartAndTargetNode.get(1), new Point2D(newX, newY))) && randomPoints.stream().noneMatch(e -> e.point == StartAndTargetNode.get(1).point)) {
                    Node finalBound = new Node(currentNode, new Point2D(StartAndTargetNode.get(1).point.x, StartAndTargetNode.get(1).point.y));
                    RRTPoints.add(finalBound);
                    randomPoints.add(StartAndTargetNode.get(1));
                    g.setStroke(Color.ROYALBLUE);
                    g.setLineWidth(6);
                    g.strokeLine(StartAndTargetNode.get(1).point.x, StartAndTargetNode.get(1).point.y, newX, newY);
                    listConnectedPoints.add("" + (char) ((int) 'A' + j) + (char) ((int) 'A' + positionForClosestPoint(RRTPoints, finalBound)));
                    break;
                }
            }
            j = j + 1;
        }

        // caz in care nu avem legatura cu punctul final
        if (randomPoints.stream().noneMatch(e -> e.point == StartAndTargetNode.get(1).point)) {
            RRTPoints.add(new Node(null, StartAndTargetNode.get(1).point));
            randomPoints.add(StartAndTargetNode.get(1));
        }

        // setare initiala//letter pentru noduri
        for (int i = 0; i < randomPoints.size(); i++) {
            setMarkers(randomPoints.get(i).point.x, randomPoints.get(i).point.y, i);
        }

        paintShortestRoadRRT(RRTPoints.get(RRTPoints.size() - 1));

        // colorare puncte start si target
        drawStartAndTargetPoints(6);

        end = System.nanoTime();

        RRTPoints.clear();
        StartAndTargetNode.clear();

        long result = (end - start) / 1000;
        System.out.println("RRT STAR " + result);

        return result;
    }


    public long addRRTStar(List<String> listConnectedPoints) {
        GraphicsContext g = getGraphicsContext2D();
        g.setLineWidth(1.0);
        g.setLineDashes(1);

        int width = 6;

        generateStartAndTarget();

        generatedPoint = UserPanel.CoordonateAlgorithms.getGeneratedPoint();

        RRTPoints.add(new Node(new Point2D(StartAndTargetNode.get(0).point.x, StartAndTargetNode.get(0).point.y)));

        long start = System.nanoTime();
        long end;

        int j = 0;
        while (j < generatedPoint.size()) {

            // generare sample
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

//            Point2D newPoint = generatedPoint2D(x, y, closestNode.point.x, closestNode.point.y, closestDistance);
            double angle = Math.atan2(y - closestNode.point.y, x - closestNode.point.x);
            float newX = (float) (closestDistance * Math.cos(angle)) + closestNode.point.x;
            float newY = (float) (closestDistance * Math.sin(angle)) + closestNode.point.y;

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

                Line2D line = new Line2D(node.point.x, node.point.y, newX, newY);

                if (isNotIntersected(line)) {
                    if (node.distance + node.helper < smallestDist) {
                        smallestDist = node.distance + node.helper;
                        closestNode = node;
                        listConnectedPoints.add("" + (char) ((int) 'A' + RRTPoints.indexOf(closestNode)) + (char) ((int) 'A' + j + 1));
                    }
                }
            }

            if (closestNode == null) {
                continue;
            }

            Node toAdd;
            drawPoint(g, width, newX, newY);

            toAdd = new Node(closestNode, new Point2D(newX, newY));
            RRTPoints.add(toAdd);

            drawLine(g, new Point2D(closestNode.point.x, closestNode.point.y), new Point2D(newX, newY), 6, Color.ROYALBLUE);

            boolean changed = false;
            for (Node node : closeNodes) {
                // TODO: 04/05/2024 add variable replacing or subtitute cost
                if (node.helper + toAdd.distance < node.distance) {
                    //2'nd verification for intersection
                    Line2D line = new Line2D(node.point.x, node.point.y, newX, newY);

                    if (isNotIntersected(line)) {
                        node.parent = toAdd;
                        node.distance = node.parent.distance + node.helper;
                        listConnectedPoints.add("" + (char) ((int) 'A' + RRTPoints.indexOf(closestNode) + 1) + (char) ((int) 'A' + j + 1));

                        changed = true;
                    }
                }
            }

            if (changed) {
                repaintRRTStar(listConnectedPoints);
            }

            j++;
        }

        paintShortestRoadRRT(RRTPoints.get(RRTPoints.size() - 1));

        // colorare puncte start si target
        drawStartAndTargetPoints(width);

        // color points
        for (int i = 0; i < RRTPoints.size(); i++) {
            setMarkers(RRTPoints.get(i).point.x, RRTPoints.get(i).point.y, i);
        }

        end = System.nanoTime();

        RRTPoints.clear();
        generatedPoint.clear();
        StartAndTargetNode.clear();

        long result = (end - start) / 1000;
        System.out.println("RRT STAR " + result);

        return result;
    }


    public long addRRTStarRandom(List<String> listConnectedPoints, List<Node> randomPoints) {
        int n = 10;
        GraphicsContext g = getGraphicsContext2D();
        g.setLineWidth(1.0);
        g.setLineDashes(1);

        long start = System.nanoTime();
        long end;

        RRTPoints.add(StartAndTargetNode.get(0));
        randomPoints.add(StartAndTargetNode.get(0));
        int d = 6;

        // draw the target point
        g.setFill(Color.GOLD);
        g.fillOval(StartAndTargetNode.get(1).point.x - d, StartAndTargetNode.get(1).point.y - d, 2 * d, 2 * d);
        int x;
        int y;
        int j = 0;
        while (j < n) {

            if (j != 0) {
                x = randomInRange(27, (int) getWidth() - 27);
                y = randomInRange(27, (int) getHeight() - 27);
            } else {
                x = (int) StartAndTargetNode.get(0).point.x;
                y = (int) StartAndTargetNode.get(0).point.y;
            }

            // case for start -> draw the start point
            if (j == 0) {
                g.setFill(Color.RED);
                g.fillOval(x - d, y - d, 2 * d, 2 * d);
            }

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
                if (j == 0)
                    j++;
                continue;
            }

            double delX = RRTMultiplier * ((x - closestNode.point.x) / closestDistance);
            double delY = RRTMultiplier * ((y - closestNode.point.y) / closestDistance);

            float newX = (float) delX + closestNode.point.x;
            float newY = (float) delY + closestNode.point.y;

            if (newX > (getWidth() - 27) || newX < 27 || newY > (getHeight() - 27) || newY < 27) {
                continue;
            }

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

            g.setFill(Color.BLACK);
            g.fillOval(newX - d, newY - d, 2 * d, 2 * d);

            Node toAdd = new Node(closestNode, new Point2D(newX, newY));
            RRTPoints.add(toAdd);
            randomPoints.add(toAdd);
            g.setStroke(Color.ROYALBLUE);
            g.setLineWidth(6);
            g.strokeLine(closestNode.point.x, closestNode.point.y, newX, newY);
            listConnectedPoints.add("" + (char) ((int) 'A' + positionForClosestPoint(RRTPoints, closestNode)) + (char) ((int) 'A' + j));

            if (isGoal(new Node(StartAndTargetNode.get(1), new Point2D(newX, newY)))) {
                g.setStroke(Color.ROYALBLUE);
                Node finalBound = new Node(
                        toAdd,
                        new Point2D(StartAndTargetNode.get(1).point.x, StartAndTargetNode.get(1).point.y));
                RRTPoints.add(finalBound);
                g.setLineWidth(6);

                g.strokeLine(StartAndTargetNode.get(1).point.x, StartAndTargetNode.get(1).point.y, newX, newY);
                randomPoints.add(finalBound);
                listConnectedPoints.add("" + (char) ((int) 'A' + j) + (char) ((int) 'A' + positionForClosestPoint(RRTPoints, finalBound)));
                break;
            }

            boolean changed = false;
            for (Node node : closeNodes) {
                //node.helper= dist
                if (node.helper + toAdd.distance < node.distance) {

                    boolean canConnect = true;
                    Line2D line = new Line2D(node.point.x, node.point.y, newX, newY);

                    for (RectBounds rect : obstacles) {
                        if (line.intersects(rect)) {
                            canConnect = false;
                            break;
                        }
                    }
                    // TODO: 21/03/2024  verifi condition+ note parrent for clossest point
                    if (canConnect) {
                        node.parent = toAdd;
                        node.distance = node.parent.distance + node.helper;
                        changed = true;
                    }
                }

            }

            if (changed) {
                repaintRRT(randomPoints, listConnectedPoints);
            }
            j++;
        }

        // caz in care nu avem legatura cu punctul final
        if (randomPoints.stream().noneMatch(e -> e.point.equals(StartAndTargetNode.get(1).point))) {
            RRTPoints.add(new Node(null, StartAndTargetNode.get(1).point));
            randomPoints.add(StartAndTargetNode.get(1));
        }

        for (int i = 0; i < randomPoints.size(); i++) {
            setMarkers(randomPoints.get(i).point.x, randomPoints.get(i).point.y, i);
        }

        paintShortestRoadRRT(RRTPoints.get(RRTPoints.size() - 1));

        // colorare puncte start si target
        drawStartAndTargetPoints(6);

        end = System.nanoTime();

        RRTPoints.clear();
        StartAndTargetNode.clear();

        long result = (end - start) / 1000;
        System.out.println("RRT STAR " + result);

        return result;
    }


    public void connect(List<String> listConnectedPoints) {
        for (int j = 0; j < PRMPoints.size(); j++) {
            NodeAStar intermediar;
            //setting euristics
            if (PRMPoints.get(j).x < 370) {
                intermediar = new NodeAStar(1.0, j);
            } else if (PRMPoints.get(j).x >= 370 && PRMPoints.get(j).x < 590) {
                intermediar = new NodeAStar(2.0, j);
            } else if (PRMPoints.get(j).x >= 590 && PRMPoints.get(j).x < 632) {
                intermediar = new NodeAStar(3.0, j);
            } else
                intermediar = new NodeAStar(4.0, j);

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
                Line2D line = new Line2D(p.x, p.y, point.x, point.y);

                if (!isNotIntersected(line)) {
                    listConnectedPoints.add("" + (char) ((int) 'A' + k) + (char) ((int) 'A' + j));

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
            for (int i = 0; i <= numOfConnections; i++) {

                Point2D close = closest.get(i);
                if (close != null) {
                    GraphicsContext g = getGraphicsContext2D();
                    drawLine(g, close, point, 6, Color.ROYALBLUE);

                    double dist = Math.sqrt((close.x - point.x) * (close.x - point.x) + (close.y - point.y) * (close.y - point.y));

                    listAStar.get(j).addBranch((int) dist, listAStar.get(i));
                    edges.add(close);
                }
            }
        }

    }

    //connect for prm

    public void connectForPRM(List<String> listConnectedPoints, List<Node> randomPoints) {
        PRMPoints.add(StartAndTargetNode.get(1).point);
        randomPoints.add(new Node(null, StartAndTargetNode.get(1).point));

        for (int j = 0; j < PRMPoints.size(); j++) {
            NodeAStar intermediar;
            //setting euristics
            if (PRMPoints.get(j).x < 370) {
                intermediar = new NodeAStar(1.0, j);
            } else if (PRMPoints.get(j).x >= 370 && PRMPoints.get(j).x < 590) {
                intermediar = new NodeAStar(2.0, j);
            } else if (PRMPoints.get(j).x >= 590 && PRMPoints.get(j).x < 632) {
                intermediar = new NodeAStar(3.0, j);
            } else
                intermediar = new NodeAStar(4.0, j);

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
                    listConnectedPoints.add("" + (char) ((int) 'A' + k) + (char) ((int) 'A' + j));

                    double dist = Math.sqrt((p.x - point.x) * (p.x - point.x) + (p.y - point.y) * (p.y - point.y));
                    listAStar.get(j).addBranch((int) dist, listAStar.get(k));

                    for (int i = 0; i < numOfConnections; i++) {

                        if (closestDist.get(i) > dist) {
                            closestDist.add(i, dist);
                            closest.add(i, p);
                            break;
                        }
                    }
                }
            }

            //List<Point2D> edges = new ArrayList<>();
            for (int i = 0; i < numOfConnections; i++) {

                Point2D close = closest.get(i);
                if (close != null) {
                    GraphicsContext g = getGraphicsContext2D();

                    g.setStroke(Color.ROYALBLUE);
                    g.setLineWidth(6);
                    g.strokeLine(close.x, close.y, point.x, point.y);

                    //edges.add(close);
                }
            }
        }

    }


    public void reset() {
        RRTPoints.clear();
        PRMPoints.clear();
        createObstacles();
    }

    public void setObstacles(int n) {
        obstacles = obstacleSets.get(n);
        reset();
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

    public int positionForClosestPoint(List<Node> points, Node closestNode) {
        return points.indexOf(closestNode);
    }

    private class NodeAStar implements Comparable<NodeAStar> {
        // Id for readability of result purposes
        public int id;

        // Parent in the path
        public NodeAStar parent = null;

        public List<Edge> neighbors;

        // Evaluation functions
        public double f = 1000;
        public double g = 1000;
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

        // adauga costul ptr nodul ce trebuie adaugat
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

    private void repaintRRTStar(List<String> listConnectedPoints) {
        GraphicsContext g = getGraphicsContext2D();
        g.setLineWidth(4);
        g.setLineDashes(1);
        int d = 2;

        g.setFill(Color.BLUE);
        for (Node n : RRTPoints) {
            g.fillOval(n.point.x - d, n.point.y - d, 2 * d, 2 * d);
        }

        listConnectedPoints.clear();
        g.setStroke(Color.BLACK);
        for (Node n : RRTPoints) {
            if (n.parent != null) {
                g.strokeLine(n.point.x, n.point.y, n.parent.point.x, n.parent.point.y);
                listConnectedPoints.add("" + (char) ((int) 'A' + RRTPoints.indexOf(n) + 1) + (char) ((int) 'A' + RRTPoints.indexOf(n.parent)));

            }
        }
    }

    private void repaintRRT(List<Node> randomPoints, List<String> listConnectedPoints) {
        paintBackground();
        randomPoints.clear();
        listConnectedPoints.clear();
        randomPoints.addAll(RRTPoints);

        GraphicsContext g = getGraphicsContext2D();
        int d = 6;

        g.setFill(Color.BLACK);
        for (Node n : RRTPoints) {
            g.fillOval(n.point.x - d, n.point.y - d, 2 * d, 2 * d);
        }

        g.setStroke(Color.ROYALBLUE);

        for (int j = 0; j < RRTPoints.size(); j++) {
            Node n = RRTPoints.get(j);
            if (n.parent != null) {
                if (isGoal(n)) {
                    g.setLineWidth(6);
                    g.setStroke(Color.ROYALBLUE);
                    g.strokeLine(StartAndTargetNode.get(1).point.x, StartAndTargetNode.get(1).point.y, n.point.x, n.point.y);
                    listConnectedPoints.add("" + (char) ((int) 'A' + positionForClosestPoint(RRTPoints, n.parent)) + (char) ((int) 'A' + j));
                }
                g.strokeLine(n.point.x, n.point.y, n.parent.point.x, n.parent.point.y);
                listConnectedPoints.add("" + (char) ((int) 'A' + positionForClosestPoint(RRTPoints, n.parent)) + (char) ((int) 'A' + j));
            }
        }

        // draw the end point
        g.setFill(Color.GOLD);
        g.fillOval(StartAndTargetNode.get(1).point.x - d, StartAndTargetNode.get(1).point.y - d, 2 * d, 2 * d);

        // draw the start point
        g.setFill(Color.RED);
        g.fillOval(StartAndTargetNode.get(0).point.x - d, StartAndTargetNode.get(0).point.y - d, 2 * d, 2 * d);
    }

    private void paintShortestRoadPRM(NodeAStar target) {
        NodeAStar n = target;

        if (n == null)
            return;

        while (n.parent != null) {
            float currentX = PRMPoints.get(n.id).x;
            float currentY = PRMPoints.get(n.id).y;

            n = n.parent;
            float parentX = PRMPoints.get(n.id).x;
            float parentY = PRMPoints.get(n.id).y;
            paint(currentX, currentY, parentX, parentY);
        }
    }

    private void paintShortestRoadRRT(Node target) {
        Node n = target;

        if (n == null)
            return;

        while (n.parent != null) {
            float currentX = n.point.x;
            float currentY = n.point.y;

            n = n.parent;
            float parentX = n.point.x;
            float parentY = n.point.y;
            paint(currentX, currentY, parentX, parentY);
        }
    }

    private void paint(float currentX, float currentY, float parentX, float parentY) {
        GraphicsContext g = getGraphicsContext2D();

        g.setLineDashes(45);
        g.setLineWidth(7.0);
        g.setStroke(Color.NAVY);
        g.strokeLine(currentX, currentY, parentX, parentY);
    }

    private void drawStartAndTargetPoints(int width) {
        GraphicsContext g = getGraphicsContext2D();
        g.setFill(Color.RED);
        g.fillOval(StartAndTargetNode.get(0).point.x - width, StartAndTargetNode.get(0).point.y - width, 2 * width, 2 * width);

        // target
        g.setFill(Color.GOLD);
        g.fillOval(StartAndTargetNode.get(1).point.x - width, StartAndTargetNode.get(1).point.y - width, 2 * width, 2 * width);
    }

    private void drawPoint(GraphicsContext g, int width, float x, float y) {
        g.setFill(Color.BLACK);
        g.fillOval(x - width, y - width, 2 * width, 2 * width);
    }

    public void setMarkers(float x, float y, int index) {
        GraphicsContext g = getGraphicsContext2D();
        int letter = 'A' + index;
        g.setFill(Paint.valueOf(Color.BLACK.toString()));
        g.fillText(String.valueOf((char) letter), x - 10, y - 10, 20);
    }

    public void setDistanceNodes(float x, float y, String nodes, Integer distance) {
        GraphicsContext g = getGraphicsContext2D();

        g.setFill(Paint.valueOf(Color.BLACK.toString()));
        g.fillText(nodes + " " + distance, x - 10, y - 10, 150);
    }

    private void drawLine(GraphicsContext g, Point2D close, Point2D point, int width, Color color) {
        g.setStroke(color);
        g.setLineWidth(width);
        g.strokeLine(close.x, close.y, point.x, point.y);
    }

    private void paintBackground() {
        GraphicsContext g = getGraphicsContext2D();
        getGraphicsContext2D().setFill(Color.PERU);
        getGraphicsContext2D().fillRect(0, 0, size + 250, size);

        int b = 2;
        for (RectBounds r : obstacles) {
            g.setFill(Color.BLACK);
            g.fillRect(r.getMinX(), r.getMinY(), r.getWidth(), r.getHeight());
            g.setFill(Color.DARKOLIVEGREEN);

            g.fillRect(r.getMinX() + b, r.getMinY() + b, r.getWidth() - 2 * b, r.getHeight() - 2 * b);
        }
    }

    private Point2D generatedPoint2D(float x, float y, float closestX, float closestY, double distClosest) {
        double angle = Math.atan2(y - closestY, x - closestX);
        float newX = (float) (distClosest * Math.cos(angle)) + closestX;
        float newY = (float) (distClosest * Math.sin(angle)) + closestY;

        return new Point2D(newX, newY);
    }

    private boolean isNotIntersected(Line2D line) {
        boolean canConnect = true;
        for (RectBounds rect : obstacles) {
            if (line.intersects(rect)) {
                canConnect = false;
                break;
            }
        }

        return canConnect;
    }

    public void generateStartAndTarget() {
        StartAndTargetNode.add(new Node(null, new Point2D(240, 260)));
        StartAndTargetNode.add(new Node(null, new Point2D(370, 450)));
    }

    private boolean isGoal(Node node) {
        boolean collision = false;

        Line2D line = new Line2D(node.point.x, node.point.y, StartAndTargetNode.get(1).point.x, StartAndTargetNode.get(1).point.y);
        for (RectBounds r : obstacles) {
            if (line.intersects(r)) {
                collision = true;
                break;
            }
        }
        if (!collision) {

            // Check if the node's state is close enough to the goal (adjust based on your problem)
            double dist = Math.sqrt((node.point.x - StartAndTargetNode.get(1).point.x) * (node.point.x - StartAndTargetNode.get(1).point.x)
                    + (node.point.y - StartAndTargetNode.get(1).point.y) * (node.point.y - StartAndTargetNode.get(1).point.y));

            return dist < 150;
        }

        return false;
    }

    public void setRRTMultiplier(int m) {
        RRTMultiplier = m;
    }

    private int randomInRange(int min, int max) {
        // Check for invalid input
        if (min > max) {
            throw new IllegalArgumentException("min must be less than or equal to max");
        }

        // Create a Random object (optional for better randomness across program runs)
        Random random = new Random();

        // Generate a random int between min (inclusive) and max (inclusive)
        int randomInt = random.nextInt(max - min + 1) + min;

        return randomInt;
    }
}
