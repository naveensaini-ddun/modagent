package com.legacyfirst.modagent.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class StartupLogger {

    private static final Logger log = LoggerFactory.getLogger(StartupLogger.class);

    private final ModAgentProperties props;

    public StartupLogger(ModAgentProperties props) {
        this.props = props;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onReady() {
        var llm = props.llm();
        var apiKeyState = (llm.apiKey() == null || llm.apiKey().isBlank()) ? "<unset>" : "<set>";
        log.info(
                "ModAgent ready — llm.provider={} llm.model={} llm.timeoutSeconds={} llm.apiKey={} target.framework={}",
                llm.provider(), llm.model(), llm.timeoutSeconds(), apiKeyState, props.target().framework()
        );
    }
}
