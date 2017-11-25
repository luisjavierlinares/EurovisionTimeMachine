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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.binarylemons.android.eurovisiontimemachine.controller.EuroController;
import com.binarylemons.android.eurovisiontimemachine.model.EuroCountry;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Luis on 17/11/2017.
 */

public class CountryFragment extends Fragment {

    private static final String ARG_COUNTRY_CODE = "com.binarylemons.android.eurovisiontimemachine.CountryFragment.arg_country_code";

    private EuroController mController;
    private EuroCountry mCountry;

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.tabs)
    TabLayout mTabLayout;
    @BindView(R.id.progress_bar) ProgressBar mProgressBar;
    @BindView(R.id.view_pager) ViewPager mViewPager;
    private SectionsPageAdapter mAdapter;

    private SongCallbacks mSongCallbacks;

    public static CountryFragment newInstance(String countryCode) {
        Bundle arguments = new Bundle();
        arguments.putString(ARG_COUNTRY_CODE, countryCode);

        CountryFragment fragment = new CountryFragment();
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
        View view = inflater.inflate(R.layout.fragment_country, container, false);
        ButterKnife.bind(this, view);

        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        setHasOptionsMenu(true);

        mToolbar.setSubtitle(mCountry.getName(getActivity()) + " " + getString(R.string.on_eurovision));
        mToolbar.setTitleTextColor(getResources().getColor(R.color.toolbarTitle));
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
        mAdapter.addFragment(CountrySongsFragment.newInstance(mCountry.getCountryCode().toString()), getString(R.string.songs_tab_text));
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
