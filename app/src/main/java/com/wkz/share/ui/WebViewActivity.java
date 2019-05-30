package com.wkz.share.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.wkz.share.R;
import com.wkz.share.immersionbar.BarHide;
import com.wkz.share.immersionbar.ImmersionBar;
import com.wkz.share.share.OnShareListener;
import com.wkz.share.share.ShareDialog;
import com.wkz.share.share.SharePlatformAdapter;
import com.wkz.share.utils.ScreenShotUtils;

/**
 * 网页视图的长截图分享界面
 *
 * @author Administrator
 * @date 2018/5/16
 */
@SuppressLint("SetJavaScriptEnabled")
public class WebViewActivity extends MainActivity implements View.OnClickListener {

    private static final String TAG = WebViewActivity.class.getSimpleName();
    private WebView mWvWeb;

    private Activity mContext;
    /**
     * 分享弹窗
     */
    private ShareDialog mShareDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        // 在Android5.0及以上版本，Android对WebView进行了优化，为了减少内存使用和提高性能，使用WebView加载网页时只绘制显示部分。
        // 如果我们不做处理，仍然使用上述代码截图的话，就会出现只截到屏幕内显示的WebView内容，其它部分是空白的情况。
        // 这时候，我们通过调用WebView.enableSlowWholeDocumentDraw()方法可以关闭这种优化，但要注意的是，该方法需要在WebView实例被创建前就要调用，否则没有效果。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            android.webkit.WebView.enableSlowWholeDocumentDraw();
        }

        setContentView(R.layout.activity_web_view);
        initView();
        initWebView();
        // 初始化状态栏
        initStatusBar();
        // 初始化分享弹窗
        initShareDialog();
    }

    private void initView() {
        mWvWeb = (WebView) findViewById(R.id.wv_web);
        mWvWeb.setOnClickListener(this);
    }

    private void initWebView() {
        WebSettings webSettings = mWvWeb.getSettings();
        //如果访问的页面中要与Javascript交互，则WebView必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);
        // 若加载的 html 里有JS 在执行动画等操作，会造成资源浪费（CPU、电量）
        // 在 onStop 和 onResume 里分别把 setJavaScriptEnabled() 给设置成 false 和 true 即可

        //设置自适应屏幕，两者合用
        //将图片调整到适合WebView的大小
        webSettings.setUseWideViewPort(true);
        // 缩放至屏幕的大小
        webSettings.setLoadWithOverviewMode(true);

        //缩放操作
        //支持缩放，默认为true。是下面那个的前提。
        webSettings.setSupportZoom(true);
        //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setBuiltInZoomControls(true);
        //隐藏原生的缩放控件
        webSettings.setDisplayZoomControls(false);

        //其他细节操作
        //关闭WebView中缓存
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        //设置可以访问文件
        webSettings.setAllowFileAccess(true);
        //支持通过JS打开新窗口
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        //支持自动加载图片
        webSettings.setLoadsImagesAutomatically(true);
        //设置编码格式
        webSettings.setDefaultTextEncodingName("utf-8");
        mWvWeb.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                Log.d(TAG, "newProgress：" + newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                Log.d(TAG, "标题：" + title);
            }
        });
        mWvWeb.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.d(TAG, "开始加载");
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.d(TAG, "加载结束");
            }

            // 链接跳转都会走这个方法
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d(TAG, "Url：" + url);
                // 强制在当前 WebView 中加载 url
                view.loadUrl(url);
                return true;
            }
        });
        mWvWeb.loadUrl("https://github.com/FPhoenixCorneaE?tab=repositories");
    }

    private void initStatusBar() {
        // 隐藏状态栏
        ImmersionBar.with(this)
                .fitsSystemWindows(false)
                .statusBarDarkFont(false)
                .hideBar(BarHide.FLAG_HIDE_NAVIGATION_BAR)
                .init();
    }

    private void initShareDialog() {
        // 分享弹窗
        mShareDialog = new ShareDialog(this)
                .setShareTitle("图片分享到")
                .setOnShareListener(new OnShareListener() {
                    @Override
                    public void onClickCloseBtn(ShareDialog shareDialog) {
                        finish();
                    }

                    @Override
                    public void onTouchOutSide(ShareDialog shareDialog) {
                        zoomIn(mWvWeb);
                    }

                    @Override
                    public void onDismiss(ShareDialog shareDialog) {

                    }

                    @Override
                    public void onClickSharePlatform(ShareDialog shareDialog, SharePlatformAdapter.ViewHolder holder, int sharePlatform) {
                        // 长截图
                        Bitmap bitmap = ScreenShotUtils.shotWebView(mWvWeb);
                    }
                });
        mShareDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.wv_web:
                if (!mShareDialog.isShowing()) {
                    mShareDialog.show();
                }
                zoomOut(mWvWeb);
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mWvWeb.getSettings().setJavaScriptEnabled(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWvWeb.getSettings().setJavaScriptEnabled(true);
    }

    @Override
    public void onBackPressed() {
        if (mWvWeb.canGoBack()) {
            mWvWeb.goBack();
            return;
        }
        super.onBackPressed();
    }
}
