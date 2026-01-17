package com.auditwp.platform;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "audit.service")
@Data
public class BackendApplicationProperties {

    private String auditBaseUrl;
}
