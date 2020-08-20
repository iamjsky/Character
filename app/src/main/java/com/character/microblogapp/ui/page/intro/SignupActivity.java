package com.character.microblogapp.ui.page.intro;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.character.microblogapp.R;
import com.character.microblogapp.data.MyInfo;
import com.character.microblogapp.ui.page.BaseActivity;
import com.character.microblogapp.ui.page.BaseFragment;
import com.character.microblogapp.ui.page.intro.fragment.SignupAccountCreateFragment;
import com.character.microblogapp.ui.page.intro.fragment.SignupEmailFragment;
import com.character.microblogapp.ui.page.intro.fragment.SignupProfileRegisterFragment;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;

public class SignupActivity extends BaseActivity {





    public GENDER sex = GENDER.MALE;
    public int loginType;
    public String email;
    public String sns_id = "";
    public String emailType = "";
    public String phone;
    public String realname = "";
    public String certname = "";
    public String birthday;
    public int birth = 28;
    public String gender = "";
    public String password = "111111";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewDataBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_signup);
    }

    @Override
    protected void initUI() {
        super.initUI();
        getSupportFragmentManager().addOnBackStackChangedListener(getListener());

        loginType = getIntent().getIntExtra("login_type", LOGIN_EMAIL);
        if (loginType != LOGIN_EMAIL) {
            email = getIntent().getStringExtra("email");
            sns_id = getIntent().getStringExtra("sns_id");
            realname = getIntent().getStringExtra("sns_name");
            birthday = getIntent().getStringExtra("birthday");
            String tempBirth = getIntent().getStringExtra("birth");
            if (tempBirth != null && !tempBirth.equals("")) {
                birth = Integer.parseInt(tempBirth);
            }

            String tempGender = getIntent().getStringExtra("gender");
            if (tempGender != null && !tempGender.equals("")) {
                gender = tempGender;
            }

            String tempPhone = getIntent().getStringExtra("phone");
            if (tempPhone != null && !tempPhone.equals("")) {
                phone = tempPhone;
            }

            String tempName = getIntent().getStringExtra("name");
            if (tempName != null && !tempName.equals("")) {
                certname = tempName;
            }
        } else {
            birthday = getIntent().getStringExtra("birthday");
            String tempBirth = getIntent().getStringExtra("birth");
            if (tempBirth != null && !tempBirth.equals("")) {
                birth = Integer.parseInt(tempBirth);
            }

            String tempGender = getIntent().getStringExtra("gender");
            if (tempGender != null && !tempGender.equals("")) {
                gender = tempGender;
            }

            String tempPhone = getIntent().getStringExtra("phone");
            if (tempPhone != null && !tempPhone.equals("")) {
                phone = tempPhone;
            }

            String tempName = getIntent().getStringExtra("name");
            if (tempName != null && !tempName.equals("")) {
                certname = tempName;
            }
        }

        showEmailFragment();
    }

    @OnClick(R.id.btnBack)
    void onBack() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        //. 자식화면이 보인다면 back stack
        FragmentManager manager = getSupportFragmentManager();
        if (manager.getBackStackEntryCount() <= 1) {
            new AlertDialog.Builder(this).setTitle(R.string.signup_cancel).setMessage(R.string.signup_cancel_message).setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    getSupportFragmentManager().popBackStack();
                }
            }).setNegativeButton(R.string.cancel, null).show();
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }

    private FragmentManager.OnBackStackChangedListener getListener() {
        FragmentManager.OnBackStackChangedListener result = new FragmentManager.OnBackStackChangedListener() {
            public void onBackStackChanged() {
                FragmentManager manager = getSupportFragmentManager();
                if (manager != null) {
                    int backStackEntryCount = manager.getBackStackEntryCount();
                    if (backStackEntryCount == 0) {
                        finish();
                        return;
                    }


                }
            }
        };
        return result;
    }

    public void showEmailFragment() {
        showFragment(SignupEmailFragment.newInstance(email));
    }


    public void showProfileRegisterFragment() {
        showFragment(SignupProfileRegisterFragment.newInstance());
    }

    public void showSuccess() {
        MyInfo.getInstance().sns_id = sns_id;
        Intent intent = new Intent(
                this, SignupSuccessActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void setStep(int step) {




    }

    private void showFragment(BaseFragment p_fragment) {
        FragmentTransaction w_ft = getSupportFragmentManager().beginTransaction();
        w_ft.add(R.id.flContainer, p_fragment);
        w_ft.addToBackStack(null);
        w_ft.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        FragmentManager manager = getSupportFragmentManager();
        if (manager != null) {
            for (Fragment fragment : manager.getFragments()) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }
}
