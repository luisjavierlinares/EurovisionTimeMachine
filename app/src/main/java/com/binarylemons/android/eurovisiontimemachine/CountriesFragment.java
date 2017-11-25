package com.binarylemons.android.eurovisiontimemachine;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.binarylemons.android.eurovisiontimemachine.controller.EuroController;
import com.binarylemons.android.eurovisiontimemachine.model.EuroCountry;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Luis on 25/10/2017.
 */

public class CountriesFragment extends Fragment {

    private EuroController mController;
    private List<EuroCountry> mCountries;

    @BindView(R.id.countries_recycler_view) RecyclerView mRecyclerView;
    private CountryAdapter mCountryAdapter;

    private CountryCallbacks mCountryCallbacks;

    public static CountriesFragment newInstance() {
        Bundle args = new Bundle();

        CountriesFragment fragment = new CountriesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof CountryCallbacks) {
            mCountryCallbacks = (CountryCallbacks) context;
        } else {
            throw new RuntimeException(context.toString() + "must implement CountryCallbacks)");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCountryCallbacks = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mController = EuroController.get(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_countries, container, false);
        ButterKnife.bind(this, view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUi();

        return view;
    }

    public void updateUi() {
        mCountries = mController.getAllCountries();

        mCountryAdapter = new CountryAdapter(mCountries);
        mRecyclerView.setAdapter(mCountryAdapter);
    }

    public class CountryHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private EuroCountry mCountry;

        @BindView(R.id.country_flag) ImageView mCountryFlag;
        @BindView(R.id.country_name) TextView mCountryName;
        @BindView(R.id.country_appearances_count) TextView mAppearances;
        @BindView(R.id.country_wins_count) TextView mWinsCount;
        @BindView(R.id.country_seconds_count) TextView mSecondPlacesCount;
        @BindView(R.id.country_thirds_count) TextView mThirdPlacesCount;

        public CountryHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        public void bindCountry(EuroCountry country) {
            mCountry = country;
            mCountryFlag.setImageDrawable(mCountry.getPlainFlag(getActivity()));
            mCountryName.setText(mCountry.getName(getActivity()));
            mAppearances.setText(String.valueOf(mCountry.getAppearances()));
            mWinsCount.setText(String.valueOf(mCountry.getWinsCount()));
            mSecondPlacesCount.setText(String.valueOf(mCountry.getSecondPlacesCount()));
            mThirdPlacesCount.setText(String.valueOf(mCountry.getThirdPlacesCount()));
        }

        @Override
        public void onClick(View v) {
            mCountryCallbacks.onCountrySelected(mCountry.getCountryCode().toString());
        }
    }

    public class CountryAdapter extends RecyclerView.Adapter<CountryHolder> {

        private List<EuroCountry> mCountries;

        public CountryAdapter(List<EuroCountry> countries) {
            mCountries = countries;
        }

        @Override
        public CountryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.item_country, parent, false);

            return new CountryHolder(view);
        }

        @Override
        public void onBindViewHolder(CountryHolder holder, int position) {
            EuroCountry country = mCountries.get(position);
            holder.bindCountry(country);
        }

        @Override
        public int getItemCount() {
            return mCountries.size();
        }
    }
}
