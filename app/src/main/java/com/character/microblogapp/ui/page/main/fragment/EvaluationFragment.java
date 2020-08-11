package com.character.microblogapp.ui.page.main.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.character.microblogapp.GlideApp;
import com.character.microblogapp.MyApplication;
import com.character.microblogapp.R;
import com.character.microblogapp.data.BaseUserInfo;
import com.character.microblogapp.data.MyInfo;
import com.character.microblogapp.model.MBase;
import com.character.microblogapp.model.MCurrentEstimateUser;
import com.character.microblogapp.model.MError;
import com.character.microblogapp.model.MEstimateResult;
import com.character.microblogapp.model.MUserList;
import com.character.microblogapp.net.Net;
import com.character.microblogapp.ui.dialog.WarningDialog;
import com.character.microblogapp.ui.page.BaseFragment;
import com.character.microblogapp.ui.page.main.MainActivity;
import com.character.microblogapp.ui.page.model.ModelFindActivity;
import com.character.microblogapp.ui.page.profile.ProfileActivity;
import com.character.microblogapp.ui.page.setting.dialog.BlameDialog;
import com.character.microblogapp.util.Util;
import com.viewpagerindicator.CirclePageIndicator;
import com.willy.ratingbar.BaseRatingBar;
import com.willy.ratingbar.ScaleRatingBar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class EvaluationFragment extends BaseFragment {

    @BindView(R.id.vpProfiles)
    ViewPager vpProfiles;

    @BindView(R.id.pageIndicator)
    CirclePageIndicator pageIndicator;

    @BindView(R.id.txv_manner1)
    TextView txvManner1;

    @BindView(R.id.txv_manner2)
    TextView txvManner2;

    @BindView(R.id.txv_manner_desc1)
    TextView txvMannerDesc1;

    @BindView(R.id.cb_heart0)
    CheckBox cbHeart0;

    @BindView(R.id.cb_heart1)
    CheckBox cbHeart1;

    @BindView(R.id.cb_heart2)
    CheckBox cbHeart2;

    @BindView(R.id.cb_heart3)
    CheckBox cbHeart3;

    @BindView(R.id.cb_heart4)
    CheckBox cbHeart4;

    @BindView(R.id.cl_progress)
    RelativeLayout clProgress;

    @BindView(R.id.tv_nickname)
    TextView tvNickname;

    @BindView(R.id.tv_address)
    TextView tvAddress;

    @BindView(R.id.tv_job)
    TextView tv_job;

    @BindView(R.id.tv_message)
    TextView tvMessage;

    @BindView(R.id.tv_body_type)
    TextView tvBody;

    @BindView(R.id.tv_status)
    TextView tvStatus;

    @BindView(R.id.rate)
    ScaleRatingBar ratingBar;

    @BindView(R.id.ll_main)
    LinearLayout llMainEstimate;

    @BindView(R.id.rly_empty_user)
    RelativeLayout clEmptyUser;

    MyApplication mApp = null;
    ImagePagerAdapter mUsersAdapter = null;
    BaseUserInfo mEstimateUser = null;
    boolean is_overflow_oneday_cnt = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        createView(inflater, container, R.layout.fragment_eval);
        initData();
        return mRoot;
    }

    @Override
    protected void initUI() {
        mUsersAdapter = new ImagePagerAdapter();
        vpProfiles.setAdapter(mUsersAdapter);
        pageIndicator.setViewPager(vpProfiles);

        initStar();
        setStar();
    }

    private void setStar() {

        ratingBar.setRating(MyInfo.getInstance().star_value);
        ratingBar.setOnRatingChangeListener(new ScaleRatingBar.OnRatingChangeListener() {

            @Override
            public void onRatingChange(BaseRatingBar ratingBar, float rating) {
                MyInfo.getInstance().star_value = (int)rating;
                mark = MyInfo.getInstance().star_value;
                initStar();

                if (mark != 0) {
                    showProgress();
                    _remainHandler.sendEmptyMessageDelayed(0, 1000);
                }
            }
        });
    }

    private void initStar() {
        String value = String.format("%.1f", Float.valueOf(MyInfo.getInstance().star_value)) + "\n\n";

        switch (MyInfo.getInstance().star_value) {
            case 1:
                tvStatus.setText(value + getString(R.string.estimate_user_point_1));
                break;
            case 2:
                tvStatus.setText(value + getString(R.string.estimate_user_point_2));
                break;
            case 3:
                tvStatus.setText(value + getString(R.string.estimate_user_point_3));
                break;
            case 4:
                tvStatus.setText(value + getString(R.string.estimate_user_point_4));
                break;
            case 5:
                tvStatus.setText(value + getString(R.string.estimate_user_point_5));
                break;
        }
        mark = MyInfo.getInstance().star_value;

        tvStatus.setVisibility(MyInfo.getInstance().star_value != 0 ? View.VISIBLE : View.INVISIBLE);
    }

    private void initData() {
        mApp = (MyApplication) getActivity().getApplicationContext();
        loadCurrentEstimateUser();
    }

    private void showEstimateUser() {
        clEmptyUser.setVisibility(mEstimateUser == null ? View.VISIBLE : View.GONE);
        if (mEstimateUser == null) return;
        llMainEstimate.setVisibility(View.VISIBLE);
//        tvStatus.setVisibility(View.INVISIBLE);
        clearScore();

        mUsersAdapter = new ImagePagerAdapter();
        vpProfiles.setAdapter(mUsersAdapter);
        pageIndicator.setViewPager(vpProfiles);

        mUsersAdapter.mProfileUrls.clear();
        mUsersAdapter.mProfileUrls.addAll(Arrays.asList(mEstimateUser.profile));

        mUsersAdapter.notifyDataSetChanged();
        vpProfiles.setCurrentItem(0);

        tvMessage.setText(mEstimateUser.intro);
        tvMessage.setVisibility(View.GONE);
        tvNickname.setText(mEstimateUser.nickname);
        tvAddress.setText(mEstimateUser.address + " | "); //  지역
        if (mEstimateUser.job != null && !mEstimateUser.job.isEmpty()) { // 직업
            tv_job.setText(mEstimateUser.job  + " | ");
        }

        if (!(mEstimateUser.body_type + "").isEmpty()) { // 체형
            tvBody.setText(" " + mEstimateUser.body_type);
        }
//        if (mEstimateUser.school != null && !mEstimateUser.school.isEmpty()) { // 학교
//            tv_school.setText(" " + getString(R.string.dot_middle) + "  " +  mEstimateUser.school);
//        }

        String w_type = mEstimateUser.character;
        if(w_type.equals("D=")){
            w_type = "D";
        } else if(w_type.equals("I=")){
            w_type = "I";
        } else if(w_type.equals("S=")){
            w_type = "S";
        } else if(w_type.equals("C=")){
            w_type = "C";
        }

        Spannable sb = new SpannableString(w_type);
        ArrayList<String> summary = new ArrayList<>();

        for (int ind = 0; ind < w_type.length(); ind++) {
            String item = w_type.substring(ind, ind + 1);
            switch (item) {
                case "D":
                case "d":
                    sb.setSpan(new ForegroundColorSpan(CHARACTER_D_COLOR), ind, ind + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    break;
                case "I":
                case "i":
                    sb.setSpan(new ForegroundColorSpan(CHARACTER_I_COLOR), ind, ind + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    break;
                case "C":
                case "c":
                    sb.setSpan(new ForegroundColorSpan(CHARACTER_C_COLOR), ind, ind + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    break;
                case "S":
                case "s":
                    sb.setSpan(new ForegroundColorSpan(CHARACTER_S_COLOR), ind, ind + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    break;
            }
        }

        txvManner1.setText(sb);

        txvMannerDesc1.setText(mEstimateUser.ideal_character);
    }

    private void showEmptyUser() {
        llMainEstimate.setVisibility(View.GONE);
        tvStatus.setVisibility(View.INVISIBLE);
        clEmptyUser.setVisibility(View.VISIBLE);
    }

    public void goCurrentUserProfilePage() {

        Intent goProfile = new Intent(getActivity(), ProfileActivity.class);
        goProfile.putExtra("usr_uid", mEstimateUser.uid);
        startActivity(goProfile);
    }

    @OnClick({R.id.btn_refresh})
    void onClickRefreshUser() {
        loadCurrentEstimateUser();
    }

    @OnClick({R.id.vpProfiles})
    void onClickProfileView() {
        if (mUsersAdapter.mProfileUrls.isEmpty()) {
            goCurrentUserProfilePage();
        }
    }

    int mark = 0;

    @OnClick(R.id.tv_status)
    void OnClickEstimate() {
//        sendEstimatedScore(mark);
    }

    @OnClick(R.id.btn_blame)
    void OnClickBlame() {
        new BlameDialog(getActivity(), new BlameDialog.ActionListener() {
            @Override
            public void onSucess(int type) {
                reqBlame(BLAME_REASON[type - 1]);
            }
        }).show();
    }

    @OnClick({R.id.cb_heart0, R.id.cb_heart1, R.id.cb_heart2, R.id.cb_heart3, R.id.cb_heart4})
    void onClickHeartBtn(CheckBox checkBox) {
        if (is_overflow_oneday_cnt) {
            clearScore();
            WarningDialog.show(
                    getContext(),
                    getString(R.string.estimate_overflow_count),
                    null);
            return;
        }

        CheckBox[] checkBoxes = new CheckBox[]{cbHeart0, cbHeart1, cbHeart2, cbHeart3, cbHeart4};
        int clicked = -1;
        for (int i = 0; i < checkBoxes.length; i++) {
            if (checkBoxes[i] == checkBox) {
                clicked = i;
                break;
            }
        }
        for (int i = 0; i < checkBoxes.length; i++) {
            checkBoxes[i].setChecked(false);
        }
        for (int i = 0; i <= clicked; i++) {
            checkBoxes[i].setChecked(true);
        }
        switch (clicked) {
            case 0:
                tvStatus.setText(R.string.estimate_user_point_1);
                break;
            case 1:
                tvStatus.setText(R.string.estimate_user_point_2);
                break;
            case 2:
                tvStatus.setText(R.string.estimate_user_point_3);
                break;
            case 3:
                tvStatus.setText(R.string.estimate_user_point_4);
                break;
            case 4:
                tvStatus.setText(R.string.estimate_user_point_5);
                break;
        }

        mark = clicked + 1;

        tvStatus.setVisibility(View.VISIBLE);
    }

    private void clearScore() {
        CheckBox[] checkBoxes = new CheckBox[]{cbHeart0, cbHeart1, cbHeart2, cbHeart3, cbHeart4};
        for (int i = 0; i < checkBoxes.length; i++) {
            checkBoxes[i].setChecked(false);
        }
    }

    public void loadCurrentEstimateUser() {
        showProgress();
        Net.instance().api.get_current_estimate_usr(MyInfo.getInstance().uid)
                .enqueue(new Net.ResponseCallBack<MCurrentEstimateUser>() {
                    @Override
                    public void onSuccess(MCurrentEstimateUser response) {
                        super.onSuccess(response);
                        hideProgress();
                        is_overflow_oneday_cnt = response.overflow_oneday_cnt == 1;
                        if (response.info == null) {
                            // 현시할  회원이 없음
                            // TODO: 현시할  회원이 없을때의 Design이 현재 누락됨
                            showEmptyUser();
                            return;
                        }
                        mEstimateUser = new BaseUserInfo();
                        mEstimateUser.uid = response.info.uid;
                        mEstimateUser.nickname = response.info.nickname;
                        mEstimateUser.address = response.info.address;
                        mEstimateUser.age = response.info.age;
                        mEstimateUser.school = response.info.school; // add yj 2020-04-13
                        mEstimateUser.job = response.info.job; // add yj 2020-04-13
                        mEstimateUser.ideal_character = response.info.ideal_character;
                        mEstimateUser.character = response.info.character;
                        mEstimateUser.profile = response.info.profile;
                        mEstimateUser.body_type = response.info.body_type;
                        mEstimateUser.intro= response.info.intro;

                        showEstimateUser();
                    }

                    @Override
                    public void onFailure(MError response) {
                        super.onFailure(response);
                        hideProgress();

                        showEmptyUser();
                        if (IS_UITEST) {

                            mEstimateUser = new BaseUserInfo();
                            mEstimateUser.uid = 10;
                            mEstimateUser.nickname = "tester";
                            mEstimateUser.address = "address";
                            mEstimateUser.age = 20;
                            mEstimateUser.gender = 1;
                            mEstimateUser.character = "SD";
                            mEstimateUser.ideal_character = "강한 안정형\n강한 주도형";
                            mEstimateUser.school = "서울대";
                            mEstimateUser.job = "대학생";
                            mEstimateUser.height = 167;
                            mEstimateUser.profile = new String[]{"http://192.168.56.1/Character/Source/Admin/temp/temp_item_1.png", "http://192.168.56.1/Character/Source/Admin/temp/temp_item_2.png"};

                            showEstimateUser();

//                            showEmptyUser();

                        } else {
                            if (response.resultcode == 500) {
                                networkErrorOccupied(response);
                            }
                        }
                    }
                });
    }

    private void sendEstimatedScore(int score) {

        Net.instance().api.estimate_current_usr(MyInfo.getInstance().uid, mEstimateUser.uid, score)
                .enqueue(new Net.ResponseCallBack<MEstimateResult>() {
                    @Override
                    public void onSuccess(MEstimateResult response) {
                        super.onSuccess(response);
                        hideProgress();
                        is_overflow_oneday_cnt = response.overflow_oneday_cnt == 1;
                        MyInfo.getInstance().energy += response.plus_energy;
                        if (response.plus_energy > 0) {
                            WarningDialog.show(getContext(), String.format(getString(R.string.estimate_energy_received), response.plus_energy), null);
                        }
                        if (response.next_usr_exist == 1) {
                            mEstimateUser = new BaseUserInfo();
                            mEstimateUser.uid = response.next_usr_info.uid;
                            mEstimateUser.nickname = response.next_usr_info.nickname;
                            mEstimateUser.address = response.next_usr_info.address;
                            mEstimateUser.age = response.next_usr_info.age;
                            mEstimateUser.school = response.next_usr_info.school; // add yj 2020-04-13
                            mEstimateUser.job = response.next_usr_info.job; // add yj 2020-04-13
                            mEstimateUser.ideal_character = response.next_usr_info.ideal_character;
                            mEstimateUser.character = response.next_usr_info.character;
                            mEstimateUser.profile = response.next_usr_info.profile;
                            mEstimateUser.body_type = response.next_usr_info.body_type;
                            mEstimateUser.intro= response.next_usr_info.intro;

                            showEstimateUser();
                        } else {
                            showEmptyUser();
                        }

                        MyInfo.getInstance().star_value = 0;
                        mark = MyInfo.getInstance().star_value;
                        tvStatus.setVisibility(View.INVISIBLE);
                        ratingBar.setRating(0);
                    }

                    @Override
                    public void onFailure(MError response) {
                        super.onFailure(response);
                        hideProgress();

                        if (IS_UITEST) {
                            showEstimateUser();
                        } else {
                            if (response.resultcode == 500) {
                                networkErrorOccupied(response);
                            }
                        }
                    }
                });
    }

    private void reqBlame(String reason) {

        showProgress();
        Net.instance().api.report_user(MyInfo.getInstance().uid, mEstimateUser.uid, reason)
                .enqueue(new Net.ResponseCallBack<MBase>() {
                    @Override
                    public void onSuccess(MBase response) {
                        super.onSuccess(response);
                        hideProgress();

                        new AlertDialog.Builder(getActivity()).setTitle("신고하기").setMessage("정확히 신고처리되었습니다.").setPositiveButton(R.string.confirm, null).show();
                    }

                    @Override
                    public void onFailure(MError response) {
                        super.onFailure(response);
                        hideProgress();

                        if (response.resultcode == 500) {
                            networkErrorOccupied(response);
                        } else {
                            new AlertDialog.Builder(getActivity()).setTitle("").setMessage("이미 신고한 회원입니다.").setPositiveButton(R.string.confirm, null).show();
                        }
                    }
                });
    }


    private void showProgress() {
        clProgress.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        clProgress.setVisibility(View.GONE);
    }

    class ImagePagerAdapter extends PagerAdapter {
        List<String> mProfileUrls = new ArrayList<>();

        ImagePagerAdapter() {
        }

        @Override
        public int getCount() {
            return mProfileUrls.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            LayoutInflater inflater = (LayoutInflater) container.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View itemView = inflater.inflate(R.layout.item_eval_user, container, false);

            ImageView imgProfile = itemView.findViewById(R.id.iv_profile);
            GlideApp.with(mParent).load(mProfileUrls.get(position)).into(imgProfile);
            imgProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goCurrentUserProfilePage();
                }
            });

            container.addView(itemView, 0);
            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            (container).removeView((View) object);
        }
    }

    Handler _remainHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            sendEstimatedScore(mark);

            return false;
        }
    });
}
