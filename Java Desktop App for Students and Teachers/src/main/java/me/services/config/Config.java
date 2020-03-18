package me.services.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {


    public static Properties getProperties() throws IOException {

        Properties prop = new Properties();
        String propFileName = "properties/config.properties";

        InputStream inputStream = Config.class.getClassLoader().getResourceAsStream(propFileName);
        if(inputStream != null) {
            prop.load(inputStream);
        } else {
            throw new FileNotFoundException("prop fine not found\n");
        }
        return prop;
    }
}