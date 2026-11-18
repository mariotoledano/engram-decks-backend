package com.engramdecks.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "engram.maintenance")
public class EngramMaintenanceProperties {

	/**
	 * When {@code true}, registers {@code /api/v1/maintenance/**} endpoints.
	 */
	private boolean enabled;

}