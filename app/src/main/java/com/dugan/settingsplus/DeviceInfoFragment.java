package com.dugan.settingsplus;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by leona on 12/25/2015.
 */
public class DeviceInfoFragment extends Fragment {

    public DeviceInfoFragment(){

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.device_info_fragment, container, false);

        return rootView;
    }

}