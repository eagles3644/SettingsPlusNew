package com.dugan.settingsplus;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dugan.settingsplus.utils.Constants;
import com.dugan.settingsplus.utils.MySQLHelper;
import com.dugan.settingsplus.utils.customtiles.CustomTileHelper;

import java.util.ArrayList;
import java.util.List;

public class EditTileActivity extends AppCompatActivity {

    String mTileBroadcast;
    int mTileID;
    String mTitle;
    Uri mIconUri;
    int mIconID;
    String mIconPackage;
    String mClickActionType;
    String mClickActionPackage;
    String mClickActionToastText;
    String mClickActionWebAddress;
    String mLongClickActionType;
    String mLongClickActionPackage;
    String mLongClickActionToastText;
    String mLongClickActionWebAddress;
    boolean mCollapseTray;
    boolean mUnlockPrompt;
    String mEnabled;
    MySQLHelper mDatabase;
    Cursor mCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_tile);

        RelativeLayout iconView = (RelativeLayout) findViewById(R.id.editTileIconView);
        RelativeLayout titleView = (RelativeLayout) findViewById(R.id.editTileTitleView);
        RelativeLayout clickActionView = (RelativeLayout) findViewById(R.id.editTileClickActionView);
        RelativeLayout longClickView = (RelativeLayout) findViewById(R.id.editTileLongClickActionView);
        RelativeLayout unlockPromptView = (RelativeLayout) findViewById(R.id.editTileUnlockPromptView);
        RelativeLayout collapseTrayView = (RelativeLayout) findViewById(R.id.editTileCollapseTrayView);

        final CheckBox unlockPromptCheck = (CheckBox) findViewById(R.id.editTileUnlockPromptCheck);
        final CheckBox collapseTrayCheck = (CheckBox) findViewById(R.id.editTileCollapseTrayCheck);

        final TextView previewTitle = (TextView) findViewById(R.id.editTilePreviewTitle);
        final ImageView previewIcon = (ImageView) findViewById(R.id.editTilePreviewIcon);

        mDatabase = new MySQLHelper(this);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        Intent intent = getIntent();
        mTileID = intent.getIntExtra(Constants.EDIT_TILE_ID_EXTRA, 0);
        final String tileType = intent.getStringExtra(Constants.EDIT_TILE_TYPE_INTENT_EXTRA);
        if(tileType.equals("new")) {
            setTitle("New Tile");
            int tileBroadcastNum = (prefs.getInt(Constants.PREF_LAST_TILE_NUMBER, 0) + 1);
            mTileBroadcast = Constants.TILE_BASE_BROADCAST_NAME + tileBroadcastNum;
            mIconUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                    "://" + getPackageName() + "/drawable/built_in_blob");
            mIconPackage = getPackageName();
            mIconID = R.drawable.built_in_blob;
            mTitle = "Blob";
            mClickActionType = "N";
            mClickActionPackage = "";
            mClickActionToastText = "";
            mClickActionWebAddress = "";
            mLongClickActionType = "N";
            mLongClickActionPackage = "";
            mLongClickActionToastText = "";
            mLongClickActionWebAddress = "";
            mUnlockPrompt = false;
            mCollapseTray = true;
            mEnabled = "Y";
            previewTitle.setText(mTitle);
            previewIcon.setImageURI(mIconUri);
        } else {
            new DBTask().execute(new DBRunnable() {
                @Override
                public void executeDBTask() {
                    mCursor = mDatabase.getTileById(mTileID);
                }

                @Override
                public void postExecuteDBTask() {
                    mTileBroadcast = mCursor.getString(mCursor.getColumnIndex(MySQLHelper.COL_TILE_BROADCAST_ID));
                    mIconUri = Uri.parse(mCursor.getString(mCursor.getColumnIndex(MySQLHelper.COL_TILE_ICON_URI)));
                    mIconPackage = mCursor.getString(mCursor.getColumnIndex(MySQLHelper.COL_TILE_ICON_PACKAGE));
                    mIconID = mCursor.getInt(mCursor.getColumnIndex(MySQLHelper.COL_TILE_ICON_ID));
                    mTitle = mCursor.getString(mCursor.getColumnIndex(MySQLHelper.COL_TILE_TITLE));
                    mClickActionType = mCursor.getString(mCursor.getColumnIndex(MySQLHelper.COL_TILE_CLICK_ACT_TYPE));
                    mClickActionPackage = mCursor.getString(mCursor.getColumnIndex(MySQLHelper.COL_TILE_CLICK_ACT_PACKAGE));
                    mClickActionToastText = mCursor.getString(mCursor.getColumnIndex(MySQLHelper.COL_TILE_CLICK_ACT_TOAST));
                    mClickActionWebAddress = mCursor.getString(mCursor.getColumnIndex(MySQLHelper.COL_TILE_CLICK_ACT_WEB_ADDR));
                    mLongClickActionType = mCursor.getString(mCursor.getColumnIndex(MySQLHelper.COL_TILE_LONG_CLICK_ACT_TYPE));
                    mLongClickActionPackage = mCursor.getString(mCursor.getColumnIndex(MySQLHelper.COL_TILE_LONG_CLICK_ACT_PACKAGE));
                    mLongClickActionToastText = mCursor.getString(mCursor.getColumnIndex(MySQLHelper.COL_TILE_LONG_CLICK_ACT_TOAST));
                    mLongClickActionWebAddress = mCursor.getString(mCursor.getColumnIndex(MySQLHelper.COL_TILE_LONG_CLICK_ACT_WEB_ADDR));
                    if(mCursor.getString(mCursor.getColumnIndex(MySQLHelper.COL_TILE_COLLAPSE_TRAY)).equals("Y")){
                        collapseTrayCheck.setChecked(true);
                        mCollapseTray = true;
                    } else {
                        collapseTrayCheck.setChecked(false);
                        mCollapseTray = false;
                    }
                    if(mCursor.getString(mCursor.getColumnIndex(MySQLHelper.COL_TILE_PROMPT_UNLOCK)).equals("Y")){
                        unlockPromptCheck.setChecked(true);
                        mUnlockPrompt = true;
                    } else {
                        unlockPromptCheck.setChecked(false);
                        mUnlockPrompt = false;
                    }
                    mEnabled = mCursor.getString(mCursor.getColumnIndex(MySQLHelper.COL_TILE_ENABLE_TILE));
                    previewTitle.setText(mTitle);
                    previewIcon.setImageURI(mIconUri);
                }
            });
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.editTileFAB);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DBTask().execute(new DBRunnable() {
                    @Override
                    public void executeDBTask() {
                        String collapseTray = "Y";
                        if(!mCollapseTray){
                            collapseTray = "N";
                        }
                        String unlockPrompt = "Y";
                        if(!mUnlockPrompt){
                            unlockPrompt = "N";
                        }
                        if(tileType.equals("new")) {
                            mDatabase.insertTile(mTileBroadcast, mTitle, mIconUri.toString(),
                                    mIconPackage, mIconID, mClickActionType, mClickActionPackage,
                                    mClickActionToastText, mClickActionWebAddress, mLongClickActionType,
                                    mLongClickActionPackage, mLongClickActionToastText, mLongClickActionWebAddress,
                                    collapseTray, unlockPrompt);
                        } else {
                            mDatabase.updateTile(mTileBroadcast, mTitle, mIconUri.toString(),
                                    mIconPackage, mIconID, mClickActionType, mClickActionPackage,
                                    mClickActionToastText, mClickActionWebAddress, mLongClickActionType,
                                    mLongClickActionPackage, mLongClickActionToastText, mLongClickActionWebAddress,
                                    collapseTray, unlockPrompt, mTileID, mEnabled);
                        }
                    }

                    @Override
                    public void postExecuteDBTask() {
                        finish();
                    }
                });
            }
        });

        iconView.setClickable(true);
        iconView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(v.getContext());
                dialogBuilder.setTitle("Select icon type:");
                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(v.getContext(),
                        android.R.layout.simple_list_item_1);
                arrayAdapter.add("Built in");
                arrayAdapter.add("App");
                dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialogBuilder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String iconType = arrayAdapter.getItem(which);
                        Intent intent1;
                        switch (iconType) {
                            case "Built in":
                                intent1 = new Intent(arrayAdapter.getContext(), ChooserActivity.class);
                                intent1.putExtra("Chooser", "Resource");
                                startActivityForResult(intent1, 11);
                                break;
                            case "App":
                                intent1 = new Intent(arrayAdapter.getContext(), ChooserActivity.class);
                                intent1.putExtra("Chooser", "App");
                                startActivityForResult(intent1, 12);
                                break;
                        }
                    }
                });
                dialogBuilder.create().show();
            }
        });

        titleView.setClickable(true);
        titleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText editText = new EditText(v.getContext());
                editText.setText(mTitle);
                editText.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                editText.setHint("Title");
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(v.getContext());
                dialogBuilder.setView(editText);
                dialogBuilder.setTitle("Title:");
                dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mTitle = editText.getText().toString();
                        previewTitle.setText(mTitle);
                    }
                });
                dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = dialogBuilder.create();
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                dialog.show();
                editText.requestFocus();
            }
        });

        clickActionView.setClickable(true);
        clickActionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(v.getContext());
                dialogBuilder.setTitle("Select click action:");
                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(v.getContext(),
                        android.R.layout.simple_list_item_1);
                arrayAdapter.add("Do Nothing");
                arrayAdapter.add("Launch App");
                arrayAdapter.add("Launch Web Page");
                arrayAdapter.add("Toast Message");
                dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialogBuilder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String clickActionType = arrayAdapter.getItem(which);
                        Intent intent2;
                        switch (clickActionType) {
                            case "Do Nothing":
                                mClickActionType = "N";
                                mClickActionPackage = "";
                                mClickActionToastText = "";
                                mClickActionWebAddress = "";
                                break;
                            case "Launch App":
                                intent2 = new Intent(arrayAdapter.getContext(), ChooserActivity.class);
                                intent2.putExtra("Chooser", "App");
                                startActivityForResult(intent2, 21);
                                break;
                            case "Launch Web Page":
                                final EditText editText = new EditText(arrayAdapter.getContext());
                                if (!mClickActionWebAddress.equals("")) {
                                    editText.setText(mClickActionWebAddress);
                                }
                                editText.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_EDIT_TEXT);
                                editText.setHint("https://www.google.com/");
                                AlertDialog.Builder dialogBuilderWebLaunch = new AlertDialog.Builder(arrayAdapter.getContext());
                                dialogBuilderWebLaunch.setView(editText);
                                dialogBuilderWebLaunch.setTitle("Web Address:");
                                dialogBuilderWebLaunch.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mClickActionType = "W";
                                        mClickActionPackage = "";
                                        mClickActionToastText = "";
                                        mClickActionWebAddress = editText.getText().toString();
                                    }
                                });
                                dialogBuilderWebLaunch.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                AlertDialog dialogWebLaunch = dialogBuilderWebLaunch.create();
                                dialogWebLaunch.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                                dialogWebLaunch.show();
                                editText.requestFocus();
                                break;
                            case "Toast Message":
                                final EditText tEditText = new EditText(arrayAdapter.getContext());
                                if (!mClickActionToastText.equals("")) {
                                    tEditText.setText(mClickActionToastText);
                                }
                                tEditText.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                                tEditText.setHint("Hello World");
                                AlertDialog.Builder dialogBuilderToast = new AlertDialog.Builder(arrayAdapter.getContext());
                                dialogBuilderToast.setView(tEditText);
                                dialogBuilderToast.setTitle("Toast Message:");
                                dialogBuilderToast.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mClickActionType = "T";
                                        mClickActionPackage = "";
                                        mClickActionToastText = "";
                                        mClickActionWebAddress = tEditText.getText().toString();
                                    }
                                });
                                dialogBuilderToast.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                AlertDialog dialogToast = dialogBuilderToast.create();
                                dialogToast.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                                dialogToast.show();
                                tEditText.requestFocus();
                                break;
                        }
                    }
                });
                dialogBuilder.create().show();
            }
        });

        longClickView.setClickable(true);
        longClickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(v.getContext());
                dialogBuilder.setTitle("Select click action:");
                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(v.getContext(),
                        android.R.layout.simple_list_item_1);
                arrayAdapter.add("Do Nothing");
                arrayAdapter.add("Launch App");
                arrayAdapter.add("Launch Web Page");
                arrayAdapter.add("Toast Message");
                dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialogBuilder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String longClickActionType = arrayAdapter.getItem(which);
                        Intent intent3;
                        switch (longClickActionType) {
                            case "Do Nothing":
                                mLongClickActionType = "N";
                                mLongClickActionPackage = "";
                                mLongClickActionToastText = "";
                                mLongClickActionWebAddress = "";
                                break;
                            case "Launch App":
                                intent3 = new Intent(arrayAdapter.getContext(), ChooserActivity.class);
                                intent3.putExtra("Chooser", "App");
                                startActivityForResult(intent3, 31);
                                break;
                            case "Launch Web Page":
                                final EditText editText = new EditText(arrayAdapter.getContext());
                                if (!mLongClickActionWebAddress.equals("")) {
                                    editText.setText(mLongClickActionWebAddress);
                                }
                                editText.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_EDIT_TEXT);
                                editText.setHint("https://www.google.com/");
                                AlertDialog.Builder dialogBuilderWebLaunch = new AlertDialog.Builder(arrayAdapter.getContext());
                                dialogBuilderWebLaunch.setView(editText);
                                dialogBuilderWebLaunch.setTitle("Web Address:");
                                dialogBuilderWebLaunch.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mLongClickActionType = "W";
                                        mLongClickActionPackage = "";
                                        mLongClickActionToastText = "";
                                        mLongClickActionWebAddress = editText.getText().toString();
                                    }
                                });
                                dialogBuilderWebLaunch.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                AlertDialog dialogWebLaunch = dialogBuilderWebLaunch.create();
                                dialogWebLaunch.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                                dialogWebLaunch.show();
                                editText.requestFocus();
                                break;
                            case "Toast Message":
                                final EditText tEditText = new EditText(arrayAdapter.getContext());
                                if (!mLongClickActionToastText.equals("")) {
                                    tEditText.setText(mLongClickActionToastText);
                                }
                                tEditText.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                                tEditText.setHint("Hello World");
                                AlertDialog.Builder dialogBuilderToast = new AlertDialog.Builder(arrayAdapter.getContext());
                                dialogBuilderToast.setView(tEditText);
                                dialogBuilderToast.setTitle("Toast Message:");
                                dialogBuilderToast.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mLongClickActionType = "T";
                                        mLongClickActionPackage = "";
                                        mLongClickActionToastText = "";
                                        mLongClickActionWebAddress = tEditText.getText().toString();
                                    }
                                });
                                dialogBuilderToast.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                AlertDialog dialogToast = dialogBuilderToast.create();
                                dialogToast.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                                dialogToast.show();
                                tEditText.requestFocus();
                                break;
                        }
                    }
                });
                dialogBuilder.create().show();
            }
        });

        unlockPromptView.setClickable(true);
        unlockPromptView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (unlockPromptCheck.isChecked()) {
                    unlockPromptCheck.setChecked(false);
                    mUnlockPrompt = false;
                } else {
                    unlockPromptCheck.setChecked(true);
                    mUnlockPrompt = true;
                }
            }
        });

        unlockPromptCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mUnlockPrompt = isChecked;
            }
        });

        collapseTrayView.setClickable(true);
        collapseTrayView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (collapseTrayCheck.isChecked()) {
                    collapseTrayCheck.setChecked(false);
                    mCollapseTray = false;
                } else {
                    collapseTrayCheck.setChecked(true);
                    mCollapseTray = true;
                }
            }
        });

        collapseTrayCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCollapseTray = isChecked;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            if(requestCode < 20){
                ImageView previewIcon = (ImageView) findViewById(R.id.editTilePreviewIcon);
                ApplicationInfo app;
                switch (requestCode){
                    case 11:
                        mIconUri = resourceToUri(this, data.getIntExtra("selected_icon", 0));
                        mIconPackage = getPackageName();
                        mIconID = data.getIntExtra("selected_icon", 0);
                        previewIcon.setImageURI(mIconUri);
                        break;
                    case 12:
                        app = data.getParcelableExtra("selected_app");
                        mIconUri = appInfoToUri(app);
                        mIconPackage = app.packageName;
                        mIconID = app.icon;
                        previewIcon.setImageURI(mIconUri);
                        break;
                    case 21:
                        app = data.getParcelableExtra("selected_app");
                        mClickActionType = "A";
                        mClickActionPackage = app.packageName;
                        mClickActionToastText = "";
                        mClickActionWebAddress = "";
                        break;
                    case 31:
                        app = data.getParcelableExtra("selected_app");
                        mLongClickActionType = "A";
                        mLongClickActionPackage = app.packageName;
                        mLongClickActionToastText = "";
                        mLongClickActionWebAddress = "";
                        break;
                }
            }
        }

    }

    private Uri resourceToUri (Context context, int resID) {

        return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                context.getResources().getResourcePackageName(resID) + "/" +
                context.getResources().getResourceTypeName(resID) + "/" +
                context.getResources().getResourceEntryName(resID));

    }

    private Uri appInfoToUri (ApplicationInfo appInfo) {

        return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                appInfo.packageName + "/" +
                appInfo.icon);

    }


    class AppInfo {
        private String appName = "";
        private String pName = "";
        private String versionName = "";
        private int versionCode = 0;
        private Drawable icon;
        private void prettyPrint() {
            Log.v("AppInfo:", appName + "\t" + pName + "\t" + versionName + "\t" + versionCode);
        }
    }

    private ArrayList<AppInfo> getPackages() {
        // false = no system packages
        ArrayList<AppInfo> apps = getInstalledApps(false);

        final int max = apps.size();
        for (int i=0; i<max; i++) {
            apps.get(i).prettyPrint();
        }
        return apps;
    }

    private ArrayList<AppInfo> getInstalledApps(boolean getSysPackages) {
        ArrayList<AppInfo> res = new ArrayList<>();
        List<PackageInfo> packs = getPackageManager().getInstalledPackages(0);
        for(int i=0;i<packs.size();i++) {
            PackageInfo p = packs.get(i);
            if ((!getSysPackages) && (p.versionName == null)) {
                continue ;
            }
            AppInfo newInfo = new AppInfo();
            newInfo.appName = p.applicationInfo.loadLabel(getPackageManager()).toString();
            newInfo.pName = p.packageName;
            newInfo.versionName = p.versionName;
            newInfo.versionCode = p.versionCode;
            newInfo.icon = p.applicationInfo.loadIcon(getPackageManager());
            res.add(newInfo);
        }
        return res;
    }
}