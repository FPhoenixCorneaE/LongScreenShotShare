package com.wkz.longscreenshotshare;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.sunfusheng.glideimageview.GlideImageView;
import com.wkz.longscreenshotshare.immersionbar.BarHide;
import com.wkz.longscreenshotshare.immersionbar.ImmersionBar;
import com.wkz.longscreenshotshare.widget.ShareDialog;
import com.wkz.longscreenshotshare.zxing.QRCode;

/**
 * 图片分享界面
 * Created by Administrator on 2018/5/16.
 */

public class ScrollViewActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    private static final float ZOOM_BEFORE = 1.0F;
    private static final float ZOOM_AFTER = 1.05F;
    private static final long DURATION = 200L;


    private ShareDialog mShareDialog;
    private GlideImageView mGameImageIv;
    private GlideImageView mGameIconIv;
    private TextView mGameNameTv;
    private TextView mGameDescriptionTv;
    private RelativeLayout mGameInfoRl;
    private NestedScrollView mScrollNsv;
    private ImageView mQrCodeIv;
    private LinearLayout mChildLl;

    private Activity mContext;
    /*二维码中心图片地址*/
    private String mCenterImageUrl = "http://f9.topitme.com/9/37/30/11224703137bb30379o.jpg";
    /*二维码顶角颜色*/
    @ColorInt
    private int mVertexColor = Color.parseColor("#ff000000");
    /*下载游戏地址*/
    private String mDownloadUrl = "http://files.rastargame.com/andriodapk/RSClient.apk";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;

        setContentView(R.layout.activity_scroll_view);

        initView();

        //隐藏状态栏
        ImmersionBar.with(this)
                .fitsSystemWindows(false)
                .statusBarDarkFont(false)
                .hideBar(BarHide.FLAG_HIDE_NAVIGATION_BAR)
                .init();

        //分享弹窗
        mShareDialog = new ShareDialog(this)
                .setShareTitle("图片分享到")
                .setOnClickListener(new ShareDialog.OnClickListener() {
                    @Override
                    public void onClickClose(ShareDialog shareDialog) {
                        finish();
                    }

                    @Override
                    public void onClickBlank(ShareDialog shareDialog) {
                        zoomIn();
                    }

                    @Override
                    public void onClickSharePlatform(ShareDialog.ShareAdapter shareAdapter) {
                        shareAdapter.setView(mScrollNsv);
                    }
                });
        mShareDialog.show();
    }

    private void initView() {
        mGameImageIv = (GlideImageView) findViewById(R.id.iv_game_image);
        mGameImageIv.setOnClickListener(this);
        mGameIconIv = (GlideImageView) findViewById(R.id.iv_game_icon);
        mGameIconIv.setOnClickListener(this);
        mGameNameTv = (TextView) findViewById(R.id.tv_game_name);
        mGameDescriptionTv = (TextView) findViewById(R.id.tv_game_description);
        mGameInfoRl = (RelativeLayout) findViewById(R.id.rl_game_info);
        mGameInfoRl.setOnClickListener(this);
        mScrollNsv = (NestedScrollView) findViewById(R.id.nsv_scroll);
        mQrCodeIv = (ImageView) findViewById(R.id.iv_qr_code);
        mQrCodeIv.setOnLongClickListener(this);
        mChildLl = (LinearLayout) findViewById(R.id.ll_child);

//        Glide.with(this)
//                .load(R.mipmap.pic_image)
//                .apply(new RequestOptions().transform(new GlideBlurTransformation(this)))
//                .into(new SimpleTarget<Drawable>() {
//                    @Override
//                    public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
//                        mChildLl.setBackground(resource);
//                    }
//                });

        //二维码中心图片
        Glide.with(mContext)
                .asBitmap()
                .load(mCenterImageUrl)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        mQrCodeIv.setImageBitmap(QRCode.createQRCodeWithLogo6(mDownloadUrl, 500, resource, mVertexColor));
                    }
                });

    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_game_image:
                if (!mShareDialog.isShowing()) {
                    mShareDialog.show();
                }
                zoomOut();
                break;
            case R.id.iv_game_icon:
                if (!mShareDialog.isShowing()) {
                    mShareDialog.show();
                }
                zoomOut();
                break;
            case R.id.rl_game_info:
                if (!mShareDialog.isShowing()) {
                    mShareDialog.show();
                }
                zoomOut();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        //打开浏览器下载游戏
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(mDownloadUrl));
        mContext.startActivity(intent);
        return false;
    }

    /**
     * 缩小
     */
    private void zoomOut() {
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(ObjectAnimator.ofFloat(mGameInfoRl, "scaleX", ZOOM_AFTER, ZOOM_BEFORE))
                .with(ObjectAnimator.ofFloat(mGameInfoRl, "scaleY", ZOOM_AFTER, ZOOM_BEFORE));
        animSet.setDuration(DURATION);
        animSet.start();
    }

    /**
     * 放大
     */
    private void zoomIn() {
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(ObjectAnimator.ofFloat(mGameInfoRl, "scaleX", ZOOM_BEFORE, ZOOM_AFTER))
                .with(ObjectAnimator.ofFloat(mGameInfoRl, "scaleY", ZOOM_BEFORE, ZOOM_AFTER));
        animSet.setDuration(DURATION);
        animSet.start();
    }
}
