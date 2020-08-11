package com.character.microblogapp.ui.page.profile;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.character.microblogapp.R;
import com.character.microblogapp.data.Constant;
import com.character.microblogapp.data.MyInfo;
import com.character.microblogapp.model.MBase;
import com.character.microblogapp.model.MEnergy;
import com.character.microblogapp.model.MError;
import com.character.microblogapp.model.MEstimateResult;
import com.character.microblogapp.model.MUser;
import com.character.microblogapp.net.Net;
import com.character.microblogapp.ui.dialog.ConfirmDialog2;
import com.character.microblogapp.ui.dialog.WarningDialog;
import com.character.microblogapp.ui.page.BaseActivity;
import com.character.microblogapp.ui.page.character.CharacterActivity;
import com.character.microblogapp.ui.page.setting.EnergyActivity;
import com.character.microblogapp.ui.page.setting.EnergyupActivity;
import com.character.microblogapp.ui.page.setting.dialog.BlameDialog;
import com.character.microblogapp.util.Util;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;

public class ProfileDISCActivity extends BaseActivity {

    @BindView(R.id.txv_top_nickname)
    TextView txvTopNickName;

    @BindView(R.id.txv_manner21)
    TextView txvManner21;

    @BindView(R.id.txv_manner22)
    TextView txvManner22;

    @BindView(R.id.btn_find_model)
    Button btnConfirm;

    @BindView(R.id.btn_job1)
    Button btnJob1;

    @BindView(R.id.btn_job2)
    Button btnJob2;

    @BindView(R.id.btn_character)
    Button btnCharacter;

    @BindView(R.id.txv_manner_desc2)
    TextView txvMannerDesc2;

    @BindView(R.id.txv_manner_text)
    TextView tvCharacterText;

    @BindView(R.id.rlt_info)
    RelativeLayout rlInfo;

    @BindView(R.id.llView)
    LinearLayout llView;

    @BindView(R.id.svDetail)
    ScrollView svDetail;

    int nType = 0; // 1 ? 유형별 직업정보 2 ? 유형별 선호 직업환경 3 ? 유형별 선호 성격

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewDataBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_profile_disc);

        initUI();
    }

    MUser user = null;

    @Override
    protected void initUI() {
        super.initUI();

        Intent intent = getIntent();
        int usr_uid = intent.getIntExtra("usr_uid", 0);

        if (usr_uid == 0) {
            finish();
        } else {
            getUserInfo(usr_uid);
        }

        btnConfirm.setText(MyInfo.getInstance().uid == usr_uid ? "테스트 다시하기(에너지 20개)" : "확인");

        btnJob1.setVisibility(MyInfo.getInstance().uid != usr_uid ? View.GONE : View.VISIBLE);
        btnJob2.setVisibility(MyInfo.getInstance().uid != usr_uid ? View.GONE : View.VISIBLE);
        btnCharacter.setVisibility(MyInfo.getInstance().uid != usr_uid ? View.GONE : View.VISIBLE);

        rlInfo.setVisibility(MyInfo.getInstance().uid != usr_uid ? View.GONE : View.VISIBLE);
    }

    void updateUI() {

        txvTopNickName.setText(user.info.nickname);  //닉네임

        String w_type = user.info.character;
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
                    sb.setSpan(new ForegroundColorSpan(CHARACTER_D_COLOR), ind, ind + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    break;
                case "I":
                case "i":
                    sb.setSpan(new ForegroundColorSpan(CHARACTER_I_COLOR), ind, ind + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    break;
                case "C":
                case "c":
                    sb.setSpan(new ForegroundColorSpan(CHARACTER_C_COLOR), ind, ind + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    break;
                case "S":
                case "s":
                    sb.setSpan(new ForegroundColorSpan(CHARACTER_S_COLOR), ind, ind + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    break;
            }
        }

        txvManner21.setText(sb);

        txvMannerDesc2.setText(user.info.ideal_character);
        tvCharacterText.setText(user.info.character_text);
    }

    private void getUserInfo(final int usr_uid) {

        showProgress(this);
        Net.instance().api.get_user_profile_info(MyInfo.getInstance().uid, usr_uid)
                .enqueue(new Net.ResponseCallBack<MUser>() {
                    @Override
                    public void onSuccess(MUser response) {
                        super.onSuccess(response);
                        hideProgress();

                        user = response;

                        updateUI();
                    }

                    @Override
                    public void onFailure(MError response) {
                        super.onFailure(response);
                        hideProgress();

                        if (response.resultcode == 500) {
                            networkErrorOccupied(response);

                            if (Constant.IS_UITEST) {
                                user = new MUser();
                                user.info.nickname = "빨간망토챠챠";
                                user.info.profile = new String[]{"http://192.168.56.1/Character/Source/Admin/temp/temp_item_1.png", "http://192.168.56.1/Character/Source/Admin/temp/temp_item_2.png"};
                                user.info.character = "DI";
                                user.info.ideal_character = "강한 주도형\n강한 사교형";

                                user.info.is_liked = 0;
                                user.info.intro = "설계는 침착하게, 개발은 여유있게, 검사는 깐깐하게,  투자는 화끈하게~!!";
                                user.info.job = "뷰티샵운영";
                                user.info.address = "부산";
                                user.info.body_type = "글래머";
                                user.info.school = "동아대";
                                user.info.major = "섬유미술";
                                user.info.height = 167;
                                user.info.religion = "무교";
                                user.info.drink = "가끔";
                                user.info.smoke = "비흡연";
                                user.info.character_text = "당신은 과정보다 결과를 중시하고 과감성을 가지고 있는 전형적인 리더타입";
                                user.info.character_director = "외향적";
                                user.info.character_express = "자기중심적, 경쟁적, 직선적";
                                user.info.character_reason = "목적, 성취";
                                user.info.interest = "공포영화, 매운 음식, 소맥, 산보다 바다, 집순이";
                                user.info.love_style = "비오는날 데이트, 스킨십자주, 낮져밤이, 빠른 진도, 톡보다 전화";
                                user.info.is_rated_byme = 0;
                                user.info.rate_byme = 0;

                                updateUI();
                            }
                        } else {
                            new AlertDialog.Builder(ProfileDISCActivity.this).setTitle("").setMessage(response.res_msg).setPositiveButton(R.string.confirm, null).show();
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
                        hideProgress();

                        MyInfo.getInstance().energy = response.energy;

                        Intent intent = new Intent(ProfileDISCActivity.this, CharacterActivity.class);
                        intent.putExtra("go", 1);
                        startActivity(intent);

                        finish();
                    }

                    @Override
                    public void onFailure(MError response) {
                        super.onFailure(response);
                        hideProgress();
                        if (response.resultcode == 500) {
                            networkErrorOccupied(response);
                        } else {
                            new AlertDialog.Builder(ProfileDISCActivity.this).setTitle("").setMessage(response.res_msg).setPositiveButton(R.string.confirm, null).show();
                        }
                    }
                });
    }

    @OnClick({R.id.tvD, R.id.tvI, R.id.tvC, R.id.tvS})
    void onDISC(View view) {
        Intent intent = new Intent(this, DISCDetailActivity.class);
        intent.putExtra("status", nType);
        if (view.getId() == R.id.tvD) {
            intent.putExtra("type", "D");
        } else if (view.getId() == R.id.tvI) {
            intent.putExtra("type", "I");
        } else if (view.getId() == R.id.tvC) {
            intent.putExtra("type", "C");
        } else if (view.getId() == R.id.tvS) {
            intent.putExtra("type", "S");
        }
        startActivity(intent);
    }

    @OnClick({R.id.btn_job1, R.id.btn_job2, R.id.btn_character, R.id.txv_manner21})
    void onView(View view) {
        svDetail.setVisibility(View.GONE);
        llView.setVisibility(View.GONE);
        nType = 0;

        if (view.getId() == R.id.btn_job1) {
            nType = 1;
            llView.setVisibility(View.VISIBLE);

            tvCharacterText.setText(user.info.character_content1);
        } else if (view.getId() == R.id.btn_job2) {
            nType = 2;
            llView.setVisibility(View.VISIBLE);

            tvCharacterText.setText(user.info.character_content2);
        } else if (view.getId() == R.id.btn_character) {
            nType = 3;
            llView.setVisibility(View.VISIBLE);

            tvCharacterText.setText(user.info.character_content3);
        } else {
            svDetail.setVisibility(View.VISIBLE);

            tvCharacterText.setText(user.info.character_text);
        }
    }

    @OnClick(R.id.rlt_back)
    void goBack() {
        finish();
    }

    @OnClick(R.id.rlt_info)
    void goInfo() {
        Intent intent = new Intent(this, ProfileDISCInfoActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_find_model)
    void onConfirm() {
        if (btnConfirm.getText().toString().equals("확인")) {
            finish();
        } else {
            ConfirmDialog2.show(ProfileDISCActivity.this, "테스트를 다시할래요?", "에너지 20개를 사용할까요?", new ConfirmDialog2.ActionListener() {
                @Override
                public void onOk() {

                    if (MyInfo.getInstance().energy >= 20) {
                        calcEnergy(20);
                    } else {

                        ConfirmDialog2.show(ProfileDISCActivity.this, "에너지가 부족해요!!", String.format(getString(R.string.confirm_get_energy), (20 - MyInfo.getInstance().energy)),
                                new ConfirmDialog2.ActionListener() {
                                    @Override
                                    public void onOk() {
                                        Intent goEnergy = new Intent(ProfileDISCActivity.this, EnergyActivity.class);
                                        goEnergy.putExtra("nickname", MyInfo.getInstance().nickname);
                                        startActivity(goEnergy);
                                    }
                                });
                    }
                }
            });
        }
    }
}
