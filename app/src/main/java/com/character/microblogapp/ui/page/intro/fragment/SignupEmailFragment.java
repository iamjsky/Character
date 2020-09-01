package com.character.microblogapp.ui.page.intro.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.character.microblogapp.R;
import com.character.microblogapp.data.Constant;
import com.character.microblogapp.data.MyInfo;
import com.character.microblogapp.model.MBase;
import com.character.microblogapp.model.MError;
import com.character.microblogapp.net.Net;
import com.character.microblogapp.ui.adapter.PopupSelectAdapter;
import com.character.microblogapp.ui.page.BaseFragment;
import com.character.microblogapp.ui.page.intro.SignupActivity;
import com.character.microblogapp.util.RegexUtil;
import com.character.microblogapp.util.Toaster;
import com.character.microblogapp.util.Util;

//import org.cybergarage.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.TEXT_ALIGNMENT_TEXT_START;

public class SignupEmailFragment extends BaseFragment {

    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.tvInvalideEmailError)
    TextView tvInvalideEmailError;

    PopupWindow pWindow;
    RecyclerView rvPopup;

    boolean emailConfirm = false;

    String m_sEmail = "";


    @BindView(R.id.tvMale)
    TextView tvMale;
    @BindView(R.id.tvFemale)
    TextView tvFemale;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.etPasswordConfirm)
    EditText etPasswordConfirm;
    @BindView(R.id.tvPasswordError)
    TextView tvPasswordError;
    @BindView(R.id.tvPasswordConfirmError)
    TextView tvPasswordConfirmError;
    @BindView(R.id.btnRegistering)
    Button btnRegistering;
    @BindView(R.id.layout_emailSignUpArea)
    LinearLayout layout_emailSignUpArea;

    public int signUpType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        createView(inflater, container, R.layout.fragment_signup_email);

        return mRoot;
    }

    public static SignupEmailFragment newInstance(String email) {
        SignupEmailFragment fragment = new SignupEmailFragment();
        fragment.m_sEmail = email;
        if (fragment.m_sEmail == null)
            fragment.m_sEmail = "";
        return fragment;
    }

    @Override
    protected void initUI() {
        super.initUI();

        initPopup();

        signUpType = MyInfo.getInstance().signUp_type;
        if(signUpType != 0){
            layout_emailSignUpArea.setVisibility(View.GONE);
        }else{
            layout_emailSignUpArea.setVisibility(View.VISIBLE);
        }
        etEmail.setText(m_sEmail);
//        etEmail.setFocusable(m_sEmail.equals(""));
//        etEmail.setSelected(!m_sEmail.equals(""));
        tvInvalideEmailError.setVisibility(m_sEmail.equals("") ? View.VISIBLE : View.INVISIBLE);

        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                emailConfirm = false;
                boolean isValid = RegexUtil.validateEmail(etEmail.getText().toString());
                tvInvalideEmailError.setVisibility(isValid ? View.INVISIBLE : View.VISIBLE);
                etEmail.setSelected(isValid);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    tvInvalideEmailError.setVisibility(View.INVISIBLE);
                }
            }
        });

        if(signUpType == 0) {
            etPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        tvPasswordError.setVisibility(View.INVISIBLE);
                    } else {
                        checkPassword();
                    }
                }
            });

            etPasswordConfirm.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        tvPasswordConfirmError.setVisibility(View.INVISIBLE);
                    } else {
                        checkPasswordConfirm();
                    }
                }
            });
            etPassword.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    checkPassword();
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            etPasswordConfirm.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    checkPasswordConfirm();
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            SignupActivity signupActivity = (SignupActivity) mParent;
            if (signupActivity == null || signupActivity.gender.isEmpty())
                return;


            if (signupActivity.gender.equals("male")) {
                tvMale.setSelected(true);
                tvFemale.setSelected(false);
            } else {
                tvMale.setSelected(false);
                tvFemale.setSelected(true);
            }


        }
    }

    @OnClick(R.id.btnRegistering)
    void onRegistering() {

//        if (!emailConfirm) {
//            tvInvalideEmailError.setText(R.string.email_duplicate_confirm);
//            tvInvalideEmailError.setVisibility(View.VISIBLE);
//            return;
//        }

        onDuplicateConfirm();
    }

    void initPopup() {
        LayoutInflater inflater = (LayoutInflater) mParent.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.popup_select, null);

        pWindow = new PopupWindow(layout);
        pWindow.setContentView(layout);
        pWindow.setWindowLayoutMode(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        pWindow.setOutsideTouchable(true);
        pWindow.setBackgroundDrawable(new BitmapDrawable());

        rvPopup = layout.findViewById(R.id.rvContent);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(rvPopup.getContext(), DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(getResources().getDrawable(R.drawable.item_divider));
        rvPopup.addItemDecoration(itemDecoration);
    }

    void goNext() {
        SignupActivity signupActivity = (SignupActivity) mParent;
        if (signupActivity == null)
            return;
        signupActivity.email = etEmail.getText().toString();

        //TODO: 본인인증 (이때 전화번호와 생년월일 정보를 가져온다)
//        signupActivity.phone = "123123213";
//        signupActivity.birthday = "19940502";



        signupActivity.showProfileRegisterFragment();




    }

    void onDuplicateConfirm() {
        String email = etEmail.getText().toString();

        //if (!StringUtil.hasData(email)) {
        if (email.isEmpty()) {
            tvInvalideEmailError.setText(R.string.input_email);
            tvInvalideEmailError.setVisibility(View.VISIBLE);
            return;
        }

        if (!RegexUtil.isValidEmail(email)) {
            tvInvalideEmailError.setText(R.string.email_invalid_message);
            tvInvalideEmailError.setVisibility(View.VISIBLE);
            return;
        }


        if(signUpType == 0) {
            if (tvMale.isSelected())
                ((SignupActivity) getActivity()).sex = GENDER.MALE;
            else if (tvFemale.isSelected())
                ((SignupActivity) getActivity()).sex = GENDER.FEMALE;
            else {
                Toaster.showShort(mParent, R.string.select_gender);
                return;
            }

            String password = etPassword.getText().toString();
            //if (!StringUtil.hasData(password)) {
            if (password.isEmpty()) {
                Toaster.showShort(mParent, R.string.input_password);
                return;
            }
            ((SignupActivity) getActivity()).password = password;

            checkPassword();
            checkPasswordConfirm();

            if (tvPasswordError.getVisibility() == View.VISIBLE
                    || tvPasswordConfirmError.getVisibility() == View.VISIBLE) {
                return;
            }
        }
        mParent.showProgress(mParent);
        Net.instance().api.check_email_dup(email)
                .enqueue(new Net.ResponseCallBack<MBase>() {
                    @Override
                    public void onSuccess(MBase response) {
                        super.onSuccess(response);
                        mParent.hideProgress();

//                        new AlertDialog.Builder(mParent).setTitle(R.string.email_duplicate).setMessage(R.string.email_identify_message).setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                emailConfirm = true;
//                                goNext();
//                            }
//                        }).show();

                        emailConfirm = true;
                        goNext();
                    }

                    @Override
                    public void onFailure(MError response) {
                        super.onFailure(response);

                        if (response.resultcode == 500) {
                            mParent.hideProgress();
                            mParent.networkErrorOccupied(response);
                        } else {
                            mParent.hideProgress();

                            new AlertDialog.Builder(mParent).setTitle(R.string.email_duplicate).setMessage(R.string.email_duplicate_message).setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    etEmail.setText("");
                                }
                            }).show();
                        }
                    }
                });
    }


    void checkPassword() {
        boolean isValid = RegexUtil.validatePassword(etPassword.getText().toString());
        tvPasswordError.setVisibility(isValid ? View.INVISIBLE : View.VISIBLE);
        etPassword.setSelected(isValid);
    }

    void checkPasswordConfirm() {
        boolean isValid = etPassword.getText().toString().equals(etPasswordConfirm.getText().toString());
        tvPasswordConfirmError.setVisibility(isValid ? View.INVISIBLE : View.VISIBLE);
        etPasswordConfirm.setSelected(isValid);
    }

    @OnClick({R.id.tvMale, R.id.tvFemale})
    void onGender(View view) {
        int id = view.getId();
        tvMale.setSelected(id == R.id.tvMale);
        tvFemale.setSelected(id == R.id.tvFemale);
        if(id == R.id.tvMale){
            tvMale.setBackgroundResource(R.drawable.bg_rounded_05);
            tvMale.setTextColor(getResources().getColor(R.color.color_white));
            tvFemale.setBackgroundResource(R.drawable.bg_rounded_06);
            tvFemale.setTextColor(getResources().getColor(R.color.color_char_gray));
        }else{
            tvMale.setBackgroundResource(R.drawable.bg_rounded_06);
            tvMale.setTextColor(getResources().getColor(R.color.color_char_gray));
            tvFemale.setBackgroundResource(R.drawable.bg_rounded_05);
            tvFemale.setTextColor(getResources().getColor(R.color.color_white));
        }
    }



}

