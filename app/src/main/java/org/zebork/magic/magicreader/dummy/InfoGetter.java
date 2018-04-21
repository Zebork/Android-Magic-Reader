package org.zebork.magic.magicreader.dummy;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


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

        // 此处易报 NoSuchFileException
        String curFreqCPU = "N/A";
        String maxFreqCPU = "N/A";
        try {
            FileReader fr = new FileReader("/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq");
            BufferedReader br = new BufferedReader(fr);
            String text = br.readLine();
            curFreqCPU = "CPU当前频率：" + text.trim() + "KHz\n";
            br.close();
            fr.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            curFreqCPU = "";
        } catch (IOException e) {
            e.printStackTrace();
            curFreqCPU = "";
        }
        try {
            FileReader fr = new FileReader("/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq");
            BufferedReader br = new BufferedReader(fr);
            String text = br.readLine();
            maxFreqCPU = "CPU最大频率：" + text.trim() + "KHz\n";
            br.close();
            fr.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            maxFreqCPU = "";
        } catch (IOException e) {
            e.printStackTrace();
            maxFreqCPU = "";
        }

        String company = "系统品牌: " + Build.BRAND;
        String device = "系统参数:" + Build.DEVICE;

        // 其他参数太多，不一一使用变量了，一个string得了
        //BOARD 主板
        String phoneInfo = "主板: " + Build.BOARD;
        phoneInfo += "\nBOOTLOADER: " + Build.BOOTLOADER;
        //DISPLAY Rom的名字 例如 Flyme 1.1.2（魅族rom） &nbsp;JWR66V（Android nexus系列原生4.3rom）
        phoneInfo += "\nRom版本: " + Build.DISPLAY;
        //指纹
        phoneInfo += "\n指纹: " + Build.FINGERPRINT;
        //HARDWARE 硬件
        phoneInfo += "\nHARDWARE: " + Build.HARDWARE;
        phoneInfo += "\nHOST: " + Build.HOST;
        phoneInfo += "\nID: " + Build.ID;
        //MANUFACTURER 生产厂家
        phoneInfo += "\n生产厂家: " + Build.MANUFACTURER;
        //MODEL 机型
        // phoneInfo += "\n机型: " + Build.MODEL;         // 与前面重复
        phoneInfo += "\nPRODUCT: " + Build.PRODUCT;
        phoneInfo += "\nRADITAGSO: " + Build.TAGS;
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        Date date = new Date(Build.TIME);
        phoneInfo += "\n编译时间: " + sdf.format(date);
        phoneInfo += "\n类型: " + Build.TYPE;
        phoneInfo += "\n用户: " + Build.USER;
        //VERSION.RELEASE 固件版本
        phoneInfo += "\n系统版本: " + Build.VERSION.RELEASE;
        phoneInfo += "\nCODENAME: " + Build.VERSION.CODENAME;
        //VERSION.INCREMENTAL 基带版本
        phoneInfo += "\n基带版本: " + Build.VERSION.INCREMENTAL;
        //VERSION.SDK SDK版本
        phoneInfo += "\nSDK版本: " + Build.VERSION.SDK_INT;

        return model + "\n" + api_builder.toString() + "\n" + maxFreqCPU + curFreqCPU
                + company + "\n" + device + "\n" + phoneInfo + "\n";

    }

    /**
     * 获取IMEI号，IESI号，手机型号
     */
    public static String getPhoneInfo(Context ctx) {
        String imi = null;
        try {
            TelephonyManager mTm = (TelephonyManager) ctx.getSystemService(ctx.TELEPHONY_SERVICE);
            String imei = mTm.getDeviceId();
            String imsi = mTm.getSubscriberId();
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
            imi = "手机IMEI号：" + imei + "\n手机IESI号：" + imsi + "\n手机号码：" + numer
                    + "\nSIM ICCID：" + sim + "\n运营商：" + imsi_o;
        } catch (SecurityException e) {
            imi = "";
        }

        return imi + "\n";
    }

    /**
     * 获取wifi信息（需要wifi权限）、屏幕分辨率
     * @return MacAddr + dis (height * width)
     */
    public static String getMacAddress(Context ctx){
        String info = "";
        WifiManager wifiManager = (WifiManager) ctx.getSystemService(ctx.WIFI_SERVICE);
        if (wifiManager.isWifiEnabled())
            info += "WiFi状态：Enabled\n";
        else
            info += "WiFi状态：Unable\n";
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        info = "手机macAdd: " + wifiInfo.getMacAddress() + "\n";
        info += "路由Addr：" + wifiInfo.getBSSID() + "\n";
        info += "WiFi SSID：" + wifiInfo.getSSID() + "\n";
        int ip = wifiInfo.getIpAddress();
        StringBuilder sb = new StringBuilder();
        sb.append(ip & 0xFF).append(".");
        sb.append((ip >> 8) & 0xFF).append(".");
        sb.append((ip >> 16) & 0xFF).append(".");
        sb.append((ip >> 24) & 0xFF);
        info += "IP地址：" + sb.toString() + "\n";

        DisplayMetrics mDisplayMetrics = ctx.getResources().getDisplayMetrics();
        int width = mDisplayMetrics.widthPixels;
        int height = mDisplayMetrics.heightPixels;
        float density = mDisplayMetrics.density;
        String dis = "";
        dis = "屏幕分辨率：" + String.valueOf(height) + "*" + String.valueOf(width) + "\n";

        return  info + dis;
    }

    /**
     * 获取网络状态
     * @param ctx 需要ACCESS_NETWORK_STATE权限
     * @return 网络状态（2g/3g/4g/unknown
     */
    public static String getNetworkInfo(Context ctx) {
        String type = "unknown";
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();

        if (info == null) {
            type = "Unknown";
            ;
        } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {
            type = "WiFi";
        } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
            int subType = info.getSubtype();
            if (subType == TelephonyManager.NETWORK_TYPE_CDMA || subType == TelephonyManager.NETWORK_TYPE_GPRS
                    || subType == TelephonyManager.NETWORK_TYPE_EDGE) {
                type = "2G";
            } else if (subType == TelephonyManager.NETWORK_TYPE_UMTS || subType == TelephonyManager.NETWORK_TYPE_HSDPA
                    || subType == TelephonyManager.NETWORK_TYPE_EVDO_A
                    || subType == TelephonyManager.NETWORK_TYPE_EVDO_0
                    || subType == TelephonyManager.NETWORK_TYPE_EVDO_B) {
                type = "3G";
            } else {// LTE是3g到4g的过渡，是3.9G的全球标准 if (subType ==
                // TelephonyManager.NETWORK_TYPE_LTE)
                type = "4G";
            }
        }
        return "数据网络状态：" + type + "\n";
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

    /**
     * 获取已安装非系统应用
     *
     * @return
     */
    public static List<APPInfo> getInstallApp(Context context) {
        List<APPInfo> appInfos = new ArrayList<>();
        PackageManager pm = context.getPackageManager(); // 获得PackageManager对象
        List<ApplicationInfo> listAppcations = pm
                .getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
        Collections.sort(listAppcations,
                new ApplicationInfo.DisplayNameComparator(pm));// 字典排序
        for (ApplicationInfo app : listAppcations) {
            if ((app.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {//非系统程序
                appInfos.add(getAppInfo(app, pm, -2));
            }//本来是系统程序，被用户手动更新后，该系统程序也成为第三方应用程序了
            else if ((app.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
                appInfos.add(getAppInfo(app, pm, -1));
            } else { // 系统应用
                appInfos.add(getAppInfo(app, pm, 1));
            }
        }
        return appInfos;
    }

    /**
     *  构造一个AppInfo对象 ，并赋值
     */
    private static APPInfo getAppInfo(ApplicationInfo app, PackageManager pm, int flag) {
        APPInfo appInfo = new APPInfo();
        appInfo.setAppLabel(pm.getApplicationLabel(app).toString());    // 应用名称
        appInfo.setAppIcon(app.loadIcon(pm));   // 应用icon
        appInfo.setPkgName(app.packageName);    // 应用包名，用来卸载
        appInfo.setFlag(flag);                  // 是否系统应用标记
        if ((app.flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) != 0) {
            appInfo.setInSDCard(true);
        }
        File file = new File(app.sourceDir);
        float size = file.length();
        DecimalFormat df = new DecimalFormat("#.00");
        appInfo.setAppSize(df.format(size / (1024 * 1024)) + "M");//应用大小，M单位，保留两位小数
        PackageInfo packageInfo = null;
        try {
            packageInfo = pm.getPackageInfo(app.packageName, 0);
            long lastUpdateTime = packageInfo.lastUpdateTime;//应用最近一次更新时间
            appInfo.setUpdateDate(lastUpdateTime);//将毫秒时间对比当前时间转换为多久以前
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appInfo;
    }

    /**
     * 获取位置信息
     */
    public static String getLocationInfo(Context ctx) {
        LocationInfo locationInfo = new LocationInfo(ctx);
        String location = locationInfo.getDetailedLocation();
        return location;
    }

    /**
     * 获取联系人信息
     */
    public static String getContactInfo(Context ctx) {
        ContactInfo contactInfo = new ContactInfo(ctx);
        String contact = null;
        try {
            contact = contactInfo.getContactInfo();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return contact;
    }

    /**
     * json 2 string
     *
     * @param jsonObject 联系人json信息
     * @return string 提取一丢丢出来

    public String json2String(JSONObject jsonObject) {


    }*/

    /*
    // 读指定路径的文件
    private static String read(String path) throws IOException {
        StringBuilder output = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(path));
        output.append(reader.readLine());
        for (String line = reader.readLine(); line != null; line = reader.readLine()) {
            output.append('\n').append(line);
        }
        reader.close();
        return output.toString().trim();
    }
    */

}
