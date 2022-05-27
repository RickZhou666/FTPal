package com.rick.ftpal.controller;

import com.rick.ftpal.domain.FTRunSummary;
import com.rick.ftpal.entity.Environment;
import com.rick.ftpal.entity.Execution;
import com.rick.ftpal.service.ConfigService;
import com.rick.ftpal.service.ExecutionService;
import com.rick.ftpal.util.BooneHelper;
import com.rick.ftpal.util.Constants;
import com.rick.ftpal.util.SlackMessenger;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "/ftpal/execution")
public class ExecutionController {

    @Autowired
    private ExecutionService executionService;

    @Autowired
    private ConfigService configService;

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
            sb.append(configService.fetchSpec(execution.getId()));
            sb.append("```");

            SlackMessenger.sendThreadMessage(execution.getActor(), Constants.SLACK_CHANNEL_NAME, execution.getThreadTs(), sb.toString());
            return new ResponseEntity(execution.getStatus(), HttpStatus.OK);
        } catch (Exception e) {
            log.error("processExecution Failed", e);
            return new ResponseEntity("ERROR", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/handle_execution_failure/{executionId}/{name}")
    public ResponseEntity<String> handleExecutionFailure(@PathVariable("executionId") Integer executionId, @PathVariable("name") String name) {
        try {
            Execution execution = executionService.handleExceptionFailure(executionId, name);
            SlackMessenger.sendThreadMessage(
                    execution.getActor(),
                    Constants.SLACK_CHANNEL_NAME,
                    execution.getThreadTs(),
                    String.format("Error happened during execution, move environment <%s|%s> to MAINTENANCE",
                            Constants.CONSOLE_TESTENV_URL + name, name));
            return new ResponseEntity(execution.getStatus(), HttpStatus.OK);
        } catch (Exception e) {
            log.error("handleExecutionFailure Failed", e);
            return new ResponseEntity("ERROR", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/complete_execution/{executionId}")
    public ResponseEntity<String> completeExecution(@PathVariable("executionId") Integer executionId) {
        try {
            Execution execution = executionService.completeExecution(executionId);
            FTRunSummary ftRunSummary = BooneHelper.fetchFTRunSummary(execution.getComponent(), execution.getPrId());
            SlackMessenger.sendThreadMessage(
                    execution.getActor(),
                    Constants.SLACK_CHANNEL_NAME,
                    execution.getThreadTs(),
                    String.format("FTPal job for component *%s* with version *%s* just *COMPLETED*.\nCI job Link: <%s|%s>\n%s",
                            execution.getComponent(),
                            execution.getVersion(),
                            execution.getBuildUrl(),
                            "JDCloud-#" + execution.getBuildNumber(),
                            ftRunSummary == null ? StringUtils.EMPTY : ftRunSummary.toString()));

            // Trigger failed-only run.
            if (!execution.getSuite().endsWith("ftpal_failed.xml")
                    && ftRunSummary != null
                    && Integer.valueOf(ftRunSummary.getFailed()) + Integer.valueOf(ftRunSummary.getNotStarted()) > 0) {
                SlackMessenger.sendFailedOnlyMessage(Constants.SLACK_CHANNEL_NAME, execution);
            }
            return new ResponseEntity(execution.getStatus(), HttpStatus.OK);
        } catch (Exception e) {
            log.error("completeExecution Failed", e);
            return new ResponseEntity("ERROR", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("find_execution/{buildNumber}")
    public ResponseEntity<String> findExecution(@PathVariable("buildNumber") String buildNumber) {
        try {
            return new ResponseEntity(executionService.findExecutionByBuildNumber(buildNumber).getId(), HttpStatus.OK);
        } catch (Exception e) {
            log.error("find_by_build_number Failed", e);
            return new ResponseEntity("ERROR", HttpStatus.NOT_FOUND);
        }
    }
}
