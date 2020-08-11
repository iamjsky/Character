package com.character.microblogapp.ui.page.main.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.blankj.utilcode.util.ConvertUtils;
import com.character.microblogapp.MyApplication;
import com.character.microblogapp.R;
import com.character.microblogapp.data.Common;
import com.character.microblogapp.ui.page.BaseFragment;
import com.character.microblogapp.ui.page.main.MainActivity;
import com.character.microblogapp.ui.page.model.ModelFindActivity;
import com.character.microblogapp.util.PagerSlidingTabStrip;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

public class HistoryFragment extends BaseFragment {

    @BindView(R.id.pager_tabs)
    PagerSlidingTabStrip pagerTabLayout;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @BindView(R.id.btn_find_model)
    Button btnFindModel;

    MyApplication mApp = null;
    SectionsPagerAdapter mPagerAdapter = null;
    ArrayList<HistoryItemsFragment> fragments = new ArrayList<>();

    int m_nCurrentTab = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        createView(inflater, container, R.layout.fragment_history);

        initData();
        initUI(container);
        return mRoot;
    }

    @OnClick(R.id.btn_find_model)
    public void onClickFindModel() {
        //이상형 찾기
        MainActivity mainActivity = (MainActivity) getActivity();

        Intent goModelFind = new Intent(getContext(), ModelFindActivity.class);
        mainActivity.startActivityForResult(goModelFind, 1002);

        mainActivity.goHome();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        FragmentManager manager = getFragmentManager();
        if (manager != null) {
            for (Fragment fragment : manager.getFragments()) {
                if (fragment.equals(mPagerAdapter.getItem(m_nCurrentTab)))
                    fragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    private void initData() {
        mApp = (MyApplication) getActivity().getApplicationContext();
        mPagerAdapter = new SectionsPagerAdapter(getFragmentManager());
        viewPager.setAdapter(mPagerAdapter);
    }

    private void initUI(View p_view) {
        pagerTabLayout.setViewPager(viewPager);
        pagerTabLayout.setScrollOffset(ConvertUtils.dp2px(64));
//        btnFindModel.setVisibility(View.GONE);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                fragments.get(i).loadUsers();
                viewPager.setCurrentItem(i);
                m_nCurrentTab = i;
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
//        viewPager.setClipToPadding(false);
//        viewPager.setPadding(0, 0, 0, 0);
    }

    public void selectTab(int tab) {
        if (viewPager != null) {
            viewPager.setCurrentItem(tab);
            m_nCurrentTab = tab;
        }
    }


    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            fragments.add(new HistoryItemsFragment());
            fragments.add(new HistoryItemsFragment());
            fragments.add(new HistoryItemsFragment());
            fragments.add(new HistoryItemsFragment());
            fragments.add(new HistoryItemsFragment());
        }

        @Override
        public Fragment getItem(int position) {
            HistoryItemsFragment fragment = fragments.get(position);
            fragment.setViewType(position);
            return fragment;
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String[] titles = new String[]{
                    getString(R.string.top_tab_receive_like),
                    getString(R.string.top_tab_send_like),
                    getString(R.string.top_tab_receive_high_score),
                    getString(R.string.top_tab_send_high_score),
                    "지난 소개",
            };
            return titles[position];
        }
    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        if (requestCode == 1002 && resultCode == RESULT_OK) {
//
//        }
//    }
}
