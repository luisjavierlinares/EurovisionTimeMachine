package com.binarylemons.android.eurovisiontimemachine;

import android.app.Application;

import com.binarylemons.android.eurovisiontimemachine.controller.EuroController;
import com.binarylemons.android.eurovisiontimemachine.database.JsonToRealm;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Luis on 13/11/2017.
 */

public class EurovisionApp extends Application {

    private enum AppType {LOAD, NORMAL};

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name("eurovisiondb.realm")
                .build();

        Realm.setDefaultConfiguration(realmConfiguration);

        AppType appType = AppType.LOAD;

        if (appType.equals(AppType.LOAD)) {
            JsonToRealm jsonToRealm = new JsonToRealm(this);
            jsonToRealm.importFromJson();
        }
    }
}
