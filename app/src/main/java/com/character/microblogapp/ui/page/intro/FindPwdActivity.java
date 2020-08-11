package com.character.microblogapp.ui.page.intro;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.character.microblogapp.R;
import com.character.microblogapp.model.MError;
import com.character.microblogapp.model.MFindEmail;
import com.character.microblogapp.model.MFindPwd;
import com.character.microblogapp.net.Net;
import com.character.microblogapp.ui.page.BaseActivity;
import com.character.microblogapp.util.Toaster;
import com.character.microblogapp.util.Util;

//import org.cybergarage.util.StringUtil;

import butterknife.BindView;
import butterknife.OnClick;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.widget.TextView;

public class FindPwdActivity extends BaseActivity {

    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.etBirthday)
    EditText etBirthday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewDataBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_find_pwd);

        String strBirthday = getIntent().getStringExtra("birthday");
        if (strBirthday == null)
            strBirthday = "";
        if (!strBirthday.isEmpty()) {
            strBirthday = strBirthday.replace("-", "");
        }
        etBirthday.setText(strBirthday);
        etName.addTextChangedListener(new MyTextWatcher(etName));
        etBirthday.addTextChangedListener(new MyTextWatcher(etBirthday));

        if (!strBirthday.isEmpty()) {
            etBirthday.setSelected(true);
        }
    }

    @Override
    protected void initUI() {
        super.initUI();
    }

    @OnClick(R.id.rlt_back)
    void onBack() {
        onBackPressed();
    }

    @OnClick(R.id.btnFind)
    void onFind() {
        String nickname = etName.getText().toString();
        String birthday = etBirthday.getText().toString();

        //if (!StringUtil.hasData(nickname)) {
        if (nickname.isEmpty()) {
            Toaster.showShort(this, R.string.input_name);
            return;
        }

        if (!Util.isNickname(nickname) || nickname.length() < 2 || nickname.length() > 10) {
            Toaster.showShort(this, "이름을 확인해주세요.");
            return;
        }
        //if (!StringUtil.hasData(birthday)) {
        if (birthday.isEmpty()) {
            Toaster.showShort(this, R.string.input_birthday);
            return;
        }

        if (birthday.length() != 8) {
            Toaster.showShort(this, "생년월일을 확인해주세요.");
            return;
        }

        showProgress(this);
        Net.instance().api.find_pwd(nickname, birthday)
                .enqueue(new Net.ResponseCallBack<MFindPwd>() {
                    @Override
                    public void onSuccess(MFindPwd response) {
                        super.onSuccess(response);
                        hideProgress();

                        String findMessage = String.format(getString(R.string.pwd_find_success_message), response.email);
                        new AlertDialog.Builder(FindPwdActivity.this).setTitle(R.string.pwd_find_success).setMessage(findMessage).setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        }).show();
                    }

                    @Override
                    public void onFailure(MError response) {
                        super.onFailure(response);
                        hideProgress();

                        if (response.resultcode == 500) {
                            networkErrorOccupied(response);
                        } else {
                            new AlertDialog.Builder(FindPwdActivity.this).setTitle(R.string.pwd_find_fail).setMessage(R.string.pwd_find_fail).setPositiveButton(R.string.confirm, null).show();
                        }
                    }
                });
    }

    class MyTextWatcher implements TextWatcher {

        TextView textView;

        public MyTextWatcher(TextView textView) {
            this.textView = textView;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String string = textView.getText().toString();
//            textView.setSelected(StringUtil.hasData(string));
            if (!string.isEmpty())
                textView.setSelected(true);
            else
                textView.setSelected(false);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
