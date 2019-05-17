package com.wkz.share.share;

/**
 * 分享平台实体
 *
 * @author Administrator
 * @date 2019/5/15
 */
public class SharePlatformBean {

    private int iconRes;
    private String platformName;

    public int getIconRes() {
        return iconRes;
    }

    public SharePlatformBean setIconRes(int iconRes) {
        this.iconRes = iconRes;
        return this;
    }

    public String getPlatformName() {
        return platformName;
    }

    public SharePlatformBean setPlatformName(String platformName) {
        this.platformName = platformName;
        return this;
    }

    @Override
    public String toString() {
        return "SharePlatformBean{" +
                "iconRes=" + iconRes +
                ", platformName='" + platformName + '\'' +
                '}';
    }
}
