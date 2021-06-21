package com.rm.rmjbm.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.rm.rmjbm.activity.MainActivity;
import com.rm.rmjbm.model.LovModel;

import java.util.ArrayList;
import java.util.HashMap;

public class SessionManagement {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "JbmTouchStonePref";
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_USERNAME = "keyUserName";
    public static final String KEY_PASSWORD = "keyPassword";
    public static final String KEY_USERPLANT = "keyUserPlant";
    public static final String KEY_DEVICEID = "keyDeviceId";
    public static final String KEY_IMEI = "keyImei";
    public static final String KEY_MACID = "keyMac";
    public static final String KEY_LOV_LIST = "lovList";


    public SessionManagement(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(String username, String password, String plant,
                                   String deviceid, String imei,
                                   String macID, String lovList) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_PASSWORD, password);
        editor.putString(KEY_USERPLANT, plant);
        editor.putString(KEY_DEVICEID, deviceid);
        editor.putString(KEY_IMEI, imei);
        editor.putString(KEY_MACID, macID);
        editor.putString(KEY_LOV_LIST, lovList);
        editor.commit();
    }

    public void checkLogin() {
        // Check login status
        if (!this.isLoggedIn()) {
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, MainActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }

    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_USERNAME, pref.getString(KEY_USERNAME, null));
        user.put(KEY_PASSWORD, pref.getString(KEY_PASSWORD, null));
        user.put(KEY_USERPLANT, pref.getString(KEY_USERPLANT, null));
        user.put(KEY_DEVICEID, pref.getString(KEY_DEVICEID, null));
        user.put(KEY_IMEI, pref.getString(KEY_IMEI, null));
        user.put(KEY_MACID, pref.getString(KEY_MACID, null));
        user.put(KEY_LOV_LIST, pref.getString(KEY_LOV_LIST, null));
        return user;
    }

    public void logoutUser() {
        editor.clear();
        editor.commit();

        Intent i = new Intent(_context, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(i);
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }
}