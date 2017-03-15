package com.dugan.settingsplus;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dugan.settingsplus.utils.Constants;
import com.dugan.settingsplus.utils.rootchecker.RootChecker;

/**
 * Created by leona on 12/13/2015.
 */
public class RootCheckerFragment extends Fragment {

    public RootCheckerFragment(){

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.root_checker_fragment, container, false);

        Button whatIsRootLinkTV = (Button) rootView.findViewById(R.id.rootCheckLink);
        whatIsRootLinkTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myWebLink = new Intent(android.content.Intent.ACTION_VIEW);
                myWebLink.setData(Uri.parse("http://www.cnet.com/how-to/how-to-easily-root-an-android-device/"));
                startActivity(myWebLink);
            }
        });

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        boolean deviceRooted = prefs.getBoolean(Constants.PREF_DEVICE_ROOTED, false);
        long milliLastRootCheck = prefs.getLong(Constants.PREF_LAST_ROOT_CHECK, 0);
        String rootStatus = "Not Rooted";
        String rootStatusDesc = "Your device is NOT rooted! This means you will not be able to use all of the features this application has to offer.";
        String lastRootCheck = MyDateTimeFormatter.formatDateTime(milliLastRootCheck);
        String lastChecked  = "Last Checked: ";

        if(deviceRooted){
            rootStatus = "Rooted";
            rootStatusDesc = "Your device is rooted! This means you will be able to use all of the features this application has to offer.";
        }

        final TextView rootStatusTV = (TextView) rootView.findViewById(R.id.RootCheckerCardTitle);
        rootStatusTV.setText(rootStatus);
        final TextView rootStatusDescTV = (TextView) rootView.findViewById(R.id.RootCheckerCardDesc);
        rootStatusDescTV.setText(rootStatusDesc);
        final TextView rootLastCheckTV = (TextView) rootView.findViewById(R.id.RootCheckerCardLastChecked);
        rootLastCheckTV.setText(lastChecked + lastRootCheck);
        final Button recheckRootStatusTV = (Button) rootView.findViewById(R.id.RootCheckerCardAction);
        recheckRootStatusTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Checking root status...", Toast.LENGTH_SHORT).show();
                RootChecker.deviceRooted(getContext());
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                boolean deviceRooted = prefs.getBoolean(Constants.PREF_DEVICE_ROOTED, false);
                long milliLastRootCheck = prefs.getLong(Constants.PREF_LAST_ROOT_CHECK, 0);
                String rootStatus = "Not Rooted";
                String rootStatusDesc = "Your device is NOT rooted! This means you will not be able to use all of the features this application has to offer.";
                String lastRootCheck = MyDateTimeFormatter.formatDateTime(milliLastRootCheck);
                String lastChecked = "Last Checked: ";
                String recheckRootStatusLink = "<a href=''>Check for Root Access Now</a>";

                if (deviceRooted) {
                    rootStatus = "Rooted";
                    rootStatusDesc = "Your device is rooted! This means you will be able to use all of the features this application has to offer.";
                }

                rootStatusTV.setText(rootStatus);
                rootStatusDescTV.setText(rootStatusDesc);
                rootLastCheckTV.setText(lastChecked + lastRootCheck);
            }
        });

        return rootView;

    }

}
