package com.wkz.share.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * 视图工具类,设置某个view的padding/margin
 *
 * @author Administrator
 * @date 2018/3/28
 */

public class ViewUtils {

    /**
     * 设置某个View的padding
     *
     * @param view   需要设置的view
     * @param isDp   需要设置的数值是否为DP
     * @param left   左边距
     * @param top    上边距
     * @param right  右边距
     * @param bottom 下边距
     */
    public static void setViewPadding(View view, boolean isDp, float left, float top, float right, float bottom) {
        if (view == null) {
            return;
        }

        int leftPx;
        int rightPx;
        int topPx;
        int bottomPx;

        if (isDp) {
            //根据DP与PX转换计算值
            leftPx = dp2px(view.getContext(), left);
            topPx = dp2px(view.getContext(), top);
            rightPx = dp2px(view.getContext(), right);
            bottomPx = dp2px(view.getContext(), bottom);
        } else {
            leftPx = (int) left;
            rightPx = (int) right;
            topPx = (int) top;
            bottomPx = (int) bottom;
        }

        //设置padding
        view.setPadding(leftPx, topPx, rightPx, bottomPx);
    }

    /**
     * 设置某个View的margin
     *
     * @param view   需要设置的view
     * @param isDp   需要设置的数值是否为DP
     * @param left   左边距
     * @param top    上边距
     * @param right  右边距
     * @param bottom 下边距
     */
    public static void setViewMargin(View view, boolean isDp, float left, float top, float right, float bottom) {
        if (view == null) {
            return;
        }

        int leftPx;
        int rightPx;
        int topPx;
        int bottomPx;

        ViewGroup.LayoutParams params = view.getLayoutParams();
        ViewGroup.MarginLayoutParams marginParams;
        //获取view的margin设置参数
        if (params instanceof ViewGroup.MarginLayoutParams) {
            marginParams = (ViewGroup.MarginLayoutParams) params;
        } else {
            //不存在时创建一个新的参数
            marginParams = new ViewGroup.MarginLayoutParams(params);
        }

        if (isDp) {
            //根据DP与PX转换计算值
            leftPx = dp2px(view.getContext(), left);
            topPx = dp2px(view.getContext(), top);
            rightPx = dp2px(view.getContext(), right);
            bottomPx = dp2px(view.getContext(), bottom);
        } else {
            leftPx = (int) left;
            rightPx = (int) right;
            topPx = (int) top;
            bottomPx = (int) bottom;
        }
        //设置margin
        marginParams.setMargins(leftPx, topPx, rightPx, bottomPx);
        view.setLayoutParams(marginParams);
    }

    /**
     * dp转px
     *
     * @param dpValue dp值
     * @return px值
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getApplicationContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
