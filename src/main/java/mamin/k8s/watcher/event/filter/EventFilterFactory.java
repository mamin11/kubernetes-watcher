package mamin.k8s.watcher.event.filter;

import lombok.extern.slf4j.Slf4j;
import mamin.k8s.watcher.event.filter.config.EventWatcherFilterConfig;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class EventFilterFactory {

    public static EventFilter createFilter(EventWatcherFilterConfig config) {
        List<EventWatcherFilterConfig.FilterConfig> filters = config.getFilters();

        if (filters == null || filters.isEmpty()) {
            log.warn("No filters configured, default events will be watched");
            return new CustomEventFilter(
                    PodErrorEventFilter.kind,
                    Set.copyOf(PodErrorEventFilter.errorReasons)
            );
        }

        log.info("Creating event filter with filters config {}", filters);
        return new CompositeEventFilter(
                filters.stream()
                        .map(filterConfig -> new CustomEventFilter(
                                filterConfig.getKind(),
                                Set.copyOf(filterConfig.getReasons())
                        ))
                        .collect(Collectors.toList())
        );
    }
}
