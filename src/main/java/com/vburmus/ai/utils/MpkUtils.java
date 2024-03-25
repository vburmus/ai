package com.vburmus.ai.utils;

import com.vburmus.ai.algorithms.AStar;
import com.vburmus.ai.algorithms.AlgorithmType;
import com.vburmus.ai.algorithms.Dijkstra;
import com.vburmus.ai.algorithms.models.DistanceType;
import com.vburmus.ai.dto.Connection;
import com.vburmus.ai.graph.Graph;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.util.List;

@UtilityClass
public class MpkUtils {
    public static void findRoute(String startStop, String endStop, LocalDateTime time, AlgorithmType algorithmType,
                                 List<Connection> connections, DistanceType distanceType) {
        Graph graph = new Graph(connections);
        switch (algorithmType) {
            case DIJKSTRA ->
                    Dijkstra.run(graph, graph.getNode(startStop), graph.getNode(endStop), time);
            case A_STAR_TIME ->
                    AStar.runTimeBased(graph.getNode(startStop), graph.getNode(endStop), time, distanceType);
            case A_STAR_DISTANCE ->
                    AStar.runTransferBased(graph.getNode(startStop), graph.getNode(endStop), time, distanceType);
        }
    }
}
