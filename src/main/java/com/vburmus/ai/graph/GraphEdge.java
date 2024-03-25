package com.vburmus.ai.graph;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GraphEdge {
    private GraphNode startNode;
    private GraphNode endNode;
    private String lineName;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private Integer durationMinutes;
    private Double distance;
}