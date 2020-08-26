package com.character.microblogapp.ui.page.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.character.microblogapp.R;
import com.character.microblogapp.data.MyInfo;
import com.character.microblogapp.model.MBase;
import com.character.microblogapp.model.MError;
import com.character.microblogapp.net.Net;
import com.character.microblogapp.ui.page.BaseActivity;
import com.character.microblogapp.util.RegexUtil;
import com.character.microblogapp.util.Toaster;

//import org.cybergarage.util.StringUtil;

import butterknife.BindView;
import butterknife.OnClick;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;

public class ResetPwdActivity extends BaseActivity {

    @BindView(R.id.btnBack)
    ImageButton btnBack;

    @BindView(R.id.btnMenu)
    ImageButton btnMenu;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.etOldPassword)
    EditText etOldPassword;

    @BindView(R.id.etPassword)
    EditText etPassword;

    @BindView(R.id.etPasswordConfirm)
    EditText etPasswordConfirm;

    @BindView(R.id.tvOldPasswordError)
    TextView tvOldPasswordError;

    @BindView(R.id.tvPasswordError)
    TextView tvPasswordError;

    @BindView(R.id.tvPasswordConfirmError)
    TextView tvPasswordConfirmError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewDataBinding binding = DataBindingUtil.setContentView(this,R.layout.activity_reset_pwd);
    }

    @Override
    protected void initUI() {
        super.initUI();

        tvTitle.setText("비밀번호 변경");
        btnMenu.setVisibility(View.GONE);
        btnBack.setVisibility(View.VISIBLE);
        etOldPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    tvOldPasswordError.setVisibility(View.GONE);
                } else {
                    checkOldPassword();
                }
            }
        });

        etPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    tvPasswordError.setVisibility(View.GONE);
                } else {
                    checkPassword();
                }
            }
        });

        etPasswordConfirm.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    tvPasswordConfirmError.setVisibility(View.GONE);
                } else {
                    checkPasswordConfirm();
                }
            }
        });
    }

    @OnClick(R.id.btnBack)
    void onBack() {
        finish();
    }

    @OnClick(R.id.tvChange)
    void onChange() {
        apiChange();
    }

    void checkOldPassword() {
        boolean isValid = RegexUtil.validatePassword(etOldPassword.getText().toString());
        tvOldPasswordError.setVisibility(isValid ? View.GONE : View.VISIBLE);
        etOldPassword.setSelected(isValid);
    }

    void checkPassword() {
        boolean isValid = RegexUtil.validatePassword(etPassword.getText().toString());
        tvPasswordError.setVisibility(isValid ? View.GONE : View.VISIBLE);
        etPassword.setSelected(isValid);
    }

    void checkPasswordConfirm() {
        boolean isValid = etPassword.getText().toString().equals(etPasswordConfirm.getText().toString());
        tvPasswordConfirmError.setVisibility(isValid ? View.GONE : View.VISIBLE);
        etPasswordConfirm.setSelected(isValid);
    }

    void apiChange() {
        if (!etOldPassword.getText().toString().equals(MyInfo.getInstance().pwd)) {
            Toaster.showShort(ResetPwdActivity.this, "이전 비밀번호를 다시 확인해주세요.");
            return;
        }

        if (!etPassword.getText().toString().equals(etPasswordConfirm.getText().toString())) {
            Toaster.showShort(ResetPwdActivity.this, "비밀번호를 다시 확인해주세요.");
            return;
        }

        String old_pwd = etOldPassword.getText().toString();
        final String new_pwd = etPassword.getText().toString();

        showProgress(this);
        Net.instance().api.change_usr_pwd(MyInfo.getInstance().uid, old_pwd, new_pwd)
                .enqueue(new Net.ResponseCallBack<MBase>() {
                    @Override
                    public void onSuccess(MBase response) {
                        super.onSuccess(response);
                        hideProgress();

                        MyInfo.getInstance().pwd = new_pwd;
                        MyInfo.getInstance().save(ResetPwdActivity.this);
                        Toaster.showShort(ResetPwdActivity.this, "비밀번호가 변경되었습니다.");
                        finish();
                    }

                    @Override
                    public void onFailure(MError response) {
                        super.onFailure(response);
                        hideProgress();

                        if (response.resultcode == 500) {
                            networkErrorOccupied(response);
                        } else {
                            Toaster.showShort(ResetPwdActivity.this, "오류입니다.");
                        }
                    }
                });
    }

}
