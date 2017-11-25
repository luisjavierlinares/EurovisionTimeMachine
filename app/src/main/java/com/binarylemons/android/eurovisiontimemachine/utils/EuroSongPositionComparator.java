package com.binarylemons.android.eurovisiontimemachine.utils;

import com.binarylemons.android.eurovisiontimemachine.model.EuroRound;
import com.binarylemons.android.eurovisiontimemachine.model.EuroSong;

import java.util.Comparator;

/**
 * Created by Luis on 16/11/2017.
 */

public class EuroSongPositionComparator implements Comparator<EuroSong> {

    private EuroRound mRound;

    public EuroSongPositionComparator(EuroRound round) {
        mRound = round;
    }

    @Override
    public int compare(EuroSong o1, EuroSong o2) {
        Integer i1 = o1.getEntry(mRound).getPosition();
        Integer i2 = o2.getEntry(mRound).getPosition();
        return i1.compareTo(i2);
    }
}