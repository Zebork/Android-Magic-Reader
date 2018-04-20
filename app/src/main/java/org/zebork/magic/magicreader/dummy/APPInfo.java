package org.zebork.magic.magicreader.dummy;

import android.graphics.drawable.Drawable;

import java.text.SimpleDateFormat;
import java.util.Date;

public class APPInfo {
    //应用名
    private String appLabel;
    //图标
    private Drawable appIcon;
    //包名
    private String pkgName;
    //大小
    private String appSize;
    //更新时间
    private long updateDate;

    //构造方法
    public APPInfo(){

    }

    public String getAppLabel() {
        return appLabel;
    }

    public void setAppLabel(String appLabel) {
        this.appLabel = appLabel;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    public String getPkgName() {
        return pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    public String getAppSize() {
        return appSize;
    }

    public void setAppSize(String appSize) {
        this.appSize = appSize;
    }

    public String getUpdateDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        Date date = new Date(updateDate);
        return sdf.format(date);
    }

    public void setUpdateDate(long updateDate) {
        this.updateDate = updateDate;
    }
}
