package com.character.microblogapp.ui.page.main.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.character.microblogapp.R;
import com.character.microblogapp.ui.page.BaseFragment;
import com.character.microblogapp.ui.page.main.MainActivity;
import com.character.microblogapp.util.PrefMgr;

import butterknife.ButterKnife;

/**
 * 프로필 심사중 페이지
 */
public class InspectingUserFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        createView(inflater, container, R.layout.fragment_inspecting);

        return mRoot;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initVC();
    }

    void initVC() {


    }
}
