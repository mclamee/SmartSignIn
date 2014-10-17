package com.wicky.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.management.ManagementFactory;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * @author williamz<quiet_dog@163.com> 2014-08-13
 */
public class ConfigUtil {
    private static final String CONFIG_FILENAME = "/ssi_config.properties";
    private static final String CONFIG_PATH_USER_HOME = System.getProperty("user.home") + CONFIG_FILENAME;
    private static final String CONFIG_PATH_APP_DIR = System.getProperty("user.dir") + CONFIG_FILENAME;

    private static Properties CONFIG = new Properties();

    static {
        BufferedInputStream inStream = null;
        try {

            File configFile = new File(CONFIG_PATH_USER_HOME);
            if (!configFile.exists() || !configFile.isFile()) {
                configFile = new File(CONFIG_PATH_APP_DIR);
            }
            inStream = new BufferedInputStream(new FileInputStream(configFile));
            CONFIG.load(inStream);
            CONFIG.setProperty("configFilePath", configFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inStream != null) {
                try {
                    inStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.setProperty("PROFILE_HOME", CONFIG.getProperty("configFilePath"));
        System.setProperty("PID", ManagementFactory.getRuntimeMXBean().getName());
    }

    // public static void main(String[] args) {
    // System.out.println(System.getProperty("user.home"));
    // System.out.println(System.getProperty("user.name"));
    // }

    private ConfigUtil() {
    }

    public static void init(){
    }
    
    public static String get(String key) {
        String property = CONFIG.getProperty(key);
        if (property == null) return "";
        return property;
    }

    public static Integer getInt(String key) {
        try {
            return Integer.valueOf(get(key));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static Double getDouble(String key) {
        try {
            return Double.valueOf(get(key));
        } catch (NumberFormatException e) {
            return 0.0d;
        }
    }

    public static void save() {
        OutputStream fos = null;
        try {
            fos = new FileOutputStream(CONFIG.getProperty("configFilePath"));
            CONFIG.store(fos, "Updated at " + new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void put(String key, String value) {
        CONFIG.put(key, value);
    }
}
