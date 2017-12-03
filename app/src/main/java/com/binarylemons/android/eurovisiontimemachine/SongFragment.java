package com.binarylemons.android.eurovisiontimemachine;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.binarylemons.android.eurovisiontimemachine.controller.EuroController;
import com.binarylemons.android.eurovisiontimemachine.model.EuroCountry;
import com.binarylemons.android.eurovisiontimemachine.model.EuroEdition;
import com.binarylemons.android.eurovisiontimemachine.model.EuroSong;
import com.binarylemons.android.eurovisiontimemachine.video.YoutubeManager;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Luis on 22/11/2017.
 */

public class SongFragment extends Fragment {

    private static final String ARG_SONG_CODE = "com.binarylemons.android.eurovisiontimemachine.SongFragment.arg_song_code";
    private static final String STATE_VIDEO_CURRENT_TIME = "videoCurrentTime";

    private EuroController mController;
    private EuroSong mSong;

    private int mCurrentTime;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.video_player_frame)
    FrameLayout mPlayerFrame;
    @BindView(R.id.song_data)
    View mSongView;
    @BindView(R.id.song_edition_image)
    ImageView mEditionFlag;
    @BindView(R.id.song_edition_year)
    TextView mEditionYear;
    @BindView(R.id.song_title)
    TextView mTitle;
    @BindView(R.id.song_artist)
    TextView mArtist;
    @BindView(R.id.song_country_flag)
    ImageView mCountryFlag;
    @BindView(R.id.song_country_name)
    TextView mCountryName;
    @BindView(R.id.song_lyrics)
    TextView mLyrics;

    private VideoFragment mVideoFragment;

    public static SongFragment newInstance(String songCode) {
        Bundle arguments = new Bundle();
        arguments.putString(ARG_SONG_CODE, songCode);

        SongFragment fragment = new SongFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mVideoFragment != null) {
            outState.putInt(STATE_VIDEO_CURRENT_TIME, mVideoFragment.getCurrentTime());
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String songCode = getArguments().getString(ARG_SONG_CODE);

        mController = EuroController.get(getActivity());
        mSong = mController.getSong(songCode);
        mVideoFragment = VideoFragment.newInstance();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_song, container, false);
        ButterKnife.bind(this, view);

        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        setHasOptionsMenu(true);

        mToolbar.setTitleTextColor(getResources().getColor(R.color.toolbarTitle));
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        new SearchAndLoadTask().execute();
        if (savedInstanceState != null) {
            mCurrentTime = savedInstanceState.getInt(STATE_VIDEO_CURRENT_TIME);
        } else {
            mCurrentTime = 0;
        }

        updateUi();

        return view;
    }

    private void updateUi() {
        EuroEdition edition = mController.getEdition(mSong.getYear());
        mEditionFlag.setImageDrawable(edition.getEuroFlag(getActivity()));
        mEditionYear.setText(edition.getYear().toString());

        mTitle.setText(mSong.getTitle());
        mArtist.setText(mSong.getArtist().getName());

        EuroCountry country = mSong.getCountry();
        mCountryFlag.setImageDrawable(country.getPlainFlag(getActivity()));
        mCountryName.setText(country.getName(getActivity()));

        mLyrics.setText(mSong.getLyrics(getActivity()));
    }

    private void showSongData() {
        mSongView.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class SearchAndLoadTask extends AsyncTask<Void, Void, Void> {

        String mQuery;
        String mVideoId;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mQuery = "Eurovision Song Contest" + " " + mSong.getYear() + " " + mSong.getTitle() + " " + mSong.getArtist().getName() + " LIVE";
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Void doInBackground(Void... params) {
            if (mSong.getVideoId() != null) {
                mVideoId = mSong.getVideoId();
            } else {
                YoutubeManager youtubeManager = new YoutubeManager(getActivity());
                mVideoId = youtubeManager.searchFirst(mQuery);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mVideoFragment = VideoFragment.newInstance(mVideoId, mCurrentTime);
            mVideoFragment.initialize(YoutubeManager.YOUTUBE_API_KEY, mVideoFragment);

            Activity activity = getActivity();
            if (activity != null) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().add(R.id.video_player_frame, mVideoFragment).commit();
            }

            showSongData();
        }

    }

}
