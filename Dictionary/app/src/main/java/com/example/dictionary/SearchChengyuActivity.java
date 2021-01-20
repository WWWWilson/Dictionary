package com.example.dictionary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;

import com.example.dictionary.db.DBManager;

import java.util.ArrayList;
import java.util.List;

public class SearchChengyuActivity extends AppCompatActivity {

    EditText chengyuEt;
    GridView chengyuGv;
    List<String> mDatas;
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_chengyu);

        chengyuEt = findViewById(R.id.search_chengyu_et);
        chengyuGv = findViewById(R.id.search_chengyu_gv);
        mDatas = new ArrayList<>();
        //创建适配器对象
        adapter = new ArrayAdapter<String>(this,R.layout.activity_search_chengyu,R.id.item_search_chengyu_tv,mDatas);
        chengyuGv.setAdapter(adapter);
        //设置GridView的点击事件
        setGVListener();
    }

    /**
     * GridView每一个item的点击事件的方法
     * */
    private void setGVListener() {
        chengyuGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String msg = mDatas.get(position);
                startPage(msg);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        chengyuEt.setText("");
        initData();
    }

    /**
     * 初始化GridView显示的历史数据
     * */
    private void initData() {
        mDatas.clear();
        List<String> list = DBManager.queryAllCyFromCytb();
        mDatas.addAll(list);
        adapter.notifyDataSetChanged();
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.search_chengyu_iv_back:
                finish();
                break;
            case R.id.search_chengyu_iv_search:
                String text = chengyuEt.getText().toString();
                if (TextUtils.isEmpty(text)) {
                    return;
                }
                //跳转到成语详情页面，将输入内容传递过去
                startPage(text);
                break;
        }
    }
    /**
     * 携带成语跳转到成语详情页面
     * */
    private void startPage(String text) {
        Intent intent = new Intent(this,ChengyuinfoActivity.class);
        intent.putExtra("chengyu",text);
        startActivity(intent);
    }
}