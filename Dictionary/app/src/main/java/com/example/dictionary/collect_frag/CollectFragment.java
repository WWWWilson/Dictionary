package com.example.dictionary.collect_frag;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.example.dictionary.ChengyuinfoActivity;
import com.example.dictionary.R;
import com.example.dictionary.WordInfoActivity;
import com.example.dictionary.db.DBManager;

import java.util.ArrayList;
import java.util.List;

public class CollectFragment extends Fragment {

    String type;

    GridView fragGv;
    List<String> mDatas;
    ArrayAdapter<String> adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_collect, container, false);
        Bundle bundle = getArguments();
        type = bundle.getString("type"); //获取当前Fragment显示的数据类型
        fragGv = view.findViewById(R.id.frag_gv);
        mDatas = new ArrayList<>();
        adapter = new ArrayAdapter<String>(getContext(), R.layout.item_search_gridview, R.id.item_gv_tv, mDatas);
        fragGv.setAdapter(adapter);
        //设置GridView的点击事件
        setGVListener();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        List<String> list;
        mDatas.clear();
        if (type.equals("汉字")) {
            list = DBManager.queryAllInCollectWordTb();
        }else {
            list = DBManager.queryAllInCollectChengyuTb();
        }
        mDatas.addAll(list);
        adapter.notifyDataSetChanged();
    }

    private void setGVListener() {
        fragGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (type.equals("汉字")) {
                    String zi = mDatas.get(position);
                    Intent intent = new Intent(getActivity(), WordInfoActivity.class);
                    intent.putExtra("zi",zi);
                    startActivity(intent);
                }else{
                    String chengyu = mDatas.get(position);
                    Intent intent = new Intent(getActivity(), ChengyuinfoActivity.class);
                    intent.putExtra("chengyu",chengyu);
                    startActivity(intent);
                }
            }
        });
    }
}