package com.example.demo.model;

import java.io.Serializable;

/**
 * Created by WY on 2017/8/27.
 */
public class NewsCategory implements Serializable{
    private int newsCategory_id;
    private String newsCategory_name;

    public int getNewsCategory_id() {
        return newsCategory_id;
    }

    public void setNewsCategory_id(int newsCategory_id) {
        this.newsCategory_id = newsCategory_id;
    }

    public String getNewsCategory_name() {
        return newsCategory_name;
    }

    public void setNewsCategory_name(String newsCategory_name) {
        this.newsCategory_name = newsCategory_name;
    }
}
