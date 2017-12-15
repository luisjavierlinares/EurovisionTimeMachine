package com.binarylemons.android.eurovisiontimemachine;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.multidex.MultiDexApplication;

import com.binarylemons.android.eurovisiontimemachine.controller.EuroController;
import com.binarylemons.android.eurovisiontimemachine.database.JsonToRealm;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Luis on 13/11/2017.
 */

public class EurovisionApp extends MultiDexApplication {

    private enum AppType {INITIAL_LOAD, LOAD_FROM_JSON, NORMAL};

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name("eurovisiondb.realm")
                .build();

        Realm.setDefaultConfiguration(realmConfiguration);

        if (isNewVersion()) {
            InputStream initialEurovisionDb = this.getResources().openRawResource(R.raw.eurovisiondb);
            copyBundledRealmFile(initialEurovisionDb, "eurovisiondb.realm");
            updateAppVersion();
        }

        AppType appType = AppType.NORMAL;

        if (appType.equals(AppType.LOAD_FROM_JSON)) {
            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            realm.deleteAll();
            realm.commitTransaction();

            JsonToRealm jsonToRealm = new JsonToRealm(this);
            jsonToRealm.importFromJson();
        }
    }

    private String copyBundledRealmFile(InputStream inputStream, String outFileName) {
        try {
            File file = new File(this.getFilesDir(), outFileName);
            FileOutputStream outputStream = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, bytesRead);
            }
            outputStream.close();
            return file.getAbsolutePath();
        } catch (IOException e) { }
        return null;
    }

    private boolean isNewVersion() {
        int oldVersion = EuroSettings.getPackageVersion(this);

        if (oldVersion == -1) {
            return true;
        }

        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            return true;
        }
        int thisVersion = packageInfo.versionCode;

        if (oldVersion == thisVersion) {
            return false;
        } else {
            return true;
        }
    }

    private void updateAppVersion() {
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {}
        int thisVersion = packageInfo.versionCode;

        EuroSettings.setPackageVersion(this, thisVersion);
    }
}
