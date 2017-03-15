package com.dugan.settingsplus;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.dugan.settingsplus.utils.MySQLHelper;
import com.dugan.settingsplus.utils.customtiles.CustomTileHelper;

/**
 * Created by leona on 11/22/2015.
 */
public class QuickSettingsListAdapter extends CursorAdapter {

    private SparseBooleanArray selectedItemIds = new SparseBooleanArray();

    public QuickSettingsListAdapter(Context context, Cursor cursor, int flags){
        super(context, cursor, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.tile_list_row, null);
        ViewHolder holder = new ViewHolder();
        holder.tileId = (TextView) view.findViewById(R.id.tileListItemID);
        holder.tileTitle = (TextView) view.findViewById(R.id.tileListItemTitle);
        holder.tileDesc = (TextView) view.findViewById(R.id.tileListItemDesc);
        holder.tileIcon = (ImageView) view.findViewById(R.id.tileListItemIcon);
        holder.tileSwitch = (Switch) view.findViewById(R.id.tileListItemSwitch);
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();
        int id = cursor.getInt(cursor.getColumnIndex(MySQLHelper.COL_TILE_ICON_ID));
        String title = cursor.getString(cursor.getColumnIndex(MySQLHelper.COL_TILE_TITLE));
        String clickActionType = cursor.getString(cursor.getColumnIndex(MySQLHelper.COL_TILE_CLICK_ACT_TYPE));
        String clickAction = "Do Nothing";
        switch (clickActionType){
            case "N":
                clickAction = "Do Nothing";
                break;
            case "A":
                PackageManager pm = context.getPackageManager();
                ApplicationInfo appInfo = null;
                try {
                    appInfo = pm.getApplicationInfo(cursor.getString(cursor.getColumnIndex(MySQLHelper.COL_TILE_CLICK_ACT_PACKAGE)), PackageManager.GET_META_DATA);
                    clickAction = "Launch " + pm.getApplicationLabel(appInfo) + " App";
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                    clickAction = "Do Nothing - App Selected to launch is not installed anymore.";
                }
                break;
            case "W":
                clickAction = "Launch URL - " + cursor.getString(cursor.getColumnIndex(MySQLHelper.COL_TILE_CLICK_ACT_WEB_ADDR));
                break;
            case "T":
                clickAction = "Toast Message - " + cursor.getString(cursor.getColumnIndex(MySQLHelper.COL_TILE_CLICK_ACT_TOAST));
                break;
        }
        boolean enabled = false;
        if(cursor.getString(cursor.getColumnIndex(MySQLHelper.COL_TILE_ENABLE_TILE)).equals("Y")){
            enabled = true;
        }
        holder.tileTitle.setText(title);
        holder.tileDesc.setText(clickAction);
        holder.tileId.setText("" + id);
        holder.tileIcon.setImageURI(Uri.parse(cursor.getString(cursor.getColumnIndex(MySQLHelper.COL_TILE_ICON_URI))));
        holder.tileSwitch.setChecked(enabled);
        holder.tileSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                CustomTileHelper customTileHelper = new CustomTileHelper(context);
                if (isChecked) {
                    customTileHelper.showTile(cursor.getString(cursor.getColumnIndex(MySQLHelper.COL_TILE_TITLE)),
                            cursor.getInt(cursor.getColumnIndex(MySQLHelper.COL_TILE_ICON_ID)),
                            cursor.getString(cursor.getColumnIndex(MySQLHelper.COL_TILE_ICON_PACKAGE)));
                } else {
                    customTileHelper.hideTile();
                }
                QuickSettingsFragment.refreshTileCursor(context);
            }
        });
    }

    static class ViewHolder {
        ImageView tileIcon;
        TextView tileTitle;
        TextView tileDesc;
        TextView tileId;
        Switch tileSwitch;
    }

    public void selectItem(int id, boolean value){
        if (value){
            selectedItemIds.put(id, true);
        } else {
            selectedItemIds.delete(id);
        }
    }

    public void removeSelection(){
        selectedItemIds = new SparseBooleanArray();
    }

    public int getSelectedCount(){
        return selectedItemIds.size();
    }

    public SparseBooleanArray getSelectedIds(){
        return selectedItemIds;
    }
}