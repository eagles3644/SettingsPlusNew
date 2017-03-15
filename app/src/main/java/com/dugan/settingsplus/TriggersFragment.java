package com.dugan.settingsplus;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by leona on 12/4/2015.
 */
public class TriggersFragment extends Fragment {

    public TriggersFragment(){
        //Empty Constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.triggers_fragment, container, false);

        return rootView;

    }

}
