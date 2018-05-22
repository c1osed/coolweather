package android.coolweather.com.coolweather;

import android.content.SharedPreferences;
import android.coolweather.com.coolweather.gson.Forecast;
import android.coolweather.com.coolweather.gson.Weather;
import android.coolweather.com.coolweather.util.GsonUtil;
import android.coolweather.com.coolweather.util.HttpUtil;
import android.coolweather.com.coolweather.util.Utility;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.internal.Util;

public class WeatherActivity extends AppCompatActivity {
    private ScrollView scrollView;
    private TextView city;
    private TextView time;
    private TextView Tdegree;
    private TextView Tweather;
    private LinearLayout forecastLinerLayot;
    private TextView aqi;
    private TextView pm;
    private TextView comfort_text;
    private TextView carwash_text;
    private TextView sport_text;
    private ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_weather );
        scrollView = findViewById( R.id.scrollview );
        city = findViewById( R.id.city_name );
        time = findViewById( R.id.time_name );
        Tdegree = findViewById( R.id.degree_text );
        Tweather = findViewById( R.id.weather_info );
        aqi = findViewById( R.id.AQI );
        pm = findViewById( R.id.pm );
        comfort_text = findViewById( R.id.comfort_text );
        carwash_text = findViewById(R.id.carwash_text);
        sport_text = findViewById( R.id.sport_text );
        forecastLinerLayot = findViewById( R.id.forcast_layout );
        imageView = findViewById( R.id.image1 );
        
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences( this );
         String content = sharedPreferences.getString( "weather",null);
         String picture = sharedPreferences.getString( "image",null );
         if(picture!=null){

             Glide.with( this ).load( picture ).into( imageView );
             
         }
         else{
             
             getimage();
             
         }
         if(content!=null){
             Weather weather = GsonUtil.handleweatherResponse( content );
             showWeather(weather);

         }else{

             String weatherId = getIntent().getStringExtra("weather_id");
             Log.e( "错误信息错误信息", weatherId);
             scrollView.setVisibility(View.INVISIBLE);
              requestWeather(weatherId);


         }
    }

    private void requestWeather(String weatherId) {
        String weatherUrl ="http://guolin.tech/api/weather?cityid="+weatherId+"&key=caeeb46eb9ff4cf292b67206f0314294";
        Log.e( "错误信息错误信息", weatherUrl);
        HttpUtil.sendOkHttpRequest( weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText( WeatherActivity.this,"获取天气信息失败",Toast.LENGTH_SHORT ).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String sw = response.body().string();
                Log.e( "错误信息错误信息", sw);
                final Weather weather = GsonUtil.handleweatherResponse( sw );
                runOnUiThread( new Runnable() {
                    @Override
                    public void run() {
                        if(weather!=null&&"ok".equals( weather.status )){
                           SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences( WeatherActivity.this ).edit();
                           editor.putString( "weather",sw );
                           editor.apply();
                           showWeather( weather );





                        }else {
                            Toast.makeText( WeatherActivity.this,"获取天气信息失败........",Toast.LENGTH_SHORT ).show();


                        }



                    }
                } );


            }
        } );







    }

    //此方法将解析到的数据以此放入到界面中
    private void showWeather(Weather weather) {
        String cityname = weather.basic.cityname;//城市名字
        String updatetime = weather.basic.update.updatetime.split( "" )[1];//显示时间
        String degree = weather.now.temperature+ "℃";//摄氏度
        String weatherinfo = weather.now.more.info;//当前天气状况
        city.setText( cityname );
        time.setText( updatetime );
        Tdegree.setText( degree );
        Tweather.setText( weatherinfo );
        Log.e( "错误信息错误信息错误信息", city.getText().toString() );
        Log.e( "错误信息错误信息错误信息", time.getText().toString() );
        Log.e( "错误信息错误信息错误信息", Tdegree.getText().toString() );
        Log.e( "错误信息错误信息错误信息", Tweather.getText().toString() );
       forecastLinerLayot.removeAllViews();
       for(Forecast forecast :weather.forecastList) {

           View view = LayoutInflater.from( this ).inflate( R.layout.forcecast_item, forecastLinerLayot, false );

           TextView riqi1 = view.findViewById( R.id.riqi );
           TextView gaikuang1 = view.findViewById( R.id.gaikuang );
           TextView big1 = view.findViewById( R.id.big );
           TextView min1 = view.findViewById( R.id.min );
           Log.e( "错误信息错误信息错误信息", riqi1.getText().toString() );

           riqi1.setText( forecast.date );
           Log.e( "错误信息错误信息错误信息", riqi1.getText().toString() );
           gaikuang1.setText( forecast.more.info );
           big1.setText( forecast.temperature.max );
           min1.setText( forecast.temperature.min );
           forecastLinerLayot.addView( view );

       }
           if(weather.aqi!=null){
               aqi.setText( weather.aqi.city.aqi );
               pm.setText( weather.aqi.city.pm25 );
           }
           Log.e( "错误嘻嘻",weather.suggestion.comfort.info );

           String qq = "舒适度"+weather.suggestion.comfort.info;
           String sss = "洗车指数:"+weather.suggestion.carWash.info;
           String mm= "运动:" + weather.suggestion.sport.info;

           comfort_text.setText(  qq);
        Log.e( "错误嘻嘻",weather.suggestion.carWash.info );
           carwash_text.setText(sss);
           sport_text.setText( mm);
        Log.e( "错误嘻嘻",weather.suggestion.sport.info );
           scrollView.setVisibility(View.VISIBLE);











    }


    public void getimage() {
        String url =  "http://guolin.tech/api/bing_pic";

        HttpUtil.sendOkHttpRequest( url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText( WeatherActivity.this,"返回图片失败",Toast.LENGTH_SHORT ).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String m = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences( WeatherActivity.this ).edit();
                editor.putString( "weather",m );
                editor.apply();
                runOnUiThread( new Runnable() {
                    @Override
                    public void run() {
                        Glide.with( WeatherActivity.this ).load( m ).into( imageView );
                    }
                } );



            }
        } );
    }
}
