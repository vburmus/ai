package com.vburmus.ai.graph;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GraphNode {
    private String stopName;
    private Double latitude;
    private Double longitude;
    @Builder.Default
    private List<GraphEdge> outgoingConnections = new ArrayList<>();
    private GraphNode cameFrom;
    private GraphEdge cameBy;
    @Builder.Default
    private Double g = 0.;
    @Builder.Default
    private Double h = 0.;
    @Builder.Default
    private Double f = 0.;
    @Builder.Default
    private Double currentDuration = 0.;

    public void addOutgoingConnection(GraphEdge edge) {
        outgoingConnections.add(edge);
    }
}