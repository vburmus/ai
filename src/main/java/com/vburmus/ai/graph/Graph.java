package com.vburmus.ai.graph;

import com.vburmus.ai.dto.Connection;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Slf4j
public class Graph {
    private Map<String, GraphNode> nodesMap;
    private List<GraphEdge> edges;

    public Graph(List<Connection> connections) {
        log.info("Creating graph...");
        nodesMap = new HashMap<>();
        edges = new ArrayList<>();
        for (Connection connection : connections) {
            GraphNode startNode = getOrCreateNode(connection.getStartStop(), connection.getStartStopLatitude(),
                    connection.getStartStopLongitude());
            GraphNode endNode = getOrCreateNode(connection.getEndStop(), connection.getEndStopLatitude(),
                    connection.getEndStopLongitude());

            GraphEdge edge = createEdge(connection, startNode, endNode);

            startNode.addOutgoingConnection(edge);
        }
        log.info("Created graph...");
    }

    public GraphNode getNode(String stopName) {
        return nodesMap.get(stopName);
    }

    private GraphEdge createEdge(Connection connection, GraphNode startNode, GraphNode endNode) {
        GraphEdge edge = GraphEdge.builder()
                                  .lineName(connection.getLine())
                                  .departureTime(connection.getDepartureTime())
                                  .arrivalTime(connection.getArrivalTime())
                                  .startNode(startNode)
                                  .endNode(endNode)
                                  .durationMinutes(
                                          (int) Duration.between(connection.getDepartureTime(),
                                                                connection.getArrivalTime())
                                                        .toMinutes())
                                  .build();
        edges.add(edge);
        return edge;
    }

    private GraphNode getOrCreateNode(String stopName, double latitude, double longitude) {
        return nodesMap.computeIfAbsent(stopName, key -> GraphNode.builder()
                                                                  .stopName(stopName)
                                                                  .latitude(latitude)
                                                                  .longitude(longitude)
                                                                  .build());
    }
}
