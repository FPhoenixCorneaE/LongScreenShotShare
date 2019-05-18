package com.wkz.share.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.wkz.share.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    protected static final float ZOOM_BEFORE = 1.0F;
    protected static final float ZOOM_AFTER = 1.05F;
    protected static final long DURATION = 200L;

    private Button mMScrollView;
    private Button mMListView;
    private Button mMRecyclerView;
    private Button mMWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mMScrollView = (Button) findViewById(R.id.mScrollView);
        mMScrollView.setOnClickListener(this);
        mMListView = (Button) findViewById(R.id.mListView);
        mMListView.setOnClickListener(this);
        mMRecyclerView = (Button) findViewById(R.id.mRecyclerView);
        mMRecyclerView.setOnClickListener(this);
        mMWebView = (Button) findViewById(R.id.mWebView);
        mMWebView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mScrollView:
                startActivity(new Intent(this, ScrollViewActivity.class));
                break;
            case R.id.mListView:
                startActivity(new Intent(this, ListViewActivity.class));
                break;
            case R.id.mRecyclerView:
                startActivity(new Intent(this, RecyclerViewActivity.class));
                break;
            case R.id.mWebView:
                startActivity(new Intent(this, WebViewActivity.class));
                break;
            default:
                break;
        }
    }
}
