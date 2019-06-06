package com.wkz.share.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.sunfusheng.glideimageview.GlideImageView;
import com.wkz.share.R;
import com.wkz.share.utils.ViewUtils;
import com.wkz.share.zxing.QRCode;

import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

/**
 * @author Administrator
 * @date 2019/5/20
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<String> mDatas;
    private String mCenterImageUrl;
    private String mDownloadUrl;
    private int mVertexColor;

    public RecyclerViewAdapter(Context mContext, List<String> mDatas, String mCenterImageUrl, String mDownloadUrl, int mVertexColor) {
        this.mContext = mContext;
        this.mDatas = mDatas;
        this.mCenterImageUrl = mCenterImageUrl;
        this.mDownloadUrl = mDownloadUrl;
        this.mVertexColor = mVertexColor;
    }

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
            //设置margin
            ViewUtils.setViewMargin(holder.itemView.findViewById(R.id.rl_game_info), true, 25, 100, 25, 0);
            ((GlideImageView) holder.itemView.findViewById(R.id.iv_game_image)).load(mDatas.get(position), new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL));
            ((GlideImageView) holder.itemView.findViewById(R.id.iv_game_icon)).load(mCenterImageUrl, new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL));
            ((TextView) holder.itemView.findViewById(R.id.tv_game_name)).setText(String.format(Locale.getDefault(), "三生三世十里桃花%d", position));
        } else if (position == getItemCount() - 1) {
            //设置margin
            ViewUtils.setViewMargin(holder.itemView.findViewById(R.id.rl_qr_code), true, 25, 0, 25, 150);

            //二维码中心图片
            Glide.with(mContext)
                    .asBitmap()
                    .load(mCenterImageUrl)
                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
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
            //设置margin
            ViewUtils.setViewMargin(holder.itemView.findViewById(R.id.rl_game_info), true, 25, 15, 25, 0);
            ((GlideImageView) holder.itemView.findViewById(R.id.iv_game_image)).load(mDatas.get(position), new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL));
            ((GlideImageView) holder.itemView.findViewById(R.id.iv_game_icon)).load(mCenterImageUrl, new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL));
            ((TextView) holder.itemView.findViewById(R.id.tv_game_name)).setText(String.format(Locale.getDefault(), "三生三世十里桃花%d", position));
        }
    }

    public void onBindViewSync(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        try {
            if (position == 0) {
                //设置margin
                ViewUtils.setViewMargin(holder.itemView.findViewById(R.id.rl_game_info), true, 25, 100, 25, 0);
                File gameImageFile = Glide.with(mContext)
                        .load(mDatas.get(position))
                        .downloadOnly(0, 0)
                        .get();
                Bitmap gameImageBitmap = BitmapFactory.decodeFile(gameImageFile.getAbsolutePath());

                File gameIconFile = Glide.with(mContext)
                        .load(mCenterImageUrl)
                        .downloadOnly(0, 0)
                        .get();
                Bitmap gameIconBitmap = BitmapFactory.decodeFile(gameIconFile.getAbsolutePath());

                ((GlideImageView) holder.itemView.findViewById(R.id.iv_game_image)).setImageBitmap(gameImageBitmap);
                ((GlideImageView) holder.itemView.findViewById(R.id.iv_game_icon)).setImageBitmap(gameIconBitmap);
                ((TextView) holder.itemView.findViewById(R.id.tv_game_name)).setText(String.format(Locale.getDefault(), "三生三世十里桃花%d", position));
            } else if (position == getItemCount() - 1) {
                //设置margin
                ViewUtils.setViewMargin(holder.itemView.findViewById(R.id.rl_qr_code), true, 25, 0, 25, 150);

                //二维码中心图片
                File centerImageFile = Glide.with(mContext)
                        .load(mCenterImageUrl)
                        .downloadOnly(0, 0)
                        .get();
                Bitmap centerImageBitmap = BitmapFactory.decodeFile(centerImageFile.getAbsolutePath());

                ((ImageView) holder.itemView.findViewById(R.id.iv_qr_code))
                        .setImageBitmap(QRCode.createQRCodeWithLogo6(mDownloadUrl, 500, centerImageBitmap, mVertexColor));

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
                //设置margin
                ViewUtils.setViewMargin(holder.itemView.findViewById(R.id.rl_game_info), true, 25, 15, 25, 0);

                File gameImageFile = Glide.with(mContext)
                        .load(mDatas.get(position))
                        .downloadOnly(0, 0)
                        .get();
                Bitmap gameImageBitmap = BitmapFactory.decodeFile(gameImageFile.getAbsolutePath());

                File gameIconFile = Glide.with(mContext)
                        .load(mCenterImageUrl)
                        .downloadOnly(0, 0)
                        .get();
                Bitmap gameIconBitmap = BitmapFactory.decodeFile(gameIconFile.getAbsolutePath());

                ((GlideImageView) holder.itemView.findViewById(R.id.iv_game_image)).setImageBitmap(gameImageBitmap);
                ((GlideImageView) holder.itemView.findViewById(R.id.iv_game_icon)).setImageBitmap(gameIconBitmap);
                ((TextView) holder.itemView.findViewById(R.id.tv_game_name)).setText(String.format(Locale.getDefault(), "三生三世十里桃花%d", position));
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return 1;
        }
        return super.getItemViewType(position);
    }
}
