package com.rick.ftpal.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.annotations.GeneratorType;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "execution_task")
public class ExecutionTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @JsonProperty("id")
    private Integer id;

    @Column(name = "execution_id")
    @JsonProperty("execution_id")
    private Integer executionId;

    @Column(name = "environment_id")
    @JsonProperty("environment_id")
    private Integer environmentId;

    @Column(name = "status")
    @JsonProperty("status")
    private String status;

    @Column(name = "create_time")
    @JsonProperty("create_time")
    private Date createTime;

    @Column(name = "update_time")
    @JsonProperty("update_time")
    private Date updateTime;
}
