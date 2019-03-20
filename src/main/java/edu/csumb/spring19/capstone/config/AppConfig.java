package edu.csumb.spring19.capstone.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AppConfig {
    @Value("${APP_NAME:PlantLogic}")
    private String appName;

    public String getAppName() {
        return appName;
    }
}
