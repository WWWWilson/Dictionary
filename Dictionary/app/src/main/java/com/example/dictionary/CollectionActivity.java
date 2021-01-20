package com.example.dictionary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;

import com.example.dictionary.Adapter.CollectFragmentAdapter;
import com.example.dictionary.collect_frag.CollectFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class CollectionActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    String[] title = {"汉字","成语"};
    List<Fragment> mDatas;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        tabLayout = findViewById(R.id.collect_tabs);
        viewPager = findViewById(R.id.collect_vp);
        initPager();
    }

    /**
     * 初始化ViewPager页面的操作
     * */
    private void initPager() {
        mDatas = new ArrayList<>();
        for (int i=0;i<title.length;i++){
            Fragment frag = new CollectFragment();
            Bundle bundle = new Bundle();
            bundle.putString("type",title[i]);
            frag.setArguments(bundle);
            mDatas.add(frag);
        }
        CollectFragmentAdapter adapter = new CollectFragmentAdapter(getSupportFragmentManager(), mDatas, title);
        viewPager.setAdapter(adapter);
        //将上下绑定
        tabLayout.setupWithViewPager(viewPager);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.collect_iv_back:
                finish();
                break;
        }
    }
}