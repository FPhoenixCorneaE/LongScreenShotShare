package com.wkz.share.share;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wkz.share.R;
import com.wkz.share.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 分享弹窗
 *
 * @author Administrator
 * @date 2018/5/15
 */
public class ShareDialog extends Dialog {

    private FrameLayout mDialogFl;
    private ImageView mCloseIv;
    private FrameLayout mCloseFl;
    private RelativeLayout mSharePlatformRl;
    private TextView mShareTitleTv;
    private RecyclerView mSharePlatformRv;

    private String mShareTitle;
    private OnShareListener mOnShareListener;
    private SharePlatformAdapter mSharePlatformAdapter;

    public ShareDialog(@NonNull Context context) {
        super(context, R.style.Theme_ShareDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化窗口属性
        initWindowAttribute();
        //设置视图
        setContentView(R.layout.dialog_share);
        //设置是否可撤销
        setCancelable(true);
        //设置触摸外部是否可撤销
        setCanceledOnTouchOutside(true);
        //初始化视图
        initView();
        //初始化监听
        initListener();
        //初始化分享平台
        initSharePlatform();
    }

    private void initView() {
        mShareTitleTv = (TextView) findViewById(R.id.tv_share_title);
        mSharePlatformRv = (RecyclerView) findViewById(R.id.rv_share_platform);
        mCloseIv = (ImageView) findViewById(R.id.iv_close);
        mCloseFl = (FrameLayout) findViewById(R.id.fl_close);
        mDialogFl = (FrameLayout) findViewById(R.id.fl_dialog);
        mSharePlatformRl = (RelativeLayout) findViewById(R.id.rl_share_platform);


        //分享标题
        if (!TextUtils.isEmpty(mShareTitle)) {
            mShareTitleTv.setText(mShareTitle);
        }
    }

    /**
     * 初始化监听
     */
    @SuppressLint("ClickableViewAccessibility")
    private void initListener() {
        mDialogFl.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getY() > mCloseFl.getBottom() && event.getY() < mSharePlatformRl.getTop()) {
                        dismiss();
                        if (mOnShareListener != null) {
                            mOnShareListener.onTouchOutSide(ShareDialog.this);
                        }
                    }
                }
                return true;
            }
        });

        mCloseFl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (mOnShareListener != null) {
                    mOnShareListener.onClickCloseBtn(ShareDialog.this);
                }
            }
        });
    }

    /**
     * 初始化窗口属性
     */
    private void initWindowAttribute() {
        if (getWindow() != null) {
            //替换掉默认主题的背景，默认主题背景有一个16dp的padding
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            //外边距
            ViewUtils.setViewPadding(getWindow().getDecorView(), true, 0, 0, 0, 0);
            //设置重力位置
            getWindow().setGravity(Gravity.BOTTOM);
            //设置弹窗的宽高
            getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            //全屏,隐藏状态栏,
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            }
        }
    }


    /**
     * 初始化分享平台
     */
    private void initSharePlatform() {
        mSharePlatformRv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayout.HORIZONTAL, false));

        List<SharePlatformBean> datas = new ArrayList<>();
        datas.add(new SharePlatformBean().setIconRes(R.mipmap.ic_share_friend_circle).setPlatformName("微信朋友圈"));
        datas.add(new SharePlatformBean().setIconRes(R.mipmap.ic_share_wechat).setPlatformName("微信好友"));
        datas.add(new SharePlatformBean().setIconRes(R.mipmap.ic_share_qqspace).setPlatformName("QQ空间"));
        datas.add(new SharePlatformBean().setIconRes(R.mipmap.ic_share_qq).setPlatformName("QQ好友"));
        datas.add(new SharePlatformBean().setIconRes(R.mipmap.ic_share_microblog).setPlatformName("微博"));
        datas.add(new SharePlatformBean().setIconRes(R.mipmap.ic_share_link).setPlatformName("复制链接"));

        mSharePlatformRv.setAdapter(mSharePlatformAdapter = new SharePlatformAdapter(this, datas)
                .setOnShareListener(mOnShareListener)
        );
    }

    @Override
    public void show() {
        super.show();
        //为了每次show的时候执行动画
        mSharePlatformAdapter.notifyDataSetChanged();
    }

    public ShareDialog setShareTitle(String mShareTitle) {
        this.mShareTitle = mShareTitle;
        return this;
    }

    public ShareDialog setOnShareListener(OnShareListener mOnShareListener) {
        this.mOnShareListener = mOnShareListener;
        return this;
    }
}
