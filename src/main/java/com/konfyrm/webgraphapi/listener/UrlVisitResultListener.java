package com.konfyrm.webgraphapi.listener;

import com.konfyrm.webgraphapi.domain.message.UrlVisitResult;
import com.konfyrm.webgraphapi.service.ExecutionService;
import com.konfyrm.webgraphapi.service.UrlVisitService;
import com.konfyrm.webgraphapi.service.WebGraphService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.konfyrm.webgraphapi.domain.KafkaTopicConstants.*;

@Component
public class UrlVisitResultListener {

    @Value("${application.api.max-visited-nodes-per-request}")
    private int maxVisitedNodesPerRequest;

    private final UrlVisitService urlVisitService;
    private final ExecutionService executionService;
    private final WebGraphService webGraphService;

    public UrlVisitResultListener(
            @Qualifier("urlVisitServiceImpl") UrlVisitService urlVisitService,
            @Qualifier("executionServiceImpl") ExecutionService executionService,
            @Qualifier("webGraphServiceImpl") WebGraphService webGraphService
    ) {
        this.urlVisitService = urlVisitService;
        this.executionService = executionService;
        this.webGraphService = webGraphService;
    }

    @KafkaListener(topics = RESULT_TOPIC, groupId = DEFAULT_GROUP, containerFactory = "urlVisitResultListenerContainerFactory")
    public synchronized void listener(UrlVisitResult result) {
        System.out.println(result);
        Map<String, Set<String>> urlMap = webGraphService.getUrlMap(result.getExecutionUuid());

        Set<String> doneTasks = result.getNeighbours().keySet().stream()
                .filter(url -> !urlMap.containsKey(url))
                .collect(Collectors.toSet());

        Set<String> todoTasks = result.getNeighbours().values().stream()
                .flatMap(Collection::stream)
                .filter(url -> !urlMap.containsKey(url))
                .collect(Collectors.toSet());

        Map<String, Set<String>> newUrls = new HashMap<>();
        doneTasks.forEach(url -> newUrls.put(url, result.getNeighbours().get(url)));

        webGraphService.updateUrlMap(result.getExecutionUuid(), newUrls);
        executionService.update(result.getExecutionUuid(), doneTasks.size(), todoTasks.size());

        todoTasks.forEach(url -> urlVisitService.sendUrlVisitRequest(result.getExecutionUuid(), url, maxVisitedNodesPerRequest));
    }

}
