package com.vburmus.ai.utils;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
public class TimeConverter extends AbstractBeanField<LocalDateTime, String> {
    @Override
    protected LocalDateTime convert(String value) throws CsvDataTypeMismatchException {
        try {
            int hour = Integer.parseInt(value.substring(0, 2));
            int minutes = Integer.parseInt(value.substring(3, 5));
            LocalDateTime localDateTime = LocalDateTime.now();
            localDateTime = localDateTime.withMinute(minutes);
            localDateTime = localDateTime.withSecond(0);
            if (hour >= 24) {
                localDateTime = localDateTime.withHour(hour - 24);
                localDateTime = localDateTime.withDayOfMonth(localDateTime.getDayOfMonth() + 1);
            } else
                localDateTime = localDateTime.withHour(hour);
            return localDateTime;
        } catch (Exception e) {
            throw new CsvDataTypeMismatchException(e.getMessage());
        }
    }
}
