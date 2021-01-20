package com.example.dictionary.utils;

public class URLUtils {

    //根据拼音查汉字的URL
    public static String pinyinUrl = "http://v.juhe.cn/xhzd/querypy?key=";
    //根据部首查汉字的URL
    public static String bushouUrl = "http://v.juhe.cn/xhzd/querybs?key=";
    //字典的请求Key
    public static final String DICTIONARY_KEY = "cc070a78782d79a4f89e145b8660aafd";
    //根据汉字查询字典
    public static String wordUrl = "http://v.juhe.cn/xhzd/query?key=";
    //成语的请求Key
    public static final String CHENGYU_KEY = "198c142b562a83e75fbe65beb28a7b32";
    //查询成语的URL
    public static String chengyuUrl = "http://apis.juhe.cn/idioms/query?key=";

    public static String getPinyinUrl(String word,int page,int pagesize) {
        String url = pinyinUrl + DICTIONARY_KEY + "&word=" + word + "&page=" + page +"&pagesize=" + pagesize;
        return url;
    }

    public static String getBushouUrl(String bushou,int page,int pagesize) {
        String url = bushouUrl + DICTIONARY_KEY + "&word=" + bushou + "&page=" + page +"&pagesize=" + pagesize;
        return url;
    }

    public static String getWordUrl(String word) {
        String url = wordUrl + DICTIONARY_KEY + "&word=" + word;
        return url;
    }


    public static String getChengyuUrl(String word) {
        String url = chengyuUrl + CHENGYU_KEY + "&wd=" + word;
        return url;
    }
}
