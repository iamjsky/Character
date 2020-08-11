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
import com.character.microblogapp.ui.page.setting.AlarmListActivity;
import com.character.microblogapp.ui.page.setting.EnergyActivity;
import com.character.microblogapp.util.PrefMgr;

import butterknife.BindView;
import butterknife.OnClick;

public class CharInfoFragment extends BaseFragment {

    @BindView(R.id.iv_home_gif)
    ImageView ivHomeGif;


    @BindView(R.id.cl_progress)
    ConstraintLayout clProgress;

    @BindView(R.id.tv_title)
    TextView txvTitle;



    @BindView(R.id.tv_my_point)
    TextView txvMyHeart;

    // 알람 아이콘 관련
    @BindView(R.id.iv_nav_menu)
    ImageView ivNavMenu;

    @BindView(R.id.rl_noti_count)
    RelativeLayout rlNotiCnt;

    @BindView(R.id.tv_noti_cnt)
    TextView tvNotiCnt;


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



        updateBadgeCount();





        ////////
//        Glide.with(this)
//                .load(R.drawable.card_refresh)
//                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
//                .into(new DrawableImageViewTarget(ivHomeGif));
    }







    private void updateBadgeCount() {
        txvMyHeart.setText(MyInfo.getInstance().energy + "");
    }





    int remain_seconds = -1;
    int unread_noti_cnt = 0;



    private void updateAlarmCnt() {

        if (unread_noti_cnt > 0) {
            rlNotiCnt.setVisibility(View.VISIBLE);
            tvNotiCnt.setText("" + unread_noti_cnt);
            ivNavMenu.setImageResource(R.drawable.ico_noti_active);
        } else {
            rlNotiCnt.setVisibility(View.GONE);
            ivNavMenu.setImageResource(R.drawable.ico_noti_normal);
        }
    }








    public void getUnreadAlarmCnt() {
        Net.instance().api.get_unread_alarm_cnt(MyInfo.getInstance().uid)
                .enqueue(new Net.ResponseCallBack<MBase>() {
                    @Override
                    public void onSuccess(MBase response) {
                        super.onSuccess(response);
                        unread_noti_cnt = response.count;
                        updateAlarmCnt();
                    }

                    @Override
                    public void onFailure(MError response) {
                        super.onFailure(response);
                        if (response.resultcode == 500) {
                            networkErrorOccupied(response);
                        }
                    }
                });
    }












    @OnClick({R.id.iv_my_point, R.id.rl_nav_menu})
    void onPoint(View view) {
        if (view.getId() == R.id.iv_my_point) {
            startActivity(new Intent(getActivity(), EnergyActivity.class));
        }
    }



}
