package com.example.dictionary.utils;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/*
* 读取assets文件夹下的工具类
* */
public class AssetsUtils {
    public static String getAssetsContent(Context context,String fileName){
        //用context对象来调用getResources()来获得Resources对象调用getAssets()获得AssetManager对象
        AssetManager manager = context.getResources().getAssets();
        //创建一个 ByteArrayOutputStream对象并实例化
        //这个对象是字节数组输出流是OutputStream的子类
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            //通过AssetManager对象调用open()打开文件夹中的资源并返回一个InputStream
            //这里的异常是可能找不到文件
            InputStream is = manager.open(fileName);

            int hasRead = 0;
            byte[] buf = new byte[1024];

            while (true){
                hasRead = is.read(buf);
                //如果hasRead==-1，说明读取完了，就跳出
                if (hasRead==-1){
                    break;
                    //将它写如内存流当中
                }else{
                    baos.write(buf,0,hasRead);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return baos.toString();
    }
}
