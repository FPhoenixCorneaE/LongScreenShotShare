package com.wkz.share.share;

/**
 * 分享监听
 *
 * @author Administrator
 * @date 2019/5/15
 */
public interface OnShareListener {

    /**
     * 点击关闭按钮
     *
     * @param shareDialog 分享弹窗
     */
    void onClickCloseBtn(ShareDialog shareDialog);

    /**
     * 触摸空白区域
     *
     * @param shareDialog 分享弹窗
     */
    void onTouchOutSide(ShareDialog shareDialog);

    /**
     * 解散弹窗
     *
     * @param shareDialog 分享弹窗
     */
    void onDismiss(ShareDialog shareDialog);

    /**
     * 点击分享平台
     *
     * @param shareDialog   分享弹窗
     * @param holder        分享视图持有者
     * @param sharePlatform 分享平台{@link SharePlatform}
     */
    void onClickSharePlatform(ShareDialog shareDialog, SharePlatformAdapter.ViewHolder holder, int sharePlatform);
}
