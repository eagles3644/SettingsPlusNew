package com.dugan.settingsplus.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

/**
 * Created by leona on 11/29/2015.
 */
public class MySQLHelper extends SQLiteOpenHelper {

    //private vars
    private static final String logTag = "MySQLHelper";

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "SettingsPlus.db";

    //Tables
    public static final String TBL_LOGS = "logs";
    public static final String TBL_PROFILES = "profiles";
    public static final String TBL_TRIGGERS = "triggers";
    public static final String TBL_CUSTOM_TILES = "custom_tiles";

    //Columns
    public static final String COL_LOG_ID = "_id";
    public static final String COL_LOG_TIME = "time";
    public static final String COL_LOG_ACTION = "action";
    public static final String COL_LOG_FROM = "from_prof";
    public static final String COL_LOG_TO = "to_prof";
    public static final String COL_LOG_DELETED = "deleted";

    public static final String COL_PROF_ID = "_id";
    public static final String COL_PROF_NAME = "name";
    public static final String COL_PROF_DO_NOT_DIST = "do_not_disturb";
    public static final String COL_PROF_RING_MODE = "ring_mode";
    public static final String COL_PROF_RING_VOL = "ring_volume";
    public static final String COL_PROF_BLUETOOTH = "bluetooth";
    public static final String COL_PROF_WIFI = "wifi";
    public static final String COL_PROF_DELETED = "deleted";

    public static final String COL_TRIG_ID = "_id";
    public static final String COL_TRIG_PROF_ID = "prof_id";
    public static final String COL_TRIG_TYPE = "trig_type";
    public static final String COL_TRIG_TIME = "time";
    public static final String COL_TRIG_BAT_LEVEL = "battery_level";
    public static final String COL_TRIG_DELETED = "deleted";

    public static final String COL_TILE_ID = "_id";
    public static final String COL_TILE_BROADCAST_ID = "broadcast_id";
    public static final String COL_TILE_TITLE = "title";
    public static final String COL_TILE_ICON_URI = "icon_uri";
    public static final String COL_TILE_ICON_PACKAGE = "icon_package";
    public static final String COL_TILE_ICON_ID = "icon_id";
    public static final String COL_TILE_CLICK_ACT_TYPE = "click_action_type";
    public static final String COL_TILE_CLICK_ACT_PACKAGE = "click_action_package";
    public static final String COL_TILE_CLICK_ACT_TOAST = "click_action_toast";
    public static final String COL_TILE_CLICK_ACT_WEB_ADDR = "click_action_web_addr";
    public static final String COL_TILE_LONG_CLICK_ACT_TYPE = "long_click_action_type";
    public static final String COL_TILE_LONG_CLICK_ACT_PACKAGE = "long_click_action_package";
    public static final String COL_TILE_LONG_CLICK_ACT_TOAST = "long_click_action_toast";
    public static final String COL_TILE_LONG_CLICK_ACT_WEB_ADDR = "long_click_action_web_addr";
    public static final String COL_TILE_COLLAPSE_TRAY = "collapse_tray";
    public static final String COL_TILE_PROMPT_UNLOCK = "prompt_unlock";
    public static final String COL_TILE_ENABLE_TILE = "enable_tile";
    public static final String COL_TILE_DELETED = "deleted";

    //Create Statements
    public static final String SQL_CREATE_LOG_TABLE = "CREATE TABLE " + TBL_LOGS + " (" +
            COL_LOG_ID + " INTEGER PRIMARY KEY, " + COL_LOG_TIME + " INTEGER, " + COL_LOG_ACTION +
            " TEXT, " + COL_LOG_FROM + " TEXT, " + COL_LOG_TO + " TEXT, " + COL_LOG_DELETED + " TEXT)";

    public static final String SQL_CREATE_PROF_TABLE = "CREATE TABLE " + TBL_PROFILES + " (" +
            COL_PROF_ID + " INTEGER PRIMARY KEY, " + COL_PROF_NAME + " TEXT, " + COL_PROF_DO_NOT_DIST
            + " INTEGER, " + COL_PROF_RING_MODE + " INTEGER, " + COL_PROF_RING_VOL + " INTEGER, " +
            COL_PROF_BLUETOOTH + " INTEGER, " + COL_PROF_WIFI + " INTEGER, " + COL_PROF_DELETED + " TEXT)";

    public static final String SQL_CREATE_TRIG_TABLE = "CREATE TABLE " + TBL_TRIGGERS + " (" +
            COL_TRIG_ID + " INTEGER PRIMARY KEY, " + COL_TRIG_PROF_ID + " INTEGER, " +
            COL_TRIG_TYPE + " TEXT, " + COL_TRIG_TIME + " TEXT, " + COL_TRIG_BAT_LEVEL + " INTEGER, "
            + COL_TRIG_DELETED + " TEXT)";

    public static final String SQL_CREATE_TILE_TABLE = "CREATE TABLE " + TBL_CUSTOM_TILES + " (" +
            COL_TILE_ID + " INTEGER PRIMARY KEY, " + COL_TILE_BROADCAST_ID + " TEXT, "
            + COL_TILE_TITLE + " TEXT, " + COL_TILE_ICON_URI + " TEXT, " + COL_TILE_ICON_PACKAGE
            + " TEXT, " + COL_TILE_ICON_ID + " INTEGER, " + COL_TILE_CLICK_ACT_TYPE + " TEXT, "
            + COL_TILE_CLICK_ACT_PACKAGE + " TEXT, " + COL_TILE_CLICK_ACT_TOAST + " TEXT, "
            + COL_TILE_CLICK_ACT_WEB_ADDR + " TEXT, " + COL_TILE_LONG_CLICK_ACT_TYPE + " TEXT, "
            + COL_TILE_LONG_CLICK_ACT_PACKAGE + " TEXT, " + COL_TILE_LONG_CLICK_ACT_TOAST
            + " TEXT, " + COL_TILE_LONG_CLICK_ACT_WEB_ADDR + " TEXT, " + COL_TILE_COLLAPSE_TRAY
            + " TEXT, " + COL_TILE_PROMPT_UNLOCK + " TEXT, " + COL_TILE_ENABLE_TILE + " TEXT, "
            + COL_TILE_DELETED + " TEXT)";

    public MySQLHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(logTag, "onCreate called! Database Name = " + DATABASE_NAME + " Version = " + DATABASE_VERSION);
        db.execSQL(SQL_CREATE_LOG_TABLE);
        db.execSQL(SQL_CREATE_PROF_TABLE);
        db.execSQL(SQL_CREATE_TRIG_TABLE);
        db.execSQL(SQL_CREATE_TILE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(logTag, "onUpgrade called! Database Name = " + DATABASE_NAME + " Version = " + DATABASE_VERSION);
        //drop tables
        db.execSQL("DROP TABLE " + TBL_LOGS);
        db.execSQL("DROP TABLE " + TBL_PROFILES);
        db.execSQL("DROP TABLE " + TBL_TRIGGERS);
        db.execSQL("DROP TABLE " + TBL_CUSTOM_TILES);

        //create tables
        db.execSQL(SQL_CREATE_LOG_TABLE);
        db.execSQL(SQL_CREATE_PROF_TABLE);
        db.execSQL(SQL_CREATE_TRIG_TABLE);
        db.execSQL(SQL_CREATE_TILE_TABLE);
    }

    public void insertLog(String action, String to, String from){
        SQLiteDatabase db = getWritableDatabase();
        long time = System.currentTimeMillis();
        ContentValues values = new ContentValues();
        values.put(COL_LOG_TIME, time);
        values.put(COL_LOG_ACTION, action);
        values.put(COL_LOG_TO, to);
        values.put(COL_LOG_FROM, from);
        values.put(COL_LOG_DELETED, "N");
        db.insert(TBL_LOGS, null, values);
    }

    public Cursor getLogs(){
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TBL_LOGS + " WHERE " + COL_LOG_DELETED + "='N' ORDER BY " + COL_LOG_ID + " DESC";
        return db.rawQuery(query, null);
    }

    public int logCount(){
        Cursor cursor = getLogs();
        cursor.moveToLast();
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public void deleteOlderLogRows(int logLimit){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TBL_LOGS, COL_LOG_ID + " NOT IN ( SELECT " + COL_LOG_ID + " FROM " + TBL_LOGS + " ORDER BY " + COL_LOG_ID + " DESC LIMIT 0," + logLimit + " )", null);
    }

    public void deleteAllLogRows(){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TBL_LOGS, null, null);
    }

    public void insertTile(String tileBroadcastID, String tileTitle, String tileIconUri,
                          String tileIconPackage, int tileIconID, String tileClickActType,
                          String tileClickActPackage, String tileClickActToast,
                          String tileClickActWebAddr, String tileLongClickActType,
                          String tileLongClickActPackage, String tileLongClickActToast,
                          String tileLongClickActWebAddr, String tileCollapseTray,
                          String tileUnlockPrompt){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TILE_BROADCAST_ID, tileBroadcastID);
        values.put(COL_TILE_TITLE, tileTitle);
        values.put(COL_TILE_ICON_URI, tileIconUri);
        values.put(COL_TILE_ICON_PACKAGE, tileIconPackage);
        values.put(COL_TILE_ICON_ID, tileIconID);
        values.put(COL_TILE_CLICK_ACT_TYPE, tileClickActType);
        values.put(COL_TILE_CLICK_ACT_PACKAGE, tileClickActPackage);
        values.put(COL_TILE_CLICK_ACT_TOAST, tileClickActToast);
        values.put(COL_TILE_CLICK_ACT_WEB_ADDR, tileClickActWebAddr);
        values.put(COL_TILE_LONG_CLICK_ACT_TYPE, tileLongClickActType);
        values.put(COL_TILE_LONG_CLICK_ACT_PACKAGE, tileLongClickActPackage);
        values.put(COL_TILE_LONG_CLICK_ACT_TOAST, tileLongClickActToast);
        values.put(COL_TILE_LONG_CLICK_ACT_WEB_ADDR, tileLongClickActWebAddr);
        values.put(COL_TILE_COLLAPSE_TRAY, tileCollapseTray);
        values.put(COL_TILE_PROMPT_UNLOCK, tileUnlockPrompt);
        values.put(COL_TILE_ENABLE_TILE, "Y");
        values.put(COL_TILE_DELETED, "N");
        db.insert(TBL_CUSTOM_TILES, null, values);
    }

    public void updateTile(String tileBroadcastID, String tileTitle, String tileIconUri,
                           String tileIconPackage, int tileIconID, String tileClickActType,
                           String tileClickActPackage, String tileClickActToast,
                           String tileClickActWebAddr, String tileLongClickActType,
                           String tileLongClickActPackage, String tileLongClickActToast,
                           String tileLongClickActWebAddr, String tileCollapseTray,
                           String tileUnlockPrompt, int tileID, String tileEnabled){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TILE_BROADCAST_ID, tileBroadcastID);
        values.put(COL_TILE_TITLE, tileTitle);
        values.put(COL_TILE_ICON_URI, tileIconUri);
        values.put(COL_TILE_ICON_PACKAGE, tileIconPackage);
        values.put(COL_TILE_ICON_ID, tileIconID);
        values.put(COL_TILE_CLICK_ACT_TYPE, tileClickActType);
        values.put(COL_TILE_CLICK_ACT_PACKAGE, tileClickActPackage);
        values.put(COL_TILE_CLICK_ACT_TOAST, tileClickActToast);
        values.put(COL_TILE_CLICK_ACT_WEB_ADDR, tileClickActWebAddr);
        values.put(COL_TILE_LONG_CLICK_ACT_TYPE, tileLongClickActType);
        values.put(COL_TILE_LONG_CLICK_ACT_PACKAGE, tileLongClickActPackage);
        values.put(COL_TILE_LONG_CLICK_ACT_TOAST, tileLongClickActToast);
        values.put(COL_TILE_LONG_CLICK_ACT_WEB_ADDR, tileLongClickActWebAddr);
        values.put(COL_TILE_COLLAPSE_TRAY, tileCollapseTray);
        values.put(COL_TILE_PROMPT_UNLOCK, tileUnlockPrompt);
        values.put(COL_TILE_DELETED, "N");
        values.put(COL_TILE_ID, tileID);
        values.put(COL_TILE_ENABLE_TILE, tileEnabled);
        db.update(TBL_CUSTOM_TILES, values, COL_TILE_ID + " = " + tileID, null);
    }

    public Cursor getTiles(){
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TBL_CUSTOM_TILES + " WHERE " + COL_TILE_DELETED + "='N' ORDER BY " + COL_TILE_ID + " ASC";
        return db.rawQuery(query, null);
    }

    public Cursor getTileById(int id){
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TBL_CUSTOM_TILES + " WHERE " + COL_TILE_ID + "=" + id;
        return db.rawQuery(query, null);
    }

    public void updateTileEnabled(int tileID, String tileEnabled){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TILE_ID, tileID);
        values.put(COL_TILE_ENABLE_TILE, tileEnabled);
        db.update(TBL_CUSTOM_TILES, values, COL_TILE_ID + " = " + tileID, null);
    }

    public void deleteTileById(int id){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TBL_CUSTOM_TILES, COL_LOG_ID + " = " + id, null);
    }

    public void deleteAllTiles(){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TBL_CUSTOM_TILES, null, null);
    }

    public int tileCount(){
        Cursor cursor = getTiles();
        cursor.moveToLast();
        int count = cursor.getCount();
        cursor.close();
        return count;
    }
}
