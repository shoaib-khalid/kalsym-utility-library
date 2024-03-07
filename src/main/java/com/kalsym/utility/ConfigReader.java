package com.kalsym.utility;

/**
 *
 * @author taufik
 */
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import org.slf4j.*;

public class ConfigReader {

    static Logger logger = LoggerFactory.getLogger("com.kalsym.utility");

    static Properties prop = new Properties();
    static String configFile;

    public static void init(String confFile) {
        try {
            prop.load(new InputStreamReader(new FileInputStream(confFile), StandardCharsets.UTF_8));
            configFile = confFile;
        } catch (FileNotFoundException e) {
            //LogProperties.WriteLog("init config:" + e);
        } catch (Exception e) {
            //LogProperties.WriteLog("init config:" + e);
        }
    }

    /**
     * Reloads the configuration file
     *
     * @return true if file loaded successfully
     */
    public boolean reload() {
        try {
            prop.load(new FileInputStream(configFile));
        } catch (IOException ex) {
        }
        return true;
    }

    /**
     *
     * @param key
     * @return returns value of the key if found else returns empty string
     */
    public static String getProperty(String key) {
        if (prop.getProperty(key) == null || prop.getProperty(key).equals("")) {
            //LogProperties.WriteLog("Function: GetProperty(?), Config not found:" + key);
            logger.info("Function: GetProperty(?), Config not found:" + key);
            return null;
        } else {
            String value = "";
            try {
                value = prop.getProperty(key);
            } catch (Exception ex) {
                try {
                    //LogProperties.WriteLog("Function: GetProperty(?), Config not found:" + key);
                } catch (Exception exx) {
                    System.out.println("Function: GetProperty(?), Config not found:" + key);
                }
            }
            return value;
        }
    }

    /**
     * Returns the defaultValue if the key doesn't exist in config
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public static String getProperty(String key, String defaultValue) {
        String val = prop.getProperty(key);
        if (val == null || val.equals("")) {
            try {
                //LogProperties.WriteLog("Function: GetProperty(?,?), Config not found using default:" + key);
                logger.info("Function: GetProperty(?,?), Config not found using default:" + key);
            } catch (Exception ex) {
            }
            return defaultValue;
        }
        return val;

    }

    /**
     * Gets the value of the key as long type, in case of any exception in
     * conversion or property not found, returns -1
     *
     * @param key
     * @return
     */
    public static long getPropertyAsNumber(String key) {
        String propVal = prop.getProperty(key);
        if (propVal == null || propVal.equals("")) {
            //LogProperties.WriteLog("Function: GetPropertyAsNumber(?), Config not found:" + key);
            logger.info("Function: GetPropertyAsNumber(?), Config not found:" + key);
        }
        try {
            long longVal = Long.parseLong(propVal);
            return longVal;
        } catch (NumberFormatException nfe) {
            //LogProperties.WriteLog("Function: GetPropertyAsNumber(?), Config not found:" + key);
            return -1;
        }
    }

    /**
     * Gets the value of the key as int type, in case of any exception in
     * conversion or property not found, Returns -1 in case of any exception
     *
     * @param key
     * @return
     */
    public static int getPropertyAsInt(String key) {
        String propVal;
        try {
            propVal = prop.getProperty(key);
            int longVal = Integer.parseInt(propVal);
            return longVal;
        } catch (Exception nfe) {
            //LogProperties.WriteLog("Function: GetPropertyAsInt(?), Config not found:" + key);
            logger.info("Function: GetPropertyAsInt(?), Config not found:" + key);
            return -1;
        }
    }

    /**
     * Gets integer value from properties, returns defaultValue in case of any
     * exception
     *
     * @param key
     * @param defaultValue
     * @return integer value from properties, if property not found returns
     * default value
     */
    public static int getPropertyAsInt(String key, int defaultValue) {
        String propVal = "";
        try {
            propVal = prop.getProperty(key);
            int longVal = Integer.parseInt(propVal);
            return longVal;
        } catch (NumberFormatException nfe) {
            //LogProperties.WriteLog("Function: GetPropertyAsInt(?,?), Config not found using default:" + key);
            logger.info("Function: GetPropertyAsInt(?,?), Config not found using default:" + key);
            return defaultValue;
        }
    }

    /**
     * Gets the property as String array
     *
     * @param key
     * @return
     */
    public static List<String> getCSVPropertyAsStringArray(String key) {
        try {
            return Arrays.asList(prop.getProperty(key).split("\\s*,\\s*"));
        } catch (Exception ex) {
            //LogProperties.WriteLog("Function: GetPropertyAsArray(?), Config not found:" + key);
            logger.info("Function: GetPropertyAsArray(?), Config not found:" + key);
            return null;
        }
    }
}
