package com.binarylemons.android.eurovisiontimemachine;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.binarylemons.android.eurovisiontimemachine.controller.EuroController;
import com.binarylemons.android.eurovisiontimemachine.model.EuroCountry;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Luis on 29/11/2017.
 */

public class SearchSelectCountriesFragment extends DialogFragment {

    private EuroController mController;
    private List<EuroCountry> mCountries;
    private List<EuroCountry> mSelectedCountries;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mController = EuroController.get(getActivity());
        mCountries = mController.getAllCountries();
        mSelectedCountries = new ArrayList<>();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    private Boolean isSelected(EuroCountry country) {
        for(EuroCountry selectedCountry : mSelectedCountries) {
            if (selectedCountry.getCountryCode() == country.getCountryCode()) {
                return true;
            }
        }
        return false;
    }

    private void selectCountry(EuroCountry country) {
        mSelectedCountries.add(country);
    }

    private void unSelectCountry(EuroCountry country) {
        for(EuroCountry selectedCountry : mSelectedCountries) {
            if (selectedCountry.getCountryCode() == country.getCountryCode()) {
                mSelectedCountries.remove(selectedCountry);
                return;
            }
        }
    }

    private void toogleSelected(EuroCountry country) {
        if (isSelected(country)) {
            unSelectCountry(country);
        } else {
            selectCountry(country);
        }
    }

    public class SearchCountryHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private EuroCountry mCountry;

        @BindView(R.id.search_country_flag) ImageView mCountryFlag;
        @BindView(R.id.search_country_name) TextView mCountryName;
        @BindView(R.id.search_country_check) ImageView mCheck;

        public SearchCountryHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        public void bindCountry(EuroCountry country) {
            mCountry = country;
            mCountryFlag.setImageDrawable(mCountry.getPlainFlag(getActivity()));
            mCountryName.setText(mCountry.getName(getActivity()));
            updateCheckMark();
        }

        @Override
        public void onClick(View v) {
            toogleSelected(mCountry);
            updateCheckMark();
        }

        private void updateCheckMark() {
            if (isSelected(mCountry)) {
                mCheck.setVisibility(View.VISIBLE);
            } else {
                mCheck.setVisibility(View.INVISIBLE);
            }
        }
    }

    public class SearchCountryAdapter extends RecyclerView.Adapter<SearchCountryHolder> {

        private List<EuroCountry> mCountries;

        public SearchCountryAdapter(List<EuroCountry> countries) {
            mCountries = countries;
        }

        @Override
        public SearchCountryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.item_search_country, parent, false);

            return new SearchCountryHolder(view);
        }

        @Override
        public void onBindViewHolder(SearchCountryHolder holder, int position) {
            EuroCountry country = mCountries.get(position);
            holder.bindCountry(country);
        }

        @Override
        public int getItemCount() {
            return mCountries.size();
        }
    }

}
