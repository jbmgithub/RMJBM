package com.rm.rmjbm.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.rm.rmjbm.R;
import com.rm.rmjbm.fragment.AppVersionFragment;
import com.rm.rmjbm.fragment.HomeFragment;
import com.google.android.material.snackbar.Snackbar;
import com.rm.rmjbm.fragment.MaterialStagingFragment;
import com.rm.rmjbm.fragment.StockViewFragment;
import com.rm.rmjbm.model.LovModel;
import com.rm.rmjbm.utils.SessionManagement;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvMaterialStaging, tvStockView, tvPhysicalInventory, tvVersion, tvContactUs, tvLogout;
    private LinearLayout llMaterialStaging, llStockView, llPhysicalInventory, llContactUs, llAppVersion, llLogout;
    private Typeface robotoRegular, robotoBold, robotoItalic;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private boolean doubleBackToExitPressedOnce = false;
    private AlertDialog.Builder builder;
    private SessionManagement session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWidgetRef();
        setWidgetEvent();
        initSession();
        init();
        setTextFont();
        setupToolBar(getResources().getString(R.string.rm) + "-" + getResources().getString(R.string.material_staging));

        replaceFragment(new MaterialStagingFragment(), null, "", false, false);


    }

    private void setupToolBar(String app_name) {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(app_name);
            toolbar.setTitleTextColor(Color.WHITE);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open,
                    R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();
        }
    }

    private void initSession() {
        session = new SessionManagement(getApplicationContext());
        if (!session.isLoggedIn()) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        } else {
            HashMap<String, String> user = session.getUserDetails();
//            username = user.get(SessionManagement.KEY_USERNAME);
//            devid = user.get(SessionManagement.KEY_DEVICEID);
//            IMEI = user.get(SessionManagement.KEY_IMEI);
//            macID = user.get(SessionManagement.KEY_MACID);
//            userPass = user.get(SessionManagement.KEY_PASSWORD);
        }
    }

    private void replaceFragment(Fragment fragment, Bundle data, String Tag,
                                 boolean isBackStack, boolean isMenuSupport) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        fragment.setArguments(data);
        fragment.setHasOptionsMenu(isMenuSupport);
        ft.setCustomAnimations(R.anim.slide_left, R.anim.slide_right);
        ft.replace(R.id.nav_host_fragment, fragment, Tag);
        if (isBackStack) {
            ft.addToBackStack(Tag);
        }
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
        drawer.closeDrawers();
    }


    private void init() {
        robotoRegular = Typeface.createFromAsset(getApplicationContext().getAssets(), "font/Roboto-Regular.ttf");
        robotoBold = Typeface.createFromAsset(getApplicationContext().getAssets(), "font/Roboto-Bold.ttf");
        robotoItalic = Typeface.createFromAsset(getApplicationContext().getAssets(), "font/Roboto-Italic.ttf");
    }

    private void setTextFont() {
        tvMaterialStaging.setTypeface(robotoRegular);
        tvStockView.setTypeface(robotoRegular);
        tvPhysicalInventory.setTypeface(robotoRegular);
        tvVersion.setTypeface(robotoRegular);
        tvContactUs.setTypeface(robotoRegular);
        tvLogout.setTypeface(robotoRegular);
    }

    private void setWidgetEvent() {
        llMaterialStaging.setOnClickListener(this);
        llStockView.setOnClickListener(this);
        llPhysicalInventory.setOnClickListener(this);
        llContactUs.setOnClickListener(this);
        llAppVersion.setOnClickListener(this);
        llLogout.setOnClickListener(this);
    }

    private void getWidgetRef() {
        toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer_layout);
        tvMaterialStaging = findViewById(R.id.tvLDMMaterialStaging);
        tvStockView = findViewById(R.id.tvLDMStockView);
        tvPhysicalInventory = findViewById(R.id.tvLDMPhysicalInventory);
        tvContactUs = findViewById(R.id.tvLDMContactUs);
        tvVersion = findViewById(R.id.tvLDMVersion);
        tvLogout = findViewById(R.id.tvLDMLogout);

        llMaterialStaging = findViewById(R.id.llLDMMaterialStaging);
        llStockView = findViewById(R.id.llLDMStockView);
        llPhysicalInventory = findViewById(R.id.llLDMPhysicalInventory);
        llContactUs = findViewById(R.id.llLDMContactUs);
        llAppVersion = findViewById(R.id.llLDMVersion);
        llLogout = findViewById(R.id.llLDMLogout);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_app_version) {
            setupToolBar(getResources().getString(R.string.rm) + "-" + getResources().getString(R.string.app_version));
            replaceFragment(new AppVersionFragment(), null, "", false, false);
            return true;
        }
        if (id == R.id.action_logout) {
            ShowLogoutAlertDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void ShowLogoutAlertDialog() {
        builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert");
        builder.setCancelable(true);
        builder.setMessage("Are you sure you want to log out?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                session.logoutUser();
                finish();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }

    @Override
    public void onClick(View v) {
        if (v == llMaterialStaging) {
            setupToolBar(getResources().getString(R.string.rm) + "-" + getResources().getString(R.string.material_staging));
            replaceFragment(new MaterialStagingFragment(), null, "", false, false);
        } else if (v == llStockView) {
            setupToolBar(getResources().getString(R.string.rm) + "-" + getResources().getString(R.string.stock_view));
            replaceFragment(new StockViewFragment(), null, "", false, false);
        } else if (v == llPhysicalInventory) {
            Snackbar.make(v, "coming soon", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        } else if (v == llContactUs) {
            Snackbar.make(v, "Contact us", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        } else if (v == llAppVersion) {
            setupToolBar(getResources().getString(R.string.rm) + "-" + getResources().getString(R.string.app_version));
            replaceFragment(new AppVersionFragment(), null, "", false, false);
        } else if (v == llLogout) {
            ShowLogoutAlertDialog();
        }
    }
}