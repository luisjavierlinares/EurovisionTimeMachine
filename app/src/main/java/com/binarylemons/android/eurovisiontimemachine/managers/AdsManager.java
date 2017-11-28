package com.binarylemons.android.eurovisiontimemachine.managers;

import android.content.Context;

import com.binarylemons.android.eurovisiontimemachine.EuroSettings;

import java.util.Calendar;

/**
 * Created by Luis on 07/09/2017.
 */

public class AdsManager {

    private static final String ADMOB_APP_ID = "ca-app-pub-2740169353239994~8127189737";
    public static final String INTERSTITIAL_AD_ID = "ca-app-pub-2740169353239994/2819022730";

    private static final int MIN_VIEWS_SINCE_LAST_AD = 15;
    private static final int MIN_TIME_SINCE_LAST_AD = 20 * 60 * 1000;

    private static AdsManager sAdsManager;

    private Context mContext;

    public static synchronized AdsManager get(Context context){
        if (sAdsManager == null){
            sAdsManager = new AdsManager(context);
        }
        return sAdsManager;
    }

    private AdsManager(Context context) {
        mContext = context;
    }

    public final String getAdmobAppId() {
        return ADMOB_APP_ID;
    }

    public boolean isTimeForInterstitialAd() {
        int viewsSinceLastAdd = EuroSettings.getViewsSinceLastAd(mContext);
        long lastAdTime = EuroSettings.getTimeLastAd(mContext);
        long actualTime = Calendar.getInstance().getTimeInMillis();
        long timeSinceLastAdd = actualTime - lastAdTime;

        if ((viewsSinceLastAdd < MIN_VIEWS_SINCE_LAST_AD) || (timeSinceLastAdd < MIN_TIME_SINCE_LAST_AD)) {
            EuroSettings.setViewsSinceLastAd(mContext, viewsSinceLastAdd + 1);
            return false;
        } else {
            return true;
        }
    }

    public void resetViewsAndTimeSinceLastAd() {
        EuroSettings.setViewsSinceLastAd(mContext, 0);
        long actualTime = Calendar.getInstance().getTimeInMillis();
        EuroSettings.setTimeLastAd(mContext, actualTime);
    }
}
