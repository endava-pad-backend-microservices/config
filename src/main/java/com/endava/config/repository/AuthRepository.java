package com.endava.config.repository;

import com.endava.config.entity.serviceEntity.AuthConfigurationData;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AuthRepository extends MongoRepository<AuthConfigurationData, String> {
    List<AuthConfigurationData> findByLabel(String label);

    List<AuthConfigurationData> findByProfile(String profile);

    List<AuthConfigurationData> findByVersion(String version);
}
