package com.character.microblogapp.ui.page.main.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.character.microblogapp.R;
import com.character.microblogapp.data.MyInfo;
import com.character.microblogapp.ui.page.BaseFragment;
import com.character.microblogapp.ui.page.main.MainActivity;
import com.character.microblogapp.util.PrefMgr;

import butterknife.OnClick;

public class DISCInfoMainFragment extends BaseFragment {




    PrefMgr m_prefMgr;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        createView(inflater, container, R.layout.fragment_discinfo_main);
        SharedPreferences prefs = getActivity().getSharedPreferences(PrefMgr.APP_PREFS,
                Context.MODE_PRIVATE);
        m_prefMgr = new PrefMgr(prefs);


        return mRoot;
    }







    @Override
    protected void initUI() {




    }




@OnClick(R.id.rlt_back)
    public void rlt_backClicked(){
    MainActivity mainActivity = (MainActivity) getActivity();
    mainActivity.selectLine(0);
    mainActivity.selectTab(8);
}

@OnClick(R.id.tv_d)
    public void tv_dClicked(){
    MainActivity mainActivity = (MainActivity) getActivity();
    mainActivity.selectLine(0);
    mainActivity.selectTab(10);
}
    @OnClick(R.id.tv_i)
    public void tv_iClicked(){
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.selectLine(0);
        mainActivity.selectTab(11);
    }
    @OnClick(R.id.tv_s)
    public void tv_sClicked(){
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.selectLine(0);
        mainActivity.selectTab(12);
    }
    @OnClick(R.id.tv_c)
    public void tv_cClicked(){
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.selectLine(0);
        mainActivity.selectTab(13);
    }














}
