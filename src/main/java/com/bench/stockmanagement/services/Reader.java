package com.bench.stockmanagement.services;

import com.bench.stockmanagement.domain.UsdRate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.bench.stockmanagement.Constants.FORMATTER_WITH_DOTS;

@Component
public class Reader {

    public static final String RATE_PATH = "src/main/resources/usd.csv";

    public List<UsdRate> readRate() {
        Path path = Paths.get(RATE_PATH);
        List<UsdRate> usdRates = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
                String[] lineParts = line.split(",");
                LocalDate localDate = LocalDate.parse(lineParts[0], FORMATTER_WITH_DOTS);
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
