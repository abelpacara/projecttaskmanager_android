package com.boliviawebdesign.projecttaskmanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by abel on 19-05-16.
 */
public class Session {

    private SharedPreferences prefs;

    public Session(Context cntx) {
        // TODO Auto-generated constructor stub
        prefs = PreferenceManager.getDefaultSharedPreferences(cntx);
    }

    public void setUserId(String user_id) {
        prefs.edit().putString("user_id", user_id).commit();
        prefs.edit().commit();
    }

    public String getUserId(){
        String user_id = prefs.getString("user_id","");
        return user_id;
    }
}
