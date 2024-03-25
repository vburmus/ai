package com.vburmus.ai.algorithms.models;

import java.util.NoSuchElementException;

public enum DistanceType {
    EUCLIDEAN,
    MANHATTAN;

    public static DistanceType getByOrder(int order) {
        return switch (order) {
            case 1 -> EUCLIDEAN;
            case 2 -> MANHATTAN;
            default -> throw new NoSuchElementException("No such DistanceType for order: " + order);
        };
    }
}
