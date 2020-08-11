package com.character.microblogapp.ui.page.main.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.character.microblogapp.R;
import com.character.microblogapp.data.MyInfo;
import com.character.microblogapp.ui.page.BaseFragment;
import com.character.microblogapp.ui.page.main.MainActivity;
import com.character.microblogapp.ui.page.setting.ProfileChangeActivity;
import com.character.microblogapp.util.PrefMgr;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 프로필 심사통과 페이지
 */
public class RejectFragment extends BaseFragment {

    @BindView(R.id.tvReason)
    TextView tvReason;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        createView(inflater, container, R.layout.fragment_reject);
        initData();
        return mRoot;
    }

    private void initData() {
        tvReason.setText(MyInfo.getInstance().status_memo);

//        nextStep();
    }

//    void nextStep() {
//        mainHandler.sendEmptyMessageDelayed(0, 3000);
//    }

//    Handler mainHandler = new Handler(new Handler.Callback() {
//        @Override
//        public boolean handleMessage(Message msg) {
//            getActivity().startActivity(new Intent(getActivity(), ProfileChangeActivity.class));
//            return false;
//        }
//    });

    @OnClick(R.id.tvReason)
    void onReason() {
        getActivity().startActivity(new Intent(getActivity(), ProfileChangeActivity.class));
    }

}
