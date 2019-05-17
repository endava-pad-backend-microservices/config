package com.endava.config.repository;

import com.endava.config.entity.serviceEntity.RegistryConfigurationData;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RegistryRepository extends MongoRepository<RegistryConfigurationData, String> {
    List<RegistryConfigurationData> findByLabel(String label);

    List<RegistryConfigurationData> findByProfile(String profile);

    List<RegistryConfigurationData> findByVersion(String version);
}
