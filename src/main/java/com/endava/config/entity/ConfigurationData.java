package com.endava.config.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

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
