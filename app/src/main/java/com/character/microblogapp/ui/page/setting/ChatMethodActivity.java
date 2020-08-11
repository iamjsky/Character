package com.character.microblogapp.ui.page.setting;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.character.microblogapp.R;
import com.character.microblogapp.data.MyInfo;
import com.character.microblogapp.model.MEnergy;
import com.character.microblogapp.model.MError;
import com.character.microblogapp.model.MUserCharacterInfo;
import com.character.microblogapp.model.MVersion;
import com.character.microblogapp.net.Net;
import com.character.microblogapp.ui.dialog.ConfirmDialog2;
import com.character.microblogapp.ui.page.BaseActivity;
import com.character.microblogapp.ui.page.profile.DISCInfoDetailActivity;
import com.character.microblogapp.util.Toaster;

import butterknife.BindView;
import butterknife.OnClick;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
public class ChatMethodActivity extends BaseActivity {

    @BindView(R.id.btnBack)
    ImageButton btnBack;

    @BindView(R.id.btnMenu)
    ImageButton btnMenu;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.txv_type)
    TextView txvType;

    @BindView(R.id.txv_method)
    TextView txvMethod;

    @BindView(R.id.txv_skill)
    TextView txvSkill;

    @BindView(R.id.lly_order_info)
    LinearLayout llyOrderInfo;

    @BindView(R.id.lly_select_type)
    LinearLayout llySelectType;

    int m_nStep = 0;
    String m_strSelectType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewDataBinding binding = DataBindingUtil.setContentView(this,R.layout.activity_chat_method);

//        setFinishAppWhenPressedBackKey();
        int usr_uid = getIntent().getIntExtra("id", 0);
        getUserData(usr_uid);

        m_nStep = 0;

        llySelectType.setVisibility(View.VISIBLE);
        txvMethod.setVisibility(View.GONE);
    }

    @Override
    protected void initUI() {
        super.initUI();

//        tvTitle.setText("닉네임 님과의 대화법");
        btnMenu.setVisibility(View.GONE);
    }

    void getUserData(int user_id) {
        Net.instance().api.get_character_info(MyInfo.getInstance().uid, user_id)
                .enqueue(new Net.ResponseCallBack<MUserCharacterInfo>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSuccess(MUserCharacterInfo response) {
                        super.onSuccess(response);
                        hideProgress();
                        tvTitle.setText(response.nickname + "님과의 대화법");
                        txvType.setText(response.type);
//                        txvMethod.setText(response.method);
//                        txvSkill.setText(response.skill);
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

    private void getInfo() {
        showProgress(this);
        Net.instance().api.get_character_chat_method(m_strSelectType)
                .enqueue(new Net.ResponseCallBack<MVersion>() {
                    @Override
                    public void onSuccess(MVersion response) {
                        hideProgress();
                        if (response.info != null) {
                            String text = response.info;
                            txvMethod.setText(text);
                        }
                    }

                    @Override
                    public void onFailure(MError response) {
                        hideProgress();
                        Toaster.showShort(ChatMethodActivity.this, "등록된 데이터가 없습니다.");
                    }
                });
    }

    private void calcEnergy(int count) {
        Net.instance().api.calc_point(MyInfo.getInstance().uid, count, "대화법 확인하기")
                .enqueue(new Net.ResponseCallBack<MEnergy>() {
                    @Override
                    public void onSuccess(MEnergy response) {
                        super.onSuccess(response);
                        hideProgress();

                        MyInfo.getInstance().energy = response.energy;
                        m_nStep = 1;
                        llyOrderInfo.setVisibility(View.GONE);
                        llySelectType.setVisibility(View.GONE);
                        txvMethod.setVisibility(View.VISIBLE);
                        getInfo();
                    }

                    @Override
                    public void onFailure(MError response) {
                        super.onFailure(response);
                        hideProgress();
                        if (response.resultcode == 500) {
                            networkErrorOccupied(response);
                        } else {
                            new AlertDialog.Builder(ChatMethodActivity.this).setTitle("").setMessage(response.res_msg).setPositiveButton(R.string.confirm, null).show();
                        }
                    }
                });
    }

    @OnClick({R.id.tvD, R.id.tvI, R.id.tvC, R.id.tvS})
    void onDISC(View view) {
        if (view.getId() == R.id.tvD) {
            m_strSelectType = "D";
        } else if (view.getId() == R.id.tvI) {
            m_strSelectType = "I";
        } else if (view.getId() == R.id.tvC) {
            m_strSelectType = "C";
        } else if (view.getId() == R.id.tvS) {
            m_strSelectType = "S";
        }

        ConfirmDialog2.show(ChatMethodActivity.this, "대화법을 확인합니다.", "에너지 20개를 사용할까요?", new ConfirmDialog2.ActionListener() {
            @Override
            public void onOk() {

                if (MyInfo.getInstance().energy >= 20) {

                    calcEnergy(20);

                } else {

                    ConfirmDialog2.show(ChatMethodActivity.this, "에너지가 부족해요!!", String.format(getString(R.string.confirm_get_energy), (20 - MyInfo.getInstance().energy)),
                            new ConfirmDialog2.ActionListener() {
                                @Override
                                public void onOk() {
                                    Intent goEnergy = new Intent(ChatMethodActivity.this, EnergyActivity.class);
                                    goEnergy.putExtra("nickname", getIntent().getStringExtra("nickname"));
                                    startActivity(goEnergy);
                                }
                            });
                }
            }
        });

    }

    @OnClick(R.id.btnBack)
    void onBack() {
        if (m_nStep == 0) {
            finish();
        } else if(m_nStep == 1) {
            m_nStep = 0;
            llyOrderInfo.setVisibility(View.VISIBLE);
            llySelectType.setVisibility(View.VISIBLE);
            txvMethod.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        onBack();
    }
}
