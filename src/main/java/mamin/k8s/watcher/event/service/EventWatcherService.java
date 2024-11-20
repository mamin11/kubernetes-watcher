package mamin.k8s.watcher.event.service;

import com.google.gson.reflect.TypeToken;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.CoreV1Event;
import io.kubernetes.client.util.Watch;
import lombok.extern.slf4j.Slf4j;
import mamin.k8s.watcher.event.filter.EventFilter;
import mamin.k8s.watcher.notification.events.NotificationEventManager;
import okhttp3.Call;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EventWatcherService {
    private final CoreV1Api coreV1Api;
    private final EventFilter eventFilter;
    private final NotificationEventManager notificationEventManager;

    public EventWatcherService(CoreV1Api coreV1Api, EventFilter eventFilter, NotificationEventManager notificationEventManager) {
        this.coreV1Api = coreV1Api;
        this.eventFilter = eventFilter;
        this.notificationEventManager = notificationEventManager;
    }

    public void watchEvents() {
            try {
                Call eventForAllNamespacesCall = coreV1Api.listEventForAllNamespacesCall(
                        null, null, null, null,
                        null, null, null, null,
                        null, Boolean.TRUE, null);

                Watch<CoreV1Event> watch = Watch.createWatch(coreV1Api.getApiClient(),
                        eventForAllNamespacesCall,
                        new TypeToken<Watch.Response<CoreV1Event>>(){}.getType());

                for (Watch.Response<CoreV1Event> event : watch) {
                    CoreV1Event coreV1Event = event.object;
                    String reason = coreV1Event.getReason();
                    String message = coreV1Event.getMessage();
                    String kind = coreV1Event.getInvolvedObject().getKind();
                    String pod = coreV1Event.getInvolvedObject().getName();

                    if (!eventFilter.shouldWatchEvent(reason, message, kind, pod)) {
                        continue;
                    }

                    log.info("Event - Reason: {}, pod: {}, message: {}", reason, pod, message);
                    notificationEventManager.notify(coreV1Event);
                }

            } catch (ApiException e) {
                log.error("Error while watching events", e);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

    }
}

