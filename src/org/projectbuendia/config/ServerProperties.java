package org.projectbuendia.config;

import org.projectbuendia.fileops.Logging;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;


public final class ServerProperties {

    private static File configFile = null;

    public File getConfigFile() {
        return configFile;
    }
    public ServerProperties(String file) {
        configFile = new File(file);
    }

	public static final Properties properties = new Properties();

	public static void read() {
		if (configFile.exists()) {
			try {
				properties.load(new FileInputStream(configFile));
			} catch (FileNotFoundException e) {
				Logging.log("STOP(CONFIG FILE WAS NOT FOUND!)", e);
			} catch (IOException e) {
				Logging.log("STOP(CONFIG FILE THREW IO EXCEPTION)",e);
			}
			Config.SERVER_NAME =(properties.getProperty("SERVER_NAME"));
			Config.SERVER_VERSION = Integer.parseInt(properties.getProperty("SERVER_VERSION"));
            Config.SERVER_TIMEZONE = (properties.getProperty("SERVER_TIMEZONE"));
			Config.ENABLE_MONGOPUSH = Boolean.parseBoolean(properties.getProperty("ENABLE_MONGOPUSH"));
			Config.HTTP_PORT = Integer.parseInt(properties.getProperty("HTTP_PORT"));
			Config.SQLITE_PATH = (properties.getProperty("SQLITE_PATH"));
			Config.MONGODB_HOST = (properties.getProperty("MONGODB_HOST"));
			Config.MONGODB_DB = (properties.getProperty("MONGODB_DB"));
			Config.MAX_ERROR_FOLDER_SIZE_MB = Integer.parseInt(properties.getProperty("MAX_ERROR_FOLDER_SIZE_MB"));
            Config.ERROR_PATH = (properties.getProperty("ERROR_PATH"));
		} else {
            Logging.log("WARNING","Configuration file was not found in project root. Now running from config.Config default properties");
        }
	}

}
