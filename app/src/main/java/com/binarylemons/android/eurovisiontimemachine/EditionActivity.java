package com.binarylemons.android.eurovisiontimemachine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Luis on 16/11/2017.
 */

public class EditionActivity extends SingleFragmentWithAdsActivity implements SongCallbacks {

    private static final String EXTRA_EDITION_YEAR = "com.binarylemons.android.eurovisiontimemachine.EditionActivity.extra_edition_year";

    public static Intent newIntent(Context packageContext, String year) {
        Intent intent = new Intent(packageContext, EditionActivity.class);
        intent.putExtra(EXTRA_EDITION_YEAR, year);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        String year = getIntent().getStringExtra(EXTRA_EDITION_YEAR);
        return EditionFragment.newInstance(year);
    }

    @Override
    public void onSongSelected(String songCode) {
        Intent intent = SongActivity.newIntent(this, songCode);
        startActivityMaybeWithAd(intent);
    }
}
