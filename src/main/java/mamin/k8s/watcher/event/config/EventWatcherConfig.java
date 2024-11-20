package mamin.k8s.watcher.event.config;

import mamin.k8s.watcher.event.filter.EventFilter;
import mamin.k8s.watcher.event.filter.EventFilterFactory;
import mamin.k8s.watcher.event.filter.config.EventWatcherFilterConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventWatcherConfig {
    private final EventWatcherFilterConfig eventWatcherFilterConfig;

    public EventWatcherConfig(EventWatcherFilterConfig eventWatcherFilterConfig) {
        this.eventWatcherFilterConfig = eventWatcherFilterConfig;
    }

    @Bean
    public EventFilter eventFilter() {
        return EventFilterFactory.createFilter(eventWatcherFilterConfig);
    }
}
