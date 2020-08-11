package com.character.microblogapp.ui.page.setting;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.character.microblogapp.R;
import com.character.microblogapp.data.MyInfo;
import com.character.microblogapp.model.MBase;
import com.character.microblogapp.model.MError;
import com.character.microblogapp.net.Net;
import com.character.microblogapp.ui.page.BaseActivity;
import com.character.microblogapp.ui.page.intro.LoginActivity;
import com.character.microblogapp.util.Toaster;

import butterknife.BindView;
import butterknife.OnClick;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;

public class QuitActivity extends BaseActivity {

    @BindView(R.id.btnBack)
    ImageButton btnBack;

    @BindView(R.id.btnMenu)
    ImageButton btnMenu;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.tvEmail)
    TextView tvEmail;

    @BindView(R.id.btnCheck)
    ImageButton btnCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewDataBinding binding = DataBindingUtil.setContentView(this,R.layout.activity_quit);
    }

    @Override
    protected void initUI() {
        super.initUI();

        tvTitle.setText("회원탈퇴");
        btnMenu.setVisibility(View.GONE);

        tvEmail.setText(MyInfo.getInstance().email);
    }

    @OnClick(R.id.btnBack)
    void onBack() {
        finish();
    }

    @OnClick({R.id.tvQuit})
    void onClick(View view) {
        if (btnCheck.isSelected()) {
            apiQuit();
        } else {
            Toaster.showShort(this, "탈퇴에 동의해주세요.");
        }
    }

    @OnClick({R.id.btnCheck})
    void onCheck(View view) {
        btnCheck.setSelected(!btnCheck.isSelected());
    }

    void apiQuit() {
        showProgress(this);
        Net.instance().api.signout(MyInfo.getInstance().uid)
                .enqueue(new Net.ResponseCallBack<MBase>() {
                    @Override
                    public void onSuccess(MBase response) {
                        super.onSuccess(response);
                        hideProgress();

                        _remainHandler.sendEmptyMessageDelayed(0, 1000);
                        }

                    @Override
                    public void onFailure(MError response) {
                        super.onFailure(response);
                        hideProgress();

                        if (response.resultcode == 500) {
                            networkErrorOccupied(response);
                        } else {
                            Toaster.showShort(QuitActivity.this, "오류입니다.");
                        }
                    }
                });
    }

    Handler _remainHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            ActivityCompat.finishAffinity(QuitActivity.this);
            System.runFinalizersOnExit(true);
            System.exit(0);

            return false;
        }
    });

}
