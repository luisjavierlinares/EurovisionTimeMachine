package com.binarylemons.android.eurovisiontimemachine.database;

import android.content.Context;

import com.binarylemons.android.eurovisiontimemachine.R;

import java.io.IOException;
import java.io.InputStream;

import io.realm.Realm;

/**
 * Created by Luis on 13/11/2017.
 */

public class JsonToRealm {

    Context mContext;

    public JsonToRealm(Context context) {
        mContext = context;
    }

    public void importFromJson() {
        importEditionsFromJson();
        importCountriesFromJson();
        importSongsFromJson();
    }

    private void importEditionsFromJson() {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    InputStream inputStream = mContext.getResources().openRawResource(R.raw.editions);
                    try {
                        realm.createAllFromJson(RoEuroEdition.class, inputStream);
                    } catch (IOException e) {
                        // do nothing
                    } catch (io.realm.exceptions.RealmPrimaryKeyConstraintException e) {
                        // do nothing
                    }
                }
            });
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

    private void importCountriesFromJson() {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    InputStream inputStream = mContext.getResources().openRawResource(R.raw.countries);
                    try {
                        realm.createAllFromJson(RoEuroCountry.class, inputStream);
                    } catch (IOException e) {
                        // do nothing
                    } catch (io.realm.exceptions.RealmPrimaryKeyConstraintException e) {
                        // do nothing
                    }
                }
            });
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

    private void importSongsFromJson() {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    InputStream inputStream = mContext.getResources().openRawResource(R.raw.songs);
                    try {
                        realm.createAllFromJson(RoEuroSong.class, inputStream);
                    } catch (IOException e) {
                        // do nothing
                    } catch (io.realm.exceptions.RealmPrimaryKeyConstraintException e) {
                        // do nothing
                    }
                }
            });
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }
}
