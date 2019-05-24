package com.endava.config.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.endava.config.entity.serviceEntity.ProdConfigurationData;

public interface ProdRepository extends MongoRepository<ProdConfigurationData, String> {
	List<ProdConfigurationData> findByLabel(String label);
    List<ProdConfigurationData> findByVersion(String version);
}
