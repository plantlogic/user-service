package edu.csumb.spring19.capstone.config;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AppConfig {
    @Value("${APP_NAME:PlantLogic}")
    private String appName;

    @Value("${APP_URL: }")
    private String appURL;

    public String getAppName() {
        return appName;
    }

    public String getAppURL() {
        return appURL;
    }

    public boolean hasAppURL() {
        return !Strings.isNullOrEmpty(appURL);
    }
}
