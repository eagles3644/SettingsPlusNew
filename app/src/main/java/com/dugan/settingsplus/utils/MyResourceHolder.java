package com.dugan.settingsplus.utils;

/**
 * Created by leona on 12/31/2015.
 */
public class MyResourceHolder {

    int mResourceID;
    String mResourceName;

    public MyResourceHolder(){
        //Empty Constructor
    }

    public MyResourceHolder(int resourceID, String resourceName){
        setResourceID(resourceID);
        setResourceName(resourceName);
    }

    public void setResourceID(int resourceID){
        this.mResourceID = resourceID;
    }

    public void setResourceName(String resourceName){
        this.mResourceName = resourceName;
    }

    public int getResourceID(){
        return this.mResourceID;
    }

    public String getResourceName(){
        return this.mResourceName;
    }

}
