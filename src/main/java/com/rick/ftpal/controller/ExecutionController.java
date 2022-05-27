package com.rick.ftpal.controller;

import com.rick.ftpal.entity.Environment;
import com.rick.ftpal.entity.Execution;
import com.rick.ftpal.service.ExecutionService;
import com.rick.ftpal.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "/ftpal/execution")
public class ExecutionController {

    @Autowired
    private ExecutionService executionService;

    @PutMapping("/generate_execution")
    public ResponseEntity<String> generateExecution(@RequestBody Execution execution) {
        Execution resultExecution = executionService.generateExecution(execution);

        return null;
    }

    @PostMapping("/process_execution/{executionId}")
    public ResponseEntity<String> processExecution(@PathVariable("executionId") Integer executionId) {
        try {
            Execution execution = executionService.processExecution(executionId, Constants.NUMBER_OF_CI_WORKER);
            StringBuilder sb = new StringBuilder();
            sb.append("Below test environments are assigned:\n");
            for (Environment environment : execution.getEnvironments()) {
                sb.append(String.format("- <%s|%s>\n", environment.getUrl(), environment.getName()));
            }
            sb.append("These test environments will be update via below spec: \n");
            sb.append("```");
            sb.append("");
            sb.append("```");

            return null;
        } catch (Exception e) {
            log.error("processExecution Failed", e);
            return null;
        }
    }


}
