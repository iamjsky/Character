package com.character.microblogapp.ui.page.intro;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.character.microblogapp.R;
import com.character.microblogapp.model.MError;
import com.character.microblogapp.model.MFindEmail;
import com.character.microblogapp.net.Net;
import com.character.microblogapp.ui.page.BaseActivity;
import com.character.microblogapp.util.Toaster;
import com.character.microblogapp.util.Util;

//import org.cybergarage.util.StringUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class FindIdActivity extends BaseActivity {

    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.etBirthday)
    EditText etBirthday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewDataBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_find_id);

        etName.addTextChangedListener(new MyTextWatcher(etName));
        etBirthday.addTextChangedListener(new MyTextWatcher(etBirthday));
    }

    @Override
    protected void initUI() {
        super.initUI();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GO_ACT_CERT_PHONE && resultCode == RESULT_OK) {
            String birthday = data.getStringExtra("birthday");

            Intent intent1 = new Intent(FindIdActivity.this, FindPwdActivity.class);
            intent1.putExtra("birthday", birthday);
            startActivity(intent1);
        }
    }

    @OnClick(R.id.btnPwd)
    void onPwd() {
        if (CERT_TEST) {
            startActivity(this, FindPwdActivity.class);
        } else {
            Intent intent1 = new Intent(FindIdActivity.this, CertActivity.class);
            startActivityForResult(intent1, GO_ACT_CERT_PHONE);
        }
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
        if (nickname.equals("")) {
            Toaster.showShort(this, R.string.input_name);
            return;
        }

        if (!Util.isNickname(nickname) || nickname.length() < 2 || nickname.length() > 10) {
            Toaster.showShort(this, "이름을 확인해주세요.");
            return;
        }
//        if (!StringUtil.hasData(birthday)) {
        if (birthday.equals("")) {
            Toaster.showShort(this, R.string.input_birthday);
            return;
        }

        if (birthday.length() != 8) {
            Toaster.showShort(this, "생년월일을 확인해주세요.");
            return;
        }

        showProgress(this);
        Net.instance().api.find_email(nickname, birthday)
                .enqueue(new Net.ResponseCallBack<MFindEmail>() {
                    @Override
                    public void onSuccess(MFindEmail response) {
                        super.onSuccess(response);
                        hideProgress();

                        String findMessage = String.format(getString(R.string.email_find_success_message), response.email);
                        new AlertDialog.Builder(FindIdActivity.this).setTitle(R.string.email_find_success).setMessage(findMessage).setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
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
                            new AlertDialog.Builder(FindIdActivity.this).setTitle(R.string.email_find_fail).setMessage(R.string.email_find_fail).setPositiveButton(R.string.confirm, null).show();
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
