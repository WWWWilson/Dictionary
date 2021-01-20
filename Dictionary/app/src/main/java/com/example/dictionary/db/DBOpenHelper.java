package com.example.dictionary.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBOpenHelper extends SQLiteOpenHelper {

    public DBOpenHelper(@Nullable Context context) {
        super(context, "dictionary.db", null, 1);

    }

    //unique not null 代表唯一性切不能为空
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table pinyinwordtable(_id integer primary key autoincrement," +
                "id varchar(20),zi varchar(4) unique not null,py varchar(10),wubi varchar(10)," +
                "pinyin varchar(10),bushou varchar(4),bihua integer )";
        //执行创建数据库
        db.execSQL(sql);
        //创建文字详情的表格
        sql = "create table wordtable(_id integer primary key autoincrement," +
                "id varchar(20),zi varchar(4) unique not null,py varchar(10),wubi varchar(10)," +
                "pinyin varchar(10),bushou varchar(4),bihua integer," +
                "jijie text,xiangjie text )";
        db.execSQL(sql);

        sql = "create table chengyutable(_id integer primary key autoincrement," +
                "chengyu varchar(10) unique not null,bushou varchar(4),head varchar(4)," +
                "pinyin varchar(30),chengyujs varchar(100),from_ text,example text," +
                "yufa varchar(30),ciyujs text,yinzhengjs text,tongyi text,fanyi text)";
        db.execSQL(sql);

        //创建收藏汉字的表
        sql = "create table collectwordtable(_id integer primary key autoincrement," +
                "zi varchar(4) unique not null)";
        db.execSQL(sql);

        //创建收藏成语的表
        sql = "create table collectchengyutable(_id integer primary key autoincrement," +
                "chengyu varchar(4) unique not null)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
