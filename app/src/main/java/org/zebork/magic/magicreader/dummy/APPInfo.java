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
    /**
     * 系统应用标记
     * -2 非系统应用
     * -1 系统应用升级后成为第三方应用
     * 1 系统应用
     */
    private int flag;
    /**
     * 安装位置
     * True 在SD卡上
     * False 不在SD卡上
     */
    private boolean inSDCard;

    //构造方法
    public APPInfo(){
        inSDCard = false;
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

    public String getFlag() {
        if (flag > 0)
            return "True";
        return "False";
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String isInSDCard() {
        if (inSDCard)
            return "SD Card";
        return "System Default";
    }

    public void setInSDCard(boolean inSDCard) {
        this.inSDCard = inSDCard;
    }
}
