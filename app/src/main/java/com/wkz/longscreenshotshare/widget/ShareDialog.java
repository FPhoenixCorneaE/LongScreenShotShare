package com.wkz.longscreenshotshare.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sunfusheng.glideimageview.GlideImageView;
import com.wkz.longscreenshotshare.R;
import com.wkz.longscreenshotshare.utils.ScreenShotUtils;
import com.wkz.longscreenshotshare.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 分享弹窗
 * Created by Administrator on 2018/5/15.
 */

public class ShareDialog extends Dialog {

    private FrameLayout mDialogFl;
    private ImageView mCloseIv;
    private FrameLayout mCloseFl;
    private RelativeLayout mSharePlatformRl;
    private TextView mShareTitleTv;
    private RecyclerView mRecyclerView;

    private Context mContext;
    private String mShareTitle;
    private OnClickListener mOnClickListener;

    public ShareDialog setShareTitle(String mShareTitle) {
        this.mShareTitle = mShareTitle;
        return this;
    }

    public ShareDialog setOnClickListener(OnClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
        return this;
    }

    public ShareDialog(@NonNull Context context) {
        super(context, R.style.Theme_ShareDialog);
        this.mContext = context;
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

        initView();
    }

    private void initView() {
        mShareTitleTv = (TextView) findViewById(R.id.tv_share_title);
        mRecyclerView = (RecyclerView) findViewById(R.id.mRecyclerView);
        mCloseIv = (ImageView) findViewById(R.id.iv_close);
        mCloseFl = (FrameLayout) findViewById(R.id.fl_close);
        mDialogFl = (FrameLayout) findViewById(R.id.fl_dialog);
        mSharePlatformRl = (RelativeLayout) findViewById(R.id.rl_share_platform);


        //分享标题
        if (!TextUtils.isEmpty(mShareTitle)) {
            mShareTitleTv.setText(mShareTitle);
        }

        initListener();

        initSharePlatform();

    }

    /**
     * 初始化监听
     */
    private void initListener() {
        mDialogFl.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        if (event.getY() > mCloseFl.getBottom() && event.getY() < mSharePlatformRl.getTop()) {
                            dismiss();
                            if (mOnClickListener != null) {
                                mOnClickListener.onClickBlank(ShareDialog.this);
                            }
                        }
                        break;
                }
                return true;
            }
        });

        mCloseFl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (mOnClickListener != null) {
                    mOnClickListener.onClickClose(ShareDialog.this);
                }
            }
        });
    }

    /**
     * 初始化窗口属性
     */
    @SuppressWarnings("ConstantConditions")
    private void initWindowAttribute() {

        //替换掉默认主题的背景，默认主题背景有一个16dp的padding
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //外边距
        ViewUtils.setViewPadding(getWindow().getDecorView(), true, 0, 0, 0, 0);

        //设置重力位置
        getWindow().setGravity(Gravity.BOTTOM);

        //设置弹窗的宽高
        getWindow().setLayout(-1, -1);
    }


    /**
     * 初始化分享平台
     */
    private void initSharePlatform() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayout.HORIZONTAL, false));

        List<ShareItemBean> datas = new ArrayList<>();
        datas.add(new ShareItemBean().setIconRes(R.mipmap.ic_share_friend_circle).setPlatformName("微信朋友圈"));
        datas.add(new ShareItemBean().setIconRes(R.mipmap.ic_share_wechat).setPlatformName("微信好友"));
        datas.add(new ShareItemBean().setIconRes(R.mipmap.ic_share_qqspace).setPlatformName("QQ空间"));
        datas.add(new ShareItemBean().setIconRes(R.mipmap.ic_share_qq).setPlatformName("QQ好友"));
        datas.add(new ShareItemBean().setIconRes(R.mipmap.ic_share_microblog).setPlatformName("微博"));
        datas.add(new ShareItemBean().setIconRes(R.mipmap.ic_share_link).setPlatformName("复制链接"));

        mRecyclerView.setAdapter(
                new ShareAdapter(mContext, datas)
                        .setOnClickListener(mOnClickListener));
    }

    public interface OnClickListener {

        void onClickClose(ShareDialog shareDialog);

        void onClickBlank(ShareDialog shareDialog);

        void onClickSharePlatform(ShareAdapter shareAdapter);

    }

    public static class ShareItemBean {

        private int iconRes;
        private String platformName;

        public int getIconRes() {
            return iconRes;
        }

        public ShareItemBean setIconRes(int iconRes) {
            this.iconRes = iconRes;
            return this;
        }

        public String getPlatformName() {
            return platformName;
        }

        public ShareItemBean setPlatformName(String platformName) {
            this.platformName = platformName;
            return this;
        }

        @Override
        public String toString() {
            return "ShareItemBean{" +
                    "iconRes=" + iconRes +
                    ", platformName='" + platformName + '\'' +
                    '}';
        }
    }


    public static class ShareAdapter extends RecyclerView.Adapter<ShareAdapter.ViewHolder> {

        private Context mContext;
        private View mView;
        private List<ShareItemBean> mDatas;
        private OnClickListener mOnClickListener;

        public ShareAdapter setView(View mView) {
            this.mView = mView;
            return this;
        }

        public ShareAdapter setOnClickListener(OnClickListener mOnClickListener) {
            this.mOnClickListener = mOnClickListener;
            return this;
        }

        public ShareAdapter(Context mContext, List<ShareItemBean> mDatas) {
            this.mContext = mContext;
            this.mDatas = mDatas == null ? new ArrayList<ShareItemBean>() : mDatas;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_share_dialog, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            ViewGroup.LayoutParams layoutParams = holder.mItemLl.getLayoutParams();
            ViewGroup.MarginLayoutParams marginParams = new ViewGroup.MarginLayoutParams(layoutParams);
            if (position == mDatas.size() - 1) {
                //最右边那个
                marginParams.leftMargin = ViewUtils.dp2px(mContext, 25f);
                marginParams.rightMargin = ViewUtils.dp2px(mContext, 25f);
                marginParams.bottomMargin = ViewUtils.dp2px(mContext, 15f);
            } else {
                marginParams.leftMargin = ViewUtils.dp2px(mContext, 25f);
                marginParams.rightMargin = ViewUtils.dp2px(mContext, 15f);
                marginParams.bottomMargin = ViewUtils.dp2px(mContext, 15f);
            }
            holder.mItemLl.setLayoutParams(marginParams);

            holder.mShareIconIv.loadLocalImage(mDatas.get(position).getIconRes(), 0);
            holder.mSharePlatformNameTv.setText(mDatas.get(position).getPlatformName());

            holder.mShareIconIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnClickListener != null) {
                        mOnClickListener.onClickSharePlatform(ShareAdapter.this);
                    }
                    if (mView != null) {
                        if (mView instanceof NestedScrollView) {
                            ScreenShotUtils.shotNestedScrollView((NestedScrollView) mView);
                        }
                    }
                    Toast.makeText(mContext, "点击了" + mDatas.get(holder.getAdapterPosition()).getPlatformName(), Toast.LENGTH_SHORT).show();
                }
            });

            holder.mItemLl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnClickListener != null) {
                        mOnClickListener.onClickSharePlatform(ShareAdapter.this);
                    }
                    if (mView != null) {
                        if (mView instanceof NestedScrollView) {
                            ScreenShotUtils.shotNestedScrollView((NestedScrollView) mView);
                        }
                    }
                    Toast.makeText(mContext, "点击了" + mDatas.get(holder.getAdapterPosition()).getPlatformName(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return mDatas.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {

            private GlideImageView mShareIconIv;
            private TextView mSharePlatformNameTv;
            private LinearLayout mItemLl;

            public ViewHolder(View itemView) {
                super(itemView);

                initView(itemView);
            }

            private void initView(@NonNull final View itemView) {
                mShareIconIv = (GlideImageView) itemView.findViewById(R.id.iv_share_icon);
                mSharePlatformNameTv = (TextView) itemView.findViewById(R.id.tv_share_platform_name);
                mItemLl = (LinearLayout) itemView.findViewById(R.id.ll_item);
            }
        }

    }
}
