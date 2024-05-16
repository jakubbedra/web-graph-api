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

    // ax^b
    public static Pair<Double, Double> fitPowerFunction(Map<Integer, Integer> samples) {


        // TODO: handle 0 values in x (simple filter will not help, because its corresponding y value needs to be handled aswell),
        // might consider moving the chart right

        // todo: also, I smell some bullshit in distances radius, maybe we should take an underlying graph?
        // todo: or check how to calculate it for digraphs dunno

        // todo: in and out components + page rank remain


        // TODO: CLUSTERING COEFFICIENTS!!!!!!!!!!!!!!!!!!!!!!!!!!!! (with underlying graph)


        List<Double> logXValues = samples.keySet().stream()
                .mapToDouble(x -> (double)x)
                .map(Math::log)
                .boxed()
                .collect(Collectors.toList());
        List<Double> logYValues = samples.values().stream()
                .mapToDouble(x -> (double)x)
                .map(Math::log)
                .boxed()
                .collect(Collectors.toList());

        double avgLogX = logXValues.stream().mapToDouble(Double::doubleValue).average().orElse(0);
        double avgLogY = logYValues.stream().mapToDouble(Double::doubleValue).average().orElse(0);

        // Calculate coefficients
        double b = calculateB(logXValues, logYValues, avgLogX, avgLogY);
        double a = calculateA(avgLogX, avgLogY, b);

        return Pair.of(a, b);
    }

    private static double calculateB(List<Double> logXValues, List<Double> logYValues, double meanLogX, double meanLogY) {
        double numerator = 0;
        double denominator = 0;
        int n = logXValues.size();
        for (int i = 0; i < n; i++) {
            numerator += logXValues.get(i) * logYValues.get(i);
            denominator += Math.pow(logXValues.get(i), 2);
        }
        numerator -=  n * meanLogX * meanLogY;
        denominator -= n * meanLogX * meanLogX;
        return numerator / denominator;
    }

    private static double calculateA(double meanLogX, double meanLogY, double b) {
        return Math.exp(meanLogY - b * meanLogX);
    }

}
