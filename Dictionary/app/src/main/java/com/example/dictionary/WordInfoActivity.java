package com.example.dictionary;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dictionary.bean.WordBean;
import com.example.dictionary.db.DBManager;
import com.example.dictionary.utils.URLUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

//文字详情页面
public class WordInfoActivity extends BaseRequestNetwork {
    TextView ziTv,pinyinTv,bihuaTv,wubiTv,bushouTv,basicExplainTv,detailTv;
    ListView explainLv;
    ImageView collectionIv,backIv;
    String zi;
    List<String>  mDatas; //ListView的数据源
    ArrayAdapter<String> adapter;

    List<String> explain;
    List<String> detail;
    //设置标志位，表示汉字是否被收藏
    boolean isCollect = false;
    boolean isExist = false;  //判断这个汉字是否存在


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_info);
        //接受上一个页面传递过来的对象
        Intent intent = getIntent();
        zi = intent.getStringExtra("zi");
        String url = URLUtils.getWordUrl(zi);
        initView();
        //初始化ListView显示的数据源
        mDatas = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this, R.layout.item_wordinfo_explain_lv,R.id.item_wordinfo_tv,mDatas);
        explainLv.setAdapter(adapter);
        //加载网络数据
        loadData(url);

        //调用判断是否已经收藏了的方法
        isExist = DBManager.isExistZiInCollectwordtable(zi);
        isCollect = isExist;  //记录初始状态
        setCollectIvStyle();
    }

    /**
     * 根据收藏的状态，改变星星的颜色
     * */
    private void setCollectIvStyle() {
        if (isCollect) {
            collectionIv.setBackgroundColor(Color.RED);
        }else{
            collectionIv.setBackgroundColor(Color.BLACK);
        }
    }

    @Override
    public void onSuccess(String json) {
        WordBean wordBean = new Gson().fromJson(json, WordBean.class);
        WordBean.ResultBean resultBean = wordBean.getResult();
        //插入数据库
        DBManager.insertWordToWordtb(resultBean);
        //将数据显示在ListView上
        notifyView(resultBean);
    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        //联网失败，获取数据库当中字的信息
        WordBean.ResultBean bean = DBManager.queryWordFromWordtb(zi);
        if (bean != null) {
            notifyView(bean);
        }
    }

    /**
     * 更新控件信息
     * */
    @SuppressLint("SetTextI18n")
    private void notifyView(WordBean.ResultBean resultBean) {
        ziTv.setText(resultBean.getZi());
        pinyinTv.setText(resultBean.getPinyin());
        wubiTv.setText("五笔:" + resultBean.getWubi());
        bihuaTv.setText("笔画:" + resultBean.getBihua());
        bushouTv.setText("部首:" + resultBean.getBushou());
        explain = resultBean.getJijie();
        detail = resultBean.getXiangjie();

        //默认一进去就显示基本解释
        mDatas.clear();
        mDatas.addAll(explain);
        adapter.notifyDataSetChanged();
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.wordinfo_iv_back:
                finish();
                break;
            case R.id.wordinfo_iv_collection:
                isCollect = !isCollect; //将收藏的状态取反
                setCollectIvStyle();
                break;
            case R.id.wordinfo_tv_explain:
                basicExplainTv.setTextColor(Color.RED);
                detailTv.setTextColor(Color.BLACK);
                mDatas.clear();
                mDatas.addAll(explain);
                adapter.notifyDataSetChanged();
                break;
            case R.id.wordinfo_tv_detail_explain:
                detailTv.setTextColor(Color.RED);
                basicExplainTv.setTextColor(Color.BLACK);
                mDatas.clear();
                mDatas.addAll(detail);
                adapter.notifyDataSetChanged();
                break;
        }
    }

    private void initView() {
        ziTv = findViewById(R.id.wordinfo_tv_zi);
        pinyinTv = findViewById(R.id.wordinfo_tv_pinyin);
        bihuaTv = findViewById(R.id.wordinfo_tv_bihua);
        wubiTv = findViewById(R.id.wordinfo_tv_wubi);
        bushouTv = findViewById(R.id.wordinfo_tv_bushou);
        basicExplainTv = findViewById(R.id.wordinfo_tv_explain);
        detailTv = findViewById(R.id.wordinfo_tv_detail_explain);
        explainLv = findViewById(R.id.wordinfo_lv_explain);
        collectionIv = findViewById(R.id.wordinfo_iv_collection);
        backIv = findViewById(R.id.wordinfo_iv_back);
    }

    /**
     * 当Activity销毁时会调用的方法
     * 将汉字进行插入或者删除
     * */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //原来数据收藏，现在取消收藏
        if (isExist && !isCollect){
            DBManager.deleteZiToCollectwordtable(zi);
            //原来不存在，现在收藏
        }else if(!isExist && isCollect){
            DBManager.insertZiToCollectwordtable(zi);
        }
    }
}