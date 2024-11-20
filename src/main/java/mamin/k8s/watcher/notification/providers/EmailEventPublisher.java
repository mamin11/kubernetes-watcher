package mamin.k8s.watcher.notification.providers;

import io.kubernetes.client.openapi.models.CoreV1Event;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.annotation.PostConstruct;
import java.util.Properties;

@Data
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "notification.email")
@ConditionalOnProperty(name = "notification.email.config.enabled", havingValue = "true")
public class EmailEventPublisher implements NotificationProvider {
    private EmailConfig config;
    private JavaMailSender emailSender;

    @Override
    @PostConstruct
    public void init() {
        log.info("Initializing email notification provider");

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(config.host);
        mailSender.setPort(config.port);
        mailSender.setUsername(config.username);
        mailSender.setPassword(config.password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", config.protocol);

        emailSender = mailSender;
    }

    @Override
    public boolean enabled() {
        return config.isEnabled();
    }

    @Override
    public void sendNotification(CoreV1Event event) {
        log.info("Sending email notification: {}", event.getMessage());
        String subject = "Kubernetes Event Notification";
        String codeBlock = "%s".formatted(event);

        try {
            sendEmailWithCodeBlock(subject, codeBlock);
            log.info("Email sent successfully.");
        } catch (Exception e) {
            log.error("Failed to send email: {}", e.getMessage());
        }
    }

    public void sendEmailWithCodeBlock(String subject, String codeBlock) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(config.to);
        helper.setFrom(config.username);
        helper.setSubject(subject);

        String htmlContent = """
                <html>
                <body>
                    <h4>New Kubernetes Event:</h4>
                    <pre style="background-color: #f4f4f4; padding: 10px; border: 1px solid #ddd; border-radius: 5px;">
                        <code>%s</code>
                    </pre>
                </body>
                </html>
                """.formatted(codeBlock);
        helper.setText(htmlContent, true);

        emailSender.send(message);
    }

    @Data
    public static class EmailConfig {
        private String host;
        private int port;
        private String protocol;
        private String username;
        private String password;
        private String to;
        private boolean enabled = false;
    }

}
