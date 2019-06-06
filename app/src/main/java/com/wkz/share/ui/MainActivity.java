package com.wkz.share.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.wkz.share.R;
import com.wkz.share.utils.AnimationUtils;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    protected static final float ZOOM_BEFORE = 1.0F;
    protected static final float ZOOM_AFTER = 1.05F;
    protected static final long DURATION = 200L;

    protected static final List<String> mDatas = Arrays.asList(
            "http://k.zol-img.com.cn/sjbbs/7692/a7691515_s.jpg",
            "http://pic37.nipic.com/20140113/8800276_184927469000_2.png",
            "http://d.hiphotos.baidu.com/lvpics/w=1000/sign=e2347e78217f9e2f703519082f00eb24/730e0cf3d7ca7bcb49f90bb1b8096b63f724a8aa.jpg",
            "http://f.hiphotos.baidu.com/lvpics/w=600/sign=b7c305c7aad3fd1f3609a13a004f25ce/dcc451da81cb39db83b5f1bfd2160924aa1830e5.jpg",
            "http://b-ssl.duitang.com/uploads/item/201412/01/20141201233907_YmPCw.jpeg",
            "http://d.hiphotos.baidu.com/lvpics/w=1000/sign=65d18f97ae6eddc426e7b0fb09ebb7fd/8326cffc1e178a82d224ded2f503738da977e8ac.jpg",
            "http://img5.imgtn.bdimg.com/it/u=1921905645,1831992008&fm=26&gp=0.jpg",
            "http://img4.imgtn.bdimg.com/it/u=903712134,2931096712&fm=26&gp=0.jpg",
            "http://img3.imgtn.bdimg.com/it/u=3394504538,1396132709&fm=26&gp=0.jpg",
            "http://img3.imgtn.bdimg.com/it/u=2995907650,3595492611&fm=26&gp=0.jpg",
            "http://img5.imgtn.bdimg.com/it/u=1517815520,1162522071&fm=26&gp=0.jpg",
            "http://img0.imgtn.bdimg.com/it/u=2346425371,1207604549&fm=26&gp=0.jpg"
    );

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

    /**
     * 缩小
     *
     * @param targetView 目标视图
     */
    protected void zoomIn(View targetView) {
        AnimationUtils.zoomIn(targetView, ZOOM_BEFORE, ZOOM_BEFORE, ZOOM_AFTER, ZOOM_AFTER, DURATION);
    }

    /**
     * 放大
     *
     * @param targetView 目标视图
     */
    protected void zoomOut(View targetView) {
        AnimationUtils.zoomOut(targetView, ZOOM_AFTER, ZOOM_AFTER, ZOOM_BEFORE, ZOOM_BEFORE, DURATION);
    }
}
