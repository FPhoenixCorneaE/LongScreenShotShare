package com.wkz.share.ui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.wkz.share.R;
import com.wkz.share.adapter.ListViewAdapter;
import com.wkz.share.immersionbar.BarHide;
import com.wkz.share.immersionbar.ImmersionBar;
import com.wkz.share.share.OnShareListener;
import com.wkz.share.share.ShareDialog;
import com.wkz.share.share.SharePlatformAdapter;
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
 * 列表视图的长截图分享界面
 *
 * @author Administrator
 * @date 2018/5/16
 */

public class ListViewActivity extends MainActivity {

    private FrameLayout mFlRoot;
    private ListView mLvList;

    private Activity mContext;
    /**
     * 分享弹窗
     */
    private ShareDialog mShareDialog;
    private ListViewAdapter mListViewAdapter;
    private Disposable mDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_list_view);
        initView();
        initListView();
        initListener();
        initData();
    }

    private void initView() {
        mLvList = (ListView) findViewById(R.id.lvList);
        mFlRoot = (FrameLayout) findViewById(R.id.fl_root);
    }

    private void initListView() {
        mLvList.setAdapter(mListViewAdapter = new ListViewAdapter(mContext, mDatas));
    }

    private void initListener() {
        mLvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //item点击
                if (!mShareDialog.isShowing()) {
                    mShareDialog.show();
                }
                zoomOut(mLvList);
            }
        });
        mListViewAdapter.setmOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //item点击
                if (!mShareDialog.isShowing()) {
                    mShareDialog.show();
                }
                zoomOut(mLvList);
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
                        zoomIn(mLvList);
                    }

                    @Override
                    public void onDismiss(ShareDialog shareDialog) {
                        zoomIn(mLvList);
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
                                                observer.onNext(ScreenShotUtils.shotListView(mLvList));
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }
}
