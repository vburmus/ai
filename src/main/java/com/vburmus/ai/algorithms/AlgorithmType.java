package com.vburmus.ai.algorithms;

import java.util.NoSuchElementException;

public enum AlgorithmType {
    DIJKSTRA,
    A_STAR_TIME,
    A_STAR_DISTANCE;

    public static AlgorithmType getByOrder(int order) {
        return switch (order) {
            case 1 -> DIJKSTRA;
            case 2 -> A_STAR_TIME;
            case 3 -> A_STAR_DISTANCE;
            default -> throw new NoSuchElementException("No such AlgorithmType for order: " + order);
        };
    }
}