package com.legacyfirst.modagent.web.controller;

import com.legacyfirst.modagent.domain.MigrationReport;
import com.legacyfirst.modagent.service.MigrationService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/migrate")
public class MigrateController {

    private final MigrationService service;

    public MigrateController(MigrationService service) {
        this.service = service;
    }

    @PostMapping("/{id}")
    public MigrationReport migrate(@PathVariable UUID id) {
        return service.migrate(id);
    }
}
