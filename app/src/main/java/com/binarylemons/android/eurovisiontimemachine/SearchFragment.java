package com.binarylemons.android.eurovisiontimemachine;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.binarylemons.android.eurovisiontimemachine.controller.EuroController;
import com.binarylemons.android.eurovisiontimemachine.model.EuroCountry;
import com.binarylemons.android.eurovisiontimemachine.model.EuroEdition;
import com.binarylemons.android.eurovisiontimemachine.model.EuroQuery;
import com.binarylemons.android.eurovisiontimemachine.model.EuroSong;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Luis on 25/10/2017.
 */

public class SearchFragment extends Fragment {

    private EuroController mController;
    private List<EuroSong> mSongs;

    @BindView(R.id.search_recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.search_progress_bar) ProgressBar mProgressBar;
    @BindView(R.id.search_song_no_song) View mNoSongsView;
    private SearchSongAdapter mSearchSongAdapter;

    private SongCallbacks mSongCallbacks;

    public static SearchFragment newInstance() {
        Bundle args = new Bundle();

        SearchFragment fragment = new SearchFragment();
        fragment.setArguments(args);
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
        setRetainInstance(true);
        mController = EuroController.get(getActivity());
        mSongs = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_search, container, false);
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
        mSearchSongAdapter = new SearchSongAdapter(mSongs);
        mRecyclerView.setAdapter(mSearchSongAdapter);

        if (mSongs.isEmpty()) {
            mRecyclerView.setVisibility(View.GONE);
            mNoSongsView.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            mNoSongsView.setVisibility(View.GONE);
        }
    }

    public class SearchSongHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private EuroSong mSong;

        @BindView(R.id.search_song_edition_image) ImageView mEditionImage;
        @BindView(R.id.search_song_edition_year) TextView mEditionYear;
        @BindView(R.id.search_song_title) TextView mTitle;
        @BindView(R.id.search_song_artist) TextView mArtist;
        @BindView(R.id.search_song_country_flag) ImageView mCountryFlag;
        @BindView(R.id.search_song_country_name) TextView mCountryName;
        @BindView(R.id.search_song_position) TextView mPosition;
        @BindView(R.id.search_song_points) TextView mPoints;
        @BindView(R.id.search_song_not_qualified) TextView mNotQualified;

        public SearchSongHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        public void bindSong(EuroSong song) {
            mSong = song;
            EuroEdition edition = mController.getEdition(mSong.getYear());
            mEditionImage.setImageDrawable(edition.getEuroFlag(getActivity()));
            mEditionYear.setText(edition.getYear());

            mTitle.setText(mSong.getTitle());
            mArtist.setText(mSong.getArtist().getName());

            EuroCountry country = song.getCountry();
            mCountryFlag.setImageDrawable(country.getPlainFlag(getActivity()));
            mCountryName.setText(country.getName(getActivity()));

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

    public class SearchSongAdapter extends RecyclerView.Adapter<SearchSongHolder> {

        private List<EuroSong> mSongs;

        public SearchSongAdapter(List<EuroSong> songs) {
            mSongs = songs;
        }

        @Override
        public SearchSongHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.item_search_song, parent, false);

            return new SearchSongHolder(view);
        }

        @Override
        public void onBindViewHolder(SearchSongHolder holder, int position) {
            EuroSong song = mSongs.get(position);
            holder.bindSong(song);
        }

        @Override
        public int getItemCount() {
            return mSongs.size();
        }
    }

    public void searchSongs(EuroQuery query) {
        new SearchTask().execute(query);
    }

    private class SearchTask extends AsyncTask<EuroQuery, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
            mController = EuroController.get(getActivity());
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Void doInBackground(EuroQuery... params) {
            EuroQuery query = params[0];
            mSongs = mController.search(query);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mProgressBar.setVisibility(View.GONE);
            updateUi();
        }

    }

}
