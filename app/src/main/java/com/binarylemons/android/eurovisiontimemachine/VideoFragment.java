package com.binarylemons.android.eurovisiontimemachine;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

/**
 * Created by Luis on 24/11/2017.
 */

public class VideoFragment extends YouTubePlayerSupportFragment implements YouTubePlayer.OnInitializedListener {

    private static final String ARG_URL = "com.binarylemons.android.eurovisiontimemachine.VideoFragment.arg_url";
    private static final String ARG_TIME = "com.binarylemons.android.eurovisiontimemachine.VideoFragment.arg_time";

    private YouTubePlayer mPlayer;
    private String mUrl;
    private int mTime;

    public static VideoFragment newInstance(String url) {
        Bundle arguments = new Bundle();
        arguments.putString(ARG_URL, url);
        arguments.putInt(ARG_TIME, 0);

        VideoFragment fragment = new VideoFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    public static VideoFragment newInstance(String url, int timeInMillis) {
        Bundle arguments = new Bundle();
        arguments.putString(ARG_URL, url);
        arguments.putInt(ARG_TIME, timeInMillis);

        VideoFragment fragment = new VideoFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        mUrl = getArguments().getString(ARG_URL);
        mTime = getArguments().getInt(ARG_TIME);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        mPlayer = youTubePlayer;
        mPlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);

        int orientation = Configuration.ORIENTATION_PORTRAIT;

        Activity activity = getActivity();
        if (activity != null) {
            orientation = getResources().getConfiguration().orientation;
        }

        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mPlayer.setFullscreen(true);
            mPlayer.setShowFullscreenButton(false);
        }

        mPlayer.cueVideo(mUrl, mTime);

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }

    public YouTubePlayer getPlayer() {
        return mPlayer;
    }

    public int getCurrentTime() {
        return mPlayer.getCurrentTimeMillis();
    }
}
