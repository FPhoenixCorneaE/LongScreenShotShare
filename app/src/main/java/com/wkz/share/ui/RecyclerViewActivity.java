package com.wkz.share.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.wkz.share.R;

/**
 * 循环视图的长截图分享界面
 *
 * @author Administrator
 * @date 2018/5/16
 */

public class RecyclerViewActivity extends AppCompatActivity {

    private RecyclerView mRvRecycler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);
        initView();

//        initRecyclerView();
    }

    private void initView() {
        mRvRecycler = (RecyclerView) findViewById(R.id.rvRecycler);
    }
}
