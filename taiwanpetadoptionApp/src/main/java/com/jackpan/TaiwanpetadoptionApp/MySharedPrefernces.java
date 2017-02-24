package com.jackpan.TaiwanpetadoptionApp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by HYXEN20141227 on 2016/6/20.
 */
public class MySharedPrefernces {


    public static final String  NAME = "MySharedPrefernces";

    //首頁-是否第一次使用
    public static final String KEY_IS_BUY = "isBuy";
    public static void saveIsBuyed(Context context, boolean isBuyed) {
        SharedPreferences sp = context.getSharedPreferences(NAME, Activity.MODE_PRIVATE);
        sp.edit().putBoolean(KEY_IS_BUY, isBuyed).apply();
    }

    public static boolean getIsBuyed(Context context) {
        SharedPreferences sp = context.getSharedPreferences(NAME, Activity.MODE_PRIVATE);
        return sp.getBoolean(KEY_IS_BUY, false);
    }

    // 儲存 userid
    public  static  final  String KEY_USERID= "userid";
    public static  void saveUserId (Context context ,String userid){
        SharedPreferences sp = context.getSharedPreferences(NAME,Activity.MODE_PRIVATE);
        sp.edit().putString(KEY_USERID, userid).commit();


    }
    public static String getUserId(Context context) {
        SharedPreferences sp = context.getSharedPreferences(NAME, Activity.MODE_PRIVATE);
        return sp.getString(KEY_USERID, "");
    }

    // 儲存 userpic
    public  static  final  String KEY_USERPIC= "userpic";
    public static  void saveUserPic (Context context ,String userid){
        SharedPreferences sp = context.getSharedPreferences(NAME,Activity.MODE_PRIVATE);
        sp.edit().putString(KEY_USERPIC, userid).commit();


    }
    public static String getUserPic(Context context) {
        SharedPreferences sp = context.getSharedPreferences(NAME, Activity.MODE_PRIVATE);
        return sp.getString(KEY_USERPIC, "");
    }
    // 儲存 username
    public  static  final  String KEY_USERNAME= "username";
    public static  void saveUserName (Context context ,String userid){
        SharedPreferences sp = context.getSharedPreferences(NAME,Activity.MODE_PRIVATE);
        sp.edit().putString(KEY_USERNAME, userid).commit();


    }
    public static String getUserName(Context context) {
        SharedPreferences sp = context.getSharedPreferences(NAME, Activity.MODE_PRIVATE);
        return sp.getString(KEY_USERNAME, "");
    }

    // 儲存 useremail
    public  static  final  String KEY_USERMAIL= "usermail";
    public static  void saveUserMail (Context context ,String userid){
        SharedPreferences sp = context.getSharedPreferences(NAME,Activity.MODE_PRIVATE);
        sp.edit().putString(KEY_USERMAIL, userid).commit();


    }
    public static String getUserMail(Context context) {
        SharedPreferences sp = context.getSharedPreferences(NAME, Activity.MODE_PRIVATE);
        return sp.getString(KEY_USERMAIL, "");
    }



    // 	// 通知中心 時間記錄
    public  static  final  String KEY_MYCARD_TIME = "mycardtime";
    public static  void saveMyCardTime (Context context ,String time){
        SharedPreferences sp = context.getSharedPreferences(NAME,Activity.MODE_PRIVATE);
        sp.edit().putString(KEY_MYCARD_TIME, time + "").commit();


    }
    public static String getMyCardTime(Context context) {
        SharedPreferences sp = context.getSharedPreferences(NAME, Activity.MODE_PRIVATE);
        return sp.getString(KEY_MYCARD_TIME, "");
    }

    // 	// 通知中心 時間記錄
    public  static  final  String KEY_MYAD_TIME = "myadtime";
    public static  void saveMyAdTime (Context context ,String time){
        SharedPreferences sp = context.getSharedPreferences(NAME,Activity.MODE_PRIVATE);
        sp.edit().putString(KEY_MYAD_TIME, time + "").commit();


    }
    public static String getMyAdTime(Context context) {
        SharedPreferences sp = context.getSharedPreferences(NAME, Activity.MODE_PRIVATE);
        return sp.getString(KEY_MYAD_TIME, "");
    }

}
