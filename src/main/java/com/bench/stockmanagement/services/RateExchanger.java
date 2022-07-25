package com.bench.stockmanagement.services;

import com.bench.stockmanagement.domain.UsdRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Component
public class RateExchanger {
    private final Reader rateReader;

    @Autowired
    public RateExchanger(Reader rateReader) {
        this.rateReader = rateReader;
    }

    public Double getRateFor(LocalDate date) {
        List<UsdRate> usdRates = rateReader.readRate();

        usdRates.sort(Comparator.comparing(UsdRate::getDate));
        if (date.isBefore(usdRates.get(0).getDate())) {
            return 0.0;
        }
        for (UsdRate rate : usdRates) {
            if (rate.getDate().equals(date)) {
                return rate.getRate();
            }
            if (rate.getDate().isAfter(date)) {
                return getRateFor(date.minusDays(1));
            }
        }
        return 0.0;
    }

}
