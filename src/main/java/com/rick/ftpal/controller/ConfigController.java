package com.rick.ftpal.controller;

import com.rick.ftpal.service.ConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@RestController
@RequestMapping("/ftpal/config")
public class ConfigController {

    @Autowired
    private ConfigService configService;

    @GetMapping("/fetch_case_splitter")
    public ResponseEntity<String> fetchCaseSplitter() {
        try {
            String scriptContent = IOUtils.toString(new ClassPathResource("jenkins/case_splitter.py").getInputStream(), StandardCharsets.UTF_8);
            return new ResponseEntity(scriptContent, HttpStatus.OK);
        } catch (IOException e) {
            log.error("fetchCaseSplitter Failed", e);
            return new ResponseEntity("ERROR", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/fetch_case_splitter_for_failed")
    public ResponseEntity<String> fetchCaseSplitterForFailed() {
        try {
            String scriptContent = IOUtils.toString(new ClassPathResource("jenkins/case_splitter_for_failed.py").getInputStream(), StandardCharsets.UTF_8);
            return new ResponseEntity(scriptContent, HttpStatus.OK);
        } catch (IOException e) {
            log.error("fetchCaseSplitterForFailed Failed", e);
            return new ResponseEntity("ERROR", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/fetch_config/{executionId}")
    public ResponseEntity<String> fetchConfig(@PathVariable("executionId") Integer executionId) {
        try {
            return new ResponseEntity(configService.fetchConfig(executionId), HttpStatus.OK);
        } catch (Exception e) {
            log.error("fetchConfig Failed", e);
            return new ResponseEntity("ERROR", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/fetch_spec/{executionId}")
    public ResponseEntity<String> fetchSpec(@PathVariable("executionId") Integer executionId) {
        try {
            return new ResponseEntity(configService.fetchSpec(executionId), HttpStatus.OK);
        } catch (Exception e) {
            log.error("fetchCaseSplitter Failed", e);
            return new ResponseEntity("ERROR", HttpStatus.NOT_FOUND);
        }
    }
}
