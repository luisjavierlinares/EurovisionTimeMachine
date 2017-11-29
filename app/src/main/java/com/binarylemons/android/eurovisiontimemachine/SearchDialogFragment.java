package com.binarylemons.android.eurovisiontimemachine;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.binarylemons.android.eurovisiontimemachine.controller.EuroController;
import com.binarylemons.android.eurovisiontimemachine.model.EuroCountry;
import com.binarylemons.android.eurovisiontimemachine.model.EuroQuery;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Luis on 28/11/2017.
 */

public class SearchDialogFragment extends DialogFragment {

    private static final String EXTRA_QUERY = "com.binarylemons.android.eurovisiontimemachine.SearchDialogFragment.EXTRA_QUERY";

    EuroQuery mQuery;

    @BindView(R.id.search_artist) TextInputEditText mArtist;
    @BindView(R.id.search_song) TextInputEditText mSong;
    @BindView(R.id.search_order_by_selection) Spinner mOrderBy;

    public static SearchDialogFragment newInstance() {
        Bundle args = new Bundle();

        SearchDialogFragment fragment = new SearchDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mQuery = new EuroQuery();
        mQuery.setOrderBy(EuroQuery.OrderBy.YEAR);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_search_dialog, null);
        ButterKnife.bind(this, view);

        ArrayAdapter<CharSequence> searchByAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.search_order_by,
                        R.layout.item_search_order_by);
        searchByAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mOrderBy.setAdapter(searchByAdapter);
        mOrderBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                onOrderBySelected(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        builder.setPositiveButton(R.string.search, null);
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
                        onSearchButtonClicked();
                        dialog.dismiss();
                    }
                });
            }
        });

        return dialog;
    }

    private void onSearchButtonClicked() {

        String artist = mArtist.getText().toString();
        String title = mSong.getText().toString();

        EuroCountry countryEsp = new EuroCountry(EuroCountry.Code.ESP);
        EuroCountry countryPrt = new EuroCountry(EuroCountry.Code.PRT);
        EuroCountry countryUkr = new EuroCountry(EuroCountry.Code.UKR);
        EuroCountry countryAlb = new EuroCountry(EuroCountry.Code.ALB);

        List<EuroCountry> countries = new ArrayList<>();
        countries.add(countryEsp);
        countries.add(countryPrt);
        countries.add(countryUkr);
        countries.add(countryAlb);

        mQuery.setArtist(artist);
        mQuery.setTitle(title);
        mQuery.setCountries(countries);

        sendQuery(mQuery);
    }

    private void onOrderBySelected(int position) {
        switch (position) {
            case 0:
                mQuery.setOrderBy(EuroQuery.OrderBy.YEAR);
                break;
            case 1:
                mQuery.setOrderBy(EuroQuery.OrderBy.COUNTRY);
                break;
            case 2:
                mQuery.setOrderBy(EuroQuery.OrderBy.POSITION);
                break;
        }
    }

    private void sendQuery(EuroQuery query) {
        if (getTargetFragment() == null) return;

        Intent intent = new Intent();
        intent.putExtra(EXTRA_QUERY, query);
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
    }

    public static EuroQuery getQuery(Intent intent) {
        return (EuroQuery) intent.getSerializableExtra(EXTRA_QUERY);
    }
}
