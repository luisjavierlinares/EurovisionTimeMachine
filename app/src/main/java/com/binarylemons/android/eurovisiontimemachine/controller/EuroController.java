package com.binarylemons.android.eurovisiontimemachine.controller;

import android.content.Context;

import com.binarylemons.android.eurovisiontimemachine.database.RoEuroCountry;
import com.binarylemons.android.eurovisiontimemachine.database.RoEuroEdition;
import com.binarylemons.android.eurovisiontimemachine.database.RoEuroSong;
import com.binarylemons.android.eurovisiontimemachine.model.EuroCountry;
import com.binarylemons.android.eurovisiontimemachine.model.EuroEdition;
import com.binarylemons.android.eurovisiontimemachine.model.EuroQuery;
import com.binarylemons.android.eurovisiontimemachine.model.EuroRound;
import com.binarylemons.android.eurovisiontimemachine.model.EuroSong;
import com.binarylemons.android.eurovisiontimemachine.utils.EuroComparatorUtils;

import java.util.ArrayList;
import java.util.List;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmQuery;
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

    private void init() {

    }

    public List<EuroEdition> getAllEditions() {
        List<EuroEdition> editions = new ArrayList<>();

        Realm realm = Realm.getDefaultInstance();

        RealmResults<RoEuroEdition> roEuroEditions = realm.where(RoEuroEdition.class)
                .findAllSorted("year", Sort.DESCENDING);

        for (RoEuroEdition roEuroEdition : roEuroEditions) {
            EuroEdition euroEdition = new EuroEdition(roEuroEdition);
            editions.add(euroEdition);
        }

        return editions;
    }

    public EuroEdition getEdition(String year) {
        Realm realm = Realm.getDefaultInstance();

        RoEuroEdition roEuroEdition = realm.where(RoEuroEdition.class)
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

        countries = EuroComparatorUtils.sortCountryByCountryName(countries, mContext);

        return countries;
    }

    public EuroCountry getCountry(String countryCode) {
        Realm realm = Realm.getDefaultInstance();

        RoEuroCountry roEuroCountry = realm.where(RoEuroCountry.class)
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

        RealmResults<RoEuroSong> roEuroSongs = realm.where(RoEuroSong.class)
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

        RealmResults<RoEuroSong> roEuroSongs = realm.where(RoEuroSong.class)
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

        songs = EuroComparatorUtils.sortSongByPosition(songs, round);

        return songs;
    }

    public List<EuroSong> getSongs(EuroCountry country) {
        List<EuroSong> songs = new ArrayList<>();

        Realm realm = Realm.getDefaultInstance();

        RealmResults<RoEuroSong> roEuroSongs = realm.where(RoEuroSong.class)
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

        RoEuroSong roEuroSong = realm.where(RoEuroSong.class)
                .equalTo("songCode", songCode)
                .findFirst();

        if (roEuroSong != null) {
            return new EuroSong(roEuroSong);
        }

        return null;
    }

    public List<EuroSong> search(EuroQuery query) {
        List<EuroSong> songs = new ArrayList<>();

        Realm realm = Realm.getDefaultInstance();

        List<EuroEdition> editions = query.getEditions();
        Integer[] years = new Integer[query.getEditions().size()];
        int i = 0;
        for (EuroEdition edition : editions) {
            years[i] = Integer.parseInt(edition.getYear());
            i++;
        }

        List<EuroCountry> countries = query.getCountries();
        String[] countryNames = new String[query.getCountries().size()];
        i = 0;
        for (EuroCountry country : countries) {
            countryNames[i] = country.getCountryCode().toString();
            i++;
        }

        String artist = query.getArtist();

        String title = query.getTitle();

        String orderBy = "year";
        Sort orderType = Sort.DESCENDING;
        if (query.getOrderBy() == EuroQuery.OrderBy.COUNTRY) {
            orderBy = "country";
            orderType = Sort.DESCENDING;
        } else if (query.getOrderBy() == EuroQuery.OrderBy.POSITION) {
            orderBy = "finalPosition";
            orderType = Sort.ASCENDING;
        }

        RealmQuery<RoEuroSong> realmQuery = realm.where(RoEuroSong.class);

        if (years.length > 0) {
            realmQuery.in("year", years);
        }

        if (countryNames.length > 0) {
            realmQuery.in("country", countryNames);
        }

        if (artist != null) {
            realmQuery.contains("normalizedArtist", artist, Case.INSENSITIVE);
        }

        if (title != null) {
            realmQuery.contains("normalizedTitle", title, Case.INSENSITIVE);
        }

        RealmResults<RoEuroSong> roEuroSongs = realmQuery.findAllSorted(orderBy, orderType);

        for (RoEuroSong roEuroSong : roEuroSongs) {
            EuroSong euroSong = new EuroSong(roEuroSong);
            songs.add(euroSong);
        }

        if (query.getOrderBy() == EuroQuery.OrderBy.COUNTRY) {
            songs = EuroComparatorUtils.sortSongByCountryName(songs, mContext);
        }

        if (query.getOrderBy() == EuroQuery.OrderBy.POSITION) {
            songs = EuroComparatorUtils.sortSongByFinalPosition(songs);
        }

        return songs;
    }
}
