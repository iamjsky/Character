package com.character.microblogapp.ui.page.main.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
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
import com.character.microblogapp.data.Constant;
import com.character.microblogapp.model.MCharacterInfo;
import com.character.microblogapp.model.MCharacterInfo2;
import com.character.microblogapp.model.MError;
import com.character.microblogapp.model.MVersion;
import com.character.microblogapp.net.Net;
import com.character.microblogapp.ui.page.BaseFragment;
import com.character.microblogapp.ui.page.main.MainActivity;
import com.character.microblogapp.ui.page.profile.DISCInfoDetailActivity;
import com.character.microblogapp.util.PrefMgr;
import com.character.microblogapp.util.Toaster;
import com.nex3z.flowlayout.FlowLayout;

import butterknife.BindView;
import butterknife.OnClick;

import static com.facebook.FacebookSdk.getApplicationContext;


public class DISCInfoTypeFragment extends BaseFragment {

    //
    @BindView(R.id.tv_char_01)
    TextView tv_char_01;

    @BindView(R.id.tv_char_03)
    TextView tv_char_03;

    @BindView(R.id.tv_char_05)
    TextView tv_char_05;

    @BindView(R.id.tv_charText_01)
    TextView tv_charText_01;

    @BindView(R.id.tv_charText_03)
    TextView tv_charText_03;

    @BindView(R.id.tv_charText_05)
    TextView tv_charText_05;

    @BindView(R.id.tv_charType_01)
    TextView tv_charType_01;
    @BindView(R.id.tv_charType_03)
    TextView tv_charType_03;


    @BindView(R.id.tv_charFact_01)
    TextView tv_charFact_01;
    @BindView(R.id.tv_charInfo)
    TextView tv_charInfo;


    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.layout_discResult)
    LinearLayout layout_discResult;
    @BindView(R.id.layout_jobEnv)
    LinearLayout layout_jobEnv;
    @BindView(R.id.layout_job)
    LinearLayout layout_job;
    @BindView(R.id.layout_jobEnvList_01)
    LinearLayout layout_jobEnvList_01;

    @BindView(R.id.tv_jobEnvList_01)
    TextView tv_jobEnvList_01;

    @BindView(R.id.tv_jobList_01)
    TextView tv_jobList_01;


    @BindView(R.id.view_jobLine)
    View view_jobLine;
    @BindView(R.id.layout_jobList_01)
    LinearLayout layout_jobList_01;


    @BindView(R.id.sv_charTestResult)
    ScrollView sv_charTestResult;

    @BindView(R.id.layout_charTagArea_01)
    FlowLayout layout_charTagArea_01;

    @BindView(R.id.layout_topCharPanel)
    LinearLayout layout_topCharPanel;
    @BindView(R.id.layout_bottomCharPanel)
    LinearLayout layout_bottomCharPanel;

    @BindView(R.id.tv_charInfoTitle)
    TextView tv_charInfoTitle;
    @BindView(R.id.tv_scared)
    TextView tv_scared;
    @BindView(R.id.layout_limitArea)
    LinearLayout layout_limitArea;
    @BindView(R.id.tv_job)
    TextView tv_job;
    @BindView(R.id.tv_office)
    TextView tv_office;
    @BindView(R.id.tv_charStar)
    TextView tv_charStar;
    @BindView(R.id.iv_topBg)
    ImageView iv_topBg;
    @BindView(R.id.tv_charTitle)
    TextView tv_charTitle;

    @BindView(R.id.layout_panel)
    LinearLayout layout_panel;

    @BindView(R.id.iv_charImg_01)
    ImageView iv_charImg_01;

    PrefMgr m_prefMgr;

    String type = "";
    int nowPage = 0;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        createView(inflater, container, R.layout.fragment_discinfo_type);
        SharedPreferences prefs = getActivity().getSharedPreferences(PrefMgr.APP_PREFS,
                Context.MODE_PRIVATE);
        m_prefMgr = new PrefMgr(prefs);
        type = getTag();
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
        if (nowPage == 0) {
            mainActivity.selectTab(9);
        } else {
            setUI();
        }


    }

    @OnClick(R.id.tv_job)
    void findJob() {
        if (result == null) {
            return;
        }
        nowPage = 1;
        layout_discResult.setVisibility(View.GONE);
        layout_job.setVisibility(View.VISIBLE);
        layout_jobEnv.setVisibility(View.GONE);
        tv_title.setText("선호 직업정보");
        sv_charTestResult.setScrollY(0);
    }

    @OnClick(R.id.tv_office)
    void findOffice() {
        if (result == null) {
            return;
        }
        nowPage = 2;
        layout_discResult.setVisibility(View.GONE);
        layout_job.setVisibility(View.GONE);
        layout_jobEnv.setVisibility(View.VISIBLE);
        tv_title.setText("선호 직무환경");

        sv_charTestResult.setScrollY(0);

    }

    void setUI() {
        if (result == null) {
            return;
        }
        tv_title.setText("성격정보 - " + type + "형");
        nowPage = 0;
        sv_charTestResult.setScrollY(0);
        //add
        String personality1 = result.personality1 + "";
        String personality_title1 = result.personality_title1;
        String name = result.name;
        String factor = result.factor;
        String keyword1 = result.keyword1;
        String personality_char2 = result.personality_char2;
        String job1 = result.job1;
        String job_env1 = result.job_env1;
        String fear = result.fear;
        String title = result.title;
        String celeb = result.celeb;
        String limit = result.limit;

        layout_discResult.setVisibility(View.VISIBLE);
        layout_job.setVisibility(View.GONE);
        layout_jobEnv.setVisibility(View.GONE);
        tv_charTitle.setText(title);


        tv_char_01.setText(personality1);
        tv_char_03.setText(personality1);
        tv_char_05.setText(personality1);

        String charText = "";
        switch (type){

            case "D":
                charText = "노빠꾸";
                break;
            case "I":
                charText = "쌉관종";
                break;
            case "S":
                charText = "핵고구마";
                break;
            case "C":
                charText = "핵노잼";
                break;
        }
        tv_charText_01.setText(charText);
        tv_charText_03.setText(name);
        tv_charText_05.setText(name);


        tv_charType_01.setText(name);
        tv_charType_03.setText(personality1);
        tv_charFact_01.setText(factor);

        tv_charStar.setText(celeb);

        layout_charTagArea_01.removeAllViews();
        layout_limitArea.removeAllViews();

        iv_topBg.setBackground(getDISCBackground(type, 3));
        iv_topBg.setVisibility(View.VISIBLE);

        if (!keyword1.equals("")) {
            String[] keyword1Arr = keyword1.split(",");
            for (int i = 0; i < keyword1Arr.length; i++) {
                ViewGroup keywordView = (ViewGroup) getLayoutInflater().inflate(R.layout.view_charinfo_keyword_item, null, false);
                TextView tv_keyword = (TextView) keywordView.findViewById(R.id.tv_keyword);
                tv_keyword.setBackground(getDISCBackground(type, 4));
                tv_keyword.setText(keyword1Arr[i].trim());
                layout_charTagArea_01.addView(keywordView);
            }
        }
        if (!limit.equals("")) {
            String[] limitArr = limit.split(",");
            for (int i = 0; i < limitArr.length; i++) {
                ViewGroup limitView = (ViewGroup) getLayoutInflater().inflate(R.layout.view_charinfo_limit_item, null, false);
                TextView tv_limit = (TextView) limitView.findViewById(R.id.tv_limit);
                tv_limit.setBackground(getDISCBackground(type, 2));

                tv_limit.setText(limitArr[i].trim());
                layout_limitArea.addView(limitView);
            }
        }

        tv_char_01.setTextColor(getDISCColor(type));
        tv_char_03.setTextColor(getDISCColor(type));
        tv_char_05.setTextColor(getDISCColor(type));

        tv_job.setTextColor(getDISCColor(type));
        tv_office.setTextColor(getDISCColor(type));
        tv_charInfoTitle.setTextColor(getDISCColor(type));

        layout_topCharPanel.setBackground(getDISCBackground(type, 0));
        layout_bottomCharPanel.setBackground(getDISCBackground(type, 1));
        layout_panel.setBackground(getDISCBackground(type, 5));


        tv_charInfo.setText(setDISCCharColor(personality_char2, personality1));


        tv_scared.setText(fear);

        tv_jobEnvList_01.setText(job_env1);
        tv_jobList_01.setText(job1);


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

    private Drawable getDISCBackground(String type, int layout) {
        Drawable drawable = null;
        switch (type) {

            case "D":
                if (layout == 0) {
                    drawable = getResources().getDrawable(R.drawable.bg_rounded_charinfo_d_01);
                } else if (layout == 1) {
                    drawable = getResources().getDrawable(R.drawable.bg_rounded_charinfo_d_02);
                } else if (layout == 2) {
                    drawable = getResources().getDrawable(R.drawable.bg_rounded_charinfo_keyword_d_02);
                } else if (layout == 3) {
                    drawable = getResources().getDrawable(R.drawable.bg_d_img);
                } else if (layout == 4) {
                    drawable = getResources().getDrawable(R.drawable.bg_rounded_charinfo_keyword_d_01);
                } else if (layout == 5) {
                    drawable = getResources().getDrawable(R.drawable.bg_rounded_charinfo_panel_d_01);
                }


                break;
            case "I":
                if (layout == 0) {
                    drawable = getResources().getDrawable(R.drawable.bg_rounded_charinfo_i_01);
                } else if (layout == 1) {
                    drawable = getResources().getDrawable(R.drawable.bg_rounded_charinfo_i_02);
                } else if (layout == 2) {
                    drawable = getResources().getDrawable(R.drawable.bg_rounded_charinfo_keyword_i_02);
                } else if (layout == 3) {
                    drawable = getResources().getDrawable(R.drawable.bg_i_img);
                } else if (layout == 4) {
                    drawable = getResources().getDrawable(R.drawable.bg_rounded_charinfo_keyword_i_01);
                } else if (layout == 5) {
                    drawable = getResources().getDrawable(R.drawable.bg_rounded_charinfo_panel_i_01);
                }


                break;
            case "C":
                if (layout == 0) {
                    drawable = getResources().getDrawable(R.drawable.bg_rounded_charinfo_c_01);
                } else if (layout == 1) {
                    drawable = getResources().getDrawable(R.drawable.bg_rounded_charinfo_c_02);
                } else if (layout == 2) {
                    drawable = getResources().getDrawable(R.drawable.bg_rounded_charinfo_keyword_c_02);
                } else if (layout == 3) {
                    drawable = getResources().getDrawable(R.drawable.bg_c_img);
                } else if (layout == 4) {
                    drawable = getResources().getDrawable(R.drawable.bg_rounded_charinfo_keyword_c_01);
                } else if (layout == 5) {
                    drawable = getResources().getDrawable(R.drawable.bg_rounded_charinfo_panel_c_01);
                }


                break;
            case "S":
                if (layout == 0) {
                    drawable = getResources().getDrawable(R.drawable.bg_rounded_charinfo_s_01);
                } else if (layout == 1) {
                    drawable = getResources().getDrawable(R.drawable.bg_rounded_charinfo_s_02);
                } else if (layout == 2) {
                    drawable = getResources().getDrawable(R.drawable.bg_rounded_charinfo_keyword_s_02);
                } else if (layout == 3) {
                    drawable = getResources().getDrawable(R.drawable.bg_s_img);
                } else if (layout == 4) {
                    drawable = getResources().getDrawable(R.drawable.bg_rounded_charinfo_keyword_s_01);
                } else if (layout == 5) {
                    drawable = getResources().getDrawable(R.drawable.bg_rounded_charinfo_panel_s_01);
                }

                break;


        }
        return drawable;
    }

    private void getInfo() {

        Net.instance().api.get_character_info_style2(type)
                .enqueue(new Net.ResponseCallBack<MCharacterInfo2>() {
                    @Override
                    public void onSuccess(MCharacterInfo2 response) {

                        if (response.info != null) {
                            result = response.info;
                            setUI();
                        }
                    }

                    @Override
                    public void onFailure(MError response) {

                        Toaster.showShort(getActivity(), "등록된 데이터가 없습니다.");
                    }
                });
    }

    private Spannable setDISCCharColor(String desc, String type){
        Spannable sb = new SpannableString(desc);
        for (int i = 0; i < desc.length(); i++) {
            char temp = (desc.charAt(i));
            switch (temp) {
                case 'D':
                    sb.setSpan(new ForegroundColorSpan(Constant.CHARACTER_D_COLOR), i, i + 1, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                    break;
                case 'I':
                    sb.setSpan(new ForegroundColorSpan(Constant.CHARACTER_I_COLOR), i, i + 1, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                    break;
                case 'S':
                    sb.setSpan(new ForegroundColorSpan(Constant.CHARACTER_S_COLOR), i, i + 1, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                    break;
                case 'C':
                    sb.setSpan(new ForegroundColorSpan(Constant.CHARACTER_C_COLOR), i, i + 1, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                    break;

                case '-':
                    if(type.equals("D")){
                        sb.setSpan(new ForegroundColorSpan(Constant.CHARACTER_D_COLOR), i, i + 1, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                    }else if(type.equals("I")){
                        sb.setSpan(new ForegroundColorSpan(Constant.CHARACTER_I_COLOR), i, i + 1, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                    }else if(type.equals("S")){
                        sb.setSpan(new ForegroundColorSpan(Constant.CHARACTER_S_COLOR), i, i + 1, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                    }else if(type.equals("C")){
                        sb.setSpan(new ForegroundColorSpan(Constant.CHARACTER_C_COLOR), i, i + 1, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                    }

                    break;

            }

        }

        return sb;
    }
}
