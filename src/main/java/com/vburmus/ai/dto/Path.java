package com.vburmus.ai.dto;

import com.vburmus.ai.graph.GraphEdge;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Path {
    private String node;
    private GraphEdge edge;
}