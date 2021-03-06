package com.binarylemons.android.eurovisiontimemachine.database;

import android.content.Context;

import com.binarylemons.android.eurovisiontimemachine.model.EuroSong;
import com.binarylemons.android.eurovisiontimemachine.video.YoutubeManager;

import java.text.Normalizer;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Luis on 16/11/2017.
 */

public class RoEuroSong extends RealmObject {

    @PrimaryKey
    private String songCode;

    private int year;
    private String country;
    private String artist;
    private String title;
    private String language;

    private String videoId;
    private String secondaryId;

    private String normalizedArtist;
    private String normalizedTitle;

    private int finalPosition;
    private int finalPoints;
    private int finalOrder;
    private int semi1Position;
    private int semi1Points;
    private int semi1Order;
    private int semi2Position;
    private int semi2Points;
    private int semi2Order;

    public String getSongCode() {
        return songCode;
    }

    public void setSongCode(String songCode) {
        this.songCode = songCode;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getSecondaryId() {
        return secondaryId;
    }

    public void setSecondaryId(String secondaryId) {
        this.secondaryId = secondaryId;
    }

    public String getNormalizedArtist() {
        return normalizedArtist;
    }

    public void setNormalizedArtist(String normalizedArtist){
        this.normalizedArtist = normalizedArtist;
    }

    public String getNormalizedTitle() {
        return normalizedTitle;
    }

    public void setNormalizedTitle(String normalizedTitle) {
        this.normalizedTitle = normalizedTitle;
    }

    public int getFinalPosition() {
        return finalPosition;
    }

    public void setFinalPosition(int finalPosition) {
        this.finalPosition = finalPosition;
    }

    public int getFinalPoints() {
        return finalPoints;
    }

    public void setFinalPoints(int finalPoints) {
        this.finalPoints = finalPoints;
    }

    public int getFinalOrder() {
        return finalOrder;
    }

    public void setFinalOrder(int finalOrder) {
        this.finalOrder = finalOrder;
    }

    public int getSemi1Position() {
        return semi1Position;
    }

    public void setSemi1Position(int semi1Position) {
        this.semi1Position = semi1Position;
    }

    public int getSemi1Points() {
        return semi1Points;
    }

    public void setSemi1Points(int semi1Points) {
        this.semi1Points = semi1Points;
    }

    public int getSemi1Order() {
        return semi1Order;
    }

    public void setSemi1Order(int semi1Order) {
        this.semi1Order = semi1Order;
    }

    public int getSemi2Position() {
        return semi2Position;
    }

    public void setSemi2Position(int semi2Position) {
        this.semi2Position = semi2Position;
    }

    public int getSemi2Points() {
        return semi2Points;
    }

    public void setSemi2Points(int semi2Points) {
        this.semi2Points = semi2Points;
    }

    public int getSemi2Order() {
        return semi2Order;
    }

    public void setSemi2Order(int semi2Order) {
        this.semi2Order = semi2Order;
    }

    public String getString(Context context) {
        YoutubeManager youtubeManager = new YoutubeManager(context);

        String thisSecondaryId = secondaryId;
        if ((videoId == null) && (thisSecondaryId == null)) {
            EuroSong song = new EuroSong(this);
            String query = youtubeManager.generateSongQuery(song);
            thisSecondaryId = youtubeManager.searchFirst(query);
        }

        String out = "  {" + "\n" +
                "    \"songCode\" : " + "\"" + songCode + "\"" + ",\n" +
                "    \"year\" : " + year + ",\n" +
                "    \"country\" : " + "\"" + country + "\"" + ",\n" +
                "    \"artist\" : " + "\"" + artist + "\"" + ",\n" +
                "    \"title\" : " + "\"" + title + "\"" + ",\n" +
                "    \"language\" : " + "\"" + language + "\"" + ",\n";

        if (videoId != null) {
            out = out + "    \"videoId\" : " + "\"" + videoId + "\"" + ",\n";
        }

        if (thisSecondaryId != null) {
            out = out + "    \"secondaryId\" : " + "\"" + thisSecondaryId + "\"" + ",\n";
        }

        out = out + "    \"finalPosition\" : " + finalPosition + ",\n" +
                    "    \"finalPoints\" : " + finalPoints + ",\n" +
                    "    \"finalOrder\" : " + finalOrder + ",\n" +
                    "    \"semi1Position\" : " + semi1Position + ",\n" +
                    "    \"semi1Points\" : " + semi1Points + ",\n" +
                    "    \"semi1Order\" : " + semi1Order + ",\n" +
                    "    \"semi2Position\" : " + semi2Position + ",\n" +
                    "    \"semi2Points\" : " + semi2Points + ",\n" +
                    "    \"semi2Order\" : " + semi2Order + "\n" +
                    "  }," + "\n\n";

        return out;
    }
}
