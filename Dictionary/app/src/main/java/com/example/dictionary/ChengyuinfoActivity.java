package com.example.dictionary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dictionary.bean.ChengyuBean;
import com.example.dictionary.db.DBManager;
import com.example.dictionary.utils.MyGridView;
import com.example.dictionary.utils.URLUtils;
import com.google.gson.Gson;

import org.xutils.DbManager;

import java.util.ArrayList;
import java.util.List;

public class ChengyuinfoActivity extends BaseRequestNetwork {

    TextView ziTv1,ziTv2,ziTv3,ziTv4,pinyinTv,explainTv,fromTv,exampleTv,yufaTv,yinzhengTv,yinhanTv;
    MyGridView tongyiGv,fanyiGv;
    ImageView collectIv;
    String chengyu;
    List<String> tongyiList,fanyiList;  //GridView的数据源
    ArrayAdapter<String> tongyiAdapter;
    ArrayAdapter<String> fanyiAdapter;

    //设置标志位，表示成语是否被收藏
    boolean isCollect = false;
    boolean isExist = false;  //判断这个成语是否存在

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chengyuinfo);
        initView();
        initAdapter();
        //获取上一个页面Intent传递的数据
        Intent intent = getIntent();
        chengyu = intent.getStringExtra("chengyu");

        String url = URLUtils.getChengyuUrl(chengyu);
        loadData(url);

        isExist = DBManager.isExistChengyuInCollectwordtable(chengyu);
        isCollect = isExist;

        setCollectIvStyle();
    }

    private void setCollectIvStyle() {
        if (isCollect) {
            collectIv.setBackgroundColor(Color.RED);
        }else{
            collectIv.setBackgroundColor(Color.BLACK);
        }
    }

    //为GridView设置加载数据的适配器和数据源
    private void initAdapter() {
        tongyiList = new ArrayList<>();
        fanyiList = new ArrayList<>();
        tongyiAdapter = new ArrayAdapter<>(this, R.layout.item_wordinfo_explain_lv, R.id.item_wordinfo_tv, tongyiList);
        fanyiAdapter = new ArrayAdapter<>(this, R.layout.item_wordinfo_explain_lv, R.id.item_wordinfo_tv, fanyiList);
        tongyiGv.setAdapter(tongyiAdapter);
        fanyiGv.setAdapter(fanyiAdapter);
    }

    @Override
    public void onSuccess(String result) {
        ChengyuBean chengyuBean = new Gson().fromJson(result, ChengyuBean.class);
        ChengyuBean.ResultBean resultBean = chengyuBean.getResult();
        if (resultBean!=null) {
            //因为数据源当中不包括成语本身，但是后期要插入数据库，所以需要保存这个成语
            resultBean.setChengyu(chengyu);
            //插入到数据库当中
            DBManager.insertChengyuToCytb(resultBean);
            //显示数据
            showDataToView(resultBean);
        }else{
            Toast.makeText(this,"您输入的成语此成语无法查到，请重新输入",Toast.LENGTH_SHORT).show();

        }
    }

    /**
     * 网络加载数据失败时，会调用的方法
     * */
    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        super.onError(ex, isOnCallback);
        //获取数据库中缓存的数据
        ChengyuBean.ResultBean bean = DBManager.queryCyFromCytb(chengyu);
        if (bean!=null) {
            showDataToView(bean);
        }
    }

    //将获取到的数据显示到View上
    private void showDataToView(ChengyuBean.ResultBean resultBean) {
        String chengyu = resultBean.getChengyu();
        ziTv1.setText(String.valueOf(chengyu.charAt(0)));
        ziTv2.setText(String.valueOf(chengyu.charAt(1)));
        ziTv3.setText(String.valueOf(chengyu.charAt(2)));
        ziTv4.setText(String.valueOf(chengyu.charAt(3)));
        pinyinTv.setText("拼音：" + resultBean.getPinyin());
        explainTv.setText(resultBean.getChengyujs());
        fromTv.setText(resultBean.getFrom_());
        exampleTv.setText(resultBean.getExample());
        yufaTv.setText(resultBean.getYufa());
        yinzhengTv.setText(resultBean.getYinzhengjs());

        String ciyujs = resultBean.getCiyujs();
        if (!TextUtils.isEmpty(ciyujs)) {
            ciyujs.replace("]","\n").replace("[","").replace(":","");
            yinhanTv.setText(ciyujs);
        }

        List<String> tongyi = resultBean.getTongyi();
        //判断是否有同义词
        if (tongyi!=null && tongyi.size()!=0) {
            tongyiList.addAll(tongyi);
            tongyiAdapter.notifyDataSetChanged();
        }
        List<String> fanyi = resultBean.getFanyi();
        if (fanyi!=null && fanyi.size()!=0) {
            fanyiList.addAll(fanyi);
            fanyiAdapter.notifyDataSetChanged();
        }
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.chengyuinfo_iv_back:
                finish();
                break;
            case R.id.chengyuinfo_iv_collection:
                isCollect = !isCollect;
                setCollectIvStyle();
                break;
        }
    }

    private void initView() {
        ziTv1 = findViewById(R.id.chengyu_info_tv_zi1);
        ziTv2 = findViewById(R.id.chengyu_info_tv_zi2);
        ziTv3 = findViewById(R.id.chengyu_info_tv_zi3);
        ziTv4 = findViewById(R.id.chengyu_info_tv_zi4);
        pinyinTv = findViewById(R.id.chengyu_info_tv_pinyin);
        explainTv = findViewById(R.id.chengyu_info_tv_explain);
        exampleTv = findViewById(R.id.chengyu_info_tv_example);
        fromTv = findViewById(R.id.chengyu_info_tv_from);
        yufaTv = findViewById(R.id.chengyu_info_tv_yufa);
        yinzhengTv = findViewById(R.id.chengyu_info_tv_yinzheng);
        yinhanTv = findViewById(R.id.chengyu_info_tv_yinhan);
        tongyiGv = findViewById(R.id.chengyu_info_gv_tongyi);
        fanyiGv = findViewById(R.id.chengyu_info_gv_fanyi);
        collectIv = findViewById(R.id.chengyuinfo_iv_collection);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //原来数据收藏，现在取消收藏
        if (isExist && !isCollect){
            DBManager.deleteZiToCollectwordtable(chengyu);
            //原来不存在，现在收藏
        }else if(!isExist && isCollect){
            DBManager.insertZiToCollectwordtable(chengyu);
        }
    }
}