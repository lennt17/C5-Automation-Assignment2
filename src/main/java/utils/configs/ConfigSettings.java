package utils.configs;

import java.io.IOException;
import java.util.Properties;

public class ConfigSettings {
    private static final String BROWSER = "browser";
    private static final String DEFAULT_TIMEOUT = "timeout";

    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final String BASE_URL = "urlBase";
    private static final String BASE_URI = "baseURI";
    private static final String BASE_URI_TOKEN = "baseURIToken";

    private static final String BASEPATH_PROJECT = "basePath_project";
    private static final String BASEPATH_TASK = "basePath_task";

    private static final String PROPERTIES_FILE_NAME = "settings";

    private Properties configProperties;

    public ConfigSettings(String projectDirLocation) {
        try {
            setConfigProperties(PropertySettingStoreUtil.getSettings(projectDirLocation, PROPERTIES_FILE_NAME));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setConfigProperties(Properties configProperties) {
        this.configProperties = configProperties;
    }

    public String getBrowser() {
        return this.configProperties.getProperty(BROWSER);
    }

    public String getDefaultTimeout() {
        return this.configProperties.getProperty(DEFAULT_TIMEOUT);
    }

    public String getBaseUrl() {
        return this.configProperties.getProperty(BASE_URL);
    }
    public String getEmail(){
        return this.configProperties.getProperty(EMAIL);
    }

    public String getPassword(){
        return this.configProperties.getProperty(PASSWORD);
    }
    public String getBaseURI() {
        return this.configProperties.getProperty(BASE_URI);
    }

    public String getBaseURIToken(){
        return this.configProperties.getProperty(BASE_URI_TOKEN);
    }

    public String getBasePathProject() {
        return this.configProperties.getProperty(BASEPATH_PROJECT);
    }

    public String getBasePathTask() {
        return this.configProperties.getProperty(BASEPATH_TASK);
    }

    public Properties getConfigProperties() {
        return configProperties;
    }
}
