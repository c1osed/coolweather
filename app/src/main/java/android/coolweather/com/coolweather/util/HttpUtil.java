package android.coolweather.com.coolweather.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/5/14 0014.
 */

public class HttpUtil {
    public static void sendOkHttpRequest(String in,okhttp3.Callback callback){
        OkHttpClient ok = new OkHttpClient();
        Request request = new Request.Builder().url( in ).build();
        ok.newCall( request ).enqueue( callback );
    }


//这是用普通方法进行的延时操作，但是这个方法现在还是不能使用，因为这个方法中没有开启线程进行，调用的时候有可能使主线程阻塞

    public static String sendHttpRequest(String in) {
        HttpURLConnection connection = null;
        StringBuilder builder;
        try {
            URL url = new URL( in );
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout( 8000 );
            connection.setRequestMethod( "GET" );
            connection.setConnectTimeout( 8000 );
            connection.setDoInput( true );
            connection.setDoOutput( true );
            InputStream inputStream = connection.getInputStream();
            BufferedReader reader = new BufferedReader( new InputStreamReader( inputStream ) );
            builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append( line );

            }
            return builder.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
            }
            connection.disconnect();

        }
    return null;
    }

}
