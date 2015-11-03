package weico.gyx.org.person_comment_client.util;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

/**
 * Created by gyx on 2015/11/2.
 */
public class LocationUtils {

    public static LocationClient mLocationClient = null;
    public static BDLocationListener myListener = new MyLocationListener();
    static String cityName = "";
    public static Context mContext;

    public LocationUtils(Context mContext){
        this.mContext = mContext;
    }

    public static class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null){
                return;
            }

            if (location.getLocType() == 161) {
                ShareUtils.putOldCity(mContext.getApplicationContext()
                        ,location.getCity());
            }else{
                ShareUtils.putOldCity(mContext.getApplicationContext()
                        ,"正在定位...");
            }
        }
    }
    public static void initLocation(){
        mLocationClient = new LocationClient(mContext.getApplicationContext());  //声明LocationClient类
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

        ShareUtils.putFirstOpen(mContext.getApplicationContext(),true);
    }

    public static String getCityName() {
        return cityName;

    }


    public static void stopClient(){
        mLocationClient.stop();
    }
}
