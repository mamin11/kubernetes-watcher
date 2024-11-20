package mamin.k8s.watcher.notification.providers;

import com.slack.api.Slack;
import com.slack.api.webhook.Payload;
import com.slack.api.webhook.WebhookResponse;
import io.kubernetes.client.openapi.models.CoreV1Event;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.time.Instant;

@Data
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "notification.slack")
@ConditionalOnProperty(name = "notification.slack.config.enabled", havingValue = "true")
public class SlackEventPublisher implements NotificationProvider {
    private SlackConfig config;
    private Slack slack;

    @Override
    @PostConstruct
    public void init() {
        log.info("Initializing slack notification provider");
        slack = Slack.getInstance();
    }

    @Override
    public boolean enabled() {
        return config.isEnabled();
    }

    @Override
    public void sendNotification(CoreV1Event event) throws Exception {
        log.info("Sending slack notification: {}", event.getMessage());

        String message = "New kubernetes event: "+ Instant.now() +" \n" + "```" +event + "```";
        Payload payload = Payload.builder().text(message).build();
        WebhookResponse response = slack.send(config.webhookUrl, payload);

        if (response.getCode() != 200) {
            log.error("Something went wrong while sending slack message: {}", response);
        } else {
            log.info("Slack message sent!");
        }
    }

    @Data
    public static class SlackConfig {
        private String webhookUrl;
        private String channel;
        private String username;
        private String token;
        private boolean enabled = false;
    }

}
