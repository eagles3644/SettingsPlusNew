package com.dugan.settingsplus;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.dugan.settingsplus.utils.Constants;
import com.dugan.settingsplus.utils.inappbilling.IabHelper;
import com.dugan.settingsplus.utils.inappbilling.IabResult;
import com.dugan.settingsplus.utils.inappbilling.Inventory;
import com.dugan.settingsplus.utils.inappbilling.Purchase;

/**
 * Created by leona on 11/14/2015.
 */
public class StoreFragment extends Fragment {

    IabHelper mHelper;

    public StoreFragment(){

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.store_fragment, container, false);

        mHelper = new IabHelper(getContext(), Constants.base64EncodedPublicKey);
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            @Override
            public void onIabSetupFinished(IabResult result) {
                if (result.isSuccess()) {
                    Log.d("IabHelper", "startSetup Successful " + result);
                } else {
                    Log.d("IabHelper:", "startSetup Failed " + result);
                }
            }
        });

        Button buyAdFree = (Button) rootView.findViewById(R.id.storeAdFreeBuy);
        buyAdFree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHelper.launchPurchaseFlow(getActivity(), Constants.ITEM_SKU_AD_FREE, 10001,
                        mPurchaseFinishedListener, Constants.ITEM_PURCHASE_EXTRA_AD_FREE);
            }
        });

        return rootView;

    }

    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener
            = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result,
                                          Purchase purchase)
        {
            if (result.isFailure()) {
                Log.d("IabPFinishedListener:", "Failed");
                handleIabFailure(result.getResponse());
            }
            else if (purchase.getSku().equals(Constants.ITEM_SKU_AD_FREE)) {
                consumeItem();
                Log.d("IabPFinishedListener:", "Calling Consume");
            }

        }
    };

    private void handleIabFailure(int response) {
        switch(response){
            case 1:
                Toast.makeText(getContext(), "Purchase Cancelled.", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                Toast.makeText(getContext(), "Cannot complete purchase right now. No network connection.", Toast.LENGTH_SHORT).show();
                break;
            case 3:
                Toast.makeText(getContext(), "Cannot complete purchase right now. Invalid Billing API Version", Toast.LENGTH_SHORT).show();
                break;
            case 4:
                Toast.makeText(getContext(), "Cannot complete purchase right now. Item is no loner for sale.", Toast.LENGTH_SHORT).show();
                break;
            case 5:
                Toast.makeText(getContext(), "Cannot complete purchase right now. Error on the App Developer's end.", Toast.LENGTH_SHORT).show();
                break;
            case 6:
                Toast.makeText(getContext(), "Cannot complete purchase right now. Fatal error.", Toast.LENGTH_SHORT).show();
                break;
            case 7:
                Toast.makeText(getContext(), "You already own this item. This item cannot be purchased multiple times.", Toast.LENGTH_SHORT).show();
                break;
            case 8:
                Toast.makeText(getContext(), "Failed to consume since item is not owned.", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    public void consumeItem() {
        mHelper.queryInventoryAsync(mReceivedInventoryListener);
        Log.d("consumeItem:", "Called");
    }

    IabHelper.QueryInventoryFinishedListener mReceivedInventoryListener
            = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result,
                                             Inventory inventory) {

            if (result.isFailure()) {
                Log.d("QInventoryFinListener:", "Failed");
                handleIabFailure(result.getResponse());
            } else {
                mHelper.consumeAsync(inventory.getPurchase(Constants.ITEM_SKU_AD_FREE),
                        mConsumeFinishedListener);
                Log.d("QInventoryFinListener:", "Consume Async");
            }
        }
    };

    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener =
            new IabHelper.OnConsumeFinishedListener() {
                public void onConsumeFinished(Purchase purchase,
                                              IabResult result) {

                    if (result.isSuccess()) {
                        Log.d("ConsumeFinListener:", "Success");
                        if(purchase.getSku().equals(Constants.ITEM_SKU_AD_FREE)){
                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putBoolean(Constants.PREF_SHOW_ADS, false);
                            editor.apply();
                            ((MainActivity)getActivity()).hideAds();
                        }
                    } else {
                        handleIabFailure(result.getResponse());
                        Log.d("ConsumeFinListener:", "Failed");
                    }
                }
            };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHelper != null){
            mHelper.dispose();
        }
        mHelper = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent data)
    {
        Log.d("OnActivityResult:", "Called!");
        if (!mHelper.handleActivityResult(requestCode,
                resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
