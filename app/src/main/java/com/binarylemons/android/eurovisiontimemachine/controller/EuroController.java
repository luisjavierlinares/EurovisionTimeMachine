package com.binarylemons.android.eurovisiontimemachine.controller;

import android.content.Context;

import com.binarylemons.android.eurovisiontimemachine.database.RoEuroCountry;
import com.binarylemons.android.eurovisiontimemachine.database.RoEuroEdition;
import com.binarylemons.android.eurovisiontimemachine.database.RoEuroSong;
import com.binarylemons.android.eurovisiontimemachine.model.EuroCountry;
import com.binarylemons.android.eurovisiontimemachine.model.EuroEdition;
import com.binarylemons.android.eurovisiontimemachine.model.EuroRound;
import com.binarylemons.android.eurovisiontimemachine.model.EuroSong;
import com.binarylemons.android.eurovisiontimemachine.utils.EuroCountryNameComparator;
import com.binarylemons.android.eurovisiontimemachine.utils.EuroSongPositionComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;


/**
 * Created by Luis on 26/10/2017.
 */

public class EuroController {

    private static EuroController sEuroController;

    private Context mContext;

    public static synchronized EuroController get(Context context) {
        if (sEuroController == null) {
            sEuroController = new EuroController(context);
        }
        return sEuroController;
    }

    private EuroController(Context context) {
        mContext = context;
        init();
    }

    private void init(){

    }

    public List<EuroEdition> getAllEditions() {
        List<EuroEdition> editions = new ArrayList<>();

        Realm realm = Realm.getDefaultInstance();

        RealmResults<RoEuroEdition> roEuroEditions =    realm.where(RoEuroEdition.class)
                                                        .findAllSorted("year", Sort.DESCENDING);

        for (RoEuroEdition roEuroEdition : roEuroEditions) {
            EuroEdition euroEdition = new EuroEdition(roEuroEdition);
            editions.add(euroEdition);
        }

        return editions;
    }

    public EuroEdition getEdition(String year) {
        Realm realm = Realm.getDefaultInstance();

        RoEuroEdition roEuroEdition =   realm.where(RoEuroEdition.class)
                                        .equalTo("year", Integer.parseInt(year))
                                        .findFirst();

        if (roEuroEdition != null) {
           return new EuroEdition(roEuroEdition);
        }

        return null;
    }

    public List<EuroCountry> getAllCountries() {
        List<EuroCountry> countries = new ArrayList<>();

        Realm realm = Realm.getDefaultInstance();

        RealmResults<RoEuroCountry> roEuroCountries = realm.where(RoEuroCountry.class).findAll();

        for (RoEuroCountry roEuroCountry : roEuroCountries) {
            EuroCountry euroCountry = new EuroCountry(roEuroCountry);
            countries.add(euroCountry);
        }

        EuroCountryNameComparator euroCountryNameComparator = new EuroCountryNameComparator(mContext);
        Collections.sort(countries, euroCountryNameComparator);

        return countries;
    }

    public EuroCountry getCountry(String countryCode) {
        Realm realm = Realm.getDefaultInstance();

        RoEuroCountry roEuroCountry =   realm.where(RoEuroCountry.class)
                .equalTo("countryCode", countryCode)
                .findFirst();

        if (roEuroCountry != null) {
            return new EuroCountry(roEuroCountry);
        }

        return null;
    }

    public List<EuroSong> getSongs(EuroEdition edition) {
        List<EuroSong> songs = new ArrayList<>();

        Realm realm = Realm.getDefaultInstance();

        RealmResults<RoEuroSong> roEuroSongs =  realm.where(RoEuroSong.class)
                                                .equalTo("year", Integer.parseInt(edition.getYear()))
                                                .findAll();

        for (RoEuroSong roEuroSong : roEuroSongs) {
            EuroSong euroSong = new EuroSong(roEuroSong);
            songs.add(euroSong);
        }

        return songs;
    }

    public List<EuroSong> getSongs(EuroEdition edition, EuroRound round) {
        List<EuroSong> songs = new ArrayList<>();

        Realm realm = Realm.getDefaultInstance();

        RealmResults<RoEuroSong> roEuroSongs =  realm.where(RoEuroSong.class)
                                                .equalTo("year", Integer.parseInt(edition.getYear()))
                                                .findAll();

        for (RoEuroSong roEuroSong : roEuroSongs) {
            EuroSong euroSong = new EuroSong(roEuroSong);
            if ((round == EuroRound.FINAL) && (roEuroSong.getFinalPosition() != 0)) {
                songs.add(euroSong);
            } else if ((round == EuroRound.SEMIFINAL1) && (roEuroSong.getSemi1Position() != 0)) {
                songs.add(euroSong);
            } else if ((round == EuroRound.SEMIFINAL2) && (roEuroSong.getSemi2Position() != 0)) {
                songs.add(euroSong);
            }
        }

        EuroSongPositionComparator euroSongPositionComparator = new EuroSongPositionComparator(round);
        Collections.sort(songs, euroSongPositionComparator);

        return songs;
    }

    public List<EuroSong> getSongs(EuroCountry country) {
        List<EuroSong> songs = new ArrayList<>();

        Realm realm = Realm.getDefaultInstance();

        RealmResults<RoEuroSong> roEuroSongs =  realm.where(RoEuroSong.class)
                .equalTo("country", country.getCountryCode().toString())
                .findAllSorted("year", Sort.DESCENDING);

        for (RoEuroSong roEuroSong : roEuroSongs) {
            EuroSong euroSong = new EuroSong(roEuroSong);
            songs.add(euroSong);
        }

        return songs;
    }

    public EuroSong getSong(String songCode) {
        Realm realm = Realm.getDefaultInstance();

        RoEuroSong roEuroSong =   realm.where(RoEuroSong.class)
                .equalTo("songCode", songCode)
                .findFirst();

        if (roEuroSong != null) {
            return new EuroSong(roEuroSong);
        }

        return null;
    }
}
