package com.character.microblogapp.ui.page;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.blankj.utilcode.util.ToastUtils;
import com.character.microblogapp.MyApplication;
import com.character.microblogapp.R;
import com.character.microblogapp.data.Constant;
import com.character.microblogapp.model.MError;
import com.character.microblogapp.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BaseFragment extends Fragment implements Constant {

    private static final String TAG = BaseFragment.class.getSimpleName();

    @Nullable
    @BindView(R.id.containerView)
    ViewGroup container_view;

    protected View mRoot;
    protected MyApplication mApp = null;
    protected BaseActivity mParent;

    public void createView(LayoutInflater layoutInflater, ViewGroup viewGroup, int layoutId) {

        ViewDataBinding binding = DataBindingUtil.inflate(layoutInflater, layoutId, viewGroup, false);
        mRoot = binding.getRoot();
        mParent = (BaseActivity) getActivity();
        mApp = (MyApplication) getActivity().getApplicationContext();
//
        ButterKnife.bind(this, mRoot);

        initUI();

        if (container_view != null) {
            setupUI(container_view);
        }
    }

    protected void initUI() {

    }

    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(mParent);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(
                Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            try {
                inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void onFragmentActivated() {

    }

    public void networkErrorOccupied(MError error) {
        if (error.resultcode == 500) {
            if (Util.isNetworkAvaliable(getContext())) {
                ToastUtils.showShort(R.string.error_unknown);
            } else {
                ToastUtils.showShort(R.string.error_unknown_token);
            }
        } else {
            ToastUtils.showShort(R.string.data_parse_error);
        }
    }
}
