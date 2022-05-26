package com.rick.ftpal.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "execution")
public class Execution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @JsonProperty("id")
    private Integer id;

    @Column(name = "component")
    @JsonProperty("component")
    private String component;

    @Column(name = "version")
    @JsonProperty("version")
    private String version;

    @Column(name = "pr_id")
    @JsonProperty("pr_id")
    private String prId;

    @Column(name = "suite")
    @JsonProperty("suite")
    private String suite;

    @Column(name = "actor")
    @JsonProperty("actor")
    private String actor;

    @Column(name = "thread_ts")
    @JsonProperty("thread_ts")
    private String threadTs;

    @Column(name = "repo")
    @JsonProperty("repo")
    private String repo;

    @Column(name = "branch")
    @JsonProperty("branch")
    private String branch;

    @Column(name = "service_mocking")
    @JsonProperty("service_mocking")
    private String serviceMocking;

    @Column(name = "dependency")
    @JsonProperty("dependency")
    private String dependency;

    @Column(name = "build_number")
    @JsonProperty("build_number")
    private String buildNumber;

    @Column(name = "build_url")
    @JsonProperty("build_url")
    private String buildUrl;

    @Column(name = "status")
    @JsonProperty("status")
    private String status;

    @Column(name = "create_time")
    @JsonProperty("create_time")
    private String createTime;

    @Column(name = "update_time")
    @JsonProperty("update_time")
    private String updateTime;

    @Transient
    @JsonProperty("environments")
    private List<Environment> environments;
}
