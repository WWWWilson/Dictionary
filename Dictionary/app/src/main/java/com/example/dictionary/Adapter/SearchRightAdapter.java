package com.example.dictionary.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.dictionary.R;
import com.example.dictionary.bean.PinyinBushouWordBean;

import java.util.List;

//拼音查询，部首查询，页面右侧GridView的适配器
public class SearchRightAdapter extends BaseAdapter {
    Context context;
    List<PinyinBushouWordBean.ResultBean.ListBean> mDatas;
    LayoutInflater inflater;

    public SearchRightAdapter(Context context, List<PinyinBushouWordBean.ResultBean.ListBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_search_gridview,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        PinyinBushouWordBean.ResultBean.ListBean listBean = mDatas.get(position);
        String zi = listBean.getZi();
        holder.tv.setText(zi);

        return convertView;
    }

    class ViewHolder{

        TextView tv;
        public ViewHolder(View view){
            tv = view.findViewById(R.id.item_gv_tv);
        }
    }
}
