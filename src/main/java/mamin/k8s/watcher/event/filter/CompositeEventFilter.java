package mamin.k8s.watcher.event.filter;

import java.util.List;

public class CompositeEventFilter implements EventFilter {
    private final List<EventFilter> filters;

    public CompositeEventFilter(List<EventFilter> filters) {
        this.filters = filters;
    }

    @Override
    public boolean shouldWatchEvent(String reason, String message, String involvedObjectKind, String involvedObjectName) {
        return filters.stream().anyMatch(filter -> filter.shouldWatchEvent(reason, message, involvedObjectKind, involvedObjectName));
    }
}
