package mamin.k8s.watcher.event.filter;

import java.util.Arrays;
import java.util.List;

public class PodErrorEventFilter implements EventFilter {
    protected static final String kind = "Pod";
    protected static final List<String> errorReasons = Arrays.asList("Failed", "BackOff", "Unhealthy", "Killing");

    @Override
    public boolean shouldWatchEvent(String reason, String message, String involvedObjectKind, String involvedObjectName) {
        return involvedObjectKind.equals(kind) && errorReasons.contains(reason);
    }
}
