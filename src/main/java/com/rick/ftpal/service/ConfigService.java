package com.rick.ftpal.service;

import com.rick.ftpal.entity.Environment;
import com.rick.ftpal.entity.Execution;
import com.rick.ftpal.entity.ExecutionTask;
import com.rick.ftpal.repository.EnvironmentRepository;
import com.rick.ftpal.repository.ExecutionRepository;
import com.rick.ftpal.repository.ExecutionTaskRepository;
import com.rick.ftpal.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class ConfigService {
    @Autowired
    private EnvironmentRepository environmentRepository;

    @Autowired
    private ExecutionRepository executionRepository;

    @Autowired
    private ExecutionTaskRepository executionTaskRepository;

    public String fetchConfig(Integer executionId) throws Exception {
        StringBuilder sb = new StringBuilder();
        Optional<Execution> optionalExecution = executionRepository.findById(executionId);
        if (!optionalExecution.isPresent()) {
            throw new Exception(String.format("Exection [%d] is not available during fetchConfig", executionId));
        }
        Execution execution = optionalExecution.get();
        Iterable<ExecutionTask> deploymentTaskList = executionTaskRepository.findByExecutionId(executionId);
        int index = 0;
        for (ExecutionTask executionTask : deploymentTaskList) {
            index++;
            Environment environment = environmentRepository.findById(executionTask.getEnvironmentId()).get();
            sb.append("STAGE_").append(index).append("=").append(environment.getName()).append("\n");
            sb.append("STAGE_NAMESPACE_").append(index).append("=").append(environment.getNamespace()).append("\n");
        }
        sb.append("JD_TASK_TOTAL=").append(index).append("\n");
        sb.append("JD_REPO=").append(execution.getRepo()).append("\n");
        sb.append("JD_BRANCH=").append(execution.getBranch()).append("\n");
        sb.append("ROOT_POM=").append(execution.getComponent() + "FunctionalTests").append("\n");
        sb.append("PARALLEL=").append(10).append("\n");
        if (execution.getSuite().contains("jdcloud_failed.xml")) {
            sb.append("TEST_RETRY=").append(3).append("\n");
        } else {
            sb.append("TEST_RETRY=").append(2).append("\n");
        }
        sb.append("SERVICE_RETRY=").append(1).append("\n");
        sb.append("EXECUTION_ID=").append(executionId).append("\n");
        sb.append("THREAD_COUNT=").append(10).append("\n");
        sb.append("DATA_THREAD_COUNT=").append(10).append("\n");
        sb.append("CREATE_USER_COUNT=").append(2).append("\n");
        return sb.toString();
    }

    public String fetchSpec(Integer executionId) throws Exception {
        StringBuilder sb = new StringBuilder();
        Optional<Execution> optionalExecution = executionRepository.findById(executionId);
        if (!optionalExecution.isPresent()) {
            throw new Exception(String.format("Exection [%d] is not available during fetchSpec", executionId));
        }
        Execution execution = optionalExecution.get();
        if (StringUtils.isNotBlank(execution.getServiceMocking())) {
            sb.append("features:" + "\n");
            sb.append("  - name: service_mocking" + "\n");
            sb.append("    configs:" + "\n");
            sb.append("      - name: services" + "\n");
            sb.append("        values: " + execution.getServiceMocking() + "\n");
        }
        sb.append("applications:" + "\n");
        // sb.append("-name: amquserrecond" + "\n");
        // sb.append(" version_from: live" + "\n");
        sb.append("-name: compliancepolicyserv" + "\n");
        if (execution.getComponent().equals(Constants.COMPONENT_COMPLIANCEPOLICYSERV)) {
            sb.append("  version: " + execution.getVersion().substring(execution.getVersion().indexOf("-") + 1) + "\n");
        } else {
            sb.append("  version_from: live" + "\n");
        }
        sb.append("- name: compliancereadserv" + "\n");
        if (execution.getComponent().equals(Constants.COMPONENT_COMPLIANCEREADSERV)) {
            sb.append("  version: " + execution.getVersion().substring(execution.getVersion().indexOf("-") + 1) + "\n");
        } else {
            sb.append("  version_from: live" + "\n");
        }
        sb.append("- name: kfakarecoveryserv" + "\n");
        sb.append("  version_from: live" + "\n");
        sb.append("- name: managedaccountapiserv" + "\n");
        sb.append("  version_from: live" + "\n");
        sb.append("backed_by: msmaster.qa.paypal.com" + "\n");
        sb.append("operators:" + "\n");
        sb.append("-runzhou" + "\n");
        sb.append("-wukong_readonly" + "\n");
        sb.append("-WUKONG_READONLY" + "\n");
        return sb.toString();
    }
}
