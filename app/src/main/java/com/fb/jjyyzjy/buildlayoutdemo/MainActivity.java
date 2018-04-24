package com.fb.jjyyzjy.buildlayoutdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fb.jjyyzjy.buildlayoutdemo.utils.DisplayUtil;
import com.fb.jjyyzjy.buildlayoutdemo.view.buildview.MyBuildLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DisplayUtil.init(this);
        initView();
    }

    private int[][] data = {{0,0,1,1},{0,1,1,1},{0,2,1,1},{1,0,2,1},{1,1,2,1},{1,2,1,1},{2,2,1,1},{3,0,2,2},{3,2,2,1},{5,0,1,3}};
    private int[][] data2 = {{0,0,3,3},{3,0,1,1},{3,1,1,1},{3,2,1,1},{4,0,1,2},{4,2,1,1},{5,0,1,1},{5,1,1,2},{0,3,6,1}};
    private boolean isData = true;
    private void initView() {
        final MyBuildLayout buildLayout = (MyBuildLayout) findViewById(R.id.build_layout);
        final BuildLayoutAdapter adapter = new BuildLayoutAdapter();
        buildLayout.setAdapter(adapter);
        adapter.setData(data);
        adapter.notifyDataSetChanged();

        TextView btn = (TextView) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isData = !isData){
                    buildLayout.mySetLayoutSize(6,3);
                    buildLayout.mySetItemSize(DisplayUtil.dim2px(250),DisplayUtil.dim2px(151));
                    buildLayout.mySetDividing(DisplayUtil.dim2px(30));
                    adapter.setData(data);
                }else {
                    buildLayout.mySetLayoutSize(7,4);
                    buildLayout.mySetItemSize(DisplayUtil.dim2px(200),DisplayUtil.dim2px(100));
                    buildLayout.mySetDividing(DisplayUtil.dim2px(40));
                    adapter.setData(data2);
                }
                adapter.notifyDataSetChanged();
            }
        });
        btn.requestFocus();
    }
}
