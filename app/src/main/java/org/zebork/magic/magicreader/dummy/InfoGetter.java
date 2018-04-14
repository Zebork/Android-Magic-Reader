package org.zebork.magic.magicreader.dummy;

import android.os.Build;
import android.app.Activity;
import java.io.InputStream;
import java.io.IOException;

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
        return model + "\n" + api_builder.toString() + "\n" + company + "\n" + device + "\n";

    }

    public static String getCpuInfo() {
        String result = "";
        ProcessBuilder cmd;
        try {
//            String[] args = { "/system/bin/cat", "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq" };
            String[] args = {"/system/bin/cat", "/proc/cpuinfo"};
            cmd = new ProcessBuilder(args);
            Process process = cmd.start();
            InputStream in = process.getInputStream();
            byte[] re = new byte[24];
            while (in.read(re) != -1) {
                result = result + new String(re);
            }
            in.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            result = "N/A";
        }
        return result.trim();
    }
}
