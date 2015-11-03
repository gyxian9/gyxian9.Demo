package weico.gyx.org.person_comment_client.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnItemClick;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

import weico.gyx.org.person_comment_client.R;
import weico.gyx.org.person_comment_client.entity.City;
import weico.gyx.org.person_comment_client.util.ShareUtils;
import weico.gyx.org.person_comment_client.view.SideBar;

public class CityActivity extends Activity{

    @ViewInject(R.id.city_list)
    private ListView cityListView;
    private List<City> cityDatas = new ArrayList<City>();
    private MyAdapter mAdapter;

    @ViewInject(R.id.sidebar)
    private SideBar sideBar;

    @ViewInject(R.id.main_dialog)
    private TextView mDialog;

    private TextView oldCity;

    private final static String JSONFILENAME = "ChineseCityName.json";

    private String cityName;

    private LocationClient mLocationClient = null;
    private BDLocationListener myListener = new MyLocationListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        ViewUtils.inject(this);
        //添加sideBar布局
        View view = LayoutInflater.from(this).inflate(R.layout.activity_city_search,null);
        cityListView.addHeaderView(view);
        sideBar.setDialog(mDialog);

        initData();
        initLocation();
        oldCity = (TextView) view.findViewById(R.id.index_city_item_search_city);

//        Intent i = getIntent();
//        cityName = i.getStringExtra("cityName");

        mAdapter = new MyAdapter(cityDatas);
        cityListView.setAdapter(mAdapter);

//        oldCity.setText(cityName);

        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = mAdapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    cityListView.setSelection(position);
                }
            }
        });
        oldCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra(ShareUtils.CITY_NAME,oldCity.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }



    @OnClick({R.id.index_city_back,R.id.index_city_flushcity})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.index_city_back:
                finish();
                break;
            case R.id.index_city_flushcity:
                oldCity.setText("正在刷新...");
                try {
                    Thread.sleep(5000);

                    initLocation();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                break;
        }
    }

    @OnItemClick({R.id.city_list})
    public void onItemClick(AdapterView<?> parent,View view,
                            int position,long id){

        Intent intent = new Intent();
        TextView textView = (TextView) view.findViewById(R.id.city_list_item_name);
        intent.putExtra(ShareUtils.CITY_NAME, textView.getText().toString());

        //返回城市名
        setResult(RESULT_OK, intent);
        finish();
    }

    //获取City JSON数据
    public void initData(){
        Log.e("TAG", "initData start");
        String jsonString = readLocJsonData();
        JSONObject jsonObject;
        City c;

        try {

            jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("chineseCityName");
            Log.e("TAG", jsonArray.toString());

            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                String name = jsonObject.getString("name");
                String pinyin = jsonObject.getString("pinyin");
                c = new City(name,pinyin);
                cityDatas.add(c);
            }
            Collections.sort(cityDatas, comparator);

            Log.e("TAG", "initData end");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * a-z排序
     */
    Comparator comparator = new Comparator<City>() {
        @Override
        public int compare(City lhs, City rhs) {
            String a = lhs.getPinyin().substring(0, 1);
            String b = rhs.getPinyin().substring(0, 1);
            int flag = a.compareTo(b);
            if (flag == 0) {
                return a.compareTo(b);
            } else {
                return flag;
            }
        }
    };



    public String readLocJsonData(){
        Log.e("TAG","readLocJsonData start");
        String result = null;
        InputStream is = null;
        try {

            is = getResources().getAssets().open(JSONFILENAME);
            byte[] buffer=new byte[is.available()];
            is.read(buffer);
            result=new String(buffer,"UTF-8");
            Log.e("TAG",result.toString());
            Log.e("TAG","readLocJsonData end");
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

        //ListView的适配器
        public class MyAdapter extends BaseAdapter implements SectionIndexer{

            private List<City> listCityDatas;

            public MyAdapter(List<City> listDatas) {
                this.listCityDatas = listDatas;
            }

            @Override
            public int getCount() {
                return listCityDatas.size();
            }

            @Override
            public Object getItem(int position) {
                return listCityDatas.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                Holder holder;
                final City city = listCityDatas.get(position);

                if (convertView==null) {
                    holder = new Holder();
                    convertView = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.activity_city_item, null);
                    ViewUtils.inject(holder, convertView);

                    convertView.setTag(holder);

                }else{
                    holder = (Holder) convertView.getTag();
                }

                holder.cityName.setText(listCityDatas.get(position).getName());

                int section = getSectionForPosition(position);

                if (position == getPositionForSection(section)){
                    holder.keySort.setVisibility(View.VISIBLE);
                    holder.keySort.setText(getAlpha(city.getPinyin()));
                }else{
                    holder.keySort.setVisibility(View.GONE);
                }

                return convertView;
            }

            @Override
            public Object[] getSections() {
                return new Object[0];
            }

            @Override
            public int getPositionForSection(int section) {
                for (int i = 0; i < getCount(); i++) {
                    String s = listCityDatas.get(i).getPinyin();
                    char firstChar = s.toUpperCase().charAt(0);
                    if (section == firstChar){
                        return i;
                    }
                }
                return -1;
            }

            @Override
            public int getSectionForPosition(int i) {
                return listCityDatas.get(i).getPinyin().charAt(0);
            }
        }
        public class Holder{
            @ViewInject(R.id.city_list_item_sort)
            public TextView keySort;
            @ViewInject(R.id.city_list_item_name)
            public TextView cityName;
        }
    // 获得汉语拼音首字母
    private String getAlpha(String str) {

        if (str.equals("-")) {
            return "&";
        }
        if (str == null) {
            return "#";
        }
        if (str.trim().length() == 0) {
            return "#";
        }

        char c = str.trim().substring(0, 1).charAt(0);

        // 正则表达式，判断首字母是否是英文字母
        Pattern pattern = Pattern.compile("^[A-Za-z]+$");
        if (pattern.matcher(c + "").matches()) {
            return (c + "").toUpperCase();
        } else {
            return "#";
        }
    }

    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null){
                return;
            }

            if (location.getLocType() == 161) {
                cityName = location.getCity();
            }else{
                cityName = "正在定位...";
            }

            if (!cityName.equals(""))
                oldCity.setText(cityName);
        }
    }
    private void initLocation(){
        mLocationClient = new LocationClient(getApplicationContext());  //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);    //注册监听函数
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span=1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
    }
}
