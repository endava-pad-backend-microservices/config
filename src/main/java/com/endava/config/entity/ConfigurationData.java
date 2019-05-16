package com.endava.config.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConfigurationData {

    @Id
    private String id;
    private String label;
    private String profile;
    private String version;
    private Source source;
}
