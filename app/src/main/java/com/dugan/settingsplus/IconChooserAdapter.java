package com.dugan.settingsplus;

/**
 * Created by leona on 12/29/2015.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dugan.settingsplus.utils.MyResourceHolder;

import java.util.ArrayList;

public class IconChooserAdapter extends ArrayAdapter<MyResourceHolder> {

    private ArrayList<MyResourceHolder> iconList = null;
    private Context context;

    public IconChooserAdapter(Context context, ArrayList<MyResourceHolder> iconList) {
        super(context, 0, iconList);
        this.context = context;
        this.iconList = iconList;
    }

    @Override
    public int getCount() {
        return ((null != iconList) ? iconList.size() : 0);
    }

    @Override
    public MyResourceHolder getItem(int position) {
        return ((null != iconList) ? iconList.get(position) : null);
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

        MyResourceHolder data = iconList.get(position);

        if (null != data) {
            TextView iconDesc = (TextView) view.findViewById(R.id.chooser_Title);
            ImageView iconView = (ImageView) view.findViewById(R.id.chooser_Icon);

            iconDesc.setText(data.getResourceName());
            iconView.setImageResource(data.getResourceID());
        }

        return view;

    }
};