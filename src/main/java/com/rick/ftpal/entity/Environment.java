package com.rick.ftpal.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "environment")
public class Environment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @JsonProperty("id")
    private Integer id;

    @Column(name = "name")
    @JsonProperty("name")
    private String name;

    @Column(name = "url")
    @JsonProperty("url")
    private String url;

    @Column(name = "status")
    @JsonProperty("status")
    private String status;

    @Column(name = "version")
    @JsonProperty("version")
    private String version;

    @Column(name = "namespace")
    @JsonProperty("namespace")
    private String namespace;

    @Column(name = "create_time")
    @JsonProperty("create_time")
    private Date createTime;

    @Column(name = "update_time")
    @JsonProperty("update_time")
    private Date updateTime;

}
