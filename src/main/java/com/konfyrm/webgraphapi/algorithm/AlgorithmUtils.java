package com.konfyrm.webgraphapi.algorithm;

import java.util.HashMap;
import java.util.Map;

public class AlgorithmUtils {

    private AlgorithmUtils() {
        throw new IllegalStateException("Utils class should not be instantiated.");
    }

    public static <T> Map<T, Integer> valuesToDistribution(T[] values, int n) {
        Map<T, Integer> distributions = new HashMap<>();
        for (int v = 0; v < n; v++) {
            T val = values[v];
            int distibution = distributions.getOrDefault(val, 0);
            distibution++;
            distributions.put(val, distibution);
        }
        return distributions;
    }

}
