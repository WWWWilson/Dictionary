package com.example.dictionary;

import android.os.Bundle;

import com.example.dictionary.bean.PinyinBushouWordBean;
import com.example.dictionary.db.DBManager;
import com.example.dictionary.utils.CommonUtils;
import com.example.dictionary.utils.URLUtils;

import java.util.List;

public class SearchBushouActivity extends BaseSearchActivity {

    String url; //获取指定部首对应的网址

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        titleTv.setText(R.string.main_tv_bushou);
        initData(CommonUtils.FILE_BUSHOU,CommonUtils.TYPE_BUSHOU);
        setExLvListener(CommonUtils.TYPE_BUSHOU);
        exLv.expandGroup(0); //默认展开第一组
        word = "丨"; //默认进去获取的是第一个
        url = URLUtils.getBushouUrl(word,page,pagesize);
        //加载网络数据
        loadData(url);
        setRefreshListener(CommonUtils.TYPE_BUSHOU);
    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        List<PinyinBushouWordBean.ResultBean.ListBean> list = DBManager.queryBsWordFromPywordtb(word, page, pagesize);
        refreshDataByGV(list);
    }
}