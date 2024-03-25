package com.vburmus.ai.algorithms;

import com.vburmus.ai.algorithms.models.DistanceType;
import com.vburmus.ai.graph.GraphEdge;
import com.vburmus.ai.graph.GraphNode;
import com.vburmus.ai.utils.PathUtils;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.apache.commons.math3.ml.distance.EuclideanDistance;
import org.apache.commons.math3.ml.distance.ManhattanDistance;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
@Slf4j
public class AStar {
    public static void runTimeBased(GraphNode startNode, GraphNode endNode, LocalDateTime time,
                                    DistanceType distanceType) {
        int count = 0;
        log.info("Running A* algorithm...");
        var startTime = System.currentTimeMillis();
        DistanceMeasure distance = distanceType == DistanceType.MANHATTAN ? new ManhattanDistance() :
                new EuclideanDistance();
        List<GraphNode> openNodes = new ArrayList<>();
        openNodes.add(startNode);
        List<GraphNode> closedNodes = new ArrayList<>();
        var arrivalTime = time;
        while (!openNodes.isEmpty()) {

            GraphNode currentNode = null;
            var cost = Double.POSITIVE_INFINITY;
            for (var testNode : openNodes) {

                var testNodeCost = testNode.getF();
                if (testNodeCost < cost) {
                    currentNode = testNode;
                    cost = testNodeCost;
                }
            }

            arrivalTime = arrivalTime.plusMinutes(currentNode.getCurrentDuration().longValue());
            if (currentNode.getStopName().equals(endNode.getStopName())) {

                var endTime = System.currentTimeMillis();
                log.info("A* took: " + (endTime - startTime) / 1000.0 + " s");
                log.info("Path from {} to {} at {}", startNode.getStopName(), endNode.getStopName(),
                        time.toLocalTime());
                PathUtils.listPathFromNode(startNode, endNode, time);
                log.info("Checked nodes {}", count);
                openNodes.remove(startNode);
            }

            openNodes.remove(currentNode);
            closedNodes.add(currentNode);

            for (var edge : currentNode.getOutgoingConnections()) {
                var nextNode = edge.getEndNode();
                count++;
                if (!(openNodes.contains(nextNode) || closedNodes.contains(nextNode))) {
                    openNodes.add(nextNode);
                    nextNode.setCurrentDuration(calculateTime(edge.getArrivalTime(), time));
                    nextNode.setH(distance.compute(
                            new double[]{currentNode.getLatitude(), currentNode.getLongitude()},
                            new double[]{endNode.getLatitude(), endNode.getLongitude()}) * 100);
                    nextNode.setG(currentNode.getG() + g(edge, arrivalTime));
                    nextNode.setF(nextNode.getG() + nextNode.getH());
                } else {
                    if (nextNode.getG() > currentNode.getG() + g(edge, arrivalTime)) {
                        nextNode.setCameFrom(currentNode);
                        nextNode.setCameBy(edge);
                        nextNode.setG(currentNode.getG() + g(edge, arrivalTime));
                        nextNode.setF(nextNode.getG() + nextNode.getH());
                        nextNode.setCurrentDuration(calculateTime(edge.getArrivalTime(), time));

                        if (closedNodes.contains(nextNode)) {
                            openNodes.add(nextNode);
                            closedNodes.remove(nextNode);
                        }
                    }
                }
            }
            arrivalTime = arrivalTime.minusMinutes(currentNode.getCurrentDuration().longValue());
        }
    }


    public static void runTransferBased(GraphNode startNode, GraphNode endNode, LocalDateTime time,
                                        DistanceType distanceType) {
        int count = 0;
        log.info("Running A* algorithm...");
        var startTime = System.currentTimeMillis();
        DistanceMeasure distance = distanceType == DistanceType.MANHATTAN ? new ManhattanDistance() :
                new EuclideanDistance();
        List<GraphNode> openNodes = new ArrayList<>();
        openNodes.add(startNode);
        List<GraphNode> closedNodes = new ArrayList<>();
        var arrivalTime = time;
        while (!openNodes.isEmpty()) {

            GraphNode currentNode = null;
            var cost = Double.POSITIVE_INFINITY;
            for (var testNode : openNodes) {

                var testNodeCost = testNode.getF();
                if (testNodeCost < cost) {
                    currentNode = testNode;
                    cost = testNodeCost;
                }
            }

            arrivalTime = arrivalTime.plusMinutes(currentNode.getCurrentDuration().longValue());

            if (currentNode.getStopName().equals(endNode.getStopName())) {

                var endTime = System.currentTimeMillis();
                log.info("A* took: " + (endTime - startTime) / 1000.0 + " s");
                log.info("Path from {} to {} at {}", startNode.getStopName(), endNode.getStopName(),
                        time.toLocalTime());
                PathUtils.listPathFromNode(startNode, endNode, time);
                log.info("Checked nodes {}", count);
                openNodes.remove(startNode);
            }

            openNodes.remove(currentNode);
            closedNodes.add(currentNode);

            for (var edge : currentNode.getOutgoingConnections()) {
                var nextNode = edge.getEndNode();
                count++;
                if (!(openNodes.contains(nextNode) || closedNodes.contains(nextNode))) {
                    openNodes.add(nextNode);
                    nextNode.setCurrentDuration(calculateTime(edge.getArrivalTime(), time));
                    nextNode.setH(distance.compute(
                            new double[]{currentNode.getLatitude(), currentNode.getLongitude()},
                            new double[]{endNode.getLatitude(), endNode.getLongitude()}) * 100);
                    nextNode.setG(currentNode.getG() + g(edge, arrivalTime) + gTransfer(currentNode.getCameBy(), edge));
                    nextNode.setF(nextNode.getG() + nextNode.getH());
                } else {
                    if (nextNode.getG() > currentNode.getG() + g(edge, arrivalTime) + gTransfer(currentNode.getCameBy(), edge)) {
                        nextNode.setCameFrom(currentNode);
                        nextNode.setCameBy(edge);
                        nextNode.setG(currentNode.getG() + g(edge, arrivalTime) + gTransfer(currentNode.getCameBy(),
                                edge));
                        nextNode.setF(nextNode.getG() + nextNode.getH());
                        nextNode.setCurrentDuration(calculateTime(edge.getArrivalTime(), time));

                        if (closedNodes.contains(nextNode)) {
                            openNodes.add(nextNode);
                            closedNodes.remove(nextNode);
                        }
                    }
                }
            }
            arrivalTime = arrivalTime.minusMinutes(currentNode.getCurrentDuration().longValue());
        }
    }

    private Double calculateTime(LocalDateTime time1, LocalDateTime time2) {
        return (double) Math.abs(Duration.between(time1, time2).toMinutes());
    }

    private Double g(GraphEdge edge, LocalDateTime time) {
        return calculateTime(edge.getDepartureTime(), time) + calculateTime(edge.getDepartureTime(),
                edge.getArrivalTime());
    }

    private Double gTransfer(GraphEdge prevEdge, GraphEdge edge) {
        if (prevEdge != null && !prevEdge.getLineName().equals(edge.getLineName())) {
            return 100.;
        }
        return 0.;
    }
}