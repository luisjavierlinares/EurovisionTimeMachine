package com.binarylemons.android.eurovisiontimemachine.database;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Luis on 15/11/2017.
 */

public class RoEuroCountry extends RealmObject {

    @PrimaryKey
    private String countryCode;

    private int appearances;
    private int wins;
    private int secondPlaces;
    private int thirdPlaces;

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String code) {
        this.countryCode = code;
    }

    public int getAppearances() {
        return appearances;
    }

    public void setAppearances(int appearances) {
        this.appearances = appearances;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getSecondPlaces() {
        return secondPlaces;
    }

    public void setSecondPlaces(int secondPlaces) {
        this.secondPlaces = secondPlaces;
    }

    public int getThirdPlaces() {
        return thirdPlaces;
    }

    public void setThirdPlaces(int thirdPlaces) {
        this.thirdPlaces = thirdPlaces;
    }
}
