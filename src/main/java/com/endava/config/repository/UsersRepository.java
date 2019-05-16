package com.endava.config.repository;

import com.endava.config.entity.serviceEntity.UsersConfigurationData;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UsersRepository extends MongoRepository<UsersConfigurationData, String> {
    List<UsersConfigurationData> findByLabel(String label);

    List<UsersConfigurationData> findByProfile(String profile);

    List<UsersConfigurationData> findByVersion(String version);
}
