package com.character.microblogapp.ui.page.character;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.character.microblogapp.R;
import com.character.microblogapp.data.Constant;
import com.character.microblogapp.data.MyInfo;
import com.character.microblogapp.model.MCharacterInfo;
import com.character.microblogapp.model.MError;
import com.character.microblogapp.model.MUser;
import com.character.microblogapp.net.Net;
import com.character.microblogapp.ui.page.BaseActivity;
import com.character.microblogapp.ui.page.intro.IntroActivity;
import com.character.microblogapp.ui.page.main.MainActivity;
import com.character.microblogapp.util.Toaster;
import com.kakao.kakaolink.v2.KakaoLinkResponse;
import com.kakao.kakaolink.v2.KakaoLinkService;
import com.kakao.message.template.ButtonObject;
import com.kakao.message.template.ContentObject;
import com.kakao.message.template.FeedTemplate;
import com.kakao.message.template.LinkObject;
import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;
import com.nex3z.flowlayout.FlowLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;

public class CharacterConfirmActivity extends BaseActivity {

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
    String character = "";

    @BindView(R.id.iv_charImg_01)
    ImageView iv_charImg_01;
    @BindView(R.id.iv_charImg_02)
    ImageView iv_charImg_02;

    int go = 0;
    int nowPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewDataBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_character_confirm);

        character = getIntent().getStringExtra("result");
        saveCharacterInfo();

        go = getIntent().getIntExtra("go", 0);
    }

    @Override
    protected void initUI() {
        super.initUI();
    }

    @OnClick(R.id.btn_again_test)
    void showProfilePage() {
        if (go == 1) {
            apiInfo();
        } else {
            doLogin();
        }
    }

    @Override
    public void onBackPressed() {
        goBack();
    }

    @OnClick(R.id.rlt_back)
    void goBack() {
        switch (nowPage){

            case 0:
                showProfilePage();
                break;

            default:
                setUI();
                break;
        }

    }

//    @OnClick(R.id.tv_style)
//    void findStyle() {
//        if (result == null) {
//            return;
//        }
//
//        txv_desc.setText(String.format("%s\n%s\n%s", result.gender1, result.gender2, result.gender3));
//    }


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

    @OnClick(R.id.btn_share)
    void btn_shareClicked() {
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);

        intent.setType("text/plain");


        String text = "https://play.google.com/store/apps/details?id=com.character.dating";

        intent.putExtra(Intent.EXTRA_TEXT, text);


        Intent chooser = Intent.createChooser(intent, "DISC 성격 테스트 결과");
        startActivity(chooser);


    }

    MCharacterInfo.Info result = null;

    void saveCharacterInfo() {
        showProgress(this);
        Net.instance().api.save_character_info(MyInfo.getInstance().uid, character)
                .enqueue(new Net.ResponseCallBack<MCharacterInfo>() {
                    @Override
                    public void onSuccess(MCharacterInfo response) {
                        hideProgress();
                        result = response.character;
                        MyInfo.getInstance().result = result;
                        Log.e("char_debug", "result : " + result);
                        setUI();
                    }

                    @Override
                    public void onFailure(MError response) {
                        hideProgress();
                        Toaster.showShort(CharacterConfirmActivity.this, "등록된 데이터가 없습니다.");
                    }
                });
    }


    void setUI() {
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

        layout_discResult.setVisibility(View.VISIBLE);
        layout_job.setVisibility(View.GONE);
        layout_jobEnv.setVisibility(View.GONE);
        tv_title.setText("DISC 성격테스트 결과");


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
        if(!keyword1.equals("")) {
            String[] keyword1Arr = keyword1.split(",");
            for (int i = 0; i < keyword1Arr.length; i++) {
                ViewGroup keywordView = (ViewGroup) getLayoutInflater().inflate(R.layout.view_charinfo_keyword_item, null, false);
                TextView tv_keyword = (TextView) keywordView.findViewById(R.id.tv_keyword);
                tv_keyword.setBackgroundResource(R.drawable.bg_rounded_char_01);
                tv_keyword.setText(keyword1Arr[i].trim());
                layout_charTagArea_01.addView(keywordView);
            }
        }
        if(!keyword2.equals("")){
            String[]  keyword2Arr = keyword2.split(",");
            for (int i=0; i < keyword2Arr.length; i++){
                ViewGroup keywordView = (ViewGroup) getLayoutInflater().inflate(R.layout.view_charinfo_keyword_item, null, false);
                TextView tv_keyword = (TextView) keywordView.findViewById(R.id.tv_keyword);
                tv_keyword.setBackgroundResource(R.drawable.bg_rounded_char_02);
                tv_keyword.setText(keyword2Arr[i].trim());
                layout_charTagArea_02.addView(keywordView);
            }
        }




        tv_charInfo.setText(personality_char);
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
            layout_charType_secondArea.setVisibility(View.VISIBLE);

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

    void doLogin() {

        String email = MyInfo.getInstance().email;
        String password = MyInfo.getInstance().pwd;
        int loginType = MyInfo.getInstance().login_type;
        if (loginType != LOGIN_EMAIL)
            email = MyInfo.getInstance().sns_id;

        showProgress(this);
        Net.instance().api.login(email, password, loginType, MyInfo.getInstance().fcm_token)
                .enqueue(new Net.ResponseCallBack<MUser>() {
                    @Override
                    public void onSuccess(MUser response) {
                        super.onSuccess(response);
                        hideProgress();

                        MyInfo.getInstance().uid = response.info.uid;
                        MyInfo.getInstance().transformData(response);

                        Intent it = new Intent(CharacterConfirmActivity.this, MainActivity.class);
                        it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(it);
                        finish();
                    }

                    @Override
                    public void onFailure(MError response) {
                        super.onFailure(response);
                        hideProgress();

                        startActivity(CharacterConfirmActivity.this, IntroActivity.class);
                        finish();
                    }
                });
    }

    void apiInfo() {
        Net.instance().api.get_my_profile_info(MyInfo.getInstance().uid)
                .enqueue(new Net.ResponseCallBack<MUser>() {
                    @Override
                    public void onSuccess(MUser response) {
                        super.onSuccess(response);
                        hideProgress();

                        MyInfo.getInstance().status = response.info.status;
                        MyInfo.getInstance().uid = response.info.uid;
                        MyInfo.getInstance().transformData(response);

                        finish();
                    }

                    @Override
                    public void onFailure(MError response) {
                        super.onFailure(response);
                        hideProgress();

                        if (response.resultcode == 500) {
                            networkErrorOccupied(response);
                        } else {
                            Toaster.showShort(CharacterConfirmActivity.this, "오류입니다.");
                        }
                    }
                });
    }

}
