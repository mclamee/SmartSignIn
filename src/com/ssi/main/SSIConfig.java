package com.ssi.main;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.management.ManagementFactory;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import com.ssi.util.StringUtil;

/**
 * @author williamz<quiet_dog@163.com> 2014-08-13
 */
public class SSIConfig {
	
    private static final String CONFIG_FILENAME = "/application.properties";
    private static final String CONFIG_PATH_USER_HOME = System.getProperty("user.home") + "/.ssi";
    private static final String CONFIG_PATH_APP_DIR = System.getProperty("user.dir") + "/conf";
    
    private static SortableProperties CONFIG = new SortableProperties();
    private static Logger LOG;
    
    static {
        BufferedInputStream inStream = null;
        try {
        	
            File configFile = new File(CONFIG_PATH_USER_HOME + CONFIG_FILENAME);
            if (!configFile.exists() || !configFile.isFile()) {
                configFile = new File(CONFIG_PATH_APP_DIR + CONFIG_FILENAME);
                if (!configFile.exists() || !configFile.isFile()) {
                	// create folder
                	new File(CONFIG_PATH_USER_HOME).mkdirs();
                	// create file
                	configFile = new File(CONFIG_PATH_USER_HOME + CONFIG_FILENAME);
                	configFile.createNewFile();
                }
            }
            try {
            	inStream = new BufferedInputStream(new FileInputStream(configFile));
			} catch (FileNotFoundException e) {
				configFile.delete();
				configFile.createNewFile();
				inStream = new BufferedInputStream(new FileInputStream(new File(configFile.getAbsolutePath())));
			}
            CONFIG.load(inStream);
            CONFIG.setProperty("system.profileHome", configFile.getParent());
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
        System.setProperty("PROFILE_HOME", CONFIG.getProperty("system.profileHome"));
        System.setProperty("PID", ManagementFactory.getRuntimeMXBean().getName());
        LOG = Logger.getLogger(SSIConfig.class);
    }

    // public static void main(String[] args) {
    // System.out.println(System.getProperty("user.home"));
    // System.out.println(System.getProperty("user.name"));
    // }

    private SSIConfig() {
    }

	public static void init(){
		checkDefaultValues();
    	LOG.info("------------------------- CONFIGURATION STANDBY -------------------------");
    }
	
    private static void checkDefaultValues() {
    	
    	String font = SSIConfig.get("system.font");
    	if(StringUtil.isEmpty(font)){
    		SSIConfig.put("system.font", "微软雅黑");
    	}
        
        String language = SSIConfig.get("system.locale.language");
        if(StringUtil.isEmpty(language)){
            SSIConfig.put("system.locale.language", "zh");
        }
        
        String country = SSIConfig.get("system.locale.country");
        if(StringUtil.isEmpty(country)){
            SSIConfig.put("system.locale.country", "CN");
        }
        
        String saveIntervalMinute = SSIConfig.get("system.saveIntervalMinute");
        if(StringUtil.isEmpty(saveIntervalMinute)){
            SSIConfig.put("system.saveIntervalMinute", "1");
        }
        
    	String debugAuth = SSIConfig.get("debug.authorization");
    	if(StringUtil.isEmpty(debugAuth)){
    		SSIConfig.put("debug.authorization", "off");
    	}
    	
    	String debuFullSc = SSIConfig.get("debug.fullscreen");
    	if(StringUtil.isEmpty(debuFullSc)){
    		SSIConfig.put("debug.fullscreen", "on");
    	}
    	
    	String debugScanner = SSIConfig.get("debug.scanner");
    	if(StringUtil.isEmpty(debugScanner)){
    	    SSIConfig.put("debug.scanner", "off");
    	}

    	String recordDataFileName = SSIConfig.get("RecordView.dataFileName");
    	if(StringUtil.isEmpty(recordDataFileName)){
    		SSIConfig.put("RecordView.dataFileName", "/rv_tbl.db");
    	}
    	
    	String staffDataFileName = SSIConfig.get("StaffView.dataFileName");
    	if(StringUtil.isEmpty(staffDataFileName)){
    		SSIConfig.put("StaffView.dataFileName", "/sv_tbl.db");
    	}
    	
    	String startupView = SSIConfig.get("system.startup.view");
    	if(StringUtil.isEmpty(startupView)){
    		SSIConfig.put("system.startup.view", "MainView");
    	}
//    	
//    	String staffDataFileName = SSIConfig.get("StaffView.dataFileName");
//    	if(StringUtil.isEmpty(staffDataFileName)){
//    		SSIConfig.put("StaffView.dataFileName", "/sv_tbl.db");
//    	}
    	
    	SSIConfig.save();
	}
    
    public static String get(String key) {
        String property = CONFIG.getProperty(key);
        if (property == null) return "";
        return property;
    }

    public static String getFileName(String key) {
       String fileName = get(key);
       if(!StringUtil.isEmpty(fileName) && !fileName.contains(":") && !fileName.startsWith("/")){
    	   return "/" + fileName;
       }
       return fileName;
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
    
	public static Boolean getBoolean(String key) {
        String property = CONFIG.getProperty(key);
        if (property == null) return false;
        if(property.equalsIgnoreCase("on") || property.equalsIgnoreCase("yes") || property.equalsIgnoreCase("true")){
        	return true;
        }
        if(property.equalsIgnoreCase("off") || property.equalsIgnoreCase("no") || property.equalsIgnoreCase("false")){
        	return false;
        }
		return false;
	}
	
    public static void save() {
        OutputStream fos = null;
        try {
            fos = new FileOutputStream(CONFIG.getProperty("system.profileHome") + CONFIG_FILENAME);
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
