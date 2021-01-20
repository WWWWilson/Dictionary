package com.example.dictionary.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.example.dictionary.bean.ChengyuBean;
import com.example.dictionary.bean.PinyinBushouWordBean;
import com.example.dictionary.bean.WordBean;
import com.example.dictionary.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

public class DBManager {

    private static SQLiteDatabase db;

    public static void initDB(Context context){
        //创建DBOpenHelper对象
        DBOpenHelper helper = new DBOpenHelper(context);
        //得到db对象
        db = helper.getWritableDatabase();
    }

    /**
     * 执行插入数据到pinyinwordtable表中
     * 插入一个对象的方法
     * */

    public static void insertWordToPywordtb(PinyinBushouWordBean.ResultBean.ListBean bean){
        ContentValues values = new ContentValues();
        values.put("id",bean.getId());
        values.put("zi",bean.getZi());
        values.put("py",bean.getPy());
        values.put("wubi",bean.getWubi());
        values.put("pinyin",bean.getPinyin());
        values.put("bushou",bean.getBushou());
        values.put("bihua",bean.getBihua());
        //执行插入数据的方法
        db.insert(CommonUtils.TABLE_PYWORD,null,values);
    }

    /**
     * 插入很多数据到pinyinwordtable表中
     * 插入了多个对象所在的集合
     * */
    public static void insertListToPywordtb(List<PinyinBushouWordBean.ResultBean.ListBean> listBean){
        if (listBean.size() > 0) {
            for (int i=0;i < listBean.size();i++){
                //获取集合当中的每一个bean对象
                PinyinBushouWordBean.ResultBean.ListBean bean = listBean.get(i);
                //调用单个对象插入的方法
                try {
                    insertWordToPywordtb(bean);

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("Tag", "insertListToPywordtb: " + bean.getZi() + "已存在");
                }

            }
        }

    }

    /**
     *查询pinyinwordtable表当中指定拼音的的数据
     *
     * */

    //limit表示从多少到多少  一开始是0~48,一页有48个字,然后从48开始到96，96~144
    public static List<PinyinBushouWordBean.ResultBean.ListBean> queryPywordFromPywordtb(String pinyin,int page,int pagesize){
        List<PinyinBushouWordBean.ResultBean.ListBean> list = new ArrayList<>();
        String sql = "select * from pinyinwordtable where py=? or py like? or py like? or py like? limit ?,?";
        int startPagesize = (page-1) * pagesize;
        int endPagesize = page * pagesize;
        //百分号代表可以增加新的内容
        String type1 = pinyin+ ",%";
        String type2 = "%,"+pinyin+ ",%";
        String type3 = "%,"+pinyin;
        Cursor cursor = db.rawQuery(sql,new String[] {pinyin,type1,type2,type3 + "",startPagesize + "",endPagesize + ""});
        if (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex("id"));
            String zi = cursor.getString(cursor.getColumnIndex("zi"));
            String py = cursor.getString(cursor.getColumnIndex("py"));
            String wubi = cursor.getString(cursor.getColumnIndex("wubi"));
            String pinyin1 = cursor.getString(cursor.getColumnIndex("pinyin"));
            String bushou = cursor.getString(cursor.getColumnIndex("bushou"));
            String bihua = cursor.getString(cursor.getColumnIndex("bihua"));
            PinyinBushouWordBean.ResultBean.ListBean bean = new PinyinBushouWordBean.ResultBean.ListBean(id, zi, py, wubi, pinyin1, bushou, bihua);
            list.add(bean);
        }
        return list;
    }

    /**
     * 查询pywordtb表当中指定部首的数据
     * */
    public static List<PinyinBushouWordBean.ResultBean.ListBean> queryBsWordFromPywordtb(String bs,int page,int pagesize){
        List<PinyinBushouWordBean.ResultBean.ListBean>list = new ArrayList<>();
        // 0,48   48,48+48    96,96+48
        String sql = "select * from pywordtb where bushou=? limit ?,?";
        int start = (page-1)*pagesize;
        int end = page*pagesize;
        Cursor cursor = db.rawQuery(sql, new String[]{bs, start + "", end + ""});
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex("id"));
            String zi = cursor.getString(cursor.getColumnIndex("zi"));
            String py1 = cursor.getString(cursor.getColumnIndex("py"));
            String wubi = cursor.getString(cursor.getColumnIndex("wubi"));
            String pinyin = cursor.getString(cursor.getColumnIndex("pinyin"));
            String bushou = cursor.getString(cursor.getColumnIndex("bushou"));
            String bihua = cursor.getString(cursor.getColumnIndex("bihua"));
            PinyinBushouWordBean.ResultBean.ListBean bean = new PinyinBushouWordBean.ResultBean.ListBean(id, zi, py1, wubi, pinyin, bushou, bihua);
            list.add(bean);
        }
        return list;
    }

    /**
     * 插入汉字到汉字详情表当中
     * */
    public static void insertWordToWordtb(WordBean.ResultBean bean){
        ContentValues values = new ContentValues();
        values.put("id",bean.getId());
        values.put("zi",bean.getZi());
        values.put("py",bean.getPy());
        values.put("wubi",bean.getWubi());
        values.put("pinyin",bean.getPinyin());
        values.put("bushou",bean.getBushou());
        values.put("bihua",bean.getBihua());
        //将集合转换成字符串类型进行插入
        String jijie = listToString(bean.getJijie());
        values.put("jijie",jijie);
        String xiangjie = listToString(bean.getXiangjie());
        values.put("xiangjie",xiangjie);
        db.insert(CommonUtils.TABLE_WORD,null,values);
    }

    /**
     * 根据传入的汉字，查找对应信息组成的对象
     * */
    public static WordBean.ResultBean queryWordFromWordtb(String word){
        String sql = "select * from wordtable where zi=?";
        Cursor cursor = db.rawQuery(sql, new String[]{word});
        if (cursor.moveToFirst()) {
            String id = cursor.getString(cursor.getColumnIndex("id"));
            String zi = cursor.getString(cursor.getColumnIndex("zi"));
            String py = cursor.getString(cursor.getColumnIndex("py"));
            String wubi = cursor.getString(cursor.getColumnIndex("wubi"));
            String pinyin1 = cursor.getString(cursor.getColumnIndex("pinyin"));
            String bushou = cursor.getString(cursor.getColumnIndex("bushou"));
            String bihua = cursor.getString(cursor.getColumnIndex("bihua"));
            String jijie = cursor.getString(cursor.getColumnIndex("jijie"));
            String xiangjie = cursor.getString(cursor.getColumnIndex("xiangjie"));
            List<String> jijieList = stringToList(jijie);
            List<String> xiangjieList = stringToList(xiangjie);
            WordBean.ResultBean bean = new WordBean.ResultBean(id, zi, py, wubi, pinyin1, bushou, bihua, jijieList, xiangjieList);

            return bean;
        }
        return null;
    }

    //将字符串转换成List集合的方法
    public static List<String> stringToList(String msg){
        List<String> list = new ArrayList<>();
        if (!TextUtils.isEmpty(msg)) {
            String[] arr = msg.split("\\|");
            for (int i=0;i<arr.length;i++){
                String string = arr[i].trim();
                if (!TextUtils.isEmpty(string)){
                    list.add(string);
                }
            }
        }
        return list;
    }

    //将List集合转换成字符串的方法
    public static String listToString(List<String> list){
        StringBuilder builder = new StringBuilder();
        if (list!=null && !list.isEmpty()) {
            for (int i=0;i<list.size();i++){
                String msg = list.get(i);
                msg += "|";
                builder.append(msg);
            }
        }
        return builder.toString();
    }

    /**
     * 插入数据到成语表当中
     * */
    public static void insertChengyuToCytb(ChengyuBean.ResultBean bean){
        ContentValues values = new ContentValues();
        values.put("chengyu",bean.getChengyu());
        values.put("bushou",bean.getBushou());
        values.put("head",bean.getHead());
        values.put("pinyin",bean.getPinyin());
        values.put("chengyujs",bean.getChengyujs());
        values.put("from_",bean.getFrom_());
        values.put("example",bean.getExample());
        values.put("yufa",bean.getYufa());
        values.put("ciyujs",bean.getCiyujs());
        values.put("yinzhengjs",bean.getYinzhengjs());

        String tongyi = listToString(bean.getTongyi());
        values.put("tongyi",tongyi);
        String fanyi = listToString(bean.getFanyi());
        values.put("fanyi",fanyi);
        db.insert(CommonUtils.TABLE_CHENGYU,null,values);
    }

    /**
     * 根据传入的成语查看详情内容
     * */
    public static ChengyuBean.ResultBean queryCyFromCytb(String chengyu){
        String sql = "select * from chengyutable where chengyu = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{chengyu});
        if (cursor.moveToFirst()) {
            String chengyu1 = cursor.getString(cursor.getColumnIndex("chengyu"));
            String bushou = cursor.getString(cursor.getColumnIndex("bushou"));
            String head = cursor.getString(cursor.getColumnIndex("head"));
            String pinyin = cursor.getString(cursor.getColumnIndex("pinyin"));
            String chengyujs = cursor.getString(cursor.getColumnIndex("chengyujs"));
            String from = cursor.getString(cursor.getColumnIndex("from_"));
            String example = cursor.getString(cursor.getColumnIndex("example"));
            String yufa = cursor.getString(cursor.getColumnIndex("yufa"));
            String ciyujs = cursor.getString(cursor.getColumnIndex("ciyujs"));
            String yinzhengjs = cursor.getString(cursor.getColumnIndex("yinzhengjs"));
            String tongyi = cursor.getString(cursor.getColumnIndex("tongyi"));
            String fanyi = cursor.getString(cursor.getColumnIndex("fanyi"));
            List<String> tongyiList = stringToList(tongyi);
            List<String> fanyiList = stringToList(fanyi);
            ChengyuBean.ResultBean bean = new ChengyuBean.ResultBean(chengyu1,bushou,head,pinyin,chengyujs,
                    from,example,yufa,ciyujs,yinzhengjs,tongyiList,fanyiList);
            return bean;
        }
        return null;
    }

    /**
     * 查询成语表中所有的记录
     * */
    public static List<String> queryAllCyFromCytb(){
        List<String> chengyuList = new ArrayList<>();
        String sql = "select chengyu from cyutb";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()){
            String chengyu = cursor.getString(cursor.getColumnIndex("chengyu"));
            chengyuList.add(chengyu);
        }

        return chengyuList;
    }

    //向收藏汉字的表格当中插入数据
    public static void insertZiToCollectwordtable(String zi){
        ContentValues values = new ContentValues();
        values.put("zi",zi);
        db.insert(CommonUtils.TABLE_COLLECT_WORD,null,values);
    }

    //删除收藏表格当中的数据
    public static void deleteZiToCollectwordtable(String zi){
        String sql = "delete from collectwordtable where zi=?";
        db.execSQL(sql,new Object[]{zi});
    }

    //查找此汉字是否收藏在表格当中
    public static boolean isExistZiInCollectwordtable(String zi){
        String sql = "select * from collectwordtable where zi=?";
        Cursor cursor = db.rawQuery(sql, new String[]{zi});
        if (cursor.getCount() > 0) {
            return true;
        }
        return false;
    }

    //查找收藏汉字表中的所有数字
    public static List<String> queryAllInCollectWordTb(){
        String sql = "select * from collectwordtable ";
        Cursor cursor = db.rawQuery(sql,null);
        List<String> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            String zi = cursor.getString(cursor.getColumnIndex("zi"));
            list.add(zi);
        }

        return list;
    }

    //向收藏成语的表格当中插入数据
    public static void insertChengyuToCollectwordtable(String chengyu){
        ContentValues values = new ContentValues();
        values.put("chengyu",chengyu);
        db.insert(CommonUtils.TABLE_COLLECT_CHENGYU,null,values);
    }

    //删除收藏表格当中的成语
    public static void deleteChengyuToCollectwordtable(String chengyu){
        String sql = "delete from collectchengyutable where chengyu=?";
        db.execSQL(sql,new Object[]{chengyu});
    }

    //查找此成语是否收藏在表格当中
    public static boolean isExistChengyuInCollectwordtable(String chengyu){
        String sql = "select * from collectchengyutable where chengyu=?";
        Cursor cursor = db.rawQuery(sql, new String[]{chengyu});
        if (cursor.getCount() > 0) {
            return true;
        }
        return false;
    }
    //查找收藏成语表中的所有成语
    public static List<String> queryAllInCollectChengyuTb(){
        String sql = "select * from collectchengyutable";
        Cursor cursor = db.rawQuery(sql,null);
        List<String> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            String chengyu = cursor.getString(cursor.getColumnIndex("chengyu"));
            list.add(chengyu);
        }

        return list;
    }
}
