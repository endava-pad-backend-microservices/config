package com.endava.config.entity.serviceEntity;

import org.springframework.data.mongodb.core.mapping.Document;

import com.endava.config.ConfigConstants.ConfigConstants;
import com.endava.config.entity.ConfigurationData;
import com.endava.config.entity.Source;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(collection = "qa")
public class QAConfigurationData extends ConfigurationData  {
	QAConfigurationData(String label, String profile, String version, Source source) {
		super(null, label, profile, version, source);
	}

	public static QAConfigurationData getDefaultConfig() {
		return new QAConfigurationData(ConfigConstants.LABEL_NAME_QA, ConfigConstants.PROFILE_NAME_QA,
				ConfigConstants.VERSION_QA, null);
	}
}
