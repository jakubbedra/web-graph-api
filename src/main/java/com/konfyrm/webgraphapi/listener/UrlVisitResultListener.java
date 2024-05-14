package com.konfyrm.webgraphapi.listener;

import com.konfyrm.webgraphapi.domain.message.UrlNode;
import com.konfyrm.webgraphapi.domain.message.UrlVisitResult;
import com.konfyrm.webgraphapi.service.ExecutionService;
import com.konfyrm.webgraphapi.service.UrlVisitService;
import com.konfyrm.webgraphapi.service.WebGraphService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;

import static com.konfyrm.webgraphapi.domain.KafkaTopicConstants.*;

@Component
public class UrlVisitResultListener {

    private Set<String> urlsInQueue;

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
        this.urlsInQueue = new ConcurrentSkipListSet<>();
    }

    // TODO: UNIT TESTS!!!!!!!!!!!!!!!!!!!

    // TODO: extract todoTasks filter logic? 

    @KafkaListener(topics = RESULT_TOPIC, groupId = DEFAULT_GROUP, containerFactory = "urlVisitResultListenerContainerFactory")
    public synchronized void listener(UrlVisitResult result) {
//        System.out.println(result);
        List<UrlNode> urlMap = webGraphService.getUrlMap(result.getExecutionUuid());

        Set<String> doneTasks = result.getNodes().stream()
                .map(UrlNode::getUrl)
                .collect(Collectors.toSet());

        doneTasks.stream()
                .filter(urlsInQueue::contains)
                .forEach(urlsInQueue::remove);

        Set<String> todoTasks = result.getNodes().stream()
                .flatMap(node -> node.getNeighbours().stream())
                .filter(url -> !doneTasks.contains(url))
                .filter(url -> !urlsInQueue.contains(url))
                .filter(url -> urlMap.stream().map(UrlNode::getUrl).noneMatch(url::equals))
                .collect(Collectors.toSet());

        webGraphService.updateUrlMap(result.getExecutionUuid(), result.getNodes());
        executionService.update(result.getExecutionUuid(), doneTasks.size(), todoTasks.size());
// todo: update tasksInProgress and lastUpdateTimestamp?
        urlsInQueue.addAll(todoTasks);
        todoTasks.forEach(url -> urlVisitService.sendUrlVisitRequest(result.getExecutionUuid(), url));
    }

}
