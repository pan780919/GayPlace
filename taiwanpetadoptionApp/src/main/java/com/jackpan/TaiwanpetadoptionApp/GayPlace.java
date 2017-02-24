package com.jackpan.TaiwanpetadoptionApp;

import android.util.Log;

import java.text.Normalizer;
import java.util.List;

/**
 * Created by HYXEN20141227 on 2016/12/21.
 */

public class GayPlace{
    public  String id;
    public  String tittle;
    public  String message;
    public  String pic;
    private String name;
    private String date;
    private String url;
    public  int   like = -1;
    public  String tomsg;
    public  int   view = -1;

//    List<ForMsg> ForMsg;


    public int getView() {
        return view;
    }

    public int getLike() {
        return like;
    }

    private static final String TAG = "GayPlace";

    public void setId(String id) {
        this.id = id;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public String getPic() {
        return pic;
    }

    public String getTittle() {
        return tittle;
    }


}

