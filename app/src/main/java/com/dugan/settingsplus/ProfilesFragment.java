package com.dugan.settingsplus;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dugan.settingsplus.utils.MyLogger;
import com.dugan.settingsplus.utils.MySQLHelper;

/**
 * Created by leona on 11/14/2015.
 */
public class ProfilesFragment extends Fragment {

    private MySQLHelper mDatabase;

    public ProfilesFragment(){

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mDatabase = new MySQLHelper(getContext());

        final View rootView = inflater.inflate(R.layout.profiles_fragment, container, false);

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.profFAB);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MyLogger().insertLogRow(getContext(), "Changed Profile", "Home", "Work");
            }
        });

        return rootView;

    }
}
