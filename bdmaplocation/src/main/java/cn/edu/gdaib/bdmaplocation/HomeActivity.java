package cn.edu.gdaib.bdmaplocation;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private Button buttonLocation = null;
    private TextView textViewInfo = null;
    private LocationClient locationClient = null;
    private BDLocationListener listener = new MyLocationListener();
    private String permissionInfo = null;
    private final int SDK_PERMISSION_REQUEST = 127;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();//初始化
    }

    private void initView() {
        buttonLocation = (Button)findViewById(R.id.home_btn_location);
        textViewInfo = (TextView)findViewById(R.id.home_tv_info);
        locationClient = new LocationClient(getApplicationContext());  //声明LocationClient类
        locationClient.registerLocationListener(listener);  //注册监听函数
        initLocation();  //初始化定位参数
        getPermission(); //申请运行时权限 Android M

        buttonLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationClient.start();  //开始定位
            }
        });

    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);  //高精度
        int span=1500;
        option.setScanSpan(span);  //仅定位一次，默认0，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);  //需要地址信息，默认不需要
        option.setOpenGps(true);  //使用gps，默认false
        option.setLocationNotify(true);  //当GPS有效时按照1S/1次频率输出GPS结果，默认false
        option.setIsNeedLocationDescribe(true);  //需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”，默认false
        option.setIsNeedLocationPoiList(true);  //需要POI结果，可以在BDLocation.getPoiList里得到，默认false
        option.setIgnoreKillProcess(true);  //默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);  //默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);  //过滤GPS仿真结果，默认false,需要
        option.setIsNeedAddress(true);  //设置是否需要地址信息，默认为无地址
        option.getAddrType();  //获取地址信息设置

        locationClient.setLocOption(option);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void getPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<String>();
            //定位权限为必须权限，用户如果禁止，则每次进入都会申请
            //定位精确位置
            if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
            //读写权限和电话状态权限非必要权限(建议授予)只会申请一次，用户同意或者禁止，只会弹一次
            //读写权限
            if (addPermission(permissions, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissionInfo += "Manifest.permission.WRITE_EXTERNAL_STORAGE Deny \n";
            }
            // 读取电话状态权限
            if (addPermission(permissions, Manifest.permission.READ_PHONE_STATE)) {
                permissionInfo += "Manifest.permission.READ_PHONE_STATE Deny \n";
            }
            if (permissions.size() > 0) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), SDK_PERMISSION_REQUEST);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean addPermission(ArrayList<String> permissionList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {  //如果应用没有获得对应权限,则添加到列表中,准备批量申请
            if (shouldShowRequestPermissionRationale(permission)) {
                return true;
            }else {
                permissionList.add(permission);
                return false;
            }
        }else {
            return true;
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            //Receive Location
            StringBuffer sb = new StringBuffer(256);
            sb.append("时间 : ");
            sb.append(bdLocation.getTime());
            sb.append("\n错误码 : ");
            sb.append(bdLocation.getLocType()); //获取定位返回错误码 public int getLocType ()
            sb.append("\n纬度 : ");
            sb.append(bdLocation.getLatitude());
            sb.append("\n经度 : ");
            sb.append(bdLocation.getLongitude());
            sb.append("\n半径 : ");
            sb.append(bdLocation.getRadius());

            if (bdLocation.getLocType() == BDLocation.TypeGpsLocation){// GPS定位结果
                sb.append("\n速度 : ");
                sb.append(bdLocation.getSpeed());// 单位：公里每小时
                sb.append("\n卫星 : ");
                sb.append(bdLocation.getSatelliteNumber());
                sb.append("\n海拔高度 : ");
                sb.append(bdLocation.getAltitude());// 单位：米
                sb.append("\n方向 : ");
                sb.append(bdLocation.getDirection());// 单位度
                sb.append("\n地址 : ");
                sb.append(bdLocation.getAddrStr());
                sb.append("\n描述 : ");
                sb.append("gps定位成功");
            } else if (bdLocation.getLocType() == BDLocation.TypeNetWorkLocation){// 网络定位结果
                sb.append("\n详细地址 : ");
                sb.append(bdLocation.getAddrStr());
                sb.append("\n运营商 : ");
                String operator  = "";
                if (bdLocation.getOperators() == 0) {
                    operator = "未知运营商";
                }else if (bdLocation.getOperators() == 1){
                    operator = "中国移动";
                }else if (bdLocation.getOperators() == 2){
                    operator = "中国联通";
                }else if (bdLocation.getOperators() == 3){
                    operator = "中国电信";
                }
                sb.append(operator);
                sb.append("\n描述 : ");
                sb.append("网络定位成功");
            } else if (bdLocation.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                sb.append("\n描述 : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
            } else if (bdLocation.getLocType() == BDLocation.TypeServerError) {
                sb.append("\n描述 : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (bdLocation.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\n描述 : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (bdLocation.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\n描述 : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            sb.append("\n位置描述 : ");
            sb.append(bdLocation.getLocationDescribe());// 位置语义化信息
            List<Poi> list = bdLocation.getPoiList();// POI数据
            if (list != null) {
                sb.append("\npoi_list size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }
            Log.i("BaiduLocationApi", sb.toString());
            if (!sb.toString().isEmpty()) {
                String position  = "当前位置:\n" + sb.toString();
                textViewInfo.setText(position);
            }
        }
    }
}
