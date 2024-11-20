package mamin.k8s.watcher.event.service;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
class EventWatcher {
    private final EventWatcherService eventWatcherService;

    public EventWatcher(EventWatcherService eventWatcherService) {
        this.eventWatcherService = eventWatcherService;
        eventWatcherService.watchEvents();
    }

    @PostConstruct
    public void init() {
        new Thread(eventWatcherService::watchEvents).start();
    }
}
