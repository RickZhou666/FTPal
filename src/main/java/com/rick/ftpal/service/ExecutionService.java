package com.rick.ftpal.service;

import com.rick.ftpal.entity.Environment;
import com.rick.ftpal.entity.Execution;
import com.rick.ftpal.entity.ExecutionTask;
import com.rick.ftpal.repository.EnvironmentRepository;
import com.rick.ftpal.repository.ExecutionRepository;
import com.rick.ftpal.repository.ExecutionTaskRepository;
import com.rick.ftpal.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.scanner.Constant;

import javax.swing.text.html.Option;
import javax.transaction.Transactional;
import java.util.*;

@Slf4j
@Service
public class ExecutionService {

    @Autowired
    private EnvironmentRepository environmentRepository;

    @Autowired
    private ExecutionRepository executionRepository;

    @Autowired
    private ExecutionTaskRepository executionTaskRepository;

    // Generate a execution record
    public Execution generateExecution(Execution execution) {
        Date currentDate = new Date();
        execution.setStatus(Constants.EXECUTION_STATUS_SUBMITTED);
        if (StringUtils.isBlank(execution.getRepo())) {
            execution.setRepo("Compliance-R");
        }
        if (StringUtils.isBlank(execution.getBranch())) {
            execution.setBranch("develop");
        }
        execution.setCreateTime(currentDate);
        execution.setUpdateTime(currentDate);
        return execution;
    }

    @Transactional
    public Execution processExecution(Integer executionId, Integer size) throws Exception {
        Date currentDate = new Date();
        Optional<Execution> optionalExecution = executionRepository.findById(executionId); // CRUD interface method
        if (!optionalExecution.isPresent()) {
            throw new Exception(String.format("Execution [%s] is not available during processExecution", executionId));
        }
        Execution execution = optionalExecution.get();
        execution.setStatus(Constants.EXECUTION_STATUS_IN_PROGRESS);
        executionRepository.save(execution);
        List<Environment> assignedEnvironmentList = new ArrayList<>();
        // Try to assign IDLE stages deployed with same version
        Iterable<Environment> idleAndSameVersionEnvironmentList = environmentRepository.findByStatusAndVersionOrderByUpdateTimeAsc(Constants.ENVIRONMENT_STATUS_IDLE, execution.getVersion());
        for (Environment environment : idleAndSameVersionEnvironmentList) {
            if (assignedEnvironmentList.size() >= size) {
                break;
            }
            assignedEnvironmentList.add(environment);
        }
        // If there is no IDLE stages with same version, assign recent not used stages.
        Iterable<Environment> idleAndRecentNotUsedEnvironmentList = environmentRepository.findByStatusOrderByUpdateTimeAsc(Constants.ENVIRONMENT_STATUS_IDLE);
        for (Environment environment : idleAndRecentNotUsedEnvironmentList) {
            if (assignedEnvironmentList.size() >= size) {
                break;
            }
            assignedEnvironmentList.add(environment);
        }

        for (Environment environment : assignedEnvironmentList) {
            environment.setVersion(execution.getVersion());
            environment.setStatus(Constants.ENVIRONMENT_STATUS_BUSY);
            environment.setUpdateTime(currentDate);
        }
        environmentRepository.saveAll(assignedEnvironmentList);
        List<ExecutionTask> assignedExecutionTaskList = new ArrayList<>();
        for (Environment environment : assignedEnvironmentList) {
            ExecutionTask executionTask = new ExecutionTask();
            executionTask.setEnvironmentId(environment.getId());
            executionTask.setExecutionId(executionId);
            executionTask.setStatus(Constants.EXECUTION_TASK_STATUS_IN_PROGRESS);
            executionTask.setCreateTime(currentDate);
            executionTask.setUpdateTime(currentDate);
            assignedExecutionTaskList.add(executionTask);
        }
        executionTaskRepository.saveAll(assignedExecutionTaskList);
        execution.setEnvironments(assignedEnvironmentList);
        return execution;
    }

    public Execution handleExceptionFailure(Integer executionId, String name) throws Exception {
        Date currentDate = new Date();
        Optional<Execution> optionalExecution = executionRepository.findById(executionId);
        if (!optionalExecution.isPresent()) {
            throw new Exception(String.format("Exection [%d] os mpt available during completeExecution", executionId));
        }
        Execution execution = optionalExecution.get();
        List<Environment> environmentList = IteratorUtils.toList(environmentRepository.findByName(name).iterator());
        for (Environment environment : environmentList) {
            environment.setStatus(Constants.ENVIRONMENT_STATUS_MAINTENANCE);
            environment.setUpdateTime(currentDate);
        }
        environmentRepository.saveAll(environmentList);
        return execution;
    }

    public Execution completeExecution(Integer executionId) throws Exception {
        Date currentDate = new Date();
        Optional<Execution> optionalExecution = executionRepository.findById(executionId);
        if (!optionalExecution.isPresent()) {
            throw new Exception(String.format("Exection [%d] is not available during completeExecution", executionId));
        }

        // COMPLETE ExecutionTask
        Execution execution = optionalExecution.get();
        execution.setStatus(Constants.EXECUTION_STATUS_COMPLETED);
        executionRepository.save(execution);

        // COMPLETE ExecutionTask
        Iterable<ExecutionTask> assignedExecutionTasks = executionTaskRepository.findByExecutionId(executionId);
        List<Integer> assignedEnvironmentIdList = new ArrayList<>();
        for (ExecutionTask executionTask : assignedExecutionTasks) {
            executionTask.setStatus(Constants.EXECUTION_TASK_STATUS_COMPLETED);
            assignedEnvironmentIdList.add(executionTask.getEnvironmentId());
        }
        executionTaskRepository.saveAll(assignedExecutionTasks);

        // COMPLETE Environment
        Iterable<Environment> assignedEnvironmentList = environmentRepository.findAllById(assignedEnvironmentIdList);
        for (Environment environment : assignedEnvironmentList) {
            // Only update wen environment is BUSY
            if (StringUtils.equals(environment.getStatus(), Constants.ENVIRONMENT_STATUS_BUSY)) {
                environment.setStatus(Constants.ENVIRONMENT_STATUS_IDLE);
            }
            environment.setUpdateTime(currentDate);
        }
        environmentRepository.saveAll(assignedEnvironmentList);
        return execution;
    }


    public Execution findExecutionByBuildNumber(String buildNumber) throws Exception {
        List<Execution> executionList = IteratorUtils.toList(executionRepository.findByBuildNumberOrderByUpdateTimeDesc(buildNumber).iterator());
        if (CollectionUtils.isNotEmpty(executionList)) {
            return executionList.get(0);
        } else {
            throw new Exception(String.format("buildNumber [%s] is not available during findExecutionByBuildNumber", buildNumber));
        }
    }

}
