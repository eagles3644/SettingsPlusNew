package com.dugan.settingsplus;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by leona on 12/13/2015.
 */
public class ScheduledRebootsFragment extends Fragment {

    public ScheduledRebootsFragment(){

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.scheduled_reboots_fragment, container, false);

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.scheduledRebootsFAB);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Will add scheduled reboot.", Snackbar.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }

}
