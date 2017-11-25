package com.binarylemons.android.eurovisiontimemachine;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;

import com.binarylemons.android.eurovisiontimemachine.model.EuroRound;
import com.binarylemons.android.eurovisiontimemachine.model.EuroSong;

/**
 * Created by Luis on 17/11/2017.
 */

public class EurovisionUiUtils {

    public static int getPositionColor(Context context, EuroSong song) {
        if (song.isWinner()) {
            return ContextCompat.getColor(context, R.color.goldDark);
        } else if (song.isSecondPlace()) {
            return ContextCompat.getColor(context, R.color.silverDark);
        } else if (song.isThirdPlace()) {
            return ContextCompat.getColor(context, R.color.bronzeDark);
        } else if (!song.isClassifiedToFinal()) {
            return ContextCompat.getColor(context, R.color.red_900);
        } else {
            return ContextCompat.getColor(context, R.color.colorPrimaryDark);
        }
    }

    public static int getRoundPositionColor(Context context, EuroSong song, EuroRound round) {
        if (round == EuroRound.FINAL) {
            return getPositionColor(context, song);
        } else {
            if (song.isClassifiedToFinal()) {
                return ContextCompat.getColor(context, R.color.colorPrimaryDark);
            } else {
                return ContextCompat.getColor(context, R.color.red_900);
            }
        }
    }
}
