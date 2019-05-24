package com.endava.config.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.endava.config.entity.serviceEntity.DevConfigurationData;

public interface DevRepository extends MongoRepository<DevConfigurationData, String> {
	List<DevConfigurationData> findByLabel(String label);
    List<DevConfigurationData> findByVersion(String version);
}
