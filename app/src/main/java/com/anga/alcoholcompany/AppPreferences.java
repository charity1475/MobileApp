package com.anga.alcoholcompany;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPreferences {
    private SharedPreferences preferences;
    String KEY_LOGIN_STATE = "key_login_state";

    public AppPreferences(Context context){
        preferences = context.getSharedPreferences("travel_blog", Context.MODE_PRIVATE);
    }

    public boolean isLoggedIn(){
        return preferences.getBoolean(KEY_LOGIN_STATE,false);
    }

    public void setLoggedIn(boolean loggedIn){
        preferences.edit().putBoolean(KEY_LOGIN_STATE, loggedIn).apply();
    }
}
