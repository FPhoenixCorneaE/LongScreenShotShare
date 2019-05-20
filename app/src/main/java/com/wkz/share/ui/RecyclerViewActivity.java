package com.wkz.share.ui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;

import com.wkz.share.R;
import com.wkz.share.adapter.RecyclerViewAdapter;
import com.wkz.share.immersionbar.BarHide;
import com.wkz.share.immersionbar.ImmersionBar;
import com.wkz.share.share.OnShareListener;
import com.wkz.share.share.ShareDialog;
import com.wkz.share.share.SharePlatformAdapter;
import com.wkz.share.utils.AnimationUtils;
import com.wkz.share.utils.ScreenShotUtils;

import java.io.File;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 循环视图的长截图分享界面
 *
 * @author Administrator
 * @date 2018/5/16
 */

public class RecyclerViewActivity extends MainActivity {

    private RecyclerView mRvRecycler;

    private Activity mContext;
    /**
     * 二维码中心图片地址
     */
    private String mCenterImageUrl = "http://gss0.baidu.com/-fo3dSag_xI4khGko9WTAnF6hhy/zhidao/pic/item/5366d0160924ab1861c9326236fae6cd7a890b8c.jpg";
    /**
     * 二维码顶角颜色
     */
    @ColorInt
    private int mVertexColor = Color.parseColor("#ff000000");
    /**
     * 下载游戏地址
     */
    private String mDownloadUrl = "http://files.rastargame.com/andriodapk/RSClient.apk";
    /**
     * 分享弹窗
     */
    private ShareDialog mShareDialog;

    private Disposable mDisposable;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;

        setContentView(R.layout.activity_recycler_view);

        initView();

        initListener();

        initData();

        initRecyclerView();
    }

    private void initView() {
        mRvRecycler = (RecyclerView) findViewById(R.id.rvRecycler);
    }

    private void initListener() {
        mRvRecycler.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener() {
            private GestureDetectorCompat mGestureDetectorCompat = new GestureDetectorCompat(mRvRecycler.getContext(), new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    //item点击
                    if (!mShareDialog.isShowing()) {
                        mShareDialog.show();
                    }
                    zoomOut();
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {

                }
            });

            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                mGestureDetectorCompat.onTouchEvent(e);
                return false;
            }
        });
    }

    private void initData() {
        //隐藏状态栏
        ImmersionBar.with(this)
                .fitsSystemWindows(false)
                .statusBarDarkFont(false)
                .hideBar(BarHide.FLAG_HIDE_NAVIGATION_BAR)
                .init();

        //分享弹窗
        mShareDialog = new ShareDialog(this)
                .setShareTitle("图片分享到")
                .setOnShareListener(new OnShareListener() {
                    @Override
                    public void onClickCloseBtn(ShareDialog shareDialog) {
                        finish();
                    }

                    @Override
                    public void onTouchOutSide(ShareDialog shareDialog) {
                        zoomIn();
                    }

                    @Override
                    public void onDismiss(ShareDialog shareDialog) {
                        zoomIn();
                    }

                    @Override
                    public void onClickSharePlatform(ShareDialog shareDialog, SharePlatformAdapter.ViewHolder holder, int sharePlatform) {
                        // 长截图
                        mDisposable = Observable.defer(
                                new Callable<ObservableSource<Bitmap>>() {
                                    @Override
                                    public ObservableSource<Bitmap> call() throws Exception {
                                        return new ObservableSource<Bitmap>() {
                                            @Override
                                            public void subscribe(Observer<? super Bitmap> observer) {
                                                // 开始截图
                                                observer.onNext(ScreenShotUtils.shotRecyclerView(mRvRecycler));
                                            }
                                        };
                                    }
                                })
                                .subscribeOn(Schedulers.computation())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<Bitmap>() {
                                    @Override
                                    public void accept(Bitmap bitmap) throws Exception {
                                        // 保存图片
                                        File file = ScreenShotUtils.savePicture(mContext, bitmap);

                                        Toast.makeText(mContext, "图片已保存至 " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
        mShareDialog.show();
    }

    private void initRecyclerView() {
        mRvRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRvRecycler.setHasFixedSize(true);
        mRvRecycler.setAdapter(new RecyclerViewAdapter(mContext, mDatas, mCenterImageUrl, mDownloadUrl, mVertexColor));
    }

    /**
     * 缩小
     */
    private void zoomIn() {
        AnimationUtils.zoomIn(mRvRecycler, ZOOM_BEFORE, ZOOM_BEFORE, ZOOM_AFTER, ZOOM_AFTER, DURATION);
    }

    /**
     * 放大
     */
    private void zoomOut() {
        AnimationUtils.zoomOut(mRvRecycler, ZOOM_AFTER, ZOOM_AFTER, ZOOM_BEFORE, ZOOM_BEFORE, DURATION);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }
}
