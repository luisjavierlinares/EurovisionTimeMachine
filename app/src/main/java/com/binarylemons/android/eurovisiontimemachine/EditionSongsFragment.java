package com.binarylemons.android.eurovisiontimemachine;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.binarylemons.android.eurovisiontimemachine.controller.EuroController;
import com.binarylemons.android.eurovisiontimemachine.model.EuroEdition;
import com.binarylemons.android.eurovisiontimemachine.model.EuroEntry;
import com.binarylemons.android.eurovisiontimemachine.model.EuroRound;
import com.binarylemons.android.eurovisiontimemachine.model.EuroSong;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Luis on 16/11/2017.
 */

public class EditionSongsFragment extends Fragment {

    private static final String ARG_EDITION_YEAR = "com.binarylemons.android.eurovisiontimemachine.EditionSongsFragment.arg_edition_year";
    private static final String ARG_EDITION_ROUND = "com.binarylemons.android.eurovisiontimemachine.EditionSongsFragment.arg_edition_round";

    private EuroController mController;
    private EuroEdition mEdition;
    private List<EuroSong> mSongs;
    private EuroRound mRound;

    @BindView(R.id.editions_songs_recycler_view) RecyclerView mRecyclerView;
    private EditionSongAdapter mEditionSongAdapter;

    private SongCallbacks mSongCallbacks;

    public static EditionSongsFragment newInstance(String year, EuroRound round) {
        Bundle arguments = new Bundle();
        arguments.putString(ARG_EDITION_YEAR, year);
        arguments.putString(ARG_EDITION_ROUND, round.toString());

        EditionSongsFragment fragment = new EditionSongsFragment();
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

        String year = getArguments().getString(ARG_EDITION_YEAR);
        EuroRound round = EuroRound.valueOf(getArguments().getString(ARG_EDITION_ROUND));

        mController = EuroController.get(getActivity());
        mEdition = mController.getEdition(year);
        mRound = round;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_edition_songs, container, false);
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

    private void updateUi() {
        mSongs = mController.getSongs(mEdition, mRound);

        mEditionSongAdapter = new EditionSongAdapter(mSongs);
        mRecyclerView.setAdapter(mEditionSongAdapter);
    }

    public class EditionSongHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private EuroSong mSong;

        @BindView(R.id.song_position) TextView mPosition;
        @BindView(R.id.song_country_flag) ImageView mCountryFlag;
        @BindView(R.id.song_country_name) TextView mCountryName;
        @BindView(R.id.song_title) TextView mTitle;
        @BindView(R.id.song_artist) TextView mArtist;
        @BindView(R.id.song_points) TextView mPoints;

        public EditionSongHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        public void bindSong(EuroSong song) {
            mSong = song;
            EuroEntry entry = mSong.getEntry(mRound);
            mPosition.setText(entry.getPositionString());
            mCountryFlag.setImageDrawable(mSong.getCountry().getPlainFlag(getActivity()));
            mCountryName.setText(mSong.getCountry().getName(getActivity()));
            mTitle.setText(mSong.getTitle());
            mArtist.setText(mSong.getArtist().getName());
            mPoints.setText(entry.getPointsString());
            mPosition.setTextColor(EurovisionUiUtils.getRoundPositionColor(getActivity(), mSong, mRound));
        }

        @Override
        public void onClick(View v) {
            mSongCallbacks.onSongSelected(mSong.getSongCode());
        }
    }

    public class EditionSongAdapter extends RecyclerView.Adapter<EditionSongHolder> {

        private List<EuroSong> mSongs;

        public EditionSongAdapter(List<EuroSong> songs) {
            mSongs = songs;
        }

        @Override
        public EditionSongHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.item_edition_song, parent, false);

            return new EditionSongHolder(view);
        }

        @Override
        public void onBindViewHolder(EditionSongHolder holder, int position) {
            EuroSong song = mSongs.get(position);
            holder.bindSong(song);
        }

        @Override
        public int getItemCount() {
            return mSongs.size();
        }
    }
}
