# Kubernetes Event Watcher

Kubernetes Event Watcher is a Spring Boot application that monitors Kubernetes events and sends notifications via Slack and email.

## Features

- Monitors Kubernetes events such as Pod Killing, Failed, FailedScheduling, BackOff, and Unhealthy.
- Sends notifications to Slack and email.
- Configurable via environment variables, ConfigMap, and Secret.

## Prerequisites

- Java 11 or higher
- Maven
- Kubernetes cluster
- Docker

## Configuration

### Environment Variables

- `SPRING_PROFILES_ACTIVE`: Set the active Spring profile (e.g., `prod`).
- `KUBERNETES_SERVICE_HOST`: Kubernetes service host.
- `KUBERNETES_SERVICE_PORT`: Kubernetes service port.

### ConfigMap

Create a ConfigMap to store non-sensitive configuration properties.

```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: k8s-watcher-config
data:
  SPRING_PROFILES_ACTIVE: "dev"
  KUBERNETES_SERVICE_HOST: "kubernetes.default.svc"
  KUBERNETES_SERVICE_PORT: "443"
  notification.slack.config.enabled: "false"
  notification.slack.config.channel: "k8s-events"
  notification.slack.config.username: "Kubernetes-Watcher"
  notification.email.config.enabled: "true"
  notification.email.config.host: "host.docker.internal"
  notification.email.config.port: "1025"
  notification.email.config.protocol: "smtp"
  notification.email.config.to: "support@mycompany.com"
```

### Secret

Create a Secret to store sensitive configuration properties. Update the values accordingly.

```yaml
apiVersion: v1
kind: Secret
metadata:
  name: k8s-watcher-secret
type: Opaque
data:
  SLACK_WEBHOOK_URL: xxxx
  EMAIL_USERNAME: xxxx
  EMAIL_PASSWORD: xxxxx
  SLACK_TOKEN: xxxxx
```

### Deployment

Create a Deployment to run the application in Kubernetes. A template can be found in the manifests directory.


## Building and Running

### Build the Docker Image

```sh
mvn clean package
docker build -t your-docker-repo/k8s-watcher:latest .
```

### Deploy to Kubernetes

CD into manifests directory and apply the configuration files. 
The following command will create all necessary resources in the default namespace 
and if you have set correct configmaps and secrets, 
you should receive notifications in Slack and/or email. 
```sh
kubectl apply -f .
```

### Demo Pods

There are demo pods in the .test-pods directory to simulate Kubernetes events. 
Applying these should trigger events.
```sh
kubectl apply -f .
```

## Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

## License

This project is licensed under the MIT License.