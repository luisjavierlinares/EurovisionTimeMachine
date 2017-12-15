package com.binarylemons.android.eurovisiontimemachine.utils;

import android.content.Context;

import com.binarylemons.android.eurovisiontimemachine.R;
import com.binarylemons.android.eurovisiontimemachine.database.RoEuroSong;
import com.binarylemons.android.eurovisiontimemachine.model.EuroSong;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Luis on 15/12/2017.
 */

public class JsonFilesGenerator {

    private static JsonFilesGenerator sJsonFilesGenerator;

    private Context mContext;

    public static synchronized JsonFilesGenerator get(Context context) {
        if (sJsonFilesGenerator == null) {
            sJsonFilesGenerator = new JsonFilesGenerator(context);
        }
        return sJsonFilesGenerator;
    }

    private JsonFilesGenerator(Context context) {
        mContext = context;
    }

    public String generateSongsFiles() {
        List<EuroSong> songs = new ArrayList<>();

        Realm realm = Realm.getDefaultInstance();

        RealmResults<RoEuroSong> roEuroSongs = realm.where(RoEuroSong.class)
                .findAll();

        String out = "";
        for (RoEuroSong roEuroSong : roEuroSongs) {
            out = out + roEuroSong.getString(mContext);
        }

        return out;
    }

    public String generateSongsFilesWithFix() {
        List<EuroSong> songs = new ArrayList<>();

        Realm realm = Realm.getDefaultInstance();

        RealmResults<RoEuroSong> roEuroSongs = realm.where(RoEuroSong.class)
                .findAll();

        List<Fix> fixes = loadFixes();

        realm.beginTransaction();
        for (RoEuroSong roEuroSong : roEuroSongs) {
            for (Fix fix : fixes) {
                if (fix.getSongId().isEmpty() || fix.getVideoId().isEmpty()) {
                    continue;
                }
                if (roEuroSong.getSongCode().equals(fix.getSongId())) {
                    if ((roEuroSong.getVideoId() != null) && (roEuroSong.getSecondaryId() != null)) {
                        continue;
                    }
                    if (roEuroSong.getVideoId() != null) {
                        roEuroSong.setSecondaryId(roEuroSong.getVideoId());
                    }
                    roEuroSong.setVideoId(fix.getVideoId());
                }
            }
        }
        realm.commitTransaction();

        String out = "";
        for (RoEuroSong roEuroSong : roEuroSongs) {
            out = out + roEuroSong.getString(mContext);
        }

        return out;
    }

    private List<Fix> loadFixes() {
        List<Fix> fixes = new ArrayList<>();

        try {
            InputStream inputString = mContext.getResources().openRawResource(R.raw.fixes);

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputString, "UTF-8"));

            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    Fix fix = processFixLine(line);
                    fixes.add(fix);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    inputString.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) { }

        return fixes;
    }

    private Fix processFixLine(String line) {
        StringTokenizer st = new StringTokenizer(line, "\t");
        int i = 0;
        String songId = "";
        String videoId = "";

        while (st.hasMoreTokens()) {
            i ++;
            String field = st.nextToken();
            switch (i) {
                case 1:
                    songId = field;
                    break;
                case 2:
                    videoId = field;
                    break;
            }
        }

        return new Fix(songId, videoId);
    }

    private class Fix {
        String mSongId;
        String mVideoId;

        public Fix(String songId, String videoId) {
            mSongId = songId;
            mVideoId = videoId;
        }

        public String getSongId() {
            return mSongId;
        }

        public String getVideoId() {
            return mVideoId;
        }
    }
}
