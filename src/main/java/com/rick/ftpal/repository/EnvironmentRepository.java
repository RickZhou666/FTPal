package com.rick.ftpal.repository;

import com.rick.ftpal.entity.Environment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface EnvironmentRepository extends CrudRepository<Environment, Integer> {
    Iterable<Environment> findByName(@Param("name") String name);

    Iterable<Environment> findByStatus(@Param("status") String status);

    Iterable<Environment> findByStatusOrderByUpdateTimeAsc(@Param("status") String status);

    Iterable<Environment> findByStatusOrderByUpdateTimeDesc(@Param("name") String name);

    Iterable<Environment> findByNameAndVersionOrderByUpdateTimeAsc(@Param("status") String status, @Param("version") String version);

}
