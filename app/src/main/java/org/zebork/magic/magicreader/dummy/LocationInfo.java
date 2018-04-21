package org.zebork.magic.magicreader.dummy;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.content.ContextCompat;

import java.io.IOException;
import java.util.List;

public class LocationInfo {
    // 纬度
    private double latitude = 0.0;
    // 经度
    private double longitude = 0.0;
    private static LocationManager locationManager;
    private static Location location;
    private static String provider;
    private Context context;

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }


    /**
     * 初始化
     */
    public LocationInfo(Context context) {

        this.context = context;
        //获取定位服务
        this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        //获取当前可用的位置控制器
        List<String> list = locationManager.getProviders(true);

        if (list.contains(LocationManager.GPS_PROVIDER)) {
            //是否为GPS位置控制器
            provider = LocationManager.GPS_PROVIDER;
        } else if (list.contains(LocationManager.NETWORK_PROVIDER)) {
            //是否为网络位置控制器
            provider = LocationManager.NETWORK_PROVIDER;

        } else {
            //Toast.makeText(context, "请检查网络或GPS是否打开", Toast.LENGTH_LONG).show();
            return;
        }
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        this.location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            //获取当前位置，这里只用到了经纬度
            this.longitude = location.getLongitude();
            this.latitude = location.getLatitude();
        } else {
            this.latitude = 0;
            this.longitude = 0;
        }
    }

    /**
     * 获取经纬度
     *
     *@return 返回经纬度信息
     */
    public String getLocation() {
        return "经度：" + this.getLongitude() + "\n纬度：" + this.getLatitude();
    }

    /**
     * 获取详细位置信息
     *
     * @return String detailedLocation
     */
    public String getDetailedLocation() {

        Geocoder geocoder = new Geocoder(context);
        StringBuilder stringBuilder = new StringBuilder();
        List<Address> addresses;
        stringBuilder.append(getLocation() + "\n");
        try {
            //根据经纬度获取地理位置信息
            if (location != null)
                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            else
                addresses = geocoder.getFromLocation(getLatitude(), getLongitude(), 1);

            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                    stringBuilder.append(address.getAddressLine(i)).append("\n");
                }
                stringBuilder.append(address.getCountryName()).append("_");//国家
                stringBuilder.append(address.getFeatureName()).append("_");//周边地址
                stringBuilder.append(address.getLocality()).append("_");//市
                stringBuilder.append(address.getPostalCode()).append("_");
                stringBuilder.append(address.getCountryCode()).append("_");//国家编码
                stringBuilder.append(address.getAdminArea()).append("_");//省份
                stringBuilder.append(address.getSubAdminArea()).append("_");
                stringBuilder.append(address.getThoroughfare()).append("_");//道路
                stringBuilder.append(address.getSubLocality()).append("_");//香洲区
                stringBuilder.append(address.getLatitude()).append("_");//经度
                stringBuilder.append(address.getLongitude());//维度

            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}