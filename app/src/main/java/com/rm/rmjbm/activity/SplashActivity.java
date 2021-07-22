package com.rm.rmjbm.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.rm.rmjbm.R;
import com.rm.rmjbm.utils.ConstantsUtils;
import com.rm.rmjbm.utils.SessionManagement;
import com.rm.rmjbm.utils.Utils;

import static android.os.Binder.getCallingUid;

public class SplashActivity extends AppCompatActivity {

    private SessionManagement session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        session = new SessionManagement(getApplicationContext());

        boolean hasAndroidPermissions = hasPermissions(getApplicationContext(), new String[]{
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.READ_PHONE_STATE,
        });

        if (hasAndroidPermissions) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ConstantsUtils.TAG_UUID = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                            Settings.Secure.ANDROID_ID);
                    ConstantsUtils.TAG_MACID = Utils.getMACAddress("wlan0");
                    System.out.println("IMEI:: MAC " + ConstantsUtils.TAG_MACID);
                    System.out.println("IMEI::UUID " + ConstantsUtils.TAG_UUID);
                    System.out.println("IMEI:: IMEI " + ConstantsUtils.TAG_IMEI);
                    getDeviceIMEI();
                    System.out.println("IMEI:: IMEI2 " + ConstantsUtils.TAG_IMEI);

                    if (session.isLoggedIn()) {
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    } else {
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finish();
                    }
                }
            }, 1500);
        } else {
            ActivityCompat.requestPermissions(SplashActivity.this,
                    new String[]{
                            Manifest.permission.INTERNET,
                            Manifest.permission.ACCESS_NETWORK_STATE,
                            Manifest.permission.READ_PHONE_STATE,
                    }, 1);
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {

        boolean hasAllPermissions = true;

        System.out.println("getting the insid tjhe method");
        for (String permission : permissions) {
            System.out.println("getting all the perission" + permission);

            if (!hasPermission(context, permission)) {
                System.out.println("getting inside the false condidition");
                hasAllPermissions = false;
            }
        }
        return hasAllPermissions;
    }

    public static boolean hasPermission(Context context, String permission) {

        PackageManager pm = context.getPackageManager();
        boolean permissionaaa = (PackageManager.PERMISSION_GRANTED == pm.checkPermission(permission, pm.getNameForUid(getCallingUid())));
        System.out.println("permissio i granted" + permission + " and firs res alues" + " and  " + permissionaaa);
        return permissionaaa;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ConstantsUtils.TAG_UUID = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                                    Settings.Secure.ANDROID_ID);
                            ConstantsUtils.TAG_MACID = Utils.getMACAddress("wlan0");
                            System.out.println("IMEI:: MAC1 " + ConstantsUtils.TAG_MACID);
                            System.out.println("IMEI:: UUID1 " + ConstantsUtils.TAG_UUID);
                            System.out.println("IMEI:: IMEI1 " + ConstantsUtils.TAG_IMEI);
                            getDeviceIMEI();
                            System.out.println("IMEI:: IMEI2 " + ConstantsUtils.TAG_IMEI);

                            if (session.isLoggedIn()) {
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                finish();
                            } else {
                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                finish();
                            }
                        }
                    }, 1500);
                } else {
                    Utils.showCustomToast("Permission denied to read your External storage", getApplicationContext());

//                    Toast.makeText(SplashActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    private void getDeviceIMEI() {
        TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);

        if (tm != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    System.out.println("IMEI:: > O");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        ConstantsUtils.TAG_IMEI = tm.getManufacturerCode();
                    } else {
                        ConstantsUtils.TAG_IMEI = tm.getImei();
                    }
                } else {
                    ConstantsUtils.TAG_IMEI = tm.getDeviceId();
                }
            } else {
                ConstantsUtils.TAG_IMEI = tm.getDeviceId();
            }
        }

        if (ConstantsUtils.TAG_IMEI == null || ConstantsUtils.TAG_IMEI.length() == 0) {
            ConstantsUtils.TAG_IMEI = Settings.Secure.getString(this.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        }
    }
}