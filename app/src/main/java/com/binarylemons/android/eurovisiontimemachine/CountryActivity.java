package com.binarylemons.android.eurovisiontimemachine;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

/**
 * Created by Luis on 17/11/2017.
 */

public class CountryActivity extends SingleFragmentActivity implements SongCallbacks{

    private static final String EXTRA_COUNTRY_CODE = "com.binarylemons.android.eurovisiontimemachine.CountryActivity.extra_country_code";

    public static Intent newIntent(Context packageContext, String countryCode) {
        Intent intent = new Intent(packageContext, CountryActivity.class);
        intent.putExtra(EXTRA_COUNTRY_CODE, countryCode);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        String countryCode = getIntent().getStringExtra(EXTRA_COUNTRY_CODE);
        return CountryFragment.newInstance(countryCode);
    }

    @Override
    public void onSongSelected(String songCode) {
        Intent intent = SongActivity.newIntent(this, songCode);
        startActivity(intent);
    }
}
