package com.character.microblogapp.ui.page.main.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.character.microblogapp.R;
import com.character.microblogapp.ui.page.BaseFragment;
import com.character.microblogapp.ui.page.main.MainActivity;
import com.character.microblogapp.util.PrefMgr;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 프로필 심사통과 페이지
 */
public class PassedInspectionFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        createView(inflater, container, R.layout.fragment_passed_inspection);
        initData();
        return mRoot;
    }

    private void initData() {
        SharedPreferences prefs = getContext().getSharedPreferences(PrefMgr.APP_PREFS,
                Context.MODE_PRIVATE);
        PrefMgr prefMgr = new PrefMgr(prefs);
        prefMgr.put(PrefMgr.WATCHED_PROFILE_ACCEPTED, true);

        _remainHandler.sendEmptyMessageDelayed(0, 3000);
    }

    @OnClick(R.id.btn_start)
    public void onStartClicked() {
        MainActivity mainActivity = (MainActivity) mParent;
        if (mainActivity == null) {
            return;
        }
        mainActivity.selectTab(0);
    }

    Handler _remainHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            MainActivity mainActivity = (MainActivity) getActivity();
            if (mainActivity != null)
                mainActivity.goHome();

            return false;
        }
    });
}
