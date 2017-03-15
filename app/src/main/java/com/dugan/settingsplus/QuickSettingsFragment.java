package com.dugan.settingsplus;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dugan.settingsplus.utils.Constants;
import com.dugan.settingsplus.utils.MySQLHelper;
import com.dugan.settingsplus.utils.customtiles.CustomTileHelper;

import java.util.List;

/**
 * Created by leona on 11/14/2015.
 */
public class QuickSettingsFragment extends Fragment {

    private static MySQLHelper mDatabase;
    private static RelativeLayout mTilesLayout;
    private static RelativeLayout mNoTilesLayout;
    private static QuickSettingsListAdapter mTileListAdapter;
    private static Cursor mTileCursor;
    private static ListView mTileList;

    public QuickSettingsFragment(){

    }

    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mDatabase = new MySQLHelper(getContext());
        final int[] tileCount = new int[1];

        final View rootView = inflater.inflate(R.layout.quick_settings_fragment, container, false);

        final AsyncTask<DBRunnable, Void, DBRunnable> execute = new DBTask().execute(new DBRunnable() {
            @Override
            public void executeDBTask() {
                tileCount[0] = mDatabase.tileCount();
            }

            @Override
            public void postExecuteDBTask() {
                mTilesLayout = (RelativeLayout) rootView.findViewById(R.id.quickSettingsLayout);
                mNoTilesLayout = (RelativeLayout) rootView.findViewById(R.id.noQuickSettingsLayout);
                if (tileCount[0] > 0) {
                    new DBTask().execute(new DBRunnable() {
                        @Override
                        public void executeDBTask() {
                            mTileCursor = mDatabase.getTiles();
                        }

                        @Override
                        public void postExecuteDBTask() {
                            mTileListAdapter = new QuickSettingsListAdapter(rootView.getContext(), mTileCursor, 0);
                            mNoTilesLayout.setVisibility(View.GONE);
                            mTilesLayout.setVisibility(View.VISIBLE);
                            mTileList = (ListView) rootView.findViewById(R.id.quickSettingsList);
                            mTileList.setAdapter(mTileListAdapter);
                            mTileList.setClickable(true);
                            mTileList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Intent intent = new Intent(getContext(), EditTileActivity.class);
                                    intent.putExtra(Constants.EDIT_TILE_TYPE_INTENT_EXTRA, "edit");
                                    startActivity(intent);
                                }
                            });
                            mTileList.setLongClickable(false);
                            mTileList.setDividerHeight(0);
                        }
                    });
                } else {
                    mNoTilesLayout.setVisibility(View.VISIBLE);
                    mTilesLayout.setVisibility(View.GONE);
                }
            }
        });

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.quickSetFAB);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditTileActivity.class);
                intent.putExtra(Constants.EDIT_TILE_TYPE_INTENT_EXTRA, "new");
                startActivity(intent);
            }
        });

        return rootView;
    }

    public boolean isPackageExisted(String targetPackage){
        List<ApplicationInfo> packages;
        PackageManager pm;

        pm = getActivity().getPackageManager();
        packages = pm.getInstalledApplications(0);
        for (ApplicationInfo packageInfo : packages) {
            if(packageInfo.packageName.equals(targetPackage))
                return true;
        }
        return false;
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static void refreshTileCursor(final Context context){
        final Cursor[] newCursor = new Cursor[1];
        final int[] tileCount = new int[1];
        new DBTask().execute(new DBRunnable() {
            @Override
            public void executeDBTask() {
                MySQLHelper db = new MySQLHelper(context);
                newCursor[0] = db.getTiles();
                tileCount[0] = db.tileCount();
            }

            @Override
            public void postExecuteDBTask() {
                mTileListAdapter.swapCursor(newCursor[0]);
                if (tileCount[0] == 0) {
                    mTilesLayout.setVisibility(View.GONE);
                    mNoTilesLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }

}
