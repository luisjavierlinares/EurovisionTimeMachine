package com.binarylemons.android.eurovisiontimemachine.video;

import android.content.Context;
import android.content.pm.PackageManager;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.common.io.BaseEncoding;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import android.content.pm.Signature;
import java.util.List;

/**
 * Created by Luis on 24/11/2017.
 */

public class YoutubeManager {

    public static final String YOUTUBE_API_KEY = "AIzaSyD0vrK6TIroRdwio0nV9E61BD4b8fl36iE";
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();

    private Context mContext;
    private static YouTube youtube;

    public YoutubeManager(Context context) {
        mContext = context;
    }

    public String searchFirst(String query) {
        try {
            youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {
                public void initialize(HttpRequest request) throws IOException {
                    String packageName = mContext.getPackageName();
                    String SHA1 = getSHA1(packageName);

                    request.getHeaders().set("X-Android-Package", packageName);
                    request.getHeaders().set("X-Android-Cert",SHA1);
                }
            }).setApplicationName(mContext.getPackageName()).build();

            YouTube.Search.List search = youtube.search().list("id,snippet");
            search.setKey(YOUTUBE_API_KEY)
                    .setQ(query)
                    .setType("video")
//                    .setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)")
                    .setFields("items(id/videoId,snippet/title)")
                    .setMaxResults((long) 1);

            SearchListResponse searchResponse = search.execute();

            SearchResult searchResult = searchResponse.getItems().get(0);

            String videoId = searchResult.getId().getVideoId();
            if (videoId != null) {
                return videoId;
            } else {
                return "";
            }

        } catch (GoogleJsonResponseException e) {
            System.err.println("There was a service error: " + e.getDetails().getCode() + " : "
                    + e.getDetails().getMessage());
        } catch (IOException e) {
            System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
        }

        return "";
    }

    private String getSHA1(String packageName){
        try {
            android.content.pm.Signature[] signatures = mContext
                    .getPackageManager()
                    .getPackageInfo(packageName, PackageManager.GET_SIGNATURES).signatures;

            for (Signature signature: signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA-1");
                md.update(signature.toByteArray());
                return BaseEncoding.base16().encode(md.digest());
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
