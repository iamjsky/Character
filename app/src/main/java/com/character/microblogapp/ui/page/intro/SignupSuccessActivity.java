package com.character.microblogapp.ui.page.intro;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;

import com.character.microblogapp.R;
import com.character.microblogapp.ui.page.BaseActivity;
import com.character.microblogapp.ui.page.character.CharacterActivity;
import com.character.microblogapp.ui.page.main.MainActivity;

import butterknife.OnClick;

public class SignupSuccessActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewDataBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_signup_success);
    }

    @Override
    protected void initUI() {
        super.initUI();

        setFinishAppWhenPressedBackKey();
    }

    @OnClick(R.id.btnStart)
    void onMainStart() {
        Intent intent = new Intent(
                this, CharacterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
