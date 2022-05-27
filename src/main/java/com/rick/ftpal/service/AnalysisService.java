package com.rick.ftpal.service;

import com.rick.ftpal.domain.AnalysisEnvironmentReport;
import com.rick.ftpal.domain.AnalysisExecutionReport;
import com.rick.ftpal.entity.Execution;
import com.rick.ftpal.entity.ExecutionTask;
import com.rick.ftpal.repository.EnvironmentRepository;
import com.rick.ftpal.repository.ExecutionRepository;
import com.rick.ftpal.repository.ExecutionTaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.IteratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AnalysisService {
    @Autowired
    private EnvironmentRepository environmentRepository;

    @Autowired
    private ExecutionRepository executionRepository;

    @Autowired
    private ExecutionTaskRepository executionTaskRepository;


    public AnalysisExecutionReport generateAnalysisExecutionReport(Integer executionId) {
        Optional<Execution> optionalExecution = executionRepository.findById(Integer.valueOf(executionId));
        AnalysisExecutionReport analysisExecutionReport = new AnalysisExecutionReport();
        analysisExecutionReport.setExecution(optionalExecution.get());
        List<ExecutionTask> executionTaskList = IteratorUtils.toList(executionTaskRepository
                .findByExecutionId(executionId).iterator());
        analysisExecutionReport.setEnvrionments(executionTaskList.stream()
                .map(executionTask -> environmentRepository.findById(executionTask.getEnvironmentId()).get())
                .collect(Collectors.toList()));
        return analysisExecutionReport;
    }


    public AnalysisEnvironmentReport generateEnvironmentReport() {
        AnalysisEnvironmentReport analysisEnvironmentReport = new AnalysisEnvironmentReport();
        analysisEnvironmentReport.setEnvironments(IteratorUtils.toList(environmentRepository.findAll().iterator()));
        return analysisEnvironmentReport;
    }
}
