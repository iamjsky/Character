package com.character.microblogapp.ui.page.main.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.character.microblogapp.R;
import com.character.microblogapp.data.MyInfo;
import com.character.microblogapp.model.MBase;
import com.character.microblogapp.model.MError;
import com.character.microblogapp.net.Net;
import com.character.microblogapp.ui.page.BaseFragment;
import com.character.microblogapp.ui.page.main.MainActivity;
import com.character.microblogapp.ui.page.setting.AlarmListActivity;
import com.character.microblogapp.ui.page.setting.EnergyActivity;
import com.character.microblogapp.util.PrefMgr;

import butterknife.BindView;
import butterknife.OnClick;

public class CharInfoFragment extends BaseFragment {




    PrefMgr m_prefMgr;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        createView(inflater, container, R.layout.fragment_char_info);
        SharedPreferences prefs = getActivity().getSharedPreferences(PrefMgr.APP_PREFS,
                Context.MODE_PRIVATE);
        m_prefMgr = new PrefMgr(prefs);

        return mRoot;
    }







    @Override
    protected void initUI() {




    }


@OnClick(R.id.layout_discInfo)
public void layout_discInfoClicked(){
    MainActivity mainActivity = (MainActivity) getActivity();
    mainActivity.selectLine(0);
    mainActivity.selectTab(9);
}

@OnClick(R.id.rlt_back)
    public void rlt_backClicked(){
    MainActivity mainActivity = (MainActivity) getActivity();
    mainActivity.selectLine(0);
    mainActivity.selectTab(0);
}
















}
