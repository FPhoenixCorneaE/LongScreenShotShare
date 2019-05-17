package com.wkz.share.share;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sunfusheng.glideimageview.GlideImageView;
import com.wkz.share.R;
import com.wkz.share.utils.AnimationUtils;
import com.wkz.share.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 分享平台适配器
 *
 * @author Administrator
 * @date 2019/5/15
 */
public class SharePlatformAdapter extends RecyclerView.Adapter<SharePlatformAdapter.ViewHolder> {

    private ShareDialog mShareDialog;
    private List<SharePlatformBean> mDatas;
    private OnShareListener mOnShareListener;

    SharePlatformAdapter(ShareDialog shareDialog, List<SharePlatformBean> mDatas) {
        this.mShareDialog = shareDialog;
        this.mDatas = mDatas == null ? new ArrayList<SharePlatformBean>() : mDatas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_share_dialog, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (position == mDatas.size() - 1) {
            //最右边那个
            ViewUtils.setViewMargin(holder.mItemLl, true, 25, 0, 25, 15);
        } else {
            ViewUtils.setViewMargin(holder.mItemLl, true, 25, 0, 15, 15);
        }

        holder.mShareIconIv.loadLocalImage(mDatas.get(position).getIconRes(), 0);
        holder.mSharePlatformNameTv.setText(mDatas.get(position).getPlatformName());

        //开始动画
        AnimationUtils.startOvershootAnimation(holder.mShareIconIv, 200, position * 100);

        holder.mShareIconIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.mItemLl.performClick();
            }
        });

        holder.mItemLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mShareDialog != null && mShareDialog.isShowing()) {
                    mShareDialog.dismiss();
                }
                if (mOnShareListener != null) {
                    mOnShareListener.onClickSharePlatform(mShareDialog, holder, SharePlatform.WeChat);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    SharePlatformAdapter setOnShareListener(OnShareListener mOnShareListener) {
        this.mOnShareListener = mOnShareListener;
        return this;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private GlideImageView mShareIconIv;
        private TextView mSharePlatformNameTv;
        private LinearLayout mItemLl;

        ViewHolder(View itemView) {
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
