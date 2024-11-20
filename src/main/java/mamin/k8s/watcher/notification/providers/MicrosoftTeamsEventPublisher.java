package mamin.k8s.watcher.notification.providers;

import io.kubernetes.client.openapi.models.CoreV1Event;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@ConfigurationProperties(prefix = "notification.microsoft")
@ConditionalOnProperty(name = "notification.microsoft.enabled", havingValue = "true")
public class MicrosoftTeamsEventPublisher implements NotificationProvider {

    @Override
    public void init() {
        log.info("Initializing Microsoft Teams notification provider");
    }

    @Override
    public boolean enabled() {
        return false;
    }

    @Override
    public void sendNotification(CoreV1Event event) {
        if (!enabled()) {
            return;
        }

        log.info("Sending Microsoft Teams notification: {}", event);
    }

}
