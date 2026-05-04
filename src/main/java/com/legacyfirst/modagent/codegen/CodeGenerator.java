package com.legacyfirst.modagent.codegen;

public interface CodeGenerator {
    String targetFramework();

    String generate(String legacyCode, String language);
}
