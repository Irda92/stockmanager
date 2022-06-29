package com.bench.stockmanagement;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.bench.stockmanagement.domain.UsdRate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class RateReader {
    private static final DateTimeFormatter FORMATTER =DateTimeFormatter.ofPattern("yyyy.MM.dd.");

    public List<UsdRate> readRate() {
        Path path = Paths.get("src/main/resources/usd.csv");
        List<UsdRate> usdRates = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(path);
            for (String line : lines)
            {
                String[] lineParts = line.split(",");
                LocalDate localDate = LocalDate.parse(lineParts[0], FORMATTER);
                double rate = Double.parseDouble(lineParts[1]);
                usdRates.add(new UsdRate(localDate, rate));
            }
        } catch (IOException e) {
            //TODO add logger
            System.out.println("Something happened: " + e);
        }
        return usdRates;
    }
}
