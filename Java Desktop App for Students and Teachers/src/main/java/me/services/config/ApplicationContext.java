package me.services.config;
import java.io.IOException;
import java.util.Properties;


public class ApplicationContext {
    private static Properties PROPERTIES;

    static {
        try {
            PROPERTIES = Config.getProperties();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Properties getPROPERTIES() {
        return PROPERTIES;
    }
}
