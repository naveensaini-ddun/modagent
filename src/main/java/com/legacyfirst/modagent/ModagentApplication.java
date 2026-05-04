package com.legacyfirst.modagent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class ModagentApplication {

    public static void main(String[] args) {
        SpringApplication.run(ModagentApplication.class, args);
    }
}
