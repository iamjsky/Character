package com.character.microblogapp.ui.page.main.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.character.microblogapp.R;
import com.character.microblogapp.data.MyInfo;
import com.character.microblogapp.model.MEnergy;
import com.character.microblogapp.model.MError;
import com.character.microblogapp.net.Net;
import com.character.microblogapp.ui.dialog.ConfirmDialog2;
import com.character.microblogapp.ui.page.BaseFragment;
import com.character.microblogapp.ui.page.character.CharacterActivity;
import com.character.microblogapp.ui.page.community.CommunityActivity;
import com.character.microblogapp.ui.page.main.MainActivity;
import com.character.microblogapp.ui.page.setting.EnergyActivity;
import com.character.microblogapp.util.PrefMgr;

import butterknife.BindView;
import butterknife.OnClick;

public class CharWorldFragment extends BaseFragment {



    @BindView(R.id.tv_my_point)
    TextView txvMyHeart;



    PrefMgr m_prefMgr;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        createView(inflater, container, R.layout.fragment_char_world);
        SharedPreferences prefs = getActivity().getSharedPreferences(PrefMgr.APP_PREFS,
                Context.MODE_PRIVATE);
        m_prefMgr = new PrefMgr(prefs);

        Log.e("char_debug", "MyInfo : " + MyInfo.getInstance().character);
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







    @OnClick(R.id.layout_myPoint)
    void onPoint(View view) {
        if (view.getId() == R.id.layout_myPoint) {
            startActivity(new Intent(getActivity(), EnergyActivity.class));
        }
    }






    //add


    @OnClick(R.id.layout_charInfo)
    void layout_charInfoClicked(){
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.selectLine(0);
        mainActivity.selectTab(8);

    }
    @OnClick(R.id.layout_relation)
    void layout_relationClicked(){
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.selectLine(0);
        mainActivity.selectTab(17);

    }

    @OnClick(R.id.layout_community)
    void layout_communityClicked(){
        // 커뮤니티페이지로 이동
        Intent intent = new Intent(getContext(), CommunityActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.layout_charTest)
    void layout_charTestClicked(){
        ConfirmDialog2.show(getContext(), "테스트를 다시할래요?", "에너지 20개를 사용할까요?", new ConfirmDialog2.ActionListener() {
            @Override
            public void onOk() {

                if (MyInfo.getInstance().energy >= 20) {
                    calcEnergy(20);
                } else {

                    ConfirmDialog2.show(getContext(), "에너지가 부족해요!!", String.format(getString(R.string.confirm_get_energy), (20 - MyInfo.getInstance().energy)),
                            new ConfirmDialog2.ActionListener() {
                                @Override
                                public void onOk() {
                                    Intent goEnergy = new Intent(getContext(), EnergyActivity.class);
                                    goEnergy.putExtra("nickname", MyInfo.getInstance().nickname);
                                    startActivity(goEnergy);
                                }
                            });
                }
            }
        });
    }

    private void calcEnergy(int count) {
        Net.instance().api.calc_point(MyInfo.getInstance().uid, count, "성격 테스트 다시하기")
                .enqueue(new Net.ResponseCallBack<MEnergy>() {
                    @Override
                    public void onSuccess(MEnergy response) {
                        super.onSuccess(response);

                        MyInfo.getInstance().energy = response.energy;

                        Intent intent = new Intent(getContext(), CharacterActivity.class);
                        intent.putExtra("go", 1);
                        startActivity(intent);

                    }

                    @Override
                    public void onFailure(MError response) {
                        super.onFailure(response);

                        if (response.resultcode == 500) {
                            networkErrorOccupied(response);
                        } else {
                            new AlertDialog.Builder(getContext()).setTitle("").setMessage(response.res_msg).setPositiveButton(R.string.confirm, null).show();
                        }
                    }
                });
    }



}
