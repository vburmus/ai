package com.vburmus.ai.dto;

import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvCustomBindByPosition;
import com.vburmus.ai.utils.TimeConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Connection {
    @CsvBindByPosition(position = 0)
    private Long id;
    @CsvBindByPosition(position = 1)
    private String company;
    @CsvBindByPosition(position = 2)
    private String line;

    @CsvCustomBindByPosition(position = 3, converter = TimeConverter.class)
    private LocalDateTime departureTime;
    @CsvCustomBindByPosition(position = 4, converter = TimeConverter.class)
    private LocalDateTime arrivalTime;

    @CsvBindByPosition(position = 5)
    private String startStop;
    @CsvBindByPosition(position = 6)
    private String endStop;

    @CsvBindByPosition(position = 7)
    private Double startStopLatitude;
    @CsvBindByPosition(position = 8)
    private Double startStopLongitude;
    @CsvBindByPosition(position = 9)
    private Double endStopLatitude;
    @CsvBindByPosition(position = 10)
    private Double endStopLongitude;
}