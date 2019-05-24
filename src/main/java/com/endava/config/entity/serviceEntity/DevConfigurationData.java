package com.endava.config.entity.serviceEntity;

import org.json.simple.JSONObject;
import org.springframework.data.mongodb.core.mapping.Document;

import com.endava.config.ConfigConstants.ConfigConstants;
import com.endava.config.entity.ConfigurationData;
import com.endava.config.entity.Source;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Document(collection = "dev")
public class DevConfigurationData extends ConfigurationData {
	DevConfigurationData(String label, String profile, String version, Source source) {
        super(null, label, profile, version, source);
    }
	
	 public static DevConfigurationData getDefaultConfig() {
		 return new DevConfigurationData(ConfigConstants.LABEL_NAME_DEV,ConfigConstants.PROFILE_NAME_DEV,ConfigConstants.VERSION_DEV,null);
	 }
}