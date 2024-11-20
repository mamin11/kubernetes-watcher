package mamin.k8s.watcher.notification.providers;

import io.kubernetes.client.openapi.models.CoreV1Event;

public interface NotificationProvider {
    void init();
    boolean enabled();
    void sendNotification(CoreV1Event message) throws Exception;
}
