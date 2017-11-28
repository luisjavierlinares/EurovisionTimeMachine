package com.binarylemons.android.eurovisiontimemachine.utils;

import android.content.Context;

import com.binarylemons.android.eurovisiontimemachine.model.EuroCountry;
import com.binarylemons.android.eurovisiontimemachine.model.EuroRound;
import com.binarylemons.android.eurovisiontimemachine.model.EuroSong;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Luis on 28/11/2017.
 */

public class EuroComparatorUtils {

    public static List<EuroCountry> sortByCountryName(List<EuroCountry> countries, Context context) {
        EuroCountryNameComparator euroCountryNameComparator = new EuroCountryNameComparator(context);
        Collections.sort(countries, euroCountryNameComparator);

        return countries;
    }

    public static List<EuroSong> sortByPosition(List<EuroSong> songs, EuroRound round) {
        EuroSongPositionComparator euroSongPositionComparator = new EuroSongPositionComparator(round);
        Collections.sort(songs, euroSongPositionComparator);

        return songs;
    }

    private static class EuroCountryNameComparator implements Comparator<EuroCountry> {

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

    private static class EuroSongPositionComparator implements Comparator<EuroSong> {

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
}
