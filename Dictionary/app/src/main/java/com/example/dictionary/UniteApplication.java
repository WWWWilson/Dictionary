package com.example.dictionary;

import android.app.Application;

import com.example.dictionary.db.DBManager;

import org.xutils.x;

//其实这个就是将xUtils进行嵌入，导入到项目工程然后进行下载，使用框架搭建的一个过程
public class UniteApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this); //初始化xUtils模块数据
        //初始化数据库对象
        DBManager.initDB(this);
    }
}
