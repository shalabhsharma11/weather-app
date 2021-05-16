package com.assignment.spring.repository;

import com.assignment.spring.entity.WeatherEntity;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherRepository extends CrudRepository<WeatherEntity, Integer> {
}
