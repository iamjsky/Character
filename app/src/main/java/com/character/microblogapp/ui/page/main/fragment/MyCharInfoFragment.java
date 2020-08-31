package com.character.microblogapp.ui.page.main.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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

@SuppressLint("ValidFragment")
public class MyCharInfoFragment extends BaseFragment {

    @BindView(R.id.tv_char_01)
    TextView tv_char_01;
    @BindView(R.id.tv_char_02)
    TextView tv_char_02;
    @BindView(R.id.tv_char_03)
    TextView tv_char_03;
    @BindView(R.id.tv_char_04)
    TextView tv_char_04;
    @BindView(R.id.tv_char_05)
    TextView tv_char_05;
    @BindView(R.id.tv_char_06)
    TextView tv_char_06;
    @BindView(R.id.tv_charText_01)
    TextView tv_charText_01;
    @BindView(R.id.tv_charText_02)
    TextView tv_charText_02;
    @BindView(R.id.tv_charText_03)
    TextView tv_charText_03;
    @BindView(R.id.tv_charText_04)
    TextView tv_charText_04;
    @BindView(R.id.tv_charText_05)
    TextView tv_charText_05;
    @BindView(R.id.tv_charText_06)
    TextView tv_charText_06;
    @BindView(R.id.tv_charType_01)
    TextView tv_charType_01;
    @BindView(R.id.tv_charType_02)
    TextView tv_charType_02;
    @BindView(R.id.tv_charType_03)
    TextView tv_charType_03;
    @BindView(R.id.tv_charType_04)
    TextView tv_charType_04;

    @BindView(R.id.tv_charFact_01)
    TextView tv_charFact_01;
    @BindView(R.id.tv_charInfo)
    TextView tv_charInfo;
    @BindView(R.id.tv_charDis)
    TextView tv_charDis;
    @BindView(R.id.tv_charAdvice)
    TextView tv_charAdvice;
    @BindView(R.id.tv_plus)
    TextView tv_plus;
    @BindView(R.id.layout_char_02)
    LinearLayout layout_char_02;

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
    @BindView(R.id.layout_jobEnvList_02)
    LinearLayout layout_jobEnvList_02;
    @BindView(R.id.tv_jobEnvList_01)
    TextView tv_jobEnvList_01;
    @BindView(R.id.tv_jobEnvList_02)
    TextView tv_jobEnvList_02;
    @BindView(R.id.tv_jobList_01)
    TextView tv_jobList_01;
    @BindView(R.id.tv_jobList_02)
    TextView tv_jobList_02;

    @BindView(R.id.view_jobLine)
    View view_jobLine;
    @BindView(R.id.layout_jobList_01)
    LinearLayout layout_jobList_01;
    @BindView(R.id.layout_jobList_02)
    LinearLayout layout_jobList_02;

    @BindView(R.id.sv_charTestResult)
    ScrollView sv_charTestResult;

    @BindView(R.id.layout_charTagArea_01)
    FlowLayout layout_charTagArea_01;
    @BindView(R.id.layout_charTagArea_02)
    FlowLayout layout_charTagArea_02;

    @BindView(R.id.layout_charType_secondArea)
    LinearLayout layout_charType_secondArea;

    @BindView(R.id.iv_charImg_01)
    ImageView iv_charImg_01;
    @BindView(R.id.iv_charImg_02)
    ImageView iv_charImg_02;

    @BindView(R.id.btn_share)
    TextView btn_share;

    @BindView(R.id.iv_topBg)
    ImageView iv_topBg;

    @BindView(R.id.layout_panel)
    LinearLayout layout_panel;
    @BindView(R.id.layout_topCharPanel)
    LinearLayout layout_topCharPanel;
    @BindView(R.id.layout_bottomCharPanel)
    LinearLayout layout_bottomCharPanel;
    @BindView(R.id.tv_job)
    TextView tv_job;
    @BindView(R.id.tv_office)
    TextView tv_office;
    @BindView(R.id.tv_charInfoTitle)
    TextView tv_charInfoTitle;

    PrefMgr m_prefMgr;

    String type = "";
    int nowPage = 0;
    int pageType = -1;

    String linkCharString = "";

    String linkTitle;
    String[] linkKeyword1Arr;
    String linkFactor;
    String linkName, linkName2;
    String linkPersonality1, linkPersonality2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        createView(inflater, container, R.layout.fragment_my_charinfo);
        SharedPreferences prefs = getActivity().getSharedPreferences(PrefMgr.APP_PREFS,
                Context.MODE_PRIVATE);
        m_prefMgr = new PrefMgr(prefs);

        MainActivity mainActivity = (MainActivity) getActivity();
        /*내 성격정보*/
        if (getTag().equals("myInfo")) {

            type = MyInfo.getInstance().character;
            type = type.toUpperCase();
            if(type.length() > 2){
                type = type.substring(0,2);
            }
            pageType = 0;
            btn_share.setVisibility(View.VISIBLE);
            /*성격 유형 리스트*/
        } else if (mainActivity.charInfoPageNum == 1) {
            type = getTag();
            pageType = 1;
            btn_share.setVisibility(View.GONE);
            /*나와 잘 맞는 성격*/
        } else if (mainActivity.charInfoPageNum == 2) {
            type = getTag();
            pageType = 2;
            btn_share.setVisibility(View.GONE);
        }



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

    @OnClick(R.id.btn_share)
    void btn_shareClicked() {
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);

        intent.setType("text/plain");


        String URL = "http://personalitism.com/result_";
        String charType = linkCharString.toLowerCase();


        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < linkKeyword1Arr.length; i++) {
            if (i == 0) {
                sb.append(linkKeyword1Arr[i]);
            } else {
                sb.append(", ").append(linkKeyword1Arr[i]);
            }

        }

        String charType2 = "";
        if(!linkPersonality2.equals("")){
            charType2 = " +" + linkName2 + "(" + linkPersonality2 + ")";
        }else {
            charType2 = "";
        }
        String linkTextResult = linkTitle+"\n"+"친구의 성격유형은 " + linkName + "(" + linkPersonality1 + ")" +
                charType2 + "\n" + "* 특징 : " + sb.toString() + "\n" + "* 동기요인 : " + linkFactor;

        intent.putExtra(Intent.EXTRA_TEXT, linkTextResult + "\n" + URL + charType);
        Intent chooser = Intent.createChooser(intent, linkTextResult + "\n" + URL+charType);
        startActivity(chooser);


    }

    @OnClick(R.id.rlt_back)
    public void rlt_backClicked() {
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.selectLine(0);
        if (pageType == 0) {
            if (nowPage == 0) {
                mainActivity.selectTab(8);
            } else {
                setUI();
            }
        } else if (pageType == 1) {
            if (nowPage == 0) {
                mainActivity.selectTab(16);
            } else {
                setUI();
            }
        } else if (pageType == 2) {
            if (nowPage == 0) {
                mainActivity.selectTab(15);
            } else {
                setUI();
            }
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
        if (result == null) {
            return;
        }
        nowPage = 0;
        sv_charTestResult.setScrollY(0);
        //add
        String personality1 = result.personality1 + "";

        String personality2 = result.personality2 + "";

        String personality_title1 = result.personality_title1;

        String personality_title2 = result.personality_title2;
        String name = result.name;
        String name2 = result.name2;
        String factor = result.factor;
        String keyword1 = result.keyword1;
        String keyword2 = result.keyword2;
        String personality_char = result.personality_char;
        String weakness = result.weakness;
        String advice = result.advice;
        String job1 = result.job1;
        String job2 = result.job2;
        String job_env1 = result.job_env1;
        String job_env2 = result.job_env2;
        String title = result.title;
        linkPersonality1 = personality1;
        linkPersonality2 = personality2;
        linkName = name;
        linkName2 = name2;
        switch (personality1){
            case "D":
                linkTitle = "\"인생은 노빠꾸지!\"";
                break;
            case "I":
                linkTitle = "\"관심과 사랑을 주세요~\"";
                break;
            case "S":
                linkTitle = "\"싸우기 싫다고..\"";
                break;
            case "C":
                linkTitle = "\"제게 팩트를 주세요!\"";
                break;

            default:

        }

        linkFactor = factor;

        layout_discResult.setVisibility(View.VISIBLE);
        layout_job.setVisibility(View.GONE);
        layout_jobEnv.setVisibility(View.GONE);
        if (pageType == 0) {
            tv_title.setText("내 성격정보 결과");
        } else {
            tv_title.setText("성격정보 - " + type + "형");
        }


        tv_char_01.setText(personality1);
        tv_char_03.setText(personality1);
        tv_char_05.setText(personality1);

        tv_charText_01.setText(personality_title1);
        tv_charText_03.setText(name);
        tv_charText_05.setText(name);


        tv_charType_01.setText(name);
        tv_charType_03.setText(personality1);

        tv_charFact_01.setText(factor);


        layout_charTagArea_01.removeAllViews();
        layout_charTagArea_02.removeAllViews();
        String getColorString = "";
        if (type.length() > 1) {
            getColorString = type.substring(0, 1);
            linkCharString = type;

        } else {
            getColorString = type;
            linkCharString = type + type + "";
        }
        iv_topBg.setBackground(getDISCBackground(getColorString, 3));
        iv_topBg.setVisibility(View.VISIBLE);
        layout_topCharPanel.setBackground(getDISCBackground(getColorString, 0));
        layout_bottomCharPanel.setBackground(getDISCBackground(getColorString, 1));
        layout_panel.setBackground(getDISCBackground(getColorString, 5));
        tv_job.setTextColor(getDISCColor(personality1));
        tv_office.setTextColor(getDISCColor(personality1));
        tv_charInfoTitle.setTextColor(getDISCColor(personality1));
        btn_share.setBackground(getDISCBackground(personality1, 6));
        btn_share.setTextColor(getDISCColor(personality1));
        Drawable bgColor = getDISCBackground(getColorString, 4);
        if (!keyword1.equals("")) {
            String[] keyword1Arr = keyword1.split(",");
            linkKeyword1Arr = keyword1Arr;
            for (int i = 0; i < keyword1Arr.length; i++) {
                if (i < 4) {
                    ViewGroup keywordView = (ViewGroup) getLayoutInflater().inflate(R.layout.view_charinfo_keyword_item, null, false);
                    TextView tv_keyword = (TextView) keywordView.findViewById(R.id.tv_keyword);
                    tv_keyword.setBackground(bgColor);
                    tv_keyword.setText(keyword1Arr[i].trim());
                    layout_charTagArea_01.addView(keywordView);
                }

            }
        }
        if (!keyword2.equals("")) {
            String[] keyword2Arr = keyword2.split(",");
            for (int i = 0; i < keyword2Arr.length; i++) {
                if (i < 3) {
                    ViewGroup keywordView = (ViewGroup) getLayoutInflater().inflate(R.layout.view_charinfo_keyword_item, null, false);
                    TextView tv_keyword = (TextView) keywordView.findViewById(R.id.tv_keyword);
                    tv_keyword.setBackground(bgColor);
                    tv_keyword.setText(keyword2Arr[i].trim());
                    layout_charTagArea_02.addView(keywordView);
                }

            }
        }

        Spannable sb = new SpannableString(personality_char);
        for (int i = 0; i < personality_char.length(); i++) {
            char temp = (personality_char.charAt(i));
            switch (temp) {
                case '-':
                    if(personality1.equals("D")){
                        sb.setSpan(new ForegroundColorSpan(Constant.CHARACTER_D_COLOR), i, i + 1, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                    }else if(personality1.equals("I")){
                        sb.setSpan(new ForegroundColorSpan(Constant.CHARACTER_I_COLOR), i, i + 1, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                    }else if(personality1.equals("S")){
                        sb.setSpan(new ForegroundColorSpan(Constant.CHARACTER_S_COLOR), i, i + 1, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                    }else if(personality1.equals("C")){
                        sb.setSpan(new ForegroundColorSpan(Constant.CHARACTER_C_COLOR), i, i + 1, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                    }

                    break;

            }

        }
        tv_charInfo.setText(sb);

        tv_charDis.setText(weakness);
        tv_charAdvice.setText(advice);

        tv_jobEnvList_01.setText(job_env1);
        tv_jobList_01.setText(job1);

        if (personality2.equals("")) {

            tv_plus.setVisibility(View.GONE);
            layout_char_02.setVisibility(View.GONE);
            tv_char_02.setVisibility(View.GONE);
            view_jobLine.setVisibility(View.GONE);
            layout_jobList_02.setVisibility(View.GONE);
            layout_jobEnvList_02.setVisibility(View.GONE);
            layout_charTagArea_02.setVisibility(View.GONE);
            layout_charType_secondArea.setVisibility(View.GONE);


        } else {
            tv_plus.setVisibility(View.VISIBLE);
            layout_char_02.setVisibility(View.VISIBLE);
            view_jobLine.setVisibility(View.VISIBLE);
            layout_jobList_02.setVisibility(View.VISIBLE);
            layout_jobEnvList_02.setVisibility(View.VISIBLE);
            layout_charType_secondArea.setVisibility(View.VISIBLE);
            tv_char_02.setText(personality2);
            tv_char_04.setText(personality2);
            tv_char_06.setText(personality2);
            tv_charText_02.setText(personality_title2);
            tv_charText_04.setText(name2);
            tv_charText_06.setText(name2);
            tv_jobList_02.setText(job2);
            tv_jobEnvList_02.setText(job_env2);
            tv_charType_02.setText(name2);
            tv_charType_04.setText(personality2);
            layout_charTagArea_02.setVisibility(View.VISIBLE);

        }
        String[] personalityArr = {personality1, personality2};
        for (int ind = 0; ind < personalityArr.length; ind++) {
            int discColor;
            switch (personalityArr[ind]) {
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
            if (ind == 0) {
                tv_char_01.setTextColor(discColor);
                tv_charText_01.setTextColor(discColor);
                tv_char_03.setTextColor(discColor);
                tv_char_05.setTextColor(discColor);
            } else {
                tv_char_02.setTextColor(discColor);
                tv_charText_02.setTextColor(discColor);
                tv_char_04.setTextColor(discColor);
                tv_char_06.setTextColor(discColor);
            }
        }


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
                } else if (layout == 6) {
                    drawable = getResources().getDrawable(R.drawable.bg_rounded_share_d);
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
                } else if (layout == 6) {
                    drawable = getResources().getDrawable(R.drawable.bg_rounded_share_i);
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
                } else if (layout == 6) {
                    drawable = getResources().getDrawable(R.drawable.bg_rounded_share_c);
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
                } else if (layout == 6) {
                    drawable = getResources().getDrawable(R.drawable.bg_rounded_share_s);
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
