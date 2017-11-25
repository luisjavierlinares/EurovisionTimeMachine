package com.binarylemons.android.eurovisiontimemachine.utils;

import android.content.Context;

import com.binarylemons.android.eurovisiontimemachine.model.EuroCountry;

import java.util.Comparator;

/**
 * Created by Luis on 15/11/2017.
 */

public class EuroCountryNameComparator implements Comparator<EuroCountry> {

    private Context mContext;

    public EuroCountryNameComparator(Context context) {
        mContext = context;
    }

    @Override
    public int compare(EuroCountry o1, EuroCountry o2) {
        String s1 = o1.getName(mContext);
        String s2 = o2.getName(mContext);
        return s1.compareToIgnoreCase(s2);
    }
}
