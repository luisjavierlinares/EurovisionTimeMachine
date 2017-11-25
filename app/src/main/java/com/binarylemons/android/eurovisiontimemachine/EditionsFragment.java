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
import com.binarylemons.android.eurovisiontimemachine.model.EuroEdition;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Luis on 25/10/2017.
 */

public class EditionsFragment extends Fragment {

    private EuroController mController;
    private List<EuroEdition> mEditions;

    @BindView(R.id.editions_recycler_view) RecyclerView mRecyclerView;
    private EditionAdapter mEditionAdapter;

    private EditionCallbacks mEditionCallbacks;

    public static EditionsFragment newInstance() {
        Bundle args = new Bundle();

        EditionsFragment fragment = new EditionsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof EditionCallbacks) {
            mEditionCallbacks = (EditionCallbacks) context;
        } else {
            throw new RuntimeException(context.toString() + "must implement EditionCallbacks)");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mEditionCallbacks = null;
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
        View view = inflater.inflate(R.layout.fragment_editions, container, false);
        ButterKnife.bind(this, view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUi();

        return view;
    }

    private void updateUi() {
        mEditions = mController.getAllEditions();

        mEditionAdapter = new EditionAdapter(mEditions);
        mRecyclerView.setAdapter(mEditionAdapter);
    }

    public class EditionHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private EuroEdition mEdition;

        @BindView(R.id.edition_image) ImageView mImage;
        @BindView(R.id.edition_year) TextView mYear;
        @BindView(R.id.edition_city) TextView mHostCity;
        @BindView(R.id.edition_slogan) TextView mSlogan;

        public EditionHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        public void bindEdition(EuroEdition edition) {
            mEdition = edition;
            mImage.setImageDrawable(mEdition.getEuroFlag(getActivity()));
            mYear.setText(mEdition.getYear());
            mHostCity.setText(mEdition.getHostCity().getCityName(getActivity()));

            if (mEdition.getSlogan() != null) {
                mSlogan.setVisibility(View.VISIBLE);
                mSlogan.setText(mEdition.getSlogan());
            } else {
                mSlogan.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onClick(View v) {
            mEditionCallbacks.onEditionSelected(mEdition.getYear());
        }
    }

    public class EditionAdapter extends RecyclerView.Adapter<EditionHolder> {

        private List<EuroEdition> mEditions;

        public EditionAdapter(List<EuroEdition> editions) {
            mEditions = editions;
        }

        @Override
        public EditionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.item_edition, parent, false);

            return new EditionHolder(view);
        }

        @Override
        public void onBindViewHolder(EditionHolder holder, int position) {
            EuroEdition edition = mEditions.get(position);
            holder.bindEdition(edition);
        }

        @Override
        public int getItemCount() {
            return mEditions.size();
        }
    }

}
