package com.wkz.share.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.sunfusheng.glideimageview.GlideImageView;
import com.wkz.share.R;
import com.wkz.share.immersionbar.BarHide;
import com.wkz.share.immersionbar.ImmersionBar;
import com.wkz.share.share.OnShareListener;
import com.wkz.share.share.ShareDialog;
import com.wkz.share.share.SharePlatformAdapter;
import com.wkz.share.utils.AnimationUtils;
import com.wkz.share.utils.ScreenShotUtils;
import com.wkz.share.utils.ViewUtils;
import com.wkz.share.zxing.QRCode;

import java.util.Locale;

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
                        //长截图
                        Bitmap bitmap = ScreenShotUtils.shotRecyclerView(mRvRecycler);
                    }
                });
        mShareDialog.show();
    }

    private void initRecyclerView() {
        mRvRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRvRecycler.setHasFixedSize(true);
        mRvRecycler.setAdapter(new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                if (viewType == 1) {
                    return new RecyclerView.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_recycler2, parent, false)) {
                    };
                } else {
                    return new RecyclerView.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_recycler1, parent, false)) {
                    };
                }
            }

            @Override
            public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
                if (position == 0) {
                    ViewUtils.setViewMargin(holder.itemView.findViewById(R.id.rl_game_info), true, 25, 100, 25, 0);
                    Glide.with(mContext)
                            .asBitmap()
                            .load(R.mipmap.pic_image)
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                    ((GlideImageView) holder.itemView.findViewById(R.id.iv_game_image)).setImageBitmap(resource);
                                }
                            });
                    ((GlideImageView) holder.itemView.findViewById(R.id.iv_game_icon)).loadImage(mCenterImageUrl, R.mipmap.ic_game_icon);
                    ((TextView) holder.itemView.findViewById(R.id.tv_game_name)).setText(String.format(Locale.getDefault(), "三生三世十里桃花%d", position));
                } else if (position == getItemCount() - 1) {
                    ViewUtils.setViewMargin(holder.itemView.findViewById(R.id.rl_qr_code), true, 25, 0, 25, 150);

                    //二维码中心图片
                    Glide.with(mContext)
                            .asBitmap()
                            .load(mCenterImageUrl)
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                    ((ImageView) holder.itemView.findViewById(R.id.iv_qr_code))
                                            .setImageBitmap(QRCode.createQRCodeWithLogo6(mDownloadUrl, 500, resource, mVertexColor));
                                }
                            });

                    holder.itemView.findViewById(R.id.iv_qr_code).setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            //打开浏览器下载游戏
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(mDownloadUrl));
                            mContext.startActivity(intent);
                            return false;
                        }
                    });
                } else {
                    ViewUtils.setViewMargin(holder.itemView.findViewById(R.id.rl_game_info), true, 25, 15, 25, 0);
                    if (position >= 10 && position < 10 + mDatas.size()) {
                        Glide.with(mContext)
                                .asBitmap()
                                .load(mDatas.get(position - 10))
                                .into(new SimpleTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                        ((GlideImageView) holder.itemView.findViewById(R.id.iv_game_image)).setImageBitmap(resource);
                                    }
                                });
                    } else {
                        Glide.with(mContext)
                                .asBitmap()
                                .load(R.mipmap.pic_image)
                                .into(new SimpleTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                        ((GlideImageView) holder.itemView.findViewById(R.id.iv_game_image)).setImageBitmap(resource);
                                    }
                                });
                    }
                    ((GlideImageView) holder.itemView.findViewById(R.id.iv_game_icon)).loadImage(mCenterImageUrl, R.mipmap.ic_game_icon);
                    ((TextView) holder.itemView.findViewById(R.id.tv_game_name)).setText(String.format(Locale.getDefault(), "三生三世十里桃花%d", position));
                }
            }

            @Override
            public int getItemCount() {
                return 20;
            }

            @Override
            public int getItemViewType(int position) {
                if (position == getItemCount() - 1) {
                    return 1;
                }
                return super.getItemViewType(position);
            }
        });
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
}
