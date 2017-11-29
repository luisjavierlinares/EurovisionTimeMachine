package com.binarylemons.android.eurovisiontimemachine;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.binarylemons.android.eurovisiontimemachine.controller.EuroController;
import com.binarylemons.android.eurovisiontimemachine.model.EuroCountry;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Luis on 29/11/2017.
 */

public class SearchSelectCountriesFragment extends DialogFragment {

    private static final String KEY_SELECTED_COUNTRIES = "com.binarylemons.android.eurovisiontimemachine.SearchSelectCountriesFragment.KEY_SELECTED_COUNTRIES";
    private static final String EXTRA_SELECTED_COUNTRIES = "com.binarylemons.android.eurovisiontimemachine.SearchSelectCountriesFragment.EXTRA_SELECTED_COUNTRIES";
    private static final String ARGS_SELECTED_COUNTRIES = "com.binarylemons.android.eurovisiontimemachine.SearchSelectCountriesFragment.ARGS_SELECTED_COUNTRIES";

    private EuroController mController;
    private List<EuroCountry> mCountries;
    private List<EuroCountry> mSelectedCountries;

    @BindView(R.id.search_select_countries_recycler_view) RecyclerView mRecyclerView;
    private SearchCountryAdapter mAdapter;

    public static SearchSelectCountriesFragment newInstance(List<EuroCountry> countries) {
        Bundle args = new Bundle();

        ArrayList<String> selectedCountries = countriesToCodes(countries);
        args.putStringArrayList(ARGS_SELECTED_COUNTRIES, selectedCountries);

        SearchSelectCountriesFragment fragment = new SearchSelectCountriesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mController = EuroController.get(getActivity());
        mCountries = mController.getAllCountries();
        mSelectedCountries = new ArrayList<>();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        ArrayList<String> selectedCountries = countriesToCodes(mSelectedCountries);
        outState.putStringArrayList(KEY_SELECTED_COUNTRIES, selectedCountries);
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_search_select_countries, null);
        ButterKnife.bind(this, view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (savedInstanceState != null) {
            restoreInstanceState(savedInstanceState);
        } else if (!getArguments().isEmpty()) {
            restoreArguments(getArguments());
        }

        updateUi();

        AlertDialog dialog = createDialog(view);

        return dialog;
    }

    private void restoreInstanceState(Bundle savedInstanceState) {
        ArrayList<String> selectedCountries = savedInstanceState.getStringArrayList(KEY_SELECTED_COUNTRIES);
        mSelectedCountries = codesToCountry(selectedCountries);
    }

    private void restoreArguments(Bundle args) {
        ArrayList<String> selectedCountries = args.getStringArrayList(ARGS_SELECTED_COUNTRIES);
        mSelectedCountries = codesToCountry(selectedCountries);
    }

    private void updateUi() {
        mAdapter = new SearchCountryAdapter(mCountries);
        mRecyclerView.setAdapter(mAdapter);
    }

    private AlertDialog createDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        builder.setPositiveButton(R.string.ok, null);
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog dialog = builder.create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(final DialogInterface dialog) {

                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        sendCountries();
                        dialog.dismiss();
                    }
                });
            }
        });

        return dialog;
    }

    @OnClick(R.id.search_select_countries_select_all)
    public void onSelectAllClicked() {
        mSelectedCountries = new ArrayList<>();
        for (EuroCountry country : mCountries) {
            mSelectedCountries.add(country);
        }
        mAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.search_select_countries_select_none)
    public void onSelectNoneClicked() {
        mSelectedCountries = new ArrayList<>();
        mAdapter.notifyDataSetChanged();
    }

    private void sendCountries() {
        if (getTargetFragment() == null) return;

        Intent intent = new Intent();

        ArrayList<String> selectedCountries = countriesToCodes(mSelectedCountries);

        intent.putExtra(EXTRA_SELECTED_COUNTRIES, selectedCountries);
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
    }

    public static List<EuroCountry> getSelectedCountries(Intent intent) {
        List<String> countryCodes = intent.getStringArrayListExtra(EXTRA_SELECTED_COUNTRIES);
        List<EuroCountry> countries = codesToCountry(countryCodes);

        return countries;
    }

    private static ArrayList<EuroCountry> codesToCountry(List<String> countryCodes) {
        ArrayList<EuroCountry> countries = new ArrayList<>();

        for (String countryCode : countryCodes) {
            EuroCountry country = new EuroCountry(EuroCountry.Code.valueOf(countryCode));
            countries.add(country);
        }

        return countries;
    }

    private static ArrayList<String> countriesToCodes(List<EuroCountry> countries) {
        ArrayList<String> countryCodes = new ArrayList<>();

        for (EuroCountry country : countries) {
            countryCodes.add(country.getCountryCode().toString());
        }

        return countryCodes;
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
