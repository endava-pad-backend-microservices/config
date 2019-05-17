package com.endava.config.repository;

import com.endava.config.entity.serviceEntity.GatewayConfigurationData;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface GatewayRepository extends MongoRepository<GatewayConfigurationData, String> {
    List<GatewayConfigurationData> findByLabel(String label);

    List<GatewayConfigurationData> findByProfile(String profile);

    List<GatewayConfigurationData> findByVersion(String version);
}
