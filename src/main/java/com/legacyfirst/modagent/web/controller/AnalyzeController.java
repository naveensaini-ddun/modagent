package com.legacyfirst.modagent.web.controller;

import com.legacyfirst.modagent.service.AnalysisService;
import com.legacyfirst.modagent.web.dto.AnalyzeRequest;
import com.legacyfirst.modagent.web.dto.AnalyzeResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/analyze")
public class AnalyzeController {

    private final AnalysisService service;

    public AnalyzeController(AnalysisService service) {
        this.service = service;
    }

    @PostMapping
    public AnalyzeResponse analyze(@Valid @RequestBody AnalyzeRequest request) {
        return service.analyze(request);
    }
}
