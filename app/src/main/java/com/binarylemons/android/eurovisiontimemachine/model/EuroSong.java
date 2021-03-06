package com.binarylemons.android.eurovisiontimemachine.model;

import android.content.Context;

import com.binarylemons.android.eurovisiontimemachine.R;
import com.binarylemons.android.eurovisiontimemachine.database.RoEuroSong;
import com.binarylemons.android.eurovisiontimemachine.utils.TextUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Luis on 26/10/2017.
 */

public class EuroSong {

    private String mSongCode;
    private String mYear;
    private EuroCountry mCountry;
    private EuroArtist mArtist;
    private String mTitle;
    private String mLanguage;
    private String mVideoId;
    private String mSecondaryId;
    private List<EuroEntry> mEntries;

    public EuroSong(String songCode, String year, EuroCountry country, EuroArtist artist, String title, String language, String videoId, List<EuroEntry> entries) {
        mSongCode = songCode;
        mYear = year;
        mCountry = country;
        mArtist = artist;
        mTitle = title;
        mLanguage = language;
        mVideoId = videoId;
        mEntries = entries;
    }

    public EuroSong(RoEuroSong roEuroSong) {
        mSongCode = roEuroSong.getSongCode();

        mYear = String.valueOf(roEuroSong.getYear());

        EuroCountry.Code countryCode = EuroCountry.Code.valueOf(roEuroSong.getCountry());
        mCountry = new EuroCountry(countryCode);

        mArtist = new EuroArtist(roEuroSong.getArtist());

        mTitle = roEuroSong.getTitle();

        mLanguage = roEuroSong.getLanguage();

        mEntries = new ArrayList<>();
        if (roEuroSong.getFinalOrder() != 0) {
            EuroEntry finalEntry = new EuroEntry(mYear, EuroRound.FINAL, roEuroSong.getFinalOrder(), roEuroSong.getFinalPosition(), roEuroSong.getFinalPoints());
            mEntries.add(finalEntry);
        }

        if (roEuroSong.getSemi1Order() != 0) {
            EuroEntry semi1Entry = new EuroEntry(mYear, EuroRound.SEMIFINAL1, roEuroSong.getSemi1Order(), roEuroSong.getSemi1Position(), roEuroSong.getSemi1Points());
            mEntries.add(semi1Entry);
        }

        if (roEuroSong.getSemi2Order() != 0) {
            EuroEntry semi2Entry = new EuroEntry(mYear, EuroRound.SEMIFINAL2, roEuroSong.getSemi2Order(), roEuroSong.getSemi2Position(), roEuroSong.getSemi2Points());
            mEntries.add(semi2Entry);
        }

        mVideoId = roEuroSong.getVideoId();
        mSecondaryId = roEuroSong.getSecondaryId();
    }

    public String getSongCode() {
        return mSongCode;
    }

    public String getYear() {
        return mYear;
    }

    public EuroCountry getCountry() {
        return mCountry;
    }

    public EuroArtist getArtist() {
        return mArtist;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getLanguage() {
        return mLanguage;
    }

    public String getVideoId() {
        return mVideoId;
    }

    public String getSecondaryId() {
        return mSecondaryId;
    }

    public String getLyrics(Context context) {
        String path;
        if (mYear.equals("1956")) {
            path = getYear().toString() + "/" + getCountry().getCountryCode() + String.valueOf(getFinalEntry().getOrder()) + ".txt";
        } else {
            path = getYear().toString() + "/" + getCountry().getCountryCode() + ".txt";
        }

        try {
            InputStream lyricsInputString = context.getAssets().open(path);
            String lyrics = TextUtils.convertStreamToString(lyricsInputString);
            return lyrics;
        } catch (IOException e) { }

        return context.getString(R.string.lyrics_not_available);
    }

    public EuroEntry getEntry(EuroRound round){
        for(EuroEntry thisEntry : mEntries) {
            if (thisEntry.getRound() == round) {
                return thisEntry;
            }
        }
        return null;
    }

    public EuroEntry getFinalEntry() {
        for(EuroEntry thisEntry : mEntries) {
            if (thisEntry.isFinal()) {
                return thisEntry;
            }
        }
        return null;
    }

    public EuroEntry getFirstSemifinalEntry() {
        for(EuroEntry thisEntry : mEntries) {
            if (thisEntry.isFirstSemifinal()) {
                return thisEntry;
            }
        }
        return null;
    }

    public EuroEntry getSecondSemifinalEntry() {
        for(EuroEntry thisEntry : mEntries) {
            if (thisEntry.isSecondSemifinal()) {
                return thisEntry;
            }
        }
        return null;
    }

    public boolean isWinner() {
        EuroEntry finalEntry = getFinalEntry();
        if (finalEntry == null) return false;

        if (finalEntry.isWinner()) {
            return true;
        }

        return false;
    }

    public boolean isSecondPlace() {
        EuroEntry finalEntry = getFinalEntry();
        if (finalEntry == null) return false;

        if (finalEntry.isSecondPlace()) {
            return true;
        }

        return false;
    }

    public boolean isThirdPlace() {
        EuroEntry finalEntry = getFinalEntry();
        if (finalEntry == null) return false;

        if (finalEntry.isThirdPlace()) {
            return true;
        }

        return false;
    }

    public boolean isClassifiedToFinal() {
        EuroEntry finalEntry = getFinalEntry();
        if (finalEntry == null) return false;
        return true;
    }

}
