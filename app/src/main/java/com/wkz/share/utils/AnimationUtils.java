package com.wkz.share.utils;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.OvershootInterpolator;

/**
 * 动画工具类
 *
 * @author Administrator
 * @date 2019/5/15
 */
public class AnimationUtils {

    /**
     * OvershootInterpolator 向前甩一定值后再回到原来位置
     *
     * @param view           targetView
     * @param translateValue The vertical position of this view relative to its top position,in pixels.
     * @param startDelay     The amount of the delay, in milliseconds
     */
    public static void startOvershootAnimation(final View view, final float translateValue, long startDelay) {
        view.setTranslationY(translateValue);
        view.setAlpha(0);

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(1, 0);
        valueAnimator.setDuration(1000);
        valueAnimator.setInterpolator(new OvershootInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                view.setTranslationY(value * translateValue);
                view.setAlpha(1 - value);
            }
        });
        valueAnimator.setStartDelay(startDelay);
        valueAnimator.start();
    }

    /**
     * 缩小
     *
     * @param targetView   目标视图
     * @param beforeScaleX 缩小前X方向范围
     * @param beforeScaleY 缩小前Y方向范围
     * @param afterScaleX  缩小后X方向范围
     * @param afterScaleY  缩小后Y方向范围
     * @param duration     持续时间
     */
    public static void zoomOut(View targetView, float beforeScaleX, float beforeScaleY, float afterScaleX, float afterScaleY, long duration) {
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(ObjectAnimator.ofFloat(targetView, "scaleX", beforeScaleX, afterScaleX))
                .with(ObjectAnimator.ofFloat(targetView, "scaleY", beforeScaleY, afterScaleY));
        animSet.setDuration(duration);
        animSet.start();
    }

    /**
     * 放大
     *
     * @param targetView   目标视图
     * @param beforeScaleX 放大前X方向范围
     * @param beforeScaleY 放大前Y方向范围
     * @param afterScaleX  放大后X方向范围
     * @param afterScaleY  放大后Y方向范围
     * @param duration     持续时间
     */
    public static void zoomIn(View targetView, float beforeScaleX, float beforeScaleY, float afterScaleX, float afterScaleY, long duration) {
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(ObjectAnimator.ofFloat(targetView, "scaleX", beforeScaleX, afterScaleX))
                .with(ObjectAnimator.ofFloat(targetView, "scaleY", beforeScaleY, afterScaleY));
        animSet.setDuration(duration);
        animSet.start();
    }
}
