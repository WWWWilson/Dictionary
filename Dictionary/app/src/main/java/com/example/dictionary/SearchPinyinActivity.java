package com.example.dictionary;

import android.os.Bundle;

import com.example.dictionary.bean.PinyinBushouWordBean;
import com.example.dictionary.db.DBManager;
import com.example.dictionary.utils.CommonUtils;
import com.example.dictionary.utils.URLUtils;

import java.util.List;

public class SearchPinyinActivity extends BaseSearchActivity {

    String url; //获取指定拼音对应的网址

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData(CommonUtils.FILE_PINYIN,CommonUtils.TYPE_PINYIN);
        setExLvListener(CommonUtils.TYPE_PINYIN);
        exLv.expandGroup(0); //默认展开第一组
        word = "a"; //默认进去获取的是第一个 a
        url = URLUtils.getPinyinUrl(word,page,pagesize);
        //加载网络数据
        loadData(url);
        setRefreshListener(CommonUtils.TYPE_PINYIN);
    }

    /**
     * 网络获取失败时会调用的接口
     * 因为拼音查询和部首查询使用的获取数据的方法不一样
     * 所以需要分开写
     * 所以就把inError的方法写入子类当中
     * */
    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        List<PinyinBushouWordBean.ResultBean.ListBean> list = DBManager.queryPywordFromPywordtb(word, page, pagesize);
        refreshDataByGV(list);
    }
}