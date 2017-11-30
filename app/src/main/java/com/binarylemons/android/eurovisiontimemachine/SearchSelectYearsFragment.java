package com.binarylemons.android.eurovisiontimemachine;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
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
import com.binarylemons.android.eurovisiontimemachine.model.EuroEdition;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Luis on 29/11/2017.
 */

public class SearchSelectYearsFragment extends DialogFragment {

    private static final String KEY_SELECTED_YEARS = "com.binarylemons.android.eurovisiontimemachine.SearchSelectYearsFragment.KEY_SELECTED_YEARS";
    private static final String EXTRA_SELECTED_YEARS = "com.binarylemons.android.eurovisiontimemachine.SearchSelectYearsFragment.EXTRA_SELECTED_YEARS";
    private static final String ARGS_SELECTED_YEARS = "com.binarylemons.android.eurovisiontimemachine.SearchSelectYearsFragment.ARGS_SELECTED_YEARS";

    private EuroController mController;
    private List<EuroEdition> mEditions;
    private List<EuroEdition> mSelectedEditions;

    @BindView(R.id.search_select_years_recycler_view) RecyclerView mRecyclerView;
    private SearchEditionAdapter mAdapter;

    public static SearchSelectYearsFragment newInstance(List<EuroEdition> editions) {
        Bundle args = new Bundle();

        ArrayList<String> selectedYears = editionsToYears(editions);
        args.putStringArrayList(ARGS_SELECTED_YEARS, selectedYears);

        SearchSelectYearsFragment fragment = new SearchSelectYearsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mController = EuroController.get(getActivity());
        mEditions = mController.getAllEditions();
        mSelectedEditions = new ArrayList<>();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        ArrayList<String> years = editionsToYears(mSelectedEditions);
        outState.putStringArrayList(KEY_SELECTED_YEARS, years);
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_search_select_years, null);
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
        ArrayList<String> years = savedInstanceState.getStringArrayList(KEY_SELECTED_YEARS);
        mSelectedEditions = yearsToEditions(years);
    }

    private void restoreArguments(Bundle args) {
        ArrayList<String> years = args.getStringArrayList(ARGS_SELECTED_YEARS);
        mSelectedEditions = yearsToEditions(years);
    }

    private void updateUi() {
        mAdapter = new SearchEditionAdapter(mEditions);
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
                        sendEditions();
                        dialog.dismiss();
                    }
                });
            }
        });

        return dialog;
    }

    @OnClick(R.id.search_select_years_select_all)
    public void onSelectAllClicked() {
        mSelectedEditions = new ArrayList<>();
        for (EuroEdition edition : mEditions) {
            mSelectedEditions.add(edition);
        }
        mAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.search_select_years_select_none)
    public void onSelectNoneClicked() {
        mSelectedEditions = new ArrayList<>();
        mAdapter.notifyDataSetChanged();
    }

    private void sendEditions() {
        if (getTargetFragment() == null) return;

        Intent intent = new Intent();

        ArrayList<String> selectedYears = editionsToYears(mSelectedEditions);

        intent.putExtra(EXTRA_SELECTED_YEARS, selectedYears);
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
    }

    public static List<EuroEdition> getSelectedEditions(Intent intent, Context context) {
        List<String> years = intent.getStringArrayListExtra(EXTRA_SELECTED_YEARS);
        List<EuroEdition> editions = yearsToEditions(years, context);

        return editions;
    }

    private ArrayList<EuroEdition> yearsToEditions(List<String> years) {
        return yearsToEditions(years, getActivity());
    }

    private static ArrayList<EuroEdition> yearsToEditions(List<String> years, Context context) {
        ArrayList<EuroEdition> editions = new ArrayList<>();

        for (String year : years) {
            EuroController controller = EuroController.get(context);
            EuroEdition edition = controller.getEdition(year);
            editions.add(edition);
        }

        return editions;
    }

    private static ArrayList<String> editionsToYears(List<EuroEdition> editions) {
        ArrayList<String> years = new ArrayList<>();

        for (EuroEdition edition : editions) {
            years.add(edition.getYear());
        }

        return years;
    }

    private Boolean isSelected(EuroEdition edition) {
        for (EuroEdition selectedEdition : mSelectedEditions) {
            if (selectedEdition.getYear().equals(edition.getYear())) {
                return true;
            }
        }
        return false;
    }

    private void selectEdition(EuroEdition edition) {
        mSelectedEditions.add(edition);
    }

    private void unSelectEdition(EuroEdition edition) {
        for (EuroEdition selectedEdition : mSelectedEditions) {
            if (selectedEdition.getYear().equals(edition.getYear())) {
                mSelectedEditions.remove(selectedEdition);
                return;
            }
        }
    }

    private void toogleSelected(EuroEdition edition) {
        if (isSelected(edition)) {
            unSelectEdition(edition);
        } else {
            selectEdition(edition);
        }
    }

    public class SearchEditionHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private EuroEdition mEdition;

        @BindView(R.id.search_year_flag) ImageView mEditionFlag;
        @BindView(R.id.search_year) TextView mEditionYear;
        @BindView(R.id.search_year_check) ImageView mCheck;

        public SearchEditionHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        public void bindEdition(EuroEdition edition) {
            mEdition = edition;
            mEditionFlag.setImageDrawable(mEdition.getEuroFlag(getActivity()));
            mEditionYear.setText(mEdition.getYear());
            updateCheckMark();
        }

        @Override
        public void onClick(View v) {
            toogleSelected(mEdition);
            updateCheckMark();
        }

        private void updateCheckMark() {
            if (isSelected(mEdition)) {
                mCheck.setVisibility(View.VISIBLE);
            } else {
                mCheck.setVisibility(View.INVISIBLE);
            }
        }
    }

    public class SearchEditionAdapter extends RecyclerView.Adapter<SearchEditionHolder> {

        private List<EuroEdition> mEditions;

        public SearchEditionAdapter(List<EuroEdition> editions) {
            mEditions = editions;
        }

        @Override
        public SearchEditionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.item_search_year, parent, false);

            return new SearchEditionHolder(view);
        }

        @Override
        public void onBindViewHolder(SearchEditionHolder holder, int position) {
            EuroEdition edition = mEditions.get(position);
            holder.bindEdition(edition);
        }

        @Override
        public int getItemCount() {
            return mEditions.size();
        }
    }
}
