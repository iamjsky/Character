package com.character.microblogapp.ui.page.setting;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.character.microblogapp.R;
import com.character.microblogapp.data.MyInfo;
import com.character.microblogapp.model.MBase;
import com.character.microblogapp.model.MError;
import com.character.microblogapp.net.Net;
import com.character.microblogapp.ui.page.BaseActivity;
import com.character.microblogapp.ui.dialog.ConfirmDialog;
import com.character.microblogapp.ui.dialog.WarningDialog;
import com.character.microblogapp.ui.page.intro.LoginActivity;
import com.character.microblogapp.ui.page.main.MainActivity;
import com.character.microblogapp.util.PrefMgr;
import com.character.microblogapp.util.Toaster;

import butterknife.BindView;
import butterknife.OnClick;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;

public class SettingActivity extends BaseActivity {

    @BindView(R.id.btnBack)
    ImageButton btnBack;

    @BindView(R.id.btnMenu)
    ImageButton btnMenu;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.rlSNS)
    RelativeLayout rlSns;

    @BindView(R.id.rlPass)
    RelativeLayout rlPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewDataBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_setting);
    }

    @Override
    protected void initUI() {
        super.initUI();

        tvTitle.setText("설정");
        btnMenu.setVisibility(View.GONE);

//        if (MyInfo.getInstance().sns_id == null || MyInfo.getInstance().sns_id.isEmpty()) {
            rlSns.setVisibility(View.GONE);
//        }

        if (MyInfo.getInstance().sns_id != null && !MyInfo.getInstance().sns_id.isEmpty()) {
            rlPass.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.btnBack)
    void onBack() {
        finish();
    }

    @OnClick({R.id.rlProfile, R.id.rlAlarm, R.id.rlSNS, R.id.rlBlock, R.id.rlService, R.id.rlNotice, R.id.rlMarketing, R.id.rlPosition, R.id.rlPass, R.id.rlConfirm, R.id.rlLogout, R.id.rlQuit})
    void onClick(View view) {
        if (view.getId() == R.id.rlProfile) {
            startActivity(this, ProfileChangeActivity.class);
        } else if (view.getId() == R.id.rlAlarm) {
            startActivity(this, AlarmActivity.class);
        } else if (view.getId() == R.id.rlSNS) {

        } else if (view.getId() == R.id.rlBlock) {
            startActivity(this, BlockActivity.class);
        } else if (view.getId() == R.id.rlService) {
            Intent intent = new Intent(SettingActivity.this, NoticeActivity.class);
            intent.putExtra("type", 1);
            startActivity(intent);
        } else if (view.getId() == R.id.rlNotice) {
            Intent intent = new Intent(SettingActivity.this, NoticeActivity.class);
            intent.putExtra("type", 2);
            startActivity(intent);
        } else if (view.getId() == R.id.rlMarketing) {
            Intent intent = new Intent(SettingActivity.this, NoticeActivity.class);
            intent.putExtra("type", 4);
            startActivity(intent);
        } else if (view.getId() == R.id.rlPosition) {
            Intent intent = new Intent(SettingActivity.this, NoticeActivity.class);
            intent.putExtra("type", 3);
            startActivity(intent);
        } else if (view.getId() == R.id.rlPass) {
//            new WarningDialog(this, "비밀번호를 변경하실수 없습니다.", new WarningDialog.ActionListener() {
//                @Override
//                public void onOk() {
//
//                }
//            }).show();
            Intent intent = new Intent(SettingActivity.this, ResetPwdActivity.class);
            startActivity(intent);
        } else if (view.getId() == R.id.rlConfirm) {
            startActivity(this, NoticeListActivity.class);
        } else if (view.getId() == R.id.rlLogout) {
            new ConfirmDialog(this, "로그아웃 하시겠습니까?", new ConfirmDialog.ActionListener() {
                @Override
                public void onOk() {

                    showProgress(SettingActivity.this);
                    Net.instance().api.logout(MyInfo.getInstance().uid)
                            .enqueue(new Net.ResponseCallBack<MBase>() {
                                @Override
                                public void onSuccess(MBase response) {
                                    super.onSuccess(response);
                                    hideProgress();
                                    MyInfo.getInstance().uid = 0;
                                    MyInfo.getInstance().pwd = "";
                                    MyInfo.getInstance().status = 0;
                                    MyInfo.getInstance().save(SettingActivity.this);

                                    SharedPreferences prefs = getSharedPreferences(PrefMgr.APP_PREFS,
                                            Context.MODE_PRIVATE);
                                    PrefMgr prefMgr = new PrefMgr(prefs);
                                    prefMgr.put(PrefMgr.WATCHED_PROFILE_ACCEPTED, false);

                                    _remainHandler.sendEmptyMessageDelayed(0, 1000);
                                }

                                @Override
                                public void onFailure(MError response) {
                                    super.onFailure(response);
                                    hideProgress();

                                    if (response.resultcode == 500) {
                                        networkErrorOccupied(response);
                                    } else {
                                        Toaster.showShort(SettingActivity.this, "오류입니다.");
                                    }
                                }
                            });

                }
            }).show();
        } else if (view.getId() == R.id.rlQuit) {
            startActivity(this, QuitActivity.class);
        }
    }

    Handler _remainHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            ActivityCompat.finishAffinity(SettingActivity.this);
            System.runFinalizersOnExit(true);
            System.exit(0);

            return false;
        }
    });
}
