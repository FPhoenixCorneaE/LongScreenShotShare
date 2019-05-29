package com.wkz.share.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.wkz.share.R;

/**
 * 网页视图的长截图分享界面
 *
 * @author Administrator
 * @date 2018/5/16
 */

public class WebViewActivity extends AppCompatActivity {

    private WebView mWvWeb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        initView();
    }

    private void initView() {
        mWvWeb = (WebView) findViewById(R.id.wv_web);
    }
}
