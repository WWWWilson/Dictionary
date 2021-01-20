package com.example.dictionary.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.dictionary.R;
import com.example.dictionary.bean.PinyinBushouBean;
import com.example.dictionary.utils.CommonUtils;

import java.util.List;

public class SearchLeftAdapter extends BaseExpandableListAdapter {

    Context context;
    //表示分组的列表
    List<String> groupData;
    //将每组对应的子类列表存放到这个集合当中
    List<List<PinyinBushouBean.ResultBean>> childData;
    //创建一个LayoutInflater对象
    LayoutInflater inflater;

    int type;  //拼音和部首的activity都用这个适配器，通过设定这个属性进行区分

    //表示被选中的组的位置，和被选中组里面item的位置
    int selectGroupPos = 0,selectChildPos = 0;

    public void setSelectGroupPos(int selectGroupPos) {
        this.selectGroupPos = selectGroupPos;
    }

    public void setSelectChildPos(int selectChildPos) {
        this.selectChildPos = selectChildPos;
    }

    public SearchLeftAdapter(Context context, List<String> groupData, List<List<PinyinBushouBean.ResultBean>> childData,int type) {
        this.context = context;
        this.groupData = groupData;
        this.childData = childData;
        this.type = type;
        inflater = LayoutInflater.from(context); //初始化布局加载器
    }

    //获取分组的数量
    @Override
    public int getGroupCount() {
        return groupData.size();
    }

    //获取指定分组当中有几个item
    @Override
    public int getChildrenCount(int groupPosition) {
        return childData.get(groupPosition).size();
    }

    //获取分组指定位置的数据
    @Override
    public Object getGroup(int groupPosition) {
        return groupData.get(groupPosition);
    }

    //指定第几组第几个中的对象
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childData.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_exlv_group,null);
            holder = new GroupViewHolder(convertView);
            //进行convertView标签的绑定
            convertView.setTag(holder);
        }else{
            holder = (GroupViewHolder) convertView.getTag();
        }
        //获取指定位置的数据
        String word = groupData.get(groupPosition);

        if (type == CommonUtils.TYPE_PINYIN) {
            holder.groupTv.setText(word);
        }else {
            holder.groupTv.setText(word + "画");
        }

        //设置选中的位置颜色会变，判断是否被选中
        if (selectGroupPos == groupPosition) {
            convertView.setBackgroundColor(Color.BLUE);
            holder.groupTv.setTextColor(Color.RED);
        }else{
            convertView.setBackgroundResource(R.color.grey_F3F3F3);
            holder.groupTv.setTextColor(Color.BLACK);
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_exlv_child,null);
            holder = new ChildViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ChildViewHolder) convertView.getTag();
        }
        PinyinBushouBean.ResultBean resultBean = childData.get(groupPosition).get(childPosition);
        if (type == CommonUtils.TYPE_PINYIN) {
            holder.childTv.setText(resultBean.getPinyin());
        }else {
            holder.childTv.setText(resultBean.getBushou());
        }
        //设置改变选中的颜色
        if (selectGroupPos == groupPosition && selectChildPos == childPosition) {
            convertView.setBackgroundColor(Color.WHITE);
            holder.childTv.setTextColor(Color.RED);
        }else {
            convertView.setBackgroundResource(R.color.grey_F3F3F3);
            holder.childTv.setTextColor(Color.BLACK);
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    //创建ViewHolder的类
    class GroupViewHolder{
        TextView groupTv;
        //传递View
        public GroupViewHolder(View view){
            groupTv = view.findViewById(R.id.item_exlv_group_tv);
        }
    }

    class ChildViewHolder{
        TextView childTv;
        //传递View
        public ChildViewHolder(View view){
            childTv = view.findViewById(R.id.item_exlv_child_tv);
        }
    }
}
