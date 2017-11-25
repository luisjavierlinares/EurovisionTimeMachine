package com.binarylemons.android.eurovisiontimemachine;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.binarylemons.android.eurovisiontimemachine.controller.EuroController;
import com.binarylemons.android.eurovisiontimemachine.model.EuroCountry;
import com.binarylemons.android.eurovisiontimemachine.model.EuroEdition;
import com.binarylemons.android.eurovisiontimemachine.model.EuroSong;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Luis on 17/11/2017.
 */

public class CountrySongsFragment extends Fragment {

    private static final String ARG_COUNTRY_CODE = "com.binarylemons.android.eurovisiontimemachine.CountrySongsFragment.arg_country_code";

    private EuroController mController;
    private EuroCountry mCountry;
    private List<EuroSong> mSongs;

    @BindView(R.id.country_songs_recycler_view) RecyclerView mRecyclerView;
    private CountrySongAdapter mCountrySongAdapter;

    private SongCallbacks mSongCallbacks;

    public static CountrySongsFragment newInstance(String countryCode) {
        Bundle arguments = new Bundle();
        arguments.putString(ARG_COUNTRY_CODE, countryCode);

        CountrySongsFragment fragment = new CountrySongsFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof SongCallbacks) {
            mSongCallbacks = (SongCallbacks) context;
        } else {
            throw new RuntimeException(context.toString() + "must implement SongCallbacks)");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mSongCallbacks = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String countryCode = getArguments().getString(ARG_COUNTRY_CODE);

        mController = EuroController.get(getActivity());
        mCountry = mController.getCountry(countryCode);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_country_songs, container, false);
        ButterKnife.bind(this, view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                mRecyclerView.getContext(),
                layoutManager.getOrientation()
        );
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        updateUi();

        return view;
    }

    public void updateUi() {
        mSongs = mController.getSongs(mCountry);

        mCountrySongAdapter = new CountrySongAdapter(mSongs);
        mRecyclerView.setAdapter(mCountrySongAdapter);
    }

    public class CountrySongHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private EuroSong mSong;

        @BindView(R.id.country_song_edition_image) ImageView mEditionImage;
        @BindView(R.id.country_song_edition_year)TextView mEditionYear;
        @BindView(R.id.country_song_title) TextView mTitle;
        @BindView(R.id.country_song_artist) TextView mArtist;
        @BindView(R.id.country_song_position) TextView mPosition;
        @BindView(R.id.country_song_points) TextView mPoints;
        @BindView(R.id.country_song_not_qualified) TextView mNotQualified;

        public CountrySongHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        public void bindCountrySong(EuroSong song) {
            mSong = song;
            EuroEdition edition = mController.getEdition(mSong.getYear());
            mEditionImage.setImageDrawable(edition.getEuroFlag(getActivity()));
            mEditionYear.setText(edition.getYear());
            mTitle.setText(mSong.getTitle());
            mArtist.setText(mSong.getArtist().getName());
            if (mSong.isClassifiedToFinal()) {
                mPosition.setText(String.valueOf(mSong.getFinalEntry().getPosition()));
                mPosition.setTextColor(EurovisionUiUtils.getPositionColor(getActivity(), mSong));
                mPosition.setVisibility(View.VISIBLE);
                mPoints.setText("(" + mSong.getFinalEntry().getPoints() + " " + getString(R.string.points) + ")");
                mPoints.setVisibility(View.VISIBLE);
                mNotQualified.setVisibility(View.GONE);
            } else {
                mPosition.setVisibility(View.GONE);
                mPoints.setVisibility(View.GONE);
                mNotQualified.setText(getString(R.string.did_not_qualify));
                mNotQualified.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onClick(View v) {
            String songCode = mSong.getYear() + mSong.getCountry().getCountryCode();
            mSongCallbacks.onSongSelected(songCode);
        }
    }

    public class CountrySongAdapter extends RecyclerView.Adapter<CountrySongHolder> {

        private List<EuroSong> mSongs;

        public CountrySongAdapter(List<EuroSong> songs) {
            mSongs = songs;
        }

        @Override
        public CountrySongHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.item_country_song, parent, false);

            return new CountrySongHolder(view);
        }

        @Override
        public void onBindViewHolder(CountrySongHolder holder, int position) {
            EuroSong song = mSongs.get(position);
            holder.bindCountrySong(song);
        }

        @Override
        public int getItemCount() {
            return mSongs.size();
        }
    }

}
