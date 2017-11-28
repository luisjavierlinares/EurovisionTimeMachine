package com.binarylemons.android.eurovisiontimemachine;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.binarylemons.android.eurovisiontimemachine.model.EuroCountry;

public class EurovisionActivity extends SingleFragmentWithAdsActivity implements EditionCallbacks, CountryCallbacks {


    @Override
    protected Fragment createFragment() {
        return new EurovisionFragment().newInstance();
    }

    @Override
    public void onEditionSelected(String year) {
        Intent intent = EditionActivity.newIntent(this, year);
        startActivityMaybeWithAd(intent);
    }

    @Override
    public void onCountrySelected(String countryCode) {
        Intent intent = CountryActivity.newIntent(this, countryCode);
        startActivityMaybeWithAd(intent);
    }
}
