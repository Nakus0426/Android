package com.zhz_zsl.glutweather.utils;

import com.zhz_zsl.glutweather.model.Weather_model;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 解析空气质量
 */

public class ParsePm {
  // 获取  空气质量信息
    public Weather_model getpmInfomation(String jsonString) throws Exception {
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray resultJsonArray = jsonObject.getJSONArray("HeWeather5");
        JSONObject jsonObject1 = resultJsonArray.getJSONObject(0);
        JSONObject air = jsonObject1.getJSONObject("aqi");
        JSONObject city=air.getJSONObject("city");
        Weather_model airq = new Weather_model();
        airq.setAir(city.getString("qlty"));
        airq.setPm(city.getInt("pm25"));

        return airq;
    }

}
