package com.rick.ftpal.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rick.ftpal.entity.Environment;
import com.rick.ftpal.entity.Execution;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AnalysisExecutionReport {
    @JsonProperty("execution")
    Execution execution;

    @JsonProperty("environments")
    List<Environment> envrionments;
}
