package com.wkz.share.blur;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.wkz.share.utils.BlurBitmapUtils;

import java.security.MessageDigest;

/**
 * Glide高斯模糊图片转化
 *
 * @author Administrator
 * @date 2018/5/21
 */
public class GlideBlurTransformation extends BitmapTransformation {

    private Context context;

    public GlideBlurTransformation(Context context) {
        this.context = context;
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        return BlurBitmapUtils.blurBitmap(context, toTransform, 25, outWidth, outHeight);
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {
    }
}
