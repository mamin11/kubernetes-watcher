package mamin.k8s.watcher.config;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.util.Config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Slf4j
@Configuration
public class K8sClientApiConfig {
    @Value("${k8s.config.path}")
    private String path;

    @Bean
    public CoreV1Api coreV1Api() throws IOException {
        //check file path exists
        if (!new java.io.File(path).exists()) {
            log.error("File not found!");
            System.exit(1);
        }
        ApiClient apiClient = Config.fromConfig(path);
        return new CoreV1Api(apiClient);
    }
}
