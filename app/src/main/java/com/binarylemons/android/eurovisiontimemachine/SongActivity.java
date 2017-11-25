package com.binarylemons.android.eurovisiontimemachine;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

/**
 * Created by Luis on 22/11/2017.
 */

public class SongActivity extends SingleFragmentActivity {

    private static final String EXTRA_SONG_CODE = "com.binarylemons.android.eurovisiontimemachine.SongActivity.extra_song_code";

    public static Intent newIntent(Context packageContext, String songCode) {
        Intent intent = new Intent(packageContext, SongActivity.class);
        intent.putExtra(EXTRA_SONG_CODE, songCode);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        String songCode = getIntent().getStringExtra(EXTRA_SONG_CODE);
        return SongFragment.newInstance(songCode);
    }
}
