package mamin.k8s.watcher.event.filter.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@Slf4j
@Configuration
public class EventWatcherFilterConfig {
    private List<FilterConfig> filters = new ArrayList<>();

    @PostConstruct
    public void init() {
        try {
            List<Map<String, Object>> config = getConfig();
            config.forEach(filter -> {
                log.info("Loading filter: {}", filter);
                FilterConfig filterConfig = new FilterConfig();
                filterConfig.setKind((String) filter.get("kind"));
                filterConfig.setReasons((List<String>) filter.get("reasons"));
                filters.add(filterConfig);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Data
    public static class FilterConfig {
        private String kind;
        private List<String> reasons;
    }

    @Value("${EVENT_WATCHER_FILTERS:[]}")
    private String configJson;

    public List<Map<String, Object>> getConfig() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(configJson, List.class);
    }
}
