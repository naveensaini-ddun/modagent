package com.legacyfirst.modagent.web.controller;

import com.legacyfirst.modagent.analysis.patterns.AntiPattern;
import com.legacyfirst.modagent.analysis.patterns.PatternCatalogue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/patterns")
public class PatternsController {

    private final PatternCatalogue catalogue;

    public PatternsController(PatternCatalogue catalogue) {
        this.catalogue = catalogue;
    }

    @GetMapping
    public List<AntiPattern> list() {
        return catalogue.all();
    }
}
