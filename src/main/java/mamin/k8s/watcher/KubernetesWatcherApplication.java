package mamin.k8s.watcher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class KubernetesWatcherApplication {
	public static void main(String[] args) {
		SpringApplication.run(KubernetesWatcherApplication.class, args);
	}

}
