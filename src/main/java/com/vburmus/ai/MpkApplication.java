package com.vburmus.ai;

import com.vburmus.ai.algorithms.AlgorithmType;
import com.vburmus.ai.algorithms.models.DistanceType;
import com.vburmus.ai.dto.Connection;
import com.vburmus.ai.utils.CsvUtils;
import com.vburmus.ai.utils.MpkUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

@Slf4j
public class MpkApplication {
    public static void main(String[] args) throws IOException {
        log.info("MpkApplication start...");
        List<Connection> connections = CsvUtils.readConnectionsFromCsv("src/main/resources/connection_graph.csv");
        while (true) {
            Scanner scanner = new Scanner(System.in);
            String startStop = getStopInput(scanner, connections, "Enter start stop: "); /*"Tramwajowa"*/
            String endStop = getStopInput(scanner, connections,"Enter start stop: "); /*"KROMERA""PL. GRUNWALDZKI""Tarczyński Arena (Lotnicza)"*/
            LocalDateTime time = getTimeInput(scanner);
            AlgorithmType algorithmType = getAlgorithmType(scanner);
            DistanceType distanceType = null;
            if (!AlgorithmType.DIJKSTRA.equals(algorithmType)) {
                distanceType = getDistanceType(scanner);
            }
            MpkUtils.findRoute(startStop, endStop, time, algorithmType, connections, distanceType);
            log.info("MpkApplication finished...");
        }
    }

    private static AlgorithmType getAlgorithmType(Scanner scanner) {
        System.out.println("1. DIJKSTRA");
        System.out.println("2. A_STAR_TIME");
        System.out.println("3. A_STAR_DISTANCE");
        System.out.print("Select algorithm type: ");

        int choice = scanner.nextInt();
        return AlgorithmType.getByOrder(choice);
    }

    private static DistanceType getDistanceType(Scanner scanner) {
        System.out.println("1. EUCLIDEAN");
        System.out.println("2. MANHATTAN");
        System.out.print("Select heuristic function type: ");

        int choice = scanner.nextInt();
        return DistanceType.getByOrder(choice);
    }

    private static LocalDateTime getTimeInput(Scanner scanner) {
        System.out.print("Enter time (HH:MM format): ");
        while (true) {
            try {
                String timeInput = scanner.nextLine(); // example "15:55"
                return LocalDateTime.of(LocalDate.now(), LocalTime.parse(timeInput));
            } catch (Exception e) {
                System.out.println("Invalid time format. Please try again.");
            }
        }
    }

    private static String getStopInput(Scanner scanner, List<Connection> connections, String message) {
        System.out.print(message);
        while (true) {
            String stop = scanner.nextLine();
            if (isStopValid(connections, stop)) {
                return stop;
            } else {
                System.out.println("Stop not found. Please try again.");
            }
        }
    }

    private static boolean isStopValid(List<Connection> connections, String stop) {
        return connections.stream().anyMatch(c -> c.getStartStop().equals(stop) || c.getEndStop().equals(stop));
    }
}