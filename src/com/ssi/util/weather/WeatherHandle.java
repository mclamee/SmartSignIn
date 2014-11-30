package com.ssi.util.weather;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeSet;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;

import com.ssi.main.SSIConfig;
import com.ssi.util.DataUtil;

public class WeatherHandle {
    public static Logger LOG = Logger.getLogger(WeatherHandle.class);
    
    private static WeatherHandle instance;
    public static void initialize(String city){
        if(instance == null){
            instance = new WeatherHandle(city);
        }
        instance.refreshData();
    }

    private TreeSet<WeatherReport> rptSet = new TreeSet<WeatherReport>();

    private Timer timer = new Timer();

	private File dataFile;

	private String city;
    
	private String getDataFileName() {
		return "/weather.db";
	}
	
    private WeatherHandle(String city){
    	this.city = city;
		String dataFileName = getDataFileName();
        String profilePath = SSIConfig.get("system.profileHome");
        dataFile = new File(profilePath, dataFileName);
        TreeSet<WeatherReport> savedSet = DataUtil.loadDataFromFile(dataFile);
        if(savedSet != null){
        	rptSet = savedSet;
        }
        int hrs6 = 6 * 60 * 60 * 60 * 1000;
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				instance.refreshData();
			}
		}, hrs6, hrs6);
    }

    private void refreshData() {
        LOG.info("获取" + city + "未来7天天气预报信息：");
        TreeSet<WeatherReport> weatherReports = new WeatherUtil().getWeatherReports(city);
        if (weatherReports.size() < 1) {
            LOG.error("没有找到 " + city + " 的天气预报。");
        } else {
            for (WeatherReport report : weatherReports) {
                LOG.debug(report);
            }
        }
        
        rptSet.addAll(weatherReports);
        // 移除超过30天的数据
        purge30DaysEarlier();

        // save to file
        DataUtil.saveDataToFile(dataFile, rptSet);
    }

    private void purge30DaysEarlier() {
        Date today = DateUtils.round(new Date(), Calendar.DAY_OF_MONTH);
        Date days30Earilier = DateUtils.addDays(today, -30);
        for (Iterator<WeatherReport> it = rptSet.iterator();it.hasNext();) {
            WeatherReport rpt = it.next();
            Date rptDate = rpt.getDate();
            if(rptDate.before(days30Earilier)){
                it.remove();
            }
        }
    }

    public static String getTodayWeather() {
        Date today = new Date();
        if(instance == null){
            throw new RuntimeException("Please initialize WeatherHandle first.");
        }
        if(instance.rptSet == null){
            return null;
        }
        for (Iterator<WeatherReport> it = instance.rptSet.iterator();it.hasNext();) {
            WeatherReport rpt = it.next();
            Date date = rpt.getDate();
            if(DateUtils.isSameDay(today, date)){
                String city = rpt.getCity();
                String datOrNight = rpt.getDayOrNight();
                String weather1 = rpt.getWeather();
                String temperature1 = rpt.getTemperature();
                
                WeatherReport nextRpt = it.next();
                Date nextDate = nextRpt.getDate();
                if(DateUtils.isSameDay(date, nextDate)){
                    // 如果有两条消息
                    if(datOrNight.equals("白天")){
                        String weather2 = nextRpt.getWeather();
                        String temperature2 = nextRpt.getTemperature();
                        
                        String weather = weather1.equals(weather2)?weather1:(weather1+"转"+weather2);
                        String temperature = temperature1.equals(temperature2)?(temperature1 + "℃"):(temperature1 + "℃~" + temperature2 + "℃");
                        
                        String rt = city + "今天" + weather + "，" + "气温" + temperature;
                        return rt;
                    }else{
                        String weather2 = nextRpt.getWeather();
                        String temperature2 = nextRpt.getTemperature();
                        
                        String weather = weather1.equals(weather2)?weather2:(weather2+"转"+weather1);
                        String temperature = temperature1.equals(temperature2)?(temperature2 + "℃"):(temperature2 + "℃~" + temperature1 + "℃");
                        
                        String rt = city + "今天" + weather + "，" + "气温" + temperature;
                        return rt;
                    }
                }else{
                    // 如果只有一条消息
                    String rt = city + "今天" + datOrNight + weather1 + "，" + "气温"+temperature1+"℃";
                    return rt;
                }
            }
        }
        return null;
    }

    public static void main(String[] args) {
        WeatherHandle.initialize(SSIConfig.get("weather.city"));
        LOG.debug("Init weather instance: " + WeatherHandle.getTodayWeather());
    }
    
}
