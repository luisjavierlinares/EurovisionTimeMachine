package com.binarylemons.android.eurovisiontimemachine;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.binarylemons.android.eurovisiontimemachine.controller.EuroController;
import com.binarylemons.android.eurovisiontimemachine.model.EuroEdition;
import com.binarylemons.android.eurovisiontimemachine.model.EuroRound;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Luis on 16/11/2017.
 */

public class EditionFragment extends Fragment {

    private static final int TOTAL_TABS = 4;

    private static final String ARG_EDITION_YEAR = "com.binarylemons.android.eurovisiontimemachine.EditionFragment.arg_edition_year";

    private EuroController mController;
    private EuroEdition mEdition;

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.tabs) TabLayout mTabLayout;
    @BindView(R.id.progress_bar) ProgressBar mProgressBar;
    @BindView(R.id.view_pager) ViewPager mViewPager;
    private SectionsPageAdapter mAdapter;

    public static EditionFragment newInstance(String year) {
        Bundle arguments = new Bundle();
        arguments.putString(ARG_EDITION_YEAR, year);

        EditionFragment fragment = new EditionFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String year = getArguments().getString(ARG_EDITION_YEAR);

        mController = EuroController.get(getActivity());
        mEdition = mController.getEdition(year);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edition, container, false);
        ButterKnife.bind(this, view);

        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        setHasOptionsMenu(true);

        mToolbar.setSubtitle(getString(R.string.eurovision) + " " + mEdition.getYear());
        mToolbar.setTitleTextColor(getResources().getColor(R.color.toolbarTitle));
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mViewPager.setOffscreenPageLimit(TOTAL_TABS - 1);

        initializeUi();

        return view;
    }

    private void updateUi() {
        mViewPager.setAdapter(mAdapter);

        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void initializeUi() {
        new initializeUiTask().execute();
    }

    private void initializeUiContent(ViewPager viewPager) {
        mAdapter = new SectionsPageAdapter(getFragmentManager());
        int year = Integer.parseInt(mEdition.getYear());
        if (year > 2007) {
            mAdapter.addFragment(EditionSongsFragment.newInstance(mEdition.getYear(), EuroRound.FINAL), getString(R.string.final_tab_text));
            mAdapter.addFragment(EditionSongsFragment.newInstance(mEdition.getYear(), EuroRound.SEMIFINAL1), getString(R.string.semifinal1_tab_text));
            mAdapter.addFragment(EditionSongsFragment.newInstance(mEdition.getYear(), EuroRound.SEMIFINAL2), getString(R.string.semifinal2_tab_text));
        } else if (year > 2003) {
            mAdapter.addFragment(EditionSongsFragment.newInstance(mEdition.getYear(), EuroRound.FINAL), getString(R.string.final_tab_text));
            mAdapter.addFragment(EditionSongsFragment.newInstance(mEdition.getYear(), EuroRound.SEMIFINAL1), getString(R.string.semifinal_tab_text));
        } else {
            mAdapter.addFragment(EditionSongsFragment.newInstance(mEdition.getYear(), EuroRound.FINAL), getString(R.string.results_tab_text));
        }
    }

    public class SectionsPageAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public SectionsPageAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }

    private class initializeUiTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Void doInBackground(Void... params) {
            initializeUiContent(mViewPager);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mProgressBar.setVisibility(View.INVISIBLE);
            updateUi();
        }
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
}
