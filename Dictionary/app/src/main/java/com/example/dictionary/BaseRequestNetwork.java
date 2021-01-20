package com.example.dictionary;

import androidx.appcompat.app.AppCompatActivity;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

//网络请求页面的工具类
public class BaseRequestNetwork extends AppCompatActivity implements Callback.CommonCallback<String> {

    //封装加载网络数据的过程
    public void loadData(String path){
        //创建请求参数
        RequestParams params = new RequestParams(path);
        //网络请求数据
        x.http().get(params,this);
    }
    //网络请求成功回调的方法，result就是获取的json数据
    @Override
    public void onSuccess(String result) {

    }

    //网络请求出错时回调的方法
    @Override
    public void onError(Throwable ex, boolean isOnCallback) {

    }
    //网络请求取消时回调的方法
    @Override
    public void onCancelled(CancelledException cex) {

    }

    //网络请求完成时回调的方法
    @Override
    public void onFinished() {

    }
}
