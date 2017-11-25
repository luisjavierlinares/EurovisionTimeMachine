package com.binarylemons.android.eurovisiontimemachine.model;

import android.content.Context;

/**
 * Created by Luis on 26/10/2017.
 */

public class EuroCity {

    public enum Code {AMSTERDAM, ATHENS, BAKU, BELGRADE, BERGEN, BIRMINGHAM, BRIGHTON, BRUSSELS, CANNES, COPENHAGEN, DUBLIN, DUSSELDORF, EDINBURGH, FRANKFURT, GOTHENBURG, HARROGATE, HELSINKI, HILVERSUM, ISTANBUL, JERUSALEM, KIEV, LAUSANNE, LISBON, LONDON, LUGANO, LUXEMBOURG, MADRID, MALMO, MILLSTREET, MOSCOW, MUNICH, NAPLES, OSLO, PARIS, RIGA, ROME, STOCKHOLM, TALLINN, THE_HAGUE, VIENNA, ZAGREB};

    private EuroCountry mCountry;
    private Code mCityCode;

    public EuroCity(EuroCountry country, Code cityCode) {
        mCountry = country;
        mCityCode = cityCode;
    }

    public EuroCountry getCountry() {
        return mCountry;
    }

    public Code getCityCode() {
        return mCityCode;
    }

    public String getCityName(Context context) {
        String stringName = "city_name_" + mCityCode.toString().toLowerCase();
        int stringId = context.getResources().getIdentifier(stringName, "string", context.getApplicationInfo().packageName);
        return context.getString(stringId);
    }
}
