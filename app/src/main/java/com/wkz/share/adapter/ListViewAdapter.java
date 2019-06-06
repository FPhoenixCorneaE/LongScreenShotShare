package com.wkz.share.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.sunfusheng.glideimageview.GlideImageView;
import com.wkz.share.R;
import com.wkz.share.utils.ViewUtils;

import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

/**
 * @author Administrator
 * @date 2019/5/30
 */
public class ListViewAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> mDatas;
    private View.OnClickListener mOnClickListener;

    public ListViewAdapter(Context mContext, List<String> mDatas) {
        this.mContext = mContext;
        this.mDatas = mDatas;
    }

    @Override
    public int getCount() {
        return null != mDatas ? mDatas.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return null != mDatas ? mDatas.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getConvertView(final int position, View convertView, final ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_list, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_list, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // List中的Item的LayoutParam是直接继承自ViewGroup中的LayoutParam。 不包含有margin信息。
        // 所以在ListView中父节点设置的值会失效。
        if (position == 0) {
            // 设置margin
            ViewUtils.setViewMargin(holder.mRlGameInfo, true, 25, 100, 25, 0);
        } else if (position == getCount() - 1) {
            // 设置margin
            ViewUtils.setViewMargin(holder.mRlGameInfo, true, 25, 15, 25, 150);
        } else {
            // 设置margin
            ViewUtils.setViewMargin(holder.mRlGameInfo, true, 25, 15, 25, 0);
        }

        holder.mIvGameImage.load(mDatas.get(position), new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL));
        holder.mIvGameIcon.load(mDatas.get(position), new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL));
        holder.mTvGameName.setText(String.format(Locale.getDefault(), "《将进酒》李白%d", position));

        holder.mIvGameImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnClickListener != null) {
                    mOnClickListener.onClick(v);
                }
            }
        });
        holder.mIvGameIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.mIvGameImage.performClick();
            }
        });
        return convertView;
    }

    public void onBindViewSync(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_list, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // List中的Item的LayoutParam是直接继承自ViewGroup中的LayoutParam。 不包含有margin信息。
        // 所以在ListView中父节点设置的值会失效。
        if (position == 0) {
            // 设置margin
            ViewUtils.setViewMargin(holder.mRlGameInfo, true, 25, 100, 25, 0);
        } else if (position == getCount() - 1) {
            // 设置margin
            ViewUtils.setViewMargin(holder.mRlGameInfo, true, 25, 15, 25, 150);
        } else {
            // 设置margin
            ViewUtils.setViewMargin(holder.mRlGameInfo, true, 25, 15, 25, 0);
        }

        try {
            File gameImageFile = Glide.with(mContext)
                    .load(mDatas.get(position))
                    .downloadOnly(0, 0)
                    .get();
            Bitmap gameImageBitmap = BitmapFactory.decodeFile(gameImageFile.getAbsolutePath());

            File gameIconFile = Glide.with(mContext)
                    .load(mDatas.get(position))
                    .downloadOnly(0, 0)
                    .get();
            Bitmap gameIconBitmap = BitmapFactory.decodeFile(gameIconFile.getAbsolutePath());

            holder.mIvGameImage.setImageBitmap(gameImageBitmap);
            holder.mIvGameIcon.setImageBitmap(gameIconBitmap);
            holder.mTvGameName.setText(String.format(Locale.getDefault(), "《将进酒》李白%d", position));
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    public static class ViewHolder {

        View mItemView;
        GlideImageView mIvGameImage;
        GlideImageView mIvGameIcon;
        TextView mTvGameName;
        TextView mTvGameDescription;
        RelativeLayout mRlGameInfo;

        ViewHolder(View view) {
            this.mItemView = view;
            this.mIvGameImage = (GlideImageView) view.findViewById(R.id.iv_game_image);
            this.mIvGameIcon = (GlideImageView) view.findViewById(R.id.iv_game_icon);
            this.mTvGameName = (TextView) view.findViewById(R.id.tv_game_name);
            this.mTvGameDescription = (TextView) view.findViewById(R.id.tv_game_description);
            this.mRlGameInfo = (RelativeLayout) view.findViewById(R.id.rl_game_info);
        }
    }

    public void setmOnClickListener(View.OnClickListener mOnClickListener) {
        this.mOnClickListener = mOnClickListener;
    }
}
