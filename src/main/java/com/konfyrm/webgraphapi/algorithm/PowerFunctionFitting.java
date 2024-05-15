package com.konfyrm.webgraphapi.algorithm;

import org.springframework.data.util.Pair;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

public class PowerFunctionFitting {

    private PowerFunctionFitting() {
        throw new IllegalStateException("Utils class should not be instantiated.");
    }

    public static Pair<Double, Double> fitPowerFunction(double[] xValues, double[] yValues, Map<Integer, Integer> samples) {
        // Transform data to logarithmic space
        List<Double> logXValues = DoubleStream.of(xValues).map(Math::log).boxed().collect(Collectors.toList());
        List<Double> logYValues = DoubleStream.of(yValues).map(Math::log).boxed().collect(Collectors.toList());

        // Calculate means of logarithmic values
        double meanLogX = logXValues.stream().mapToDouble(Double::doubleValue).average().orElse(0);
        double meanLogY = logYValues.stream().mapToDouble(Double::doubleValue).average().orElse(0);

        // Calculate coefficients
        double b = calculateB(logXValues, logYValues, meanLogX, meanLogY);
        double a = calculateA(meanLogX, meanLogY, b);

        return Pair.of(a, b);
    }

    private static double calculateB(List<Double> logXValues, List<Double> logYValues, double meanLogX, double meanLogY) {
        double numerator = 0;
        double denominator = 0;
        for (int i = 0; i < logXValues.size(); i++) {
            numerator += (logXValues.get(i) - meanLogX) * (logYValues.get(i) - meanLogY);
            denominator += Math.pow(logXValues.get(i) - meanLogX, 2);
        }
        return numerator / denominator;
    }

    private static double calculateA(double meanLogX, double meanLogY, double b) {
        return Math.exp(meanLogY - b * meanLogX);
    }

}
