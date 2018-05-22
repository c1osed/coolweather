package android.coolweather.com.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2018/5/18 0018.
 */

public class Basic {
    @SerializedName( "city" )
    public String cityname;
    @SerializedName( "id" )
    public String weatherid;

    public Update update;

  public   class Update{
        @SerializedName( "loc" )
        public String updatetime;



    }


}
