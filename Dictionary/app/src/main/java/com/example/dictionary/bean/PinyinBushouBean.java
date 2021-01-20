package com.example.dictionary.bean;

import java.util.List;

//这个Bean类可以解析pinyin和bushou两个JSON格式的数据
public class PinyinBushouBean {
    private String reason;
    private int error_code;
    private List<ResultBean> result;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * id : 1
         * pinyin_key : A
         * pinyin : a
         */

        private String id;
        private String pinyin_key;
        private String pinyin;
        private String bihua;
        private String bushou;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPinyin_key() {
            return pinyin_key;
        }

        public void setPinyin_key(String pinyin_key) {
            this.pinyin_key = pinyin_key;
        }

        public String getPinyin() {
            return pinyin;
        }

        public void setPinyin(String pinyin) {
            this.pinyin = pinyin;
        }

        public String getBihua() {
            return bihua;
        }

        public void setBihua(String bihua) {
            this.bihua = bihua;
        }

        public String getBushou() {
            return bushou;
        }

        public void setBushou(String bushou) {
            this.bushou = bushou;
        }
    }
}
