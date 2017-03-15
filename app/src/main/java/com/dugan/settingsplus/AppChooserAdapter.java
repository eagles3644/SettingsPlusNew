package com.dugan.settingsplus;

/**
 * Created by leona on 12/29/2015.
 */

import android.widget.ImageView;
import java.util.List;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AppChooserAdapter extends ArrayAdapter<ApplicationInfo> {

    private List<ApplicationInfo> appsList = null;
    private Context context;
    private PackageManager packageManager;

    public AppChooserAdapter(Context context, List<ApplicationInfo> appsList) {
        super(context, 0, appsList);
        this.context = context;
        this.appsList = appsList;
        packageManager = context.getPackageManager();
    }

    @Override
    public int getCount() {
        return ((null != appsList) ? appsList.size() : 0);
    }

    @Override
    public ApplicationInfo getItem(int position) {
        return ((null != appsList) ? appsList.get(position) : null);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (null == view) {
            LayoutInflater layoutInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.chooser_row, null);
        }

        ApplicationInfo data = appsList.get(position);

        if (null != data) {
            TextView appName = (TextView) view.findViewById(R.id.chooser_Title);
            ImageView iconView = (ImageView) view.findViewById(R.id.chooser_Icon);

            appName.setText(data.loadLabel(packageManager));
            iconView.setImageDrawable(data.loadIcon(packageManager));
        }

        return view;

    }
};