package com.konfyrm.webgraphapi.service.impl;

import com.konfyrm.webgraphapi.algorithm.FloydWarshallAlgorithm;
import com.konfyrm.webgraphapi.domain.model.UrlGraph;
import com.konfyrm.webgraphapi.domain.response.GraphDistancesResponse;
import com.konfyrm.webgraphapi.service.GraphAnalysisService;
import org.springframework.stereotype.Service;

@Service
public class GraphAnalysisServiceImpl implements GraphAnalysisService {

    @Override
    public GraphDistancesResponse calculateDistances(UrlGraph urlGraph) {
        int[][] distances = FloydWarshallAlgorithm.execute(urlGraph);
        int[] radius = new int[urlGraph.getN()];
        for (int i = 0; i < urlGraph.getN(); i++) {
            int maxDistance = distances[i][0];
            for (int j = 1; j < urlGraph.getN(); j++) {
                if (maxDistance < distances[i][j]) {
                    maxDistance = distances[i][j];
                }
            }
            radius[i] = maxDistance;
        }

        int diameter = radius[0];
        for (int i = 0; i < urlGraph.getN(); i++) {
            if(diameter < radius[i]) {
                diameter = radius[i];
            }
        }
        double avgDistance = 0.0;
        for (int u = 0; u < urlGraph.getN(); u++) {
            for (int v = 0; v <urlGraph.getN(); v++) {
                if (distances[u][v] != Integer.MAX_VALUE) {
                    avgDistance += distances[u][v];
                }
            }
        }
        return GraphDistancesResponse.builder()
                .diameter(diameter)
                .radius(radius)
                .distances(distances)
                .avgDistance(avgDistance)
                .build();
    }

}

