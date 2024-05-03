package com.konfyrm.webgraphapi.service.impl;

import com.konfyrm.webgraphapi.service.WebGraphService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class WebGraphServiceImpl implements WebGraphService {

    private Map<String, Map<String, Set<String>>> webGraphs = new HashMap<>();

    @Override
    public Map<String, Set<String>> getUrlMap(String executionUuid) {
        return webGraphs.get(executionUuid);
    }

    @Override
    public void updateUrlMap(String executionUuid, Map<String, Set<String>> urlMap) {
        if (!webGraphs.containsKey(executionUuid)) {
            webGraphs.put(executionUuid, urlMap);
        }
        Map<String, Set<String>> oldUrlMap = webGraphs.get(executionUuid);
        urlMap.keySet().stream()
                .filter(url -> !oldUrlMap.containsKey(url))
                .forEach(url -> oldUrlMap.put(url, urlMap.get(url)));
    }

}
