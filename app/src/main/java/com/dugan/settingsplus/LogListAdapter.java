package com.dugan.settingsplus;

import android.content.Context;
import android.database.Cursor;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.dugan.settingsplus.utils.MySQLHelper;

/**
 * Created by leona on 11/22/2015.
 */
public class LogListAdapter extends CursorAdapter {

    private SparseBooleanArray selectedItemIds = new SparseBooleanArray();

    public LogListAdapter(Context context, Cursor cursor, int flags){
        super(context, cursor, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.log_list_row, null);
        ViewHolder holder = new ViewHolder();
        holder.logID = (TextView) view.findViewById(R.id.logListID);
        holder.logAction = (TextView) view.findViewById(R.id.logListAction);
        holder.logTrigger = (TextView) view.findViewById(R.id.logListTrigger);
        holder.logTime = (TextView) view.findViewById(R.id.logListTime);
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();
        int id = cursor.getInt(cursor.getColumnIndex(MySQLHelper.COL_LOG_ID));
        long milliTime = cursor.getLong(cursor.getColumnIndex(MySQLHelper.COL_LOG_TIME));
        String action = cursor.getString(cursor.getColumnIndex(MySQLHelper.COL_LOG_ACTION));
        String to = cursor.getString(cursor.getColumnIndex(MySQLHelper.COL_LOG_TO));
        String from = cursor.getString(cursor.getColumnIndex(MySQLHelper.COL_LOG_FROM));
        String trigger = "Changed profile from " + from + " to " + to + ".";
        String time = MyDateTimeFormatter.formatDateTime(milliTime);
        if(from.equals("") || from.isEmpty() || from.equals("null")){
            trigger = "Started " + to + " profile.";
        }
        holder.logTime.setText(time);
        holder.logAction.setText(action);
        holder.logTrigger.setText(trigger);
        holder.logID.setText(""+id);
    }

    static class ViewHolder {
        TextView logTime;
        TextView logAction;
        TextView logTrigger;
        TextView logID;
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