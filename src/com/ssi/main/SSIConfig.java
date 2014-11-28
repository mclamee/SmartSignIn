package com.ssi.main;

import java.awt.GraphicsEnvironment;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.management.ManagementFactory;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.ssi.util.StringUtil;

/**
 * @author williamz<quiet_dog@163.com> 2014-08-13
 */
public class SSIConfig {
	
    private static final String CONFIG_FILENAME = "/application.properties";
    private static final String CONFIG_PATH_APP_DIR = System.getProperty("user.dir") + "/conf";
    
    private static SortableProperties CONFIG = new SortableProperties();
    private static Logger LOG;
    
    static {
        BufferedInputStream inStream = null;
        try {
            File configFile = new File(CONFIG_PATH_APP_DIR + CONFIG_FILENAME);
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
        System.setProperty("java.awt.im.style","on-the-spot");
        LOG = Logger.getLogger(SSIConfig.class);
    }

    private SSIConfig() {}

	public static void init(){
		checkDefaultValues();
    	LOG.info("------------------------- CONFIGURATION STANDBY -------------------------");
    }
	
    private static void checkDefaultValues() {
    	
        String weatherCity = SSIConfig.get("weather.city");
        if(StringUtil.isEmpty(weatherCity)){
            SSIConfig.put("weather.city", "成都");
        }
        
    	String font = SSIConfig.get("system.font");
    	if(StringUtil.isEmpty(font)){
            //获取系统中可用的字体的名字
            GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
            String[] fontName = e.getAvailableFontFamilyNames();
            List<String> asList = Arrays.asList(fontName);
            if(asList.contains("微软雅黑")){
            	SSIConfig.put("system.font", "微软雅黑");
            }else if(asList.contains("黑体")){
            	SSIConfig.put("system.font", "黑体");
            }else{
            	SSIConfig.put("system.font", "宋体");
            }
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
    	
        String bgImage = SSIConfig.get("system.startup.background");
        if(StringUtil.isEmpty(bgImage)){
            SSIConfig.put("system.startup.background", "res/img/index_bg.png");
        }
        
    	String verifyviews = SSIConfig.get("system.verifyviews");
    	if(StringUtil.isEmpty(verifyviews)){
    		SSIConfig.put("system.verifyviews", "no");
    	}
    	
    	String enableQr = SSIConfig.get("SetupView.scanner.code.QR");
    	if(StringUtil.isEmpty(enableQr)){
    		SSIConfig.put("SetupView.scanner.code.QR", "on");
    	}
    	String enableDm = SSIConfig.get("SetupView.scanner.code.DM");
    	if(StringUtil.isEmpty(enableDm)){
    		SSIConfig.put("SetupView.scanner.code.DM", "on");
    	}
    	String enableBar = SSIConfig.get("SetupView.scanner.code.Bar");
    	if(StringUtil.isEmpty(enableBar)){
    		SSIConfig.put("SetupView.scanner.code.Bar", "on");
    	}
    	String interval = SSIConfig.get("SetupView.scanner.interval");
    	if(StringUtil.isEmpty(interval)){
    		SSIConfig.put("SetupView.scanner.interval", "1000");
    	}
    	String ai = SSIConfig.get("SetupView.scanner.ai");
    	if(StringUtil.isEmpty(ai)){
    		SSIConfig.put("SetupView.scanner.ai", "yes");
    	}
    	String aitime = SSIConfig.get("SetupView.scanner.aitime");
    	if(StringUtil.isEmpty(aitime)){
    		SSIConfig.put("SetupView.scanner.aitime", "20");
    	}
    	String beep = SSIConfig.get("SetupView.scanner.beep");
    	if(StringUtil.isEmpty(beep)){
    		SSIConfig.put("SetupView.scanner.beep", "yes");
    	}
    	String beepTimes = SSIConfig.get("SetupView.scanner.beep.times");
    	if(StringUtil.isEmpty(beepTimes)){
    		SSIConfig.put("SetupView.scanner.beep.times", "1");
    	}
    	
		String emailAddress = SSIConfig.get("email.address");
		if(StringUtil.isEmpty(emailAddress)){
			SSIConfig.put("email.address", "47640225@qq.com");
		}
		String emailPwd = SSIConfig.get("email.password");
		if(StringUtil.isEmpty(emailPwd)){
			SSIConfig.put("email.password", "3yj(\")SSfr21");
		}
		String emailRecip = SSIConfig.get("email.recipients");
		if(StringUtil.isEmpty(emailRecip)){
			SSIConfig.put("email.recipients", "quiet_dog@163.com");
		}
		String emailSmtp = SSIConfig.get("email.smtp");
		if(StringUtil.isEmpty(emailSmtp)){
			SSIConfig.put("email.smtp", "smtp.qq.com");
		}
		String synthViceName = SSIConfig.get("synth.voiceName");
		if(StringUtil.isEmpty(synthViceName)){
			SSIConfig.put("synth.voiceName", "xiaoyan");
		}
		String synthSpeed = SSIConfig.get("synth.speed");
		if(StringUtil.isEmpty(synthSpeed)){
			SSIConfig.put("synth.speed", "70");
		}
		String synthRate = SSIConfig.get("synth.sampleRate");
		if(StringUtil.isEmpty(synthRate)){
			SSIConfig.put("synth.sampleRate", "80");
		}
		String synthVolume = SSIConfig.get("synth.volume");
		if(StringUtil.isEmpty(synthVolume)){
			SSIConfig.put("synth.volume", "50");
		}
		
		String recordTemplate = SSIConfig.get("RecordView.template");
		if(StringUtil.isEmpty(recordTemplate)){
		    SSIConfig.put("RecordView.template", "${姓名-1}${称呼}，您好，欢迎光临拓德公司！！");
		}
		
		String staffTemplate = SSIConfig.get("StaffView.template");
		if(StringUtil.isEmpty(staffTemplate)){
		    SSIConfig.put("StaffView.template", "${姓名-1}${职位}，欢迎您！现在时刻：${time}，${weather}");
		}
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
        if (property == null) return null;
        if(property.equalsIgnoreCase("on") || property.equalsIgnoreCase("yes") || property.equalsIgnoreCase("true")){
        	return Boolean.TRUE;
        }
        if(property.equalsIgnoreCase("off") || property.equalsIgnoreCase("no") || property.equalsIgnoreCase("false")){
        	return Boolean.FALSE;
        }
		return null;
	}
	
	public static boolean isBooleanValue(String key){
	    return getBoolean(key) != null;
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
