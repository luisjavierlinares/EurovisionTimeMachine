package com.binarylemons.android.eurovisiontimemachine;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.binarylemons.android.eurovisiontimemachine.model.EuroQuery;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Luis on 25/10/2017.
 */

public class EurovisionFragment extends Fragment {

    private static final int REQUEST_SEARCH = 0;

    private static final String DIALOG_SEARCH = "com.binarylemons.android.eurovisiontimemachine.EurovisionFragment.DIALOG_SEARCH";

    private static final int TOTAL_TABS = 3;
    private static final int EDITIONS_TAB = 1;
    private static final int COUNTRIES_TAB = 2;
    private static final int SEARCH_TAB = 3;

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.tabs) TabLayout mTabLayout;
    @BindView(R.id.progress_bar) ProgressBar mProgressBar;
    @BindView(R.id.view_pager) ViewPager mViewPager;
    private SectionsPageAdapter mAdapter;

    public static EurovisionFragment newInstance() {
        Bundle args = new Bundle();

        EurovisionFragment fragment = new EurovisionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_eurovision, container, false);
        ButterKnife.bind(this, view);

        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        setHasOptionsMenu(true);

        mToolbar.setTitleTextColor(getResources().getColor(R.color.toolbarTitle));

        mViewPager.setOffscreenPageLimit(TOTAL_TABS - 1);

        initializeUi();

        return view;
    }

    @OnClick(R.id.search_floating_button)
    public void onSearchButtonClicked(View view) {
        FragmentManager fragmentManager = getFragmentManager();
        SearchDialogFragment searchDialog = SearchDialogFragment.newInstance();
        searchDialog.setTargetFragment(EurovisionFragment.this, REQUEST_SEARCH);
        searchDialog.show(fragmentManager, DIALOG_SEARCH);
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

        mAdapter.addFragment(EditionsFragment.newInstance(), getString(R.string.editions_tab_text));
        mAdapter.addFragment(CountriesFragment.newInstance(), getString(R.string.countries_tab_text));
        mAdapter.addFragment(SearchFragment.newInstance(), getString(R.string.search_tab_text));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) return;

        if (requestCode == REQUEST_SEARCH) {
            EuroQuery query = SearchDialogFragment.getQuery(data);
            SearchFragment searchFragment = (SearchFragment) mAdapter.instantiateItem(mViewPager, SEARCH_TAB - 1);
            searchFragment.searchSongs(query);
            mViewPager.setCurrentItem(SEARCH_TAB);
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
}
