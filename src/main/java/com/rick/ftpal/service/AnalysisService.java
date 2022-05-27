package com.rick.ftpal.service;

import com.rick.ftpal.repository.EnvironmentRepository;
import com.rick.ftpal.repository.ExecutionRepository;
import com.rick.ftpal.repository.ExecutionTaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AnalysisService {
    @Autowired
    private EnvironmentRepository environmentRepository;

    @Autowired
    private ExecutionRepository executionRepository;

    @Autowired
    private ExecutionTaskRepository executionTaskRepository;


}
