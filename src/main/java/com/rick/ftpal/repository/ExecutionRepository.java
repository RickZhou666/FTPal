package com.rick.ftpal.repository;

import com.rick.ftpal.entity.Environment;
import com.rick.ftpal.entity.Execution;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ExecutionRepository extends CrudRepository<Execution, Integer> {
    Iterable<Environment> findByStatus(@Param("status") String status);

    Iterable<Environment> findByBuildNumberOrderByUpdateTimeDesc(@Param("build_number") String buildNumber);

}
