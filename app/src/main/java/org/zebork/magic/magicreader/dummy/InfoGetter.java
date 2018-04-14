package org.zebork.magic.magicreader.dummy;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.*;
import android.app.Activity;


public class InfoGetter extends Activity {


    public String getSystemInfo() {
        String model = "手机型号: " + Build.MODEL;
        String[] apis = (Build.SUPPORTED_ABIS);
        StringBuilder api_builder = new StringBuilder();
        api_builder.append("CPU架构: ");
        api_builder.append(apis[0]);
        for (int i = 1; (i < apis.length) && (apis[i] != null); i++) {
            api_builder.append(", " + apis[i]);
        }

        String company = "系统定制商: " + Build.BRAND;
        String device = "系统参数:" + Build.DEVICE;

//        try {
//            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
//            String imei = "IMEI: " + telephonyManager.getDeviceId();
//            return model + "\n" + api_builder.toString() + "\n" + company + "\n" + device + "\n" + imei;
//        } catch(SecurityException e){
//
//        }
        return model + "\n" + api_builder.toString() + "\n" + company + "\n" + device;

    }


}
