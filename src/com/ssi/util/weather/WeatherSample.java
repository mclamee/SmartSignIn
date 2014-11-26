package com.ssi.util.weather;

import java.util.List;

public class WeatherSample {

    /**
     * 一个简单的应用
     * 
     * @param args
     */
    public static void main(String[] args) {
        // 获取成都的天气预报信息。
        String city = "成都";
        System.out.println(city + "未来7天天气预报信息：");
        List<WeatherReport> listReport = new WeatherUtil()
                .getWeatherReports(city);
        if (listReport.size() < 1) {
            System.out.println("没有找到 " + city + " 的天气预报。");
        } else {
            for (WeatherReport report : listReport) {
                System.out.println(report);
            }
        }
    }

}
