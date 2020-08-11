package com.character.microblogapp.ui.page.character;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
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

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
public class CharacterConfirmActivity extends BaseActivity {

    String character = "";

    int go = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewDataBinding binding = DataBindingUtil.setContentView(this,R.layout.activity_character_confirm);

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

    @OnClick(R.id.rlt_back)
    void goBack() {
        finish();
    }

    @OnClick(R.id.tv_style)
    void findStyle() {
        if (result == null) {
            return;
        }

        txv_desc.setText(String.format("%s\n%s\n%s", result.gender1, result.gender2, result.gender3));
    }

    @OnClick(R.id.tv_job)
    void findJob() {
        if (result == null) {
            return;
        }

        txv_desc.setText(String.format("%s\n%s\n%s", result.job1, result.job2, result.job3));
    }

    @OnClick(R.id.tv_office)
    void findOffice() {
        if (result == null) {
            return;
        }

        txv_desc.setText(String.format("%s", result.job_env));
    }

    @BindView(R.id.txv_character)
    TextView txv_character;
    @BindView(R.id.txv_character_ko)
    TextView txv_character_ko;
    @BindView(R.id.txv_desc)
    TextView txv_desc;

    MCharacterInfo.Info result = null;

    void saveCharacterInfo() {
        showProgress(this);
        Net.instance().api.save_character_info(MyInfo.getInstance().uid, character)
                .enqueue(new Net.ResponseCallBack<MCharacterInfo>() {
                    @Override
                    public void onSuccess(MCharacterInfo response) {
                        hideProgress();
                        result = response.character;
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

        String w_type = result.type;
        if(w_type.equals("D=")){
            w_type = "D";
        } else if(w_type.equals("I=")){
            w_type = "I";
        } else if(w_type.equals("S=")){
            w_type = "S";
        } else if(w_type.equals("C=")){
            w_type = "C";
        }

        Spannable sb = new SpannableString(w_type);
        ArrayList<String> summary = new ArrayList<>();

        for (int ind = 0; ind < w_type.length(); ind++) {
            String item = w_type.substring(ind, ind + 1);
            switch (item) {
                case "D":
                case "d":
                    sb.setSpan(new ForegroundColorSpan(getColor(R.color.color_d)), ind, ind + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    summary.add(getString(R.string.character_d));
                    break;
                case "I":
                case "i":
                    sb.setSpan(new ForegroundColorSpan(getColor(R.color.color_i)), ind, ind + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    summary.add(getString(R.string.character_i));
                    break;
                case "C":
                case "c":
                    sb.setSpan(new ForegroundColorSpan(getColor(R.color.color_c)), ind, ind + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    summary.add(getString(R.string.character_c));
                    break;
                case "S":
                case "s":
                    sb.setSpan(new ForegroundColorSpan(getColor(R.color.color_s)), ind, ind + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    summary.add(getString(R.string.character_s));
                    break;
            }
        }

        txv_character.setText(sb);
//        txv_character_ko.setText(TextUtils.join("\n", summary));
        txv_character_ko.setText(result.name);
        txv_desc.setText(result.desc);
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
