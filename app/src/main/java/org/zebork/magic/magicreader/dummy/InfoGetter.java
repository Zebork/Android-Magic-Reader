package org.zebork.magic.magicreader.dummy;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;

import java.io.InputStream;
import java.io.IOException;



public class InfoGetter {

    private boolean isAc = false;

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

        return model + "\n" + api_builder.toString() + "\n" + company + "\n" + device + "\n";

    }


    /**
     * 获取IMEI号，IESI号，手机型号
     */
    public static String getInfo(Context ctx) {
        String imi = null;
        try {
            TelephonyManager mTm = (TelephonyManager) ctx.getSystemService(ctx.TELEPHONY_SERVICE);
            String imei = mTm.getDeviceId();
            String imsi = mTm.getSubscriberId();
            String mtype = android.os.Build.MODEL;      // 手机型号
            String mtyb = android.os.Build.BRAND;       // 手机品牌
            String numer = mTm.getLine1Number();        // 手机号码，有的可得，有的不可得
            String sim = mTm.getSimSerialNumber();      // SIM卡的ICCID
            String imsi_o = mTm.getSubscriberId();        // 运营商
            if (imsi_o != null) {
                if (imsi.startsWith("46000") || imsi.startsWith("46002") || imsi.startsWith("46007")) {
                    // 因为移动网络编号46000下的IMSI已经用完，所以虚拟了一个46002编号，134/159号段使用了此编号
                    imsi_o = "中国移动";
                } else if (imsi.startsWith("46001") || imsi.startsWith("46006")) {
                    imsi_o = "中国联通";
                } else if (imsi.startsWith("46003") || imsi.startsWith("46005") || imsi.startsWith("46011")) {
                    imsi_o = "中国电信";
                } else
                    imsi_o = imsi_o.substring(0, 5);
            }
            imi = "手机IMEI号：" + imei + "\n手机IESI号：" + imsi + "\n手机型号：" + mtype
                    + "\n手机品牌：" + mtyb + "\n手机号码：" + numer + "\nSIM ICCID：" + sim
                    + "\n运营商：" + imsi_o;
        } catch (SecurityException e) {
            imi = "";
        }

        return imi + "\n";
    }

    /**
     * 获取MAC地址（需要wifi权限）
     * @return MacAddr
     */
    public static String getMacAddress(Context ctx){
        String macAddr = "";
        WifiManager wifiManager = (WifiManager) ctx.getSystemService(ctx.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        macAddr = wifiInfo.getMacAddress();
        return "手机macAdd: " + macAddr + "\n";
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
