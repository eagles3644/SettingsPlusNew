package com.dugan.settingsplus;

/**
 * Created by leona on 12/29/2015.
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.GridView;

import com.dugan.settingsplus.utils.MyResourceHolder;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ChooserActivity extends AppCompatActivity {

    private GridView gridView;
    private ArrayList<MyResourceHolder> resourceList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chooser);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        gridView = (GridView) findViewById(R.id.chooser_Grid);

        Intent intent = getIntent();

        if(intent.getStringExtra("Chooser").equals("Resource")) {
            setTitle("Choose an Icon:");
            final Resources resources = getResources();
            Field[] drawables = com.dugan.settingsplus.R.drawable.class.getFields();

            for (Field f : drawables){
                if(f.getName().length() > 8 && f.getName().contains("built_in")){
                    MyResourceHolder myResourceHolder = new MyResourceHolder(
                            resources.getIdentifier(f.getName(), "drawable", this.getPackageName())
                            , f.getName().substring(9));
                    resourceList.add(myResourceHolder);
                }
            }

            IconChooserAdapter listAdaptor = new IconChooserAdapter(ChooserActivity.this, resourceList);
            gridView.setAdapter(listAdaptor);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    MyResourceHolder myResourceHolder = resourceList.get(position);
                    Intent data = new Intent();
                    data.putExtra("selected_icon", myResourceHolder.getResourceID());
                    setResult(RESULT_OK, data);
                    finish();
                }
            });
        } else if(intent.getStringExtra("Chooser").equals("App")){
            setTitle("Choose an App:");
            new LoadApplications().execute();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }

    private List<ApplicationInfo> checkForLaunchIntent(List<ApplicationInfo> list) {
        ArrayList<ApplicationInfo> appList = new ArrayList<>();
        PackageManager packageManager = getPackageManager();
        for (ApplicationInfo info : list) {
            try {
                if (null != packageManager.getLaunchIntentForPackage(info.packageName)) {
                    appList.add(info);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return appList;
    }

    private class LoadApplications extends AsyncTask<Void, Void, Void> {
        ProgressDialog progress = null;
        PackageManager packageManager = null;
        List<ApplicationInfo> appList = null;
        AppChooserAdapter appChooserAdapter = null;

        @Override
        protected Void doInBackground(Void... params) {

            packageManager = getPackageManager();
            appList = checkForLaunchIntent(packageManager.getInstalledApplications(PackageManager.GET_META_DATA));
            appChooserAdapter = new AppChooserAdapter(ChooserActivity.this, appList);
            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(Void result) {
            gridView.setAdapter(appChooserAdapter);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ApplicationInfo app = appList.get(position);
                    Intent data = new Intent();
                    data.putExtra("selected_app", app);
                    setResult(RESULT_OK, data);
                    finish();
                }
            });
            progress.dismiss();
            super.onPostExecute(result);
        }

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(ChooserActivity.this, null,
                    "Loading application info...");
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
}
