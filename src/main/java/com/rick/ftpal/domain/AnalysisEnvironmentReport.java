package com.rick.ftpal.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rick.ftpal.entity.Environment;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AnalysisEnvironmentReport {

    @JsonProperty("environments")
    List<Environment> environments;
}
