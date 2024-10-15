package mamin.k8s.watcher.controller;

import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.CoreV1Event;
import io.kubernetes.client.openapi.models.CoreV1EventList;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CoreController {
    private final CoreV1Api api;

    @GetMapping("/pods")
    public List<V1Pod> getPods() {
        //todo: ability to pass namespace and additional query params
        V1PodList list = null;
        try {
            list = api.listPodForAllNamespaces(null, null,
                    null, null, null, null,
                    null, null, null, null);
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
        return list.getItems();
    }

    @GetMapping("/events")
    public List<CoreV1Event> getEvents() {
        CoreV1EventList coreV1EventList = null;
        try {
            coreV1EventList = api.listEventForAllNamespaces(null, null, null,
                    null, null, null, null,
                    null, null, null);
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }

        return coreV1EventList.getItems();
    }

}
