package mamin.k8s.watcher.event.config;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.util.Config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Configuration
public class K8sClientApiConfig {

    // Optional property for specifying custom kubeconfig for local testing
    @Value("${k8s.config.path:#{null}}")
    private String path;

    @Bean
    public CoreV1Api coreV1Api() throws IOException {
        ApiClient apiClient;
        log.info("Initializing Kubernetes API client");

        // Check if running inside a Kubernetes cluster
        if (isRunningInCluster()) {
            log.info("Running inside Kubernetes cluster. Configuring in-cluster Kubernetes API client.");
            apiClient = Config.fromCluster();
        } else {
            log.info("Running outside Kubernetes cluster. Configuring local Kubernetes API client.");
            // Use kubeconfig for local testing
            String kubeConfigPath = path != null ? path : System.getProperty("user.home") + "/.kube/config";
            log.info("Running locally. Using kubeconfig at {}", kubeConfigPath);

            // Validate the kubeconfig file exists
            if (!new java.io.File(kubeConfigPath).exists()) {
                log.error("Kubeconfig file not found at {}. Exiting.", kubeConfigPath);
                throw new IllegalStateException("Kubeconfig file not found at " + kubeConfigPath);
            }

            apiClient = Config.fromConfig(kubeConfigPath);
        }

        return new CoreV1Api(apiClient);
    }

    public boolean isRunningInCluster() {
        // Check for Kubernetes environment variables
        String kubeServiceHost = System.getenv("KUBERNETES_SERVICE_HOST");
        String kubeServicePort = System.getenv("KUBERNETES_SERVICE_PORT");
        boolean hasKubernetesEnvVars = kubeServiceHost != null && kubeServicePort != null;

        // Check for service account token file
        Path serviceAccountTokenPath = Paths.get("/var/run/secrets/kubernetes.io/serviceaccount/token");
        boolean hasServiceAccountToken = Files.exists(serviceAccountTokenPath);

        // Return true if either check passes
        return hasKubernetesEnvVars || hasServiceAccountToken;
    }
}

