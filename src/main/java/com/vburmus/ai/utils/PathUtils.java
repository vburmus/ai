package com.vburmus.ai.utils;

import com.vburmus.ai.dto.Path;
import com.vburmus.ai.graph.Graph;
import com.vburmus.ai.graph.GraphEdge;
import com.vburmus.ai.graph.GraphNode;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@UtilityClass
@Slf4j
public class PathUtils {
    public static void listPathFromMap(Graph graph, GraphNode startNode, GraphNode endNode, Map<String, Path> pathMap
            , LocalDateTime time) {
        List<Path> pathList = new ArrayList<>();
        Set<String> visitedNodes = new HashSet<>();
        var currentNode = endNode;
        while (!currentNode.getStopName().equals(startNode.getStopName())) {
            pathList.add(pathMap.get(currentNode.getStopName()));
            visitedNodes.add(pathMap.get(currentNode.getStopName()).getEdge().getLineName());
            currentNode = graph.getNode(pathMap.get(currentNode.getStopName()).getNode());

        }
        printPath(pathList, visitedNodes.size(), time);
    }

    public static void listPathFromNode(GraphNode startNode, GraphNode currentNode, LocalDateTime time) {
        List<Path> pathList = new ArrayList<>();
        Set<String> visitedNodes = new HashSet<>();
        GraphNode pathNode = currentNode;
        GraphEdge pathEdge = pathNode.getCameBy();
        while (!pathNode.getStopName().equals(startNode.getStopName())) {
            pathList.add(new Path(pathNode.getStopName(), pathEdge));
            visitedNodes.add(pathEdge.getLineName());
            pathNode = pathNode.getCameFrom();
            pathEdge = pathNode.getCameBy();
        }
        printPath(pathList, visitedNodes.size(), time);
    }

    private static void printPath(List<Path> pathList, int transfers, LocalDateTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        for (int j = pathList.size() - 1; j >= 0; j--) {
            var edge = pathList.get(j).getEdge();
            log.info("[{} - {}] -> [{} - {}] LINE: {}",
                    edge.getStartNode().getStopName(),
                    edge.getDepartureTime()
                        .toLocalTime()
                        .format(formatter),
                    edge.getEndNode().getStopName(),
                    edge.getArrivalTime()
                        .toLocalTime()
                        .format(formatter), edge.getLineName());
        }
        log.info("Transfers: {}", transfers);
        log.info("Travel time: {}", Duration.between(time, pathList.get(0).getEdge().getArrivalTime()).toMinutes());
    }
}