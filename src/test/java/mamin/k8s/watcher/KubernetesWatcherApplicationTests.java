package mamin.k8s.watcher;

import io.kubernetes.client.openapi.apis.CoreV1Api;
import mamin.k8s.watcher.event.service.EventWatcherService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class KubernetesWatcherApplicationTests {
	@MockBean
	private CoreV1Api coreV1Api;
	@MockBean
	private EventWatcherService eventWatcherService;

	@Test
	void contextLoads() {
		Mockito.doNothing().when(eventWatcherService).watchEvents();
	}

}
