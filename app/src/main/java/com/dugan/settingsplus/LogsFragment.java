package com.dugan.settingsplus;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.dugan.settingsplus.utils.MySQLHelper;

/**
 * Created by leona on 11/14/2015.
 */
public class LogsFragment extends Fragment {

    private static MySQLHelper mDatabase;
    private static RelativeLayout mLogsLayout;
    private static RelativeLayout mNoLogsLayout;
    private static LogListAdapter mLogListAdapter;
    private static Cursor mLogsCursor;
    private static ListView mLogList;

    public LogsFragment(){
        //Empty Constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mDatabase = new MySQLHelper(getContext());
        final int[] logCount = new int[1];

        final View rootView = inflater.inflate(R.layout.logs_fragment, container, false);

        final AsyncTask<DBRunnable, Void, DBRunnable> execute = new DBTask().execute(new DBRunnable() {
            @Override
            public void executeDBTask() {
                logCount[0] = mDatabase.logCount();
            }

            @Override
            public void postExecuteDBTask() {
                mLogsLayout = (RelativeLayout) rootView.findViewById(R.id.logsLayout);
                mNoLogsLayout = (RelativeLayout) rootView.findViewById(R.id.noLogsLayout);
                if (logCount[0] > 0) {
                    new DBTask().execute(new DBRunnable() {
                        @Override
                        public void executeDBTask() {
                            mLogsCursor = mDatabase.getLogs();
                        }

                        @Override
                        public void postExecuteDBTask() {
                            mLogListAdapter = new LogListAdapter(rootView.getContext(), mLogsCursor, 0);
                            mNoLogsLayout.setVisibility(View.GONE);
                            mLogsLayout.setVisibility(View.VISIBLE);
                            mLogList = (ListView) rootView.findViewById(R.id.logsList);
                            mLogList.setAdapter(mLogListAdapter);
                            mLogList.setClickable(true);
                            mLogList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    refreshLogCursor(getContext());
                                }
                            });
                            mLogList.setLongClickable(false);
                            mLogList.setDividerHeight(0);
                        }
                    });
                } else {
                    mNoLogsLayout.setVisibility(View.VISIBLE);
                    mLogsLayout.setVisibility(View.GONE);
                }
            }
        });

        return rootView;

    }

    public static void refreshLogCursor(final Context context){
        final Cursor[] newCursor = new Cursor[1];
        final int[] logCount = new int[1];
        new DBTask().execute(new DBRunnable() {
            @Override
            public void executeDBTask() {
                MySQLHelper db = new MySQLHelper(context);
                newCursor[0] = db.getLogs();
                logCount[0] = db.logCount();
            }

            @Override
            public void postExecuteDBTask() {
                mLogListAdapter.swapCursor(newCursor[0]);
                if (logCount[0] == 0) {
                    mLogsLayout.setVisibility(View.GONE);
                    mNoLogsLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
