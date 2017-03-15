package com.dugan.settingsplus;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.dugan.settingsplus.utils.Constants;
import com.dugan.settingsplus.utils.inappbilling.IabBroadcastReceiver;
import com.dugan.settingsplus.utils.inappbilling.IabHelper;
import com.dugan.settingsplus.utils.inappbilling.IabResult;
import com.dugan.settingsplus.utils.inappbilling.Inventory;
import com.dugan.settingsplus.utils.inappbilling.Purchase;
import com.dugan.settingsplus.utils.rootchecker.RootChecker;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    AdView adView;
    FrameLayout mainFrame;
    IabHelper mHelper;
    IabBroadcastReceiver mReceiver;
    String tag = "MainActivity:";
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Create local vars
        Fragment fragment = null;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ActionBar actionBar = getSupportActionBar();

        //Set action bar title
        assert actionBar != null;
        actionBar.setTitle(R.string.profiles);

        //change fragment
        fragment = new ProfilesFragment();
        ft.replace(R.id.mainFrame, fragment);
        ft.commit();

        mHelper = new IabHelper(this, Constants.base64EncodedPublicKey);
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener(){
            @Override
            public void onIabSetupFinished(IabResult result) {
                Log.d(tag, "IabHelper Setup Finished.");

                if (result.isSuccess()) {
                    Log.d(tag, "IabHelper startSetup Successful " + result);
                } else {
                    Log.d(tag, "IabHelper startSetup Failed " + result);
                }

                if(mHelper == null){
                    return;
                }

                mReceiver = new IabBroadcastReceiver(new IabBroadcastReceiver.IabBroadcastListener() {
                    @Override
                    public void receivedBroadcast() {
                        Log.d(tag, "Received broadcast notification. Querying inventory.");
                        mHelper.queryInventoryAsync(mGotInventoryListener);
                    }
                });
                IntentFilter broadcastFilter = new IntentFilter(IabBroadcastReceiver.ACTION);
                registerReceiver(mReceiver, broadcastFilter);

                Log.d(tag, "IabHelper setup successful. Querying inventory.");
                mHelper.queryInventoryAsync(mGotInventoryListener);
            }
        });

        //Get shared prefs
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = prefs.edit();

        //ads
        adView = (AdView) findViewById(R.id.mainAd);
        mainFrame = (FrameLayout) findViewById(R.id.mainFrame);
        if(prefs.getBoolean(Constants.PREF_SHOW_ADS, true)){
            showAds();
        } else {
            hideAds();
        }

        //root checker
        if(prefs.getBoolean(Constants.PREF_CHECK_ROOT_ON_START, true)){
            RootChecker.deviceRooted(this);
        }

    }

    public void hideAds(){
        adView.setVisibility(View.GONE);
        mainFrame.setPadding(0, 0, 0, 0);
    }

    public void showAds(){
        adView.setVisibility(View.VISIBLE);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("D03ACD2BB210909C9603FEE6C11B9B4C")
                .build();
        adView.loadAd(adRequest);
        mainFrame.setPadding(0,10,0,0);
    }

    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener(){
        @Override
        public void onQueryInventoryFinished(IabResult result, Inventory inv) {
            Log.d(tag, "IabHelper.QueryInventoryFinishedListener Query inventory finished.");

            if (mHelper == null) return;

            if (result.isSuccess()) {
                Log.d(tag, "IabHelper.QueryInventoryFinishedListener IabHelper.QueryInventory Successful " + result);
            } else {
                Log.d(tag, "IabHelper.QueryInventoryFinishedListener IabHelper.QueryInventory Failed " + result);
            }

            Log.d(tag, "IabHelper.QueryInventoryFinishedListener Query inventory was successful.");

            Purchase adFreePurchase = inv.getPurchase(Constants.ITEM_SKU_AD_FREE);

            if(adFreePurchase != null){
                editor.putBoolean(Constants.PREF_SHOW_ADS, false);
                editor.apply();
                hideAds();
                Log.d(tag, "IabHelper.QueryInventoryFinishedListener ad free owned");
            } else {
                editor.putBoolean(Constants.PREF_SHOW_ADS, true);
                editor.apply();
                showAds();
                Log.d(tag, "IabHelper.QueryInventoryFinishedListener ad free NOT owned");
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();

        // very important:
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }

        // very important:
        Log.d(tag, "Destroying helper.");
        if (mHelper != null) {
            mHelper.dispose();
            mHelper = null;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

/*        if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        //Create local vars
        Fragment fragment = null;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;

        if (id == R.id.nav_profiles) {
            fragment = new ProfilesFragment();
            actionBar.setTitle("Profiles");
        } else if (id == R.id.nav_triggers) {
            fragment = new TriggersFragment();
            actionBar.setTitle("Triggers");
        } else if (id == R.id.nav_quick_settings) {
            fragment = new QuickSettingsFragment();
            actionBar.setTitle("Quick Settings");
        } else if (id == R.id.nav_scheduled_reboots) {
            fragment = new ScheduledRebootsFragment();
            actionBar.setTitle("Scheduled Reboots");
        } else if (id == R.id.nav_root_checker) {
            fragment = new RootCheckerFragment();
            actionBar.setTitle("Root Checker");
        } else if (id == R.id.nav_device_info) {
            fragment = new DeviceInfoFragment();
            actionBar.setTitle("Root Checker");
        } else if (id == R.id.nav_logs) {
            fragment = new LogsFragment();
            actionBar.setTitle("Logs");
        } else if (id == R.id.nav_store) {
            fragment = new StoreFragment();
            actionBar.setTitle("Store");
        } else if (id == R.id.nav_settings) {
            fragment = new SettingsFragment();
            actionBar.setTitle("Settings");
        }

        ft.replace(R.id.mainFrame, fragment);
        ft.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        FragmentManager fm = getSupportFragmentManager();
        List<Fragment> fragments = fm.getFragments();
        for(int i = 0; i < fragments.size(); i++){
            Fragment fragment = fragments.get(i);
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}
