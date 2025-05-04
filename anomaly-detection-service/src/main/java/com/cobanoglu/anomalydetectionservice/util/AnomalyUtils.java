package com.cobanoglu.anomalydetectionservice.util;

import com.cobanoglu.anomalydetectionservice.model.Anomaly;
import com.cobanoglu.anomalydetectionservice.model.AirQualityResponse;

import java.util.List;

public class AnomalyUtils {

    public static final double PM25_THRESHOLD = 35.0;
    public static final double PM10_THRESHOLD = 50.0;
    public static final double O3_THRESHOLD = 100.0;
    public static final double SO2_THRESHOLD = 75.0;
    public static final double NO2_THRESHOLD = 100.0;
    public static final double CO_THRESHOLD = 9.0;

    private AnomalyUtils() {}

    public static boolean isThresholdExceeded(AirQualityResponse.AirData airData) {
        if (airData.getComponents() == null) return false;

        var comp = airData.getComponents();

        return  (comp.getPm2_5() != null && comp.getPm2_5() > PM25_THRESHOLD) ||
                (comp.getPm10()  != null && comp.getPm10()  > PM10_THRESHOLD)  ||
                (comp.getO3()    != null && comp.getO3()    > O3_THRESHOLD)    ||
                (comp.getSo2()   != null && comp.getSo2()   > SO2_THRESHOLD)   ||
                (comp.getNo2()   != null && comp.getNo2()   > NO2_THRESHOLD)   ||
                (comp.getCo()    != null && comp.getCo()    > CO_THRESHOLD);
    }

    public static double calculateZScore(double currentValue, double mean, double stdDev) {
        if (stdDev == 0) return 0;
        return (currentValue - mean) / stdDev;
    }

    public static boolean isSignificantIncrease(double currentValue, double averageValue) {
        if (averageValue == 0) return false;
        return currentValue > averageValue * 1.5;
    }

    public static double calculateDistanceKm(double lat1, double lon1, double lat2, double lon2) {
        final int EARTH_RADIUS_KM = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS_KM * c;
    }

    public static boolean hasRegionalAnomaly(List<Anomaly> nearbyAnomalies, double currentPm25) {
        for (Anomaly anomaly : nearbyAnomalies) {
            double difference = Math.abs(anomaly.getAqi() - currentPm25);
            if (difference > 10) return true;
        }
        return false;
    }
}
