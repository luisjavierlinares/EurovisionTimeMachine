package com.binarylemons.android.eurovisiontimemachine;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Created by Luis on 29/11/2017.
 */

public class SearchSelectYearsFragment extends DialogFragment {

    public static SearchSelectYearsFragment newInstance() {
        Bundle args = new Bundle();

        SearchSelectYearsFragment fragment = new SearchSelectYearsFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
