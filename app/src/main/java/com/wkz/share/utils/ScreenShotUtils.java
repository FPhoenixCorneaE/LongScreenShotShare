package com.wkz.share.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.LruCache;
import android.util.SparseIntArray;
import android.view.View;
import android.webkit.WebView;
import android.widget.ListView;

import com.nanchen.compresshelper.CompressHelper;
import com.wkz.share.adapter.ListViewAdapter;
import com.wkz.share.adapter.RecyclerViewAdapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Locale;

/**
 * 长截图工具类
 *
 * @author Administrator
 * @date 2018/5/21
 */

public class ScreenShotUtils {

    private static final String FILE_DIR = "LongScreenShot";

    /**
     * 截图Activity
     */
    public static Bitmap shotActivity(Activity activity) {
        // View是你需要截图的View
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();

        Bitmap b1 = view.getDrawingCache();

        // 获取状态栏高度
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;

        // 获取屏幕长和高
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindow().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        // 去掉标题栏
        Bitmap bitmap = Bitmap.createBitmap(b1, 0, statusBarHeight, width,
                height - statusBarHeight);
        view.destroyDrawingCache();

        // 保存图片
        savePicture(activity, bitmap);

        return bitmap;
    }

    /**
     * http://stackoverflow.com/questions/9791714/take-a-screenshot-of-a-whole-view
     */
    @SuppressLint("ObsoleteSdkInt")
    public static Bitmap shotView(View v) {
        if (v == null) {
            return null;
        }
        v.setDrawingCacheEnabled(true);
        v.buildDrawingCache();
        if (Build.VERSION.SDK_INT >= 11) {
            v.measure(View.MeasureSpec.makeMeasureSpec(v.getWidth(), View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(v.getHeight(), View.MeasureSpec.EXACTLY));
            v.layout((int) v.getX(),
                    (int) v.getY(),
                    (int) v.getX() + v.getMeasuredWidth(),
                    (int) v.getY() + v.getMeasuredHeight());
        } else {
            v.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        }
        Bitmap b = Bitmap.createBitmap(v.getDrawingCache(), 0, 0,
                v.getMeasuredWidth(), v.getMeasuredHeight());

        v.setDrawingCacheEnabled(false);
        v.destroyDrawingCache();

        return b;
    }

    @SuppressLint("ObsoleteSdkInt")
    public static Bitmap shotViewWithoutBottom(View v) {
        if (v == null) {
            return null;
        }
        v.setDrawingCacheEnabled(true);
        v.buildDrawingCache();
        if (Build.VERSION.SDK_INT >= 11) {
            v.measure(View.MeasureSpec.makeMeasureSpec(v.getWidth(), View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(v.getHeight(), View.MeasureSpec.EXACTLY));
            v.layout((int) v.getX(), (int) v.getY(),
                    (int) v.getX() + v.getMeasuredWidth(),
                    (int) v.getY() + v.getMeasuredHeight());
        } else {
            v.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        }
        Bitmap b = Bitmap.createBitmap(v.getDrawingCache(), 0, 0,
                v.getMeasuredWidth(), v.getMeasuredHeight() - v.getPaddingBottom());

        v.setDrawingCacheEnabled(false);
        v.destroyDrawingCache();

        return b;
    }

    /**
     * 截图View,只能截当前屏幕可见区域
     *
     * @param view
     * @return
     */
    public static Bitmap shotViewInScreen(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        // 保存图片
        savePicture(view.getContext(), bitmap);
        return bitmap;
    }

    /**
     * 截图NestedScrollView
     * http://blog.csdn.net/lyy1104/article/details/40048329
     **/
    public static Bitmap shotNestedScrollView(NestedScrollView nestedScrollView) {
        if (nestedScrollView == null) {
            return null;
        }
        try {
            int h = 0;
            // 获取ScrollView实际高度
            for (int i = 0; i < nestedScrollView.getChildCount(); i++) {
                h += nestedScrollView.getChildAt(i).getHeight();
            }
            // 创建对应大小的bitmap
            Bitmap bitmap = Bitmap.createBitmap(nestedScrollView.getWidth(), h, Bitmap.Config.ARGB_8888);
            final Canvas canvas = new Canvas(bitmap);
            nestedScrollView.draw(canvas);

            // 保存图片
            savePicture(nestedScrollView.getContext(), bitmap);

            return bitmap;
        } catch (OutOfMemoryError oom) {
            return null;
        }
    }

    /**
     * 截图NestedScrollView
     * get the bitmap of a NestedScrollView
     */
    public static Bitmap shotNestedScrollView(Activity activity, NestedScrollView sv) {
        if (null == sv) {
            return null;
        }
        // enable something
        sv.setVerticalScrollBarEnabled(false);
        sv.setVerticalFadingEdgeEnabled(false);
        sv.scrollTo(0, 0);
        sv.setDrawingCacheEnabled(true);
        sv.buildDrawingCache(true);

        Bitmap b = shotViewWithoutBottom(sv);

        /*
         * vh : the height of the scrollView that is visible <BR>
         * th : the total height of the scrollView <BR>
         **/
        int vh = sv.getHeight();
        int th = sv.getChildAt(0).getHeight();

        /* the total height is more than one screen */
        Bitmap temp;
        if (th > vh) {
            int w = ScreenUtils.getScreenWidth(activity);
            int absVh = vh - sv.getPaddingTop() - sv.getPaddingBottom();
            do {
                int restHeight = th - vh;
                if (restHeight <= absVh) {
                    sv.scrollBy(0, restHeight);
                    vh += restHeight;
                    temp = shotView(sv);
                } else {
                    sv.scrollBy(0, absVh);
                    vh += absVh;
                    temp = shotViewWithoutBottom(sv);
                }
                b = mergeBitmap(vh, w, temp, 0, sv.getScrollY(), b, 0, 0);
            } while (vh < th);
        }

        // restore something
        sv.scrollTo(0, 0);
        sv.setVerticalScrollBarEnabled(true);
        sv.setVerticalFadingEdgeEnabled(true);
        sv.setDrawingCacheEnabled(false);
        sv.destroyDrawingCache();

        // 保存图片
        savePicture(sv.getContext(), b);

        return b;
    }

    private static Bitmap mergeBitmap(int newImageH, int newImageW, Bitmap background,
                                      float backX, float backY, Bitmap foreground,
                                      float foreX, float foreY) {
        if (null == background || null == foreground) {
            return null;
        }
        // create the new blank bitmap 创建一个新的和SRC长度宽度一样的位图
        Bitmap newBitmap = Bitmap.createBitmap(newImageW, newImageH, Bitmap.Config.RGB_565);
        Canvas cv = new Canvas(newBitmap);
        // draw bg into
        cv.drawBitmap(background, backX, backY, null);
        // draw fg into
        cv.drawBitmap(foreground, foreX, foreY, null);
        // save all clip
        cv.save();
        // store
        cv.restore();// 存储

        return newBitmap;
    }

    /**
     * 截图ListView
     * http://stackoverflow.com/questions/12742343/android-get-screenshot-of-all-listview-items
     */
    public static Bitmap shotListView(ListView listView) {
        if (listView == null) {
            return null;
        }

        try {
            ListViewAdapter adapter = (ListViewAdapter) listView.getAdapter();
            Bitmap bigBitmap = null;
            if (adapter != null) {
                int size = adapter.getCount();
                int height = 0;
                Paint paint = new Paint();
                final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

                // Use 1/8th of the available memory for this memory cache.
                final int cacheSize = maxMemory / 8;
                LruCache<String, Bitmap> bitmapCache = new LruCache<>(cacheSize);
                SparseIntArray bitmapTop = new SparseIntArray(size);
                for (int i = 0; i < size; i++) {
                    View childView = adapter.getConvertView(i, null, listView);
                    adapter.onBindViewSync(i, childView, listView);
                    childView.measure(
                            View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.EXACTLY),
                            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                    );
                    childView.layout(
                            0,
                            0,
                            childView.getMeasuredWidth(),
                            childView.getMeasuredHeight()
                    );
                    childView.setDrawingCacheEnabled(true);
                    childView.buildDrawingCache();
                    Bitmap drawingCache = childView.getDrawingCache();
                    if (drawingCache != null) {
                        bitmapCache.put(String.valueOf(i), drawingCache);
                    }

                    bitmapTop.put(i, height);
                    height += childView.getMeasuredHeight();
                }

                bigBitmap = Bitmap.createBitmap(listView.getMeasuredWidth(), height, Bitmap.Config.ARGB_8888);
                Canvas bigCanvas = new Canvas(bigBitmap);
                Drawable lBackground = listView.getBackground();
                if (lBackground instanceof ColorDrawable) {
                    ColorDrawable lColorDrawable = (ColorDrawable) lBackground;
                    int lColor = lColorDrawable.getColor();
                    bigCanvas.drawColor(lColor);
                }

                for (int i = 0; i < size; i++) {
                    Bitmap bitmap = bitmapCache.get(String.valueOf(i));
                    bigCanvas.drawBitmap(bitmap, 0, bitmapTop.get(i), paint);
                    bitmap.recycle();
                }
            }
            return bigBitmap;
        } catch (OutOfMemoryError oom) {
            return null;
        }
    }

    /**
     * 截图RecyclerView
     * https://gist.github.com/PrashamTrivedi/809d2541776c8c141d9a
     */
    public static Bitmap shotRecyclerView(RecyclerView recyclerView) {
        if (recyclerView == null) {
            return null;
        }
        try {
            RecyclerViewAdapter adapter = (RecyclerViewAdapter) recyclerView.getAdapter();
            Bitmap bigBitmap = null;
            if (adapter != null) {
                int size = adapter.getItemCount();
                int height = 0;
                Paint paint = new Paint();
                final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

                // Use 1/8th of the available memory for this memory cache.
                final int cacheSize = maxMemory / 8;
                LruCache<String, Bitmap> bitmapCache = new LruCache<>(cacheSize);
                SparseIntArray bitmapLeft = new SparseIntArray(size);
                SparseIntArray bitmapTop = new SparseIntArray(size);
                for (int i = 0; i < size; i++) {
                    RecyclerView.ViewHolder holder = adapter.createViewHolder(recyclerView, adapter.getItemViewType(i));
                    adapter.onBindViewSync(holder, i);
                    RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
                    holder.itemView.measure(
                            View.MeasureSpec.makeMeasureSpec(recyclerView.getWidth() - layoutParams.leftMargin - layoutParams.rightMargin, View.MeasureSpec.EXACTLY),
                            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                    );
                    holder.itemView.layout(
                            layoutParams.leftMargin,
                            layoutParams.topMargin,
                            holder.itemView.getMeasuredWidth() + layoutParams.leftMargin,
                            holder.itemView.getMeasuredHeight() + layoutParams.topMargin
                    );
                    holder.itemView.setDrawingCacheEnabled(true);
                    holder.itemView.buildDrawingCache();
                    Bitmap drawingCache = holder.itemView.getDrawingCache();
                    if (drawingCache != null) {
                        bitmapCache.put(String.valueOf(i), drawingCache);
                    }

                    height += layoutParams.topMargin;
                    bitmapLeft.put(i, layoutParams.leftMargin);
                    bitmapTop.put(i, height);
                    height += holder.itemView.getMeasuredHeight() + layoutParams.bottomMargin;
                }

                bigBitmap = Bitmap.createBitmap(recyclerView.getMeasuredWidth(), height, Bitmap.Config.ARGB_8888);
                Canvas bigCanvas = new Canvas(bigBitmap);
                Drawable lBackground = recyclerView.getBackground();
                if (lBackground instanceof ColorDrawable) {
                    ColorDrawable lColorDrawable = (ColorDrawable) lBackground;
                    int lColor = lColorDrawable.getColor();
                    bigCanvas.drawColor(lColor);
                }

                for (int i = 0; i < size; i++) {
                    Bitmap bitmap = bitmapCache.get(String.valueOf(i));
                    bigCanvas.drawBitmap(bitmap, bitmapLeft.get(i), bitmapTop.get(i), paint);
                    bitmap.recycle();
                }
            }
            return bigBitmap;
        } catch (OutOfMemoryError oom) {
            return null;
        }
    }

    /**
     * 截图WebView，没有使用X5内核
     *
     * @param webView
     * @return
     */
    public static Bitmap shotWebView(WebView webView) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                // Android5.0以上
                float scale = webView.getScale();
                int width = webView.getWidth();
                int height = (int) (webView.getContentHeight() * scale + 0.5);
                Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                webView.draw(canvas);

                // 保存图片
                savePicture(webView.getContext(), bitmap);
                return bitmap;
            } else {
                // Android5.0以下
                Picture picture = webView.capturePicture();
                int width = picture.getWidth();
                int height = picture.getHeight();
                if (width > 0 && height > 0) {
                    Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(bitmap);
                    picture.draw(canvas);

                    // 保存图片
                    savePicture(webView.getContext(), bitmap);
                    return bitmap;
                }
                return null;
            }
        } catch (OutOfMemoryError oom) {
            return null;
        }
    }

    /**
     * 保存图片
     *
     * @param context
     * @param bitmap
     */
    public static File savePicture(final Context context, Bitmap bitmap) {

        if (bitmap == null) {
            return null;
        }

        final File mFile;
        String path = Environment.getExternalStorageDirectory() + File.separator + FILE_DIR;
        final File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        //当前时间通过MD5命名图片
        String fileName = getMD5(System.currentTimeMillis() + "").toUpperCase(Locale.getDefault());
        mFile = new File(dir, fileName);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(mFile.getAbsolutePath());
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != out) {
                    out.close();
                }
                if (!bitmap.isRecycled()) {
                    bitmap.recycle();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 默认的压缩方法，多张图片只需要直接加入循环即可
//        File newFile = CompressHelper.getDefault(context.getApplicationContext()).compressToFile(mFile);

        File newFile = new CompressHelper.Builder(context.getApplicationContext())
                // 默认最大宽度为720
                .setMaxWidth(10000)
                // 默认最大高度为960
                .setMaxHeight(50000)
                // 默认压缩质量为80
                .setQuality(80)
                // 设置你需要修改的文件名
                .setFileName(fileName)
                // 设置默认压缩为jpg格式
                .setCompressFormat(Bitmap.CompressFormat.JPEG)
//                .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
//                        Environment.DIRECTORY_PICTURES).getAbsolutePath())
                .setDestinationDirectoryPath(dir.getAbsolutePath())
                .build()
                .compressToFile(mFile);

        //更新本地图库
        Uri uri = Uri.fromFile(newFile);
        Intent mIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
        context.sendBroadcast(mIntent);

        return newFile;
    }

    /**
     * MD5加密
     *
     * @param info
     */
    private static String getMD5(String info) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                md5.update(info.getBytes(StandardCharsets.UTF_8));
            } else {
                md5.update(info.getBytes("UTF-8"));
            }
            byte[] encryption = md5.digest();

            StringBuilder strBuilder = new StringBuilder();
            for (int i = 0; i < encryption.length; i++) {
                if (Integer.toHexString(0xff & encryption[i]).length() == 1) {
                    strBuilder.append("0").append(Integer.toHexString(0xff & encryption[i]));
                } else {
                    strBuilder.append(Integer.toHexString(0xff & encryption[i]));
                }
            }

            return strBuilder.toString();
        } catch (Exception e) {
            return "";
        }
    }
}
