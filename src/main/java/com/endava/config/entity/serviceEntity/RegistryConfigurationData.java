package com.endava.config.entity.serviceEntity;

import com.endava.config.entity.ConfigurationData;
import com.endava.config.entity.Source;
import com.endava.config.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "registry")
public class RegistryConfigurationData extends ConfigurationData {
    RegistryConfigurationData(String label, String profile, String version, Source source) {
        super(null, label, profile, version, source);
    }

    public static RegistryConfigurationData getDefaultConfig() {
        User defaultUser = User.builder()
                .maxConnections(222)
                .timeoutMs(0)
                .build();
        Source defaultSource = Source.builder()
                .user(defaultUser)
                .build();

        return new RegistryConfigurationData("configuration", "dev", "1.0.2", defaultSource);
    }
}
