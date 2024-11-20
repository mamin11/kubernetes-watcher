package mamin.k8s.watcher.event.filter;

import java.util.Set;

public class CustomEventFilter implements EventFilter {
    private final String kind;
    private final Set<String> allowedReasons;

    public CustomEventFilter(String kind, Set<String> allowedReasons) {
        this.kind = kind;
        this.allowedReasons = allowedReasons;
    }

    @Override
    public boolean shouldWatchEvent(String reason, String message, String involvedObjectKind, String involvedObjectName) {
        return allowedReasons.contains(reason) && kind.equals(involvedObjectKind);
    }
}
