package com.cybene.cyposdashboard.utils.db;

import android.content.Context;
import android.content.SharedPreferences;

import com.cybene.cyposdashboard.utils.AppController;

public class SharedPrefs {
    private static String PREFERENCE_NAME = "cyposdashboard";
    private static SharedPrefs sharedPreferenceUtil;
    private final SharedPreferences preferences;

    public SharedPrefs(Context context) {
        PREFERENCE_NAME = PREFERENCE_NAME + context.getPackageName();
        this.preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }
    //get the instance of the shared preference
    public static SharedPrefs getInstance(){
        if(sharedPreferenceUtil == null) {
            sharedPreferenceUtil = new SharedPrefs(AppController.getInstance());
        }
        return sharedPreferenceUtil;
    }
    //save the shared string (username)
    public void saveString(String key, String val){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key,val);
        System.out.println("shared prefs "+key + val);
        editor.apply();
    }
    public void saveBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }
    public String getString(String key, String defVal){
        return preferences.getString(key,defVal);
    }
    public String getString(String key){
        return preferences.getString(key,"0");
    }

    public boolean isLoggedIn(){
        return preferences.getBoolean("isLoggedIn", false);
    }

    public void saveLong(String key, long value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public long getLong(String key, long defVal) {
        return preferences.getLong(key, defVal);
    }

    public void saveInt(String key, int value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public int getInt(String key, int defVal) {
        return preferences.getInt(key, defVal);
    }
}
