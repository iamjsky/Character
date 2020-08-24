package com.character.microblogapp.ui.page.main.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.character.microblogapp.R;
import com.character.microblogapp.data.MyInfo;
import com.character.microblogapp.model.MCharacterInfo2;
import com.character.microblogapp.model.MError;
import com.character.microblogapp.net.Net;
import com.character.microblogapp.ui.page.BaseFragment;
import com.character.microblogapp.ui.page.main.MainActivity;
import com.character.microblogapp.util.PrefMgr;
import com.character.microblogapp.util.Toaster;
import com.nex3z.flowlayout.FlowLayout;

import butterknife.BindView;
import butterknife.OnClick;

import static com.facebook.FacebookSdk.getApplicationContext;


public class DISCMyLoverFragment extends BaseFragment {

    //
    @BindView(R.id.tv_char_01)
    TextView tv_char_01;

    @BindView(R.id.tv_char_02)
    TextView tv_char_02;


    @BindView(R.id.tv_charLover_01)
    TextView tv_charLover_01;

    @BindView(R.id.tv_charLover_02)
    TextView tv_charLover_02;

    @BindView(R.id.layout_result)
            LinearLayout layout_result;


    PrefMgr m_prefMgr;

    String type = "";
    int nowPage = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        createView(inflater, container, R.layout.fragment_disc_mylover);
        SharedPreferences prefs = getActivity().getSharedPreferences(PrefMgr.APP_PREFS,
                Context.MODE_PRIVATE);
        m_prefMgr = new PrefMgr(prefs);
        type = MyInfo.getInstance().character;
//You need to add the following line for this solution to work; thanks skayred

        mRoot.setFocusableInTouchMode(true);
        mRoot.requestFocus();

        mRoot.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (event.getAction() == KeyEvent.ACTION_UP) {
                        rlt_backClicked();
                        return true;
                    }
                }

                return false;
            }
        });

        getInfo();

        return mRoot;
    }


    @Override
    protected void initUI() {


    }


    @OnClick(R.id.rlt_back)
    public void rlt_backClicked() {
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.selectLine(0);
        mainActivity.selectTab(8);


    }

    @OnClick(R.id.layout_charListArea)
    public void layout_charListAreaClicked() {
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.parentFrag = "DISCMyLoverFragment";
        mainActivity.selectLine(0);
        mainActivity.selectTab(16);
    }


    void setUI() {
        if (result == null) {
            return;
        }

        nowPage = 0;

        //add
        String personality1 = result.personality1 + "";
        String personality2 = result.personality2 + "";

        String gender1 = result.gender1 + "";
        String gender2 = result.gender2 + "";


        tv_char_01.setText(personality1);
        tv_char_01.setTextColor(getDISCColor(personality1));
        tv_charLover_01.setText(gender1);

        if (!personality2.equals("")) {

            tv_char_02.setText(personality2);
            tv_char_02.setTextColor(getDISCColor(personality2));
            tv_charLover_02.setText(gender2);
            tv_char_02.setVisibility(View.VISIBLE);
            tv_charLover_02.setVisibility(View.VISIBLE);
        } else {
            tv_char_02.setVisibility(View.GONE);
            tv_charLover_02.setVisibility(View.GONE);
        }

        layout_result.setVisibility(View.VISIBLE);
    }


    MCharacterInfo2.Info result = null;

    private int getDISCColor(String type) {
        int discColor;
        switch (type) {

            case "D":
                discColor = ContextCompat.getColor(getApplicationContext(), R.color.char_d_color);

                break;
            case "I":
                discColor = ContextCompat.getColor(getApplicationContext(), R.color.char_i_color);

                break;
            case "C":
                discColor = ContextCompat.getColor(getApplicationContext(), R.color.char_c_color);

                break;
            case "S":
                discColor = ContextCompat.getColor(getApplicationContext(), R.color.char_s_color);
                break;
            default:
                discColor = ContextCompat.getColor(getApplicationContext(), R.color.color_char_gray);


        }
        return discColor;
    }


    private void getInfo() {

        Net.instance().api.get_character_info_style2(type)
                .enqueue(new Net.ResponseCallBack<MCharacterInfo2>() {
                    @Override
                    public void onSuccess(MCharacterInfo2 response) {

                        if (response.info != null) {
                            result = response.info;
                            Log.e("char_debug", "type : " + type + " / info : " + result);
                            setUI();
                        }
                    }

                    @Override
                    public void onFailure(MError response) {

                        Toaster.showShort(getActivity(), "등록된 데이터가 없습니다.");
                    }
                });
    }


}
