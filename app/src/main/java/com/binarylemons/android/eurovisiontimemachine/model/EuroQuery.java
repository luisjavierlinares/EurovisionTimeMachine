package com.binarylemons.android.eurovisiontimemachine.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Luis on 29/11/2017.
 */

public class EuroQuery implements Serializable{

    public enum OrderBy {YEAR, COUNTRY, POSITION};

    private List<EuroEdition> mEditions;
    private List<EuroCountry> mCountries;
    private String mArtist;
    private String mTitle;
    private OrderBy mOrderBy;

    public EuroQuery() {
        mEditions = new ArrayList<>();
        mCountries = new ArrayList<>();
        mArtist = null;
        mTitle = null;
        mOrderBy = OrderBy.YEAR;
    }

    public List<EuroEdition> getEditions() {
        return mEditions;
    }

    public void setEditions(List<EuroEdition> editions) {
        mEditions = editions;
    }

    public List<EuroCountry> getCountries() {
        return mCountries;
    }

    public void setCountries(List<EuroCountry> countries) {
        mCountries = countries;
    }

    public String getArtist() {
        return mArtist;
    }

    public void setArtist(String artist) {
        mArtist = artist;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public OrderBy getOrderBy() {
        return mOrderBy;
    }

    public void setOrderBy(OrderBy orderBy) {
        mOrderBy = orderBy;
    }
}
