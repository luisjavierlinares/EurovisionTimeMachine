package com.binarylemons.android.eurovisiontimemachine.model;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.binarylemons.android.eurovisiontimemachine.database.RoEuroEdition;

/**
 * Created by Luis on 26/10/2017.
 */

public class EuroEdition {

    public enum ContestType {ONLY_FINAL, WITH_SEMIFINALS};
    public enum WinnerType {SINGLE_WINNER, TRIPLE_WINNER};

    private String mYear;
    private EuroCountry mHostCountry;
    private EuroCity mHostCity;
    private String mVenue;
    private String mSlogan;

    public EuroEdition(String year, EuroCity hostCity) {
        this(year, hostCity, null, null);
    }

    public EuroEdition(String year, EuroCity hostCity, String venue) {
        this(year, hostCity, null, venue);
    }

    public EuroEdition(String year, EuroCity hostCity, String venue, String slogan) {
        mYear = year;
        mHostCountry = hostCity.getCountry();
        mHostCity = hostCity;
        mSlogan = slogan;
        mVenue = venue;
    }

    public EuroEdition(RoEuroEdition roEuroEdition) {
        mYear = String.valueOf(roEuroEdition.getYear());

        EuroCountry.Code countryCode = EuroCountry.Code.valueOf(roEuroEdition.getCountry());
        mHostCountry = new EuroCountry(countryCode);

        EuroCity.Code cityCode = EuroCity.Code.valueOf(roEuroEdition.getCity());
        mHostCity = new EuroCity(mHostCountry, cityCode);

        mVenue = roEuroEdition.getVenue();

        mSlogan = roEuroEdition.getSlogan();
    }

    public String getYear() {
        return mYear;
    }

    public EuroCountry getHostCountry() {
        return mHostCountry;
    }

    public EuroCity getHostCity() {
        return mHostCity;
    }

    public String getSlogan() {
        return mSlogan;
    }

    public String getVenue() {
        return mVenue;
    }

    public Drawable getEuroFlag(Context context) {
        String drawableName = "ic_euroflag_" + mHostCountry.getCountryCode().toString().toLowerCase();
        int drawableId = context.getResources().getIdentifier(drawableName, "drawable", context.getApplicationInfo().packageName);
        return ContextCompat.getDrawable(context, drawableId);
    }
}
