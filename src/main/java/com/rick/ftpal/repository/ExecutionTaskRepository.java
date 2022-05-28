package com.rick.ftpal.repository;

import com.rick.ftpal.entity.ExecutionTask;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ExecutionTaskRepository extends CrudRepository<ExecutionTask, Integer> {
    Iterable<ExecutionTask> findByExecutionId(@Param("execution_id") Integer executionId);

    Iterable<ExecutionTask> findByStatus(@Param("status") String status);

    Iterable<ExecutionTask> findByExecutionIdOrderByExecutionIdAsc(@Param("execution_id") Integer executionId);

    Iterable<ExecutionTask> findByExecutionIdAndStatus(@Param("execution_id") Integer executionId, @Param("status") String status);

}
