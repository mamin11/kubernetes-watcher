package mamin.k8s.watcher.event.filter;

public interface EventFilter {
    boolean shouldWatchEvent(String reason, String message, String involvedObjectKind, String involvedObjectName);
}
