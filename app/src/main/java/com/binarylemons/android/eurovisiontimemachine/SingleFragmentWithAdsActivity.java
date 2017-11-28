package com.binarylemons.android.eurovisiontimemachine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.binarylemons.android.eurovisiontimemachine.managers.AdsManager;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

/**
 * Created by Luis on 19/02/2017.
 */

public abstract class SingleFragmentWithAdsActivity extends SingleFragmentActivity {

    private InterstitialAd mInterstitialAd;

    @Override
    public void onCreate(Bundle savedInstaceState) {
        super.onCreate(savedInstaceState);
        loadInterstitialAd();
    }

    public void startActivityMaybeWithAd(Intent intent) {
        setAdListener(intent);
        showInterstitialAd(intent);
    }

    public void loadInterstitialAd() {
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(AdsManager.INTERSTITIAL_AD_ID);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("BDD91807971547591A3B72B538EB15D1")
                .build();
        mInterstitialAd.loadAd(adRequest);
    }

    public void setAdListener(final Intent intent) {
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdOpened() {
                super.onAdOpened();
                AdsManager.get(getApplicationContext()).resetViewsAndTimeSinceLastAd();
            }

            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
                startActivity(intent);
            }

        });
    }

    public void showInterstitialAd(Intent intent) {
        if ((AdsManager.get(this).isTimeForInterstitialAd()) && (mInterstitialAd.isLoaded())) {
            mInterstitialAd.show();
        } else {
            startActivity(intent);
        }
    }

}
