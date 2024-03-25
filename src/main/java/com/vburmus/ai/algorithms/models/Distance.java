package com.vburmus.ai.algorithms.models;

import com.vburmus.ai.graph.GraphNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Distance {
    private GraphNode node;
    @Builder.Default
    private Double length = 0.;
    @Builder.Default
    private Integer time = 0;
}
