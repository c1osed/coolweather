package android.coolweather.com.coolweather.util;

import android.coolweather.com.coolweather.gson.Weather;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2018/5/20 0020.
 */

public class GsonUtil {
    public static Weather handleweatherResponse(String response){
        try {
            JSONObject object = new JSONObject( response );
            JSONArray array = object.getJSONArray( "HeWeather" );
            String m = array.getJSONObject( 0 ).toString();
            Log.e( "错误信息错误信息",m );
            return new Gson().fromJson( m,Weather.class );



        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }

}
