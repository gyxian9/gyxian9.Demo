package weico.gyx.org.person_comment_client.activity;

import android.app.Activity;

public class NearByMapActivity extends Activity
        {

//
//    @ViewInject(R.id.search_mymap)
//    private MapView mapView;
//    private AMap amap;
//
//    private double longitude;//经度
//    private double latitude;//维度
//
//    private LocationManagerProxy managerProxy = null;
//    private OnLocationChangedListener mListener;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_near_by_map);
//        ViewUtils.inject(this);
//        mapView.onCreate(savedInstanceState);
//
//        if(amap == null){
//            amap = mapView.getMap();
//            amap.setLocationSource(this);
//            amap.setMyLocationEnabled(true);
//        }
//    }
//
//    @OnClick({R.id.search_back,R.id.search_refresh})
//    private void onclike(View view){
//        switch(view.getId()){
//            case R.id.search_back:
//                finish();
//            break;
//            case R.id.search_refresh:
////                refreshMap(String.valueOf(longitude),String.valueOf(latitude),"1000");
//            break;
//        }
//    }
//    // 按照定位的地址和搜索半径加载周边的数据
//    private void refreshMap(String mLongitude,String mLatitude,String radius) {
//        RequestParams params = new RequestParams();
//        params.addQueryStringParameter("longitude",mLongitude);
//        params.addQueryStringParameter("latitude",mLatitude);
//        params.addQueryStringParameter("radius",radius);
//
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        mapView.onDestroy();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        mapView.onPause();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        mapView.onResume();
//    }
//
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        mapView.onSaveInstanceState(outState);
//    }
//
//        @Override
//        public void onLocationChanged(AMapLocation aMapLocation) {
//            if(aMapLocation != null){
//                longitude = aMapLocation.getLongitude();
//                latitude = aMapLocation.getLatitude();
//                Log.e("TAG", "当前的经度和纬度是：" + longitude + "," + latitude);
//                mListener.onLocationChanged(aMapLocation);
//            }
//        }
//        //回传本地定位
//        @Override
//        public void onLocationChanged(Location location) {}
//        //当状态变化的时候
//        @Override
//        public void onStatusChanged(String provider, int status, Bundle extras) {}
//        //提供者可用的时候
//        @Override
//        public void onProviderEnabled(String provider) {}
//        //提供者不可用的时候
//        @Override
//        public void onProviderDisabled(String provider) {}
//
//        @Override
//        public void activate(OnLocationChangedListener onLocationChangedListener) {
//            if(managerProxy == null){
//                mListener = onLocationChangedListener;
//                managerProxy = LocationManagerProxy.getInstance(this);
//                managerProxy.requestLocationData(
//                        LocationProviderProxy.AMapNetwork,5000,10,this);
//            }
//        }
//
//        @Override
//        public void deactivate() {}

}
