package com.vburmus.ai.algorithms;

import com.vburmus.ai.algorithms.models.Distance;
import com.vburmus.ai.dto.Path;
import com.vburmus.ai.graph.Graph;
import com.vburmus.ai.graph.GraphEdge;
import com.vburmus.ai.graph.GraphNode;
import com.vburmus.ai.utils.PathUtils;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@UtilityClass
@Slf4j
public class Dijkstra {
    public static void run(Graph graph, GraphNode startNode, GraphNode endNode, LocalDateTime time) {
        var startTime = System.currentTimeMillis();
        log.info("Running Dijkstra algorithm...");
        // Inicjalizuj d(s) = 0 oraz d(v) = ∞
        Map<String, Distance> distances = new HashMap<>();
        Map<String, Path> pathMap = new HashMap<>();
        Map<String, GraphNode> nodes = graph.getNodesMap();
        nodes.keySet()
             .forEach(stopName -> distances.put(stopName, new Distance(nodes.get(stopName), Double.POSITIVE_INFINITY,
                     Integer.MAX_VALUE)));

        distances.put(startNode.getStopName(), Distance.builder().node(startNode).build()); // d(s) = 0
        var count = goThrough(endNode, graph, distances, pathMap, time);
        var endTime = System.currentTimeMillis();
        log.info("Dijkstra took: " + (endTime - startTime) / 1000.0 + " s");
        log.info("Path from {} to {} at {}", startNode.getStopName(), endNode.getStopName(), time.toLocalTime());
        PathUtils.listPathFromMap(graph, startNode, endNode, pathMap, time);
        log.info("Counted nodes {}", count);
        log.info("Time {}", distances.get(endNode.getStopName()).getTime());
    }

    private static int goThrough(GraphNode endNode, Graph graph, Map<String, Distance> distances,
                                 Map<String, Path> pathMap, LocalDateTime time) {
        int count = 0;
        LocalDateTime currentTime = time;
        // Utwórz zbiór Q zawierający wszystkie wierzchołki grafu G.
        Set<String> nodesToVisit = new HashSet<>(graph.getNodesMap().keySet());
        //3. Dopóki Q nie jest pusty, wykonuj:
        while (!nodesToVisit.isEmpty()) {
            //(a) Wybierz wierzchołek u ∈ Q o najmniejszej wartości d(u) i usuń go ze zbioru Q
            String nearestNodeName = getNearestNodeNameByTime(distances, nodesToVisit);
            if (nearestNodeName != null) {
                var nearestNode = distances.get(nearestNodeName);
                currentTime = currentTime.plusMinutes(nearestNode.getTime());
                //(b) Dla każdego v takiego, że ∃(u, v)∈E d(v) > d(u) + w(u, v) zaktualizuj d(v) = d(u) + w(u, v)
                // oraz p(v) = u
                for (GraphEdge connection : nearestNode.getNode().getOutgoingConnections()) {
                    var destinationNode = distances.get(connection.getEndNode().getStopName());
                    count++;
                    var departureTime = connection.getDepartureTime();
                    var arrivalTime = connection.getArrivalTime();
                    Integer throughTime = getTimeThroughNode(currentTime, nearestNode, departureTime, arrivalTime);
                    if (throughTime < destinationNode.getTime()) {
                        destinationNode.setTime(throughTime);
                        pathMap.put(destinationNode.getNode().getStopName(),
                                new Path(nearestNode.getNode()
                                                    .getStopName(),
                                        connection));
                    }
                }
                currentTime = currentTime.minusMinutes(nearestNode.getTime());
                nodesToVisit.remove(nearestNodeName);
                if (nearestNodeName.equals(endNode.getStopName())) {
                    return count;
                }
            }
        }
        return 0;
    }

    private static int getTimeThroughNode(LocalDateTime currentTime, Distance nearestNode,
                                          LocalDateTime departureTime, LocalDateTime arrivalTime) {
        return Math.abs((int) Duration.between(departureTime, currentTime)
                                      .toMinutes()) + nearestNode.getTime()
                + Math.abs((int) Duration.between(departureTime, arrivalTime)
                                         .toMinutes());
    }

    private static String getNearestNodeNameByTime(Map<String, Distance> distances, Set<String> unvisitedNodes) {
        String nearestNode = null;
        var minTime = Integer.MAX_VALUE;

        for (String stopName : unvisitedNodes) {
            var distance = distances.get(stopName);
            if (distance.getTime() < minTime) {
                minTime = distance.getTime();
                nearestNode = stopName;
            }
        }
        return nearestNode;
    }
}