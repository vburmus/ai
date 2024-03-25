package com.vburmus.ai.utils;

import com.opencsv.bean.CsvToBeanBuilder;
import com.vburmus.ai.dto.Connection;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

@UtilityClass
@Slf4j
public class CsvUtils {
    public static List<Connection> readConnectionsFromCsv(String path) throws FileNotFoundException {
        try (Reader reader = new FileReader(path)) {
            log.info("Parsing connections...");
            return new CsvToBeanBuilder<Connection>(reader)
                    .withType(Connection.class)
                    .withSkipLines(1)
                    .build()
                    .parse();
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException(String.format("File not found: %s", path));
        } catch (IOException e) {
            throw new RuntimeException(String.format("File is invalid: %s", path));
        }
    }
}