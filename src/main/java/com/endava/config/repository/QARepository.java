package com.endava.config.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.endava.config.entity.serviceEntity.QAConfigurationData;

public interface QARepository extends MongoRepository<QAConfigurationData, String> {
	List<QAConfigurationData> findByLabel(String label);
    List<QAConfigurationData> findByVersion(String version);
}
