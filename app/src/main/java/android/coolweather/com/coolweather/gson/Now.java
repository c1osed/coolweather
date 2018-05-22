package android.coolweather.com.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2018/5/20 0020.
 */

public class Now {
    @SerializedName( "tmp" )
    public String  temperature;
    @SerializedName( "cond" )
    public  More more;
    public class More{
        @SerializedName( "tet" )
        public String info;
    }
}
