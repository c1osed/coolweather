package android.coolweather.com.coolweather.util;

import android.coolweather.com.coolweather.db.City;
import android.coolweather.com.coolweather.db.County;
import android.coolweather.com.coolweather.db.Province;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static org.litepal.LitePalApplication.getContext;

/**
 * Created by Administrator on 2018/5/14 0014.
 */

public class Utility {
    public static boolean handleProviceResponse(String response){

        /**
         * 解析和处理服务器返回的省级数据
         */
        if(!TextUtils.isEmpty( response )){
         try {
             JSONArray jsonArray = new JSONArray( response );
             for(int i = 0;i<jsonArray.length();i++){
                 JSONObject object = jsonArray.getJSONObject( i );
                 Province province = new Province();
                 province.setProvinceName(object.getString( "name" ));
                 province.setProvinceCode(object.getInt( "id" ));
                 province.save();

             }
             return true;

         } catch (JSONException e) {
             e.printStackTrace();
         }


     }
       return false;
    }
    /**
     * 解析和处理服务器返回的省级下的市级数据
     */
    public static boolean handleCityResponse(String response,int proviceid){
        if(!TextUtils.isEmpty( response )){
            try {
                Log.e("bm", "runnable线程： " );
                JSONArray jsonArray = new JSONArray( response );
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject object=jsonArray.getJSONObject( i );
                    City city = new City();
                    city.setCityName( object.getString( "name" ) );
                    city.setCityCode(object.getInt( "id" ));
                    city.setProvinceId( proviceid );
                    city.save();
                    Log.e("bm", "runnable线程： " +city.getCityName()+" name:");
                }
                return true;


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }


        return false;
    }

    /**
     * 解析和处理服务器返回的省级下的市级下的县级数据
     */

    public static boolean handleCountyResponse(String response,int cityid){
        if(!TextUtils.isEmpty( response )) {
            try {

                JSONArray array = new JSONArray( response );
                for (int i = 0;i<array.length();i++){
                    JSONObject object = array.getJSONObject( i );
                    County county  = new County();
                    county.setCityId( cityid );
                    county.setCountyName( object.getString( "name" ) );
                    county.setWeatherId( object.getString( "weather_id" ) );
                    county.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;

    }


}
