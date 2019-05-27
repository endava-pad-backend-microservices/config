package com.endava.config.entity.serviceEntity;

import org.springframework.data.mongodb.core.mapping.Document;

import com.endava.config.ConfigConstants.ConfigConstants;
import com.endava.config.entity.ConfigurationData;
import com.endava.config.entity.Source;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(collection = "prod")
public class ProdConfigurationData extends ConfigurationData {
	ProdConfigurationData(String label, String profile, String version, Source source) {
		super(null, label, profile, version, source);
	}

	public static ProdConfigurationData getDefaultConfig() {
		return new ProdConfigurationData(ConfigConstants.LABEL_NAME_PROD, ConfigConstants.PROFILE_NAME_PROD,
				ConfigConstants.VERSION_PROD, null);
	}
}
