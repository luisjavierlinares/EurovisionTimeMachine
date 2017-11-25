package com.binarylemons.android.eurovisiontimemachine.model;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.binarylemons.android.eurovisiontimemachine.database.RoEuroCountry;

/**
 * Created by Luis on 26/10/2017.
 */

public class EuroCountry {

    public enum Code {ALB, AND, ARM, AUS, AUT, AZE, BLR, BEL, BIH, BGR, CAN, HRV, CYP, CZE, DNK, EST, FIN, FRA, GEO, DEU, GRC, HUN, ISL, IRL, ISR, ITA, XKX, LVA, LTU, LUX, MKD, MLT, MDA, MCO, MNE, MAR, NLD, NOR, POL, PRT, ROU, RUS, SMR, SRB, SCG, SVK, SVN, ESP, SWE, CHE, TUR, UKR, GBR, GB_WLS, YUG}

    private Code mCountryCode;

    private int mAppearances;
    private int mWins;
    private int mSecondPlaces;
    private int mThirdPlaces;


    public EuroCountry(Code countryCode){
        this(countryCode, 0, 0, 0, 0);
    }

    public EuroCountry(Code countryCode, int appearances, int wins, int secondPlaces, int thirdPlaces) {
        mCountryCode = countryCode;
        mAppearances = appearances;
        mWins = wins;
        mSecondPlaces = secondPlaces;
        mThirdPlaces = thirdPlaces;
    }

    public EuroCountry(RoEuroCountry roEuroCountry) {
        mCountryCode = Code.valueOf(roEuroCountry.getCountryCode());
        mAppearances = roEuroCountry.getAppearances();
        mWins = roEuroCountry.getWins();
        mSecondPlaces = roEuroCountry.getSecondPlaces();
        mThirdPlaces = roEuroCountry.getThirdPlaces();
    }

    public Code getCountryCode() {
        return mCountryCode;
    }

    public String getName(Context context) {
        String stringName = "country_name_" + mCountryCode.toString().toLowerCase();
        int stringId = context.getResources().getIdentifier(stringName, "string", context.getApplicationInfo().packageName);
        return context.getString(stringId);
    }

    public Drawable getEuroFlag(Context context) {
        String drawableName = "ic_euroflag_" + mCountryCode.toString().toLowerCase();
        int drawableId = context.getResources().getIdentifier(drawableName, "drawable", context.getApplicationInfo().packageName);
        return ContextCompat.getDrawable(context, drawableId);
    }

    public Drawable getPlainFlag(Context context) {
        String drawableName = "ic_plainflag_" + mCountryCode.toString().toLowerCase();
        int drawableId = context.getResources().getIdentifier(drawableName, "drawable", context.getPackageName());
        return ContextCompat.getDrawable(context, drawableId);
    }

    public int getAppearances() {
        return mAppearances;
    }

    public int getWinsCount() {
        return mWins;
    }

    public int getSecondPlacesCount() {
        return mSecondPlaces;
    }

    public int getThirdPlacesCount() {
        return mThirdPlaces;
    }

}
