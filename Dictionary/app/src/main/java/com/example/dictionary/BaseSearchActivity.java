package com.example.dictionary;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dictionary.Adapter.SearchLeftAdapter;
import com.example.dictionary.Adapter.SearchRightAdapter;
import com.example.dictionary.R;
import com.example.dictionary.bean.PinyinBushouBean;
import com.example.dictionary.bean.PinyinBushouWordBean;
import com.example.dictionary.db.DBManager;
import com.example.dictionary.utils.AssetsUtils;
import com.example.dictionary.utils.CommonUtils;
import com.example.dictionary.utils.URLUtils;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

//作为pinyin界面和bushou界面的父类
public class BaseSearchActivity extends BaseRequestNetwork {
    ExpandableListView exLv;
    SmartRefreshLayout refreshLayout;
    GridView gridView;
    public TextView titleTv;

    //pinyin界面ListView中的外部列表(A~Z)
    List<String> groupData;
    //将每组的子类列表存放到这个集合里 (例如A中的ao,an,ang...)
    List<List<PinyinBushouBean.ResultBean>> childData;
    private SearchLeftAdapter adapter;
    int selGroupPos = 0; //表示被点击组的位置
    int selChildPos = 0; //表示选中组中列表的某一个位置

    //右侧GridView的数据源，先声明
    List<PinyinBushouWordBean.ResultBean.ListBean> gridDatas;
    private SearchRightAdapter gridAdapter;

    int totalpage; // 总页数
    int page = 1; // 获取当前页数
    int pagesize = 48; //默认一页获取48条数据
    String word = ""; //点击了左侧的哪个拼音或者部首
    String url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_pinyin);
        exLv = findViewById(R.id.search_pinyin_elv);
        refreshLayout = findViewById(R.id.search_pinyin_refresh_layout);
        gridView = findViewById(R.id.search_pinyin_gv);
        titleTv = findViewById(R.id.search_pinyin_tv_title); //设置title
        //初始化GridView的数据源内容
        initGridDatas();
    }

    //初始化GridView的数据源

    public void initGridDatas() {
        gridDatas = new ArrayList<>();
        //设置适配器
        gridAdapter = new SearchRightAdapter(this, gridDatas);
        gridView.setAdapter(gridAdapter);
    }

    //设置SmartRefreshLayout的监听器
    public void setRefreshListener(int type){
        //设置不启用下拉刷新
        refreshLayout.setEnableRefresh(false);
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                //判断当前的页数是否小于总页数
                if (page < totalpage){
                    page++;
                    if (type == CommonUtils.TYPE_PINYIN) {
                        url = URLUtils.getPinyinUrl(word,page,pagesize);
                    }else if (type == CommonUtils.TYPE_BUSHOU) {
                        url = URLUtils.getBushouUrl(word,page,pagesize);
                    }
                    loadData(url);
                    refreshLayout.finishLoadMore();
                }else {
                    refreshLayout.finishLoadMore();
                }
            }
        });
        //点击GridView中的每一项能够跳转到详情页面
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //跳转到文字查询详情页面
                PinyinBushouWordBean.ResultBean.ListBean bean = gridDatas.get(position);
                String zi = bean.getZi();
                Intent intent = new Intent(getBaseContext(),WordInfoActivity.class);
                intent.putExtra("zi",zi);
                startActivity(intent);
            }
        });
    }

    /**
     * 读取Assets文件夹中的数据，使用GSON解析，将数据分组储存到二维列表当中
     * 参数 assetsName表示传入的文件名，type表示文件类型，pinyin--0,bushou--1
     * */
    public void initData(String assetsName,int type) {
        groupData = new ArrayList<>();
        childData = new ArrayList<>();
        //通过调用AssetsUtils中的getAssetsContent()的到json数据
        String json = AssetsUtils.getAssetsContent(this,assetsName);
        if (!TextUtils.isEmpty(json)) {
            //通过Gson对象解析json数据
            PinyinBushouBean pinyinBushouBean = new Gson().fromJson(json,PinyinBushouBean.class);
            //得到PinyinBushouBean中的List<ResultBean>并赋值给List<PinyinBushouBean.ResultBean>
            List<PinyinBushouBean.ResultBean> list = pinyinBushouBean.getResult();
            //上一步得到的childData格式是{(A,a),(A,ai)...}，需要整理
            List<PinyinBushouBean.ResultBean> groupList = new ArrayList<>(); //声明每组的包含元素集合
            //开始遍历list中的数据，整理到groupList中
            for (int i=0;i<list.size();i++){
                PinyinBushouBean.ResultBean resultBean = list.get(i);
                if (type == CommonUtils.TYPE_PINYIN) {
                    //获取大写字母
                    String pinyin_key = resultBean.getPinyin_key();
                    //判断是否存在于分组的列表中,不存在则添加
                    if (!groupData.contains(pinyin_key)){
                        groupData.add(pinyin_key);
                        if (groupList.size() > 0){
                            childData.add(groupList);
                        }

                        //每一个大写字母对应都是新的一组List
                        groupList = new ArrayList<>();
                        groupList.add(resultBean);
                    }else {
                        groupList.add(resultBean); //大写字母存在，说明还在这组当中，可以直接添加
                    }
                }else if (type == CommonUtils.TYPE_BUSHOU){
                    String bihua = resultBean.getBihua();
                    if (!groupData.contains(bihua)) {
                        groupData.add(bihua);
                        //这里的groupList是新的一组数据，把上一组添加到childData中
                        if (groupList.size() > 0) {
                            childData.add(groupList);
                        }
                        //新的一组，创建子列表
                        groupList = new ArrayList<>();
                        groupList.add(resultBean);
                    }else {
                        //当前笔画存在，不用向groupData中添加
                        groupList.add(resultBean);
                    }
                }
            }
            //循环结束之后,最后一组并没有添加进去，所以手动添加
            childData.add(groupList);

            adapter = new SearchLeftAdapter(this, groupData, childData, type);
            exLv.setAdapter(adapter);
        }
    }

    //加载数据成功时会调用的方法，因为JSON格式相同，解析到相同的集合里，所以就将代码到父类中编写
    @Override
    public void onSuccess(String result) {
        PinyinBushouWordBean wordBean = new Gson().fromJson(result,PinyinBushouWordBean.class);
        PinyinBushouWordBean.ResultBean resultBean = wordBean.getResult();
        totalpage = resultBean.getTotalpage(); //得到当前获取数据的总页数
        List<PinyinBushouWordBean.ResultBean.ListBean> list = resultBean.getList();
        //将数据进行加载
        refreshDataByGV(list);
        //将成功加载的网络数据加入到数据库中
        writeDbByThread(list);
    }
    //将成功加载的网络数据加入到数据库中,为了避免ANR，使用子线程
    public void writeDbByThread(List<PinyinBushouWordBean.ResultBean.ListBean> list) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                DBManager.insertListToPywordtb(list);
            }
        }).start();
    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        super.onError(ex, isOnCallback);
    }

    //更新GridView当中的数据，提示适配器进行加载
    public void refreshDataByGV(List<PinyinBushouWordBean.ResultBean.ListBean> list) {
        if (page == 1) {  //加载了新的拼音或者部首对应的集合
            gridDatas.clear();
            gridDatas.addAll(list);
            gridAdapter.notifyDataSetChanged();
        }else{  //进行上拉加载新的一页获取到的数据，保留原来的数据
            gridDatas.addAll(list);
            gridAdapter.notifyDataSetChanged();
        }
    }

    //设置ExpandableListView的监听器
    public void setExLvListener(int type){
        //设置组的点击事件
        exLv.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                //设置被点击时只改变背景颜色，不改变数据
                adapter.setSelectGroupPos(groupPosition);
                adapter.notifyDataSetInvalidated();
                //获取被点击位置的内容
                getDataAlterWord(type);
                return false;
            }
        });
        //设置组内列表的点击事件
        exLv.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                adapter.setSelectGroupPos(groupPosition);
                adapter.setSelectChildPos(childPosition);
                selGroupPos = groupPosition;

                int groupSize = childData.get(selGroupPos).size();
                if (groupSize <= selChildPos){
                    selChildPos = groupSize-1;
                    adapter.setSelectChildPos(selChildPos);
                }
                //设置被点击时只改变背景颜色，不改变数据
                adapter.notifyDataSetInvalidated();
                selChildPos = childPosition;
                //网络加载右边GridView的内容
                getDataAlterWord(type);
                return false;
            }
        });
    }

    //改变了左边的选中，从网上获取对应选中的结果，显示在右边
    private void getDataAlterWord(int type) {
        //获取选中组
        List<PinyinBushouBean.ResultBean> groupList = childData.get(selGroupPos);
        page = 1;
        PinyinBushouBean.ResultBean bean = groupList.get(selChildPos);
        if (type == CommonUtils.TYPE_PINYIN){
            word = bean.getPinyin();
            url = URLUtils.getPinyinUrl(word,page,pagesize);
        }else if(type == CommonUtils.TYPE_BUSHOU){
            word = bean.getBushou();
            url = URLUtils.getBushouUrl(word,page,pagesize);
        }
        loadData(url);
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.search_pinyin_iv_back:
                finish();
                break;
        }
    }
}
