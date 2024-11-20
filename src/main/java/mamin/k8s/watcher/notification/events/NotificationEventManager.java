package mamin.k8s.watcher.notification.events;

import io.kubernetes.client.openapi.models.CoreV1Event;
import lombok.extern.slf4j.Slf4j;
import mamin.k8s.watcher.notification.providers.NotificationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class NotificationEventManager {
    private final List<NotificationProvider> providers;

    @Autowired
    public NotificationEventManager(List<NotificationProvider> providers) {
        this.providers = providers;
    }

    public void notify(CoreV1Event event) throws Exception {
        for (NotificationProvider provider : providers) {
            if (provider.enabled()) {
                provider.sendNotification(event);
            }
        }
    }
}
