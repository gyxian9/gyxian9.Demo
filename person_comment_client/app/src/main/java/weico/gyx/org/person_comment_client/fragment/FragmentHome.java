package weico.gyx.org.person_comment_client.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnItemClick;

import weico.gyx.org.person_comment_client.R;
import weico.gyx.org.person_comment_client.activity.AllCategoryActivity;
import weico.gyx.org.person_comment_client.activity.CityActivity;
import weico.gyx.org.person_comment_client.util.MyUtils;
import weico.gyx.org.person_comment_client.util.ShareUtils;


public class FragmentHome extends Fragment{
    //左上角的标签
    @ViewInject(R.id.index_top_city)
    private TextView topCity ;
    //白色区域
    @ViewInject(R.id.home_gridview)
    private GridView homeGirdView;

    private String cityName ;
    private String oldCity;


    private LocationClient mLocationClient = null;
    private BDLocationListener myListener = new MyLocationListener();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,null);
        ViewUtils.inject(this, view);
        initLocation();
        homeGirdView.setAdapter(new GridAdapter());
        return view;
    }

    /**
     * GridAdapter begin
     */
    private class GridAdapter extends BaseAdapter{

        public GridAdapter(){
        }

        @Override
        public int getCount() {
            return MyUtils.gridSort.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MyViewHolder myViewHolder = null;

            if(convertView == null){
                myViewHolder = new MyViewHolder();
                convertView = LayoutInflater.from(getActivity())
                        .inflate(R.layout.activity_nav_item,null);
                ViewUtils.inject(myViewHolder,convertView);
                convertView.setTag(myViewHolder);
            }else{
                myViewHolder = (MyViewHolder) convertView.getTag();
            }

            myViewHolder.textView.setText(MyUtils.gridSort[position]);
            myViewHolder.imageView.setImageResource(MyUtils.gridSortImages[position]);

            //当选中最后一项“全部”的情况下
            if(position == MyUtils.gridSort.length - 1){
                myViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getActivity(),AllCategoryActivity.class));
                    }
                });
            }

            return convertView;
        }
        class MyViewHolder {
            @ViewInject(R.id.home_nav_item_desc)
            public TextView textView;
            @ViewInject(R.id.home_nav_item_image)
            public ImageView imageView;
        }
    }
    /**
     * GridAdapter end
     */

    @OnItemClick(R.id.home_gridview)
    public void onItemClick(AdapterView<?> parent, View view,
                            int position, long id){
        if(!(position == MyUtils.gridSort.length - 1))
            Toast.makeText(getActivity().getApplication()
                ,MyUtils.gridSort[position]
                ,Toast.LENGTH_SHORT).show();

    }

    //顶部栏点击监听
    @OnClick(R.id.index_top_city)
    public void onClick(View view){
        switch(view.getId()){
            case R.id.index_top_city:
                Intent i = new Intent(getActivity(),CityActivity.class);
                i.putExtra("cityName",cityName);
                startActivityForResult(i, MyUtils.RequestCityCode);

                ShareUtils.putOldCity(getActivity().getApplication()
                        , cityName);
                break;
        }
    }

    //处理返回值的结果
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("TAG","onActivityResult --1");
        if(requestCode == MyUtils.RequestCityCode && resultCode == Activity.RESULT_OK){
            Log.e("TAG","onActivityResult --2");
            cityName = data.getStringExtra(ShareUtils.CITY_NAME);
            Log.e("TAG","onActivityResult --3 + "+ cityName);
            oldCity = ShareUtils.getOldCity(getActivity().getApplication());
            Log.e("TAG","onActivityResult --4 + " + oldCity);
            if(!cityName.equals(oldCity)) {
                Log.e("TAG","onActivityResult --5");
                topCity.setText(cityName);
            }
            ShareUtils.putOldCity(getActivity().getApplication()
                    ,cityName);
        }

    }


    @Override
    public void onPause() {
        super.onPause();
        Log.e("TAG","onPause");
        mLocationClient.stop();
    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        Log.e("TAG","onDestory");
//        mLocationClient.stop();
//    }


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
                topCity.setText(cityName);
        }
    }
    private void initLocation(){
        mLocationClient = new LocationClient(getActivity().getApplicationContext());  //声明LocationClient类
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
        Log.e("TAG","loc ---- frag");
    }



}
