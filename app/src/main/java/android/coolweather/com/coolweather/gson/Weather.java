package android.coolweather.com.coolweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2018/5/20 0020.
 */

public class Weather {

    public String status;
    public AQI aqi;
    public Basic basic;
    public Now now;
    public Suggestion suggestion;

    @SerializedName( "daily_forecast" )
    public List<Forecast> forecastList;

}
