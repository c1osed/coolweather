package android.coolweather.com.coolweather;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.coolweather.com.coolweather.db.City;
import android.coolweather.com.coolweather.db.County;
import android.coolweather.com.coolweather.db.Province;
import android.coolweather.com.coolweather.util.HttpUtil;
import android.coolweather.com.coolweather.util.Utility;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/5/14 0014.
 */

public class ChooseFragment extends Fragment {
    private TextView textView;
    private Button button;
    private ListView listView;
    private ArrayAdapter arrayAdapter;
    private List datalist = new ArrayList(  ) ;
    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEl_CITY=1;
    public static final int LEVEL_COUNTY = 2;
    private ProgressDialog progressDialog;
    //省列表
    private List<Province> provincelist;
    //市列表
    private List<City> citylist;
    //县列表
    private List<County> countylist;
    //当前选中的省份
    private Province selectProvice;
    //当前选中的城市
    private City selectCity;
    //当前选中的县
    private County selectCounty;
    /**
     * 当前选中的级别
     */
    private int currentLevel;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.choose_area,container,false );
        textView = view.findViewById( R.id.title_text );
        button = view.findViewById( R.id.back_button );
        listView =view.findViewById( R.id.list_view );
        arrayAdapter = new ArrayAdapter( getContext(),android.R.layout.simple_list_item_1,datalist);
        Log.d( "MainA",datalist.toString());
        listView.setAdapter( arrayAdapter );
       // Toast.makeText( getContext(),"怎么回事",Toast.LENGTH_SHORT ).show();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated( savedInstanceState );
        queryProvice();
        listView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView <?> adapterView, View view, int position, long id) {
                if(currentLevel==LEVEL_PROVINCE){
                    selectProvice = provincelist.get( position);
                    queryCitys();

                }else if(currentLevel==LEVEl_CITY){
                    selectCity = citylist.get( position );
                    queryCounty();

                }

            }

        } );
        button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentLevel==LEVEl_CITY){
                   queryProvice();
                }
                else if(currentLevel==LEVEL_COUNTY){
                    queryCitys();

                }
            }


        } );


    }

    private void queryCounty() {
        textView.setText( selectCity.getCityName() );
        button.setVisibility( View.VISIBLE );
        countylist = DataSupport.where( "cityid = ?", String.valueOf( selectCity.getId() ) ).find( County.class );
        if(countylist.size()>0){
            datalist.clear();
            for(County m:countylist){
                datalist.add( m.getCountyName());

            }
            arrayAdapter.notifyDataSetChanged();
            listView.setSelection( 0 );
            currentLevel =LEVEL_COUNTY;

        }else {
            int provinceCode = selectProvice.getProvinceCode();
            int cityCode= selectCity.getCityCode();
            String address = "http://guolin.tech/api/china/" + provinceCode + "/" +cityCode;
            queryFromServer( address,"county" );

        }


    }

    private void queryCitys() {
        textView.setText( selectProvice.getProvinceName() );
        button.setVisibility( View.VISIBLE );
        //citylist = DataSupport.where(  "provinceid=?",String.valueOf(selectProvice.getId())).find(City.class);
        citylist=DataSupport.where( "provinceId=?", String.valueOf( selectProvice.getId() ) ).find( City.class );
        Log.e( "HHHHHH",String.valueOf(  citylist.size() ) );
        if(citylist.size()>0){
            Log.e( "HHHHHH",String.valueOf(  citylist.size() ) );
            datalist.clear();
            for(City m :citylist){
                datalist.add( m.getCityName() );
            }
            arrayAdapter.notifyDataSetChanged();
            listView.setSelection( 0 );
            currentLevel=LEVEl_CITY;

        }else {
            int code = selectProvice.getProvinceCode();

            String address = "http://guolin.tech/api/china/"+code;
            Log.e( "HHHH",address );
            //Toast.makeText( getContext(),"哈哈哈哈哈",Toast.LENGTH_SHORT ).show();
            queryFromServer( address, "city");
        }



    }
    private void queryProvice() {
        textView.setText( "中国" );
        button.setVisibility( View.GONE );
        provincelist = DataSupport.findAll( Province.class );
        Log.d( "Main",provincelist.toString() );
        if(provincelist.size()>0){
            datalist.clear();
          for(Province m : provincelist){
              datalist.add( m.getProvinceName() );
          }
            arrayAdapter.notifyDataSetChanged();
            listView.setSelection( 0 );
            currentLevel = LEVEL_PROVINCE;
        }else {
            String address = "http://guolin.tech/api/china";
            Log.v( "HHHH",address );
            queryFromServer(address,"provice");

        }


    }
    private void queryFromServer( String adress, final String type){
        showProgressDialg();
        HttpUtil.sendOkHttpRequest( adress, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                closeProgressDialog();
                Toast.makeText( getContext(),"加载失败......." ,Toast.LENGTH_SHORT).show();

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String rp =response.body().string();
                boolean r = false;
                if("provice".equals(type )){
                    r = Utility.handleProviceResponse( rp );

                } else if("city".equals( type )){
                    //Log.v( "Main", rp+selectProvice.getId() );
                    r = Utility.handleCityResponse(  rp,selectProvice.getId());
            Log.e("bm", "runnable线程： " + Thread.currentThread().getId()+ " name:"
        + Thread.currentThread().getName());
            Log.e( "MMMM" ,String.valueOf( r ));
                } else if("county".equals( type )){
                    r = Utility.handleCountyResponse( rp,selectCity.getId());
                    Log.e( "KKKKK", rp  );

                    Log.e( "KKKKK", String.valueOf(  selectCity.getId())  );

                }
                if(r){

                    getActivity().runOnUiThread( new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if("provice".equals( type )){queryProvice();}
                            else if("city".equals( type )){queryCitys();}
                            else if("county".equals( type )){queryCounty();}
                        }


                    } );


                }



            }
        } );



    }

    private void showProgressDialg() {
        if(progressDialog==null){
            progressDialog = new ProgressDialog( getActivity() );
            progressDialog.setMessage( "正在加载....." );
            progressDialog.setCanceledOnTouchOutside( false );
        }
        progressDialog.show();

    }
    private void closeProgressDialog() {
        if(progressDialog!=null){
            progressDialog.dismiss();
        }
    }


}
