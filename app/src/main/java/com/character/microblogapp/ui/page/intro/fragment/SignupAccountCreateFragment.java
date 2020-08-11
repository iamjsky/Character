package com.character.microblogapp.ui.page.intro.fragment;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.character.microblogapp.R;
import com.character.microblogapp.ui.page.BaseFragment;
import com.character.microblogapp.ui.page.intro.SignupActivity;
import com.character.microblogapp.util.RegexUtil;
import com.character.microblogapp.util.Toaster;

import butterknife.BindView;
import butterknife.OnClick;

//import org.cybergarage.util.StringUtil;

public class SignupAccountCreateFragment extends BaseFragment {

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

    PopupWindow pWindow;
    RecyclerView rvPopup;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        createView(inflater, container, R.layout.fragment_signup_account_create);

        return mRoot;
    }

    public static SignupAccountCreateFragment newInstance() {
        return new SignupAccountCreateFragment();
    }

    @Override
    protected void initUI() {
        super.initUI();

        initPopup();

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

        tvMale.setOnClickListener(null);
        tvFemale.setOnClickListener(null);
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

    @OnClick({R.id.tvMale, R.id.tvFemale})
    void onGender(View view) {
        int id = view.getId();
        tvMale.setSelected(id == R.id.tvMale);
        tvFemale.setSelected(id == R.id.tvFemale);
    }
//
//    @OnClick(R.id.btnDirectInput)
//    void onEmailType(final Button view) {
//        view.setSelected(true);
//        view.setEnabled(false);
//
//        pWindow.showAsDropDown(view, 0, 0);
//
//        ArrayList<String> strings = new ArrayList<>();
//        Collections.addAll(strings, email_type);
//
//        pWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                view.setSelected(false);
//                view.setEnabled(true);
//            }
//        });
//        rvPopup.setAdapter(new PopupSelectAdapter(mParent, strings, view.getText().toString(), TEXT_ALIGNMENT_TEXT_START, new PopupSelectAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(String title, int position) {
//
//                SignupActivity signupActivity = (SignupActivity) mParent;
//                if (signupActivity == null)
//                    return;
//
//                if (position == 0) {
//                    signupActivity.emailType = "";
//                } else {
//                    signupActivity.emailType = "";
//                }
//
//                view.setText(title);
//                pWindow.dismiss();
//            }
//        }).setItemWidth(Util.dp2px(mParent, 100)));
//    }

    @OnClick(R.id.btnRegistering)
    void onRegistering() {
        SignupActivity signupActivity = (SignupActivity) mParent;
        if (signupActivity == null)
            return;

        if (tvMale.isSelected())
            signupActivity.sex = GENDER.MALE;
        else if (tvFemale.isSelected())
            signupActivity.sex = GENDER.FEMALE;
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
        signupActivity.password = password;

        checkPassword();
        checkPasswordConfirm();

        if (tvPasswordError.getVisibility() == View.VISIBLE
                || tvPasswordConfirmError.getVisibility() == View.VISIBLE) {
            return;
        }

        //TODO: 본인인증 (이때 전화번호와 생년월일 정보를 가져온다)
//        signupActivity.phone = "123123213";
//        signupActivity.birthday = "19940502";

        signupActivity.showProfileRegisterFragment();
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
}

