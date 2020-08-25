package com.character.microblogapp.ui.page.profile;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.character.microblogapp.R;
import com.character.microblogapp.data.BaseUserInfo;
import com.character.microblogapp.data.Constant;
import com.character.microblogapp.data.MyInfo;
import com.character.microblogapp.model.Interest;
import com.character.microblogapp.model.MBase;
import com.character.microblogapp.model.MError;
import com.character.microblogapp.model.MEstimateResult;
import com.character.microblogapp.model.MInterest;
import com.character.microblogapp.model.MUser;
import com.character.microblogapp.net.Net;
import com.character.microblogapp.ui.adapter.ImgAdapter;
import com.character.microblogapp.ui.dialog.ConfirmDialog2;
import com.character.microblogapp.ui.dialog.ImageGalleryDialog;
import com.character.microblogapp.ui.dialog.WarningDialog;
import com.character.microblogapp.ui.page.BaseActivity;
import com.character.microblogapp.ui.page.setting.EnergyActivity;
import com.character.microblogapp.ui.page.setting.EnergyupActivity;
import com.character.microblogapp.ui.page.setting.dialog.BlameDialog;
import com.character.microblogapp.util.Util;
import com.viewpagerindicator.CirclePageIndicator;
import com.willy.ratingbar.BaseRatingBar;
import com.willy.ratingbar.ScaleRatingBar;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
public class ProfileActivity extends BaseActivity {

    @BindView(R.id.txv_top_nickname)
    TextView txvTopNickName;

    @BindView(R.id.vpBanner)
    ViewPager vpBanner;

    @BindView(R.id.ciBanner)
    CirclePageIndicator ciBanner;

    ImagePagerAdapter mTopAdapter;

    @BindView(R.id.txv_manner1)
    TextView txvManner1;

    @BindView(R.id.txv_manner2)
    TextView txvManner2;

    @BindView(R.id.txv_manner21)
    TextView txvManner21;

    @BindView(R.id.txv_manner22)
    TextView txvManner22;

    @BindView(R.id.txv_manner_desc1)
    TextView txvMannerDesc1;

    @BindView(R.id.txv_manner_desc2)
    TextView txvMannerDesc2;

    @BindView(R.id.imv_like)
    ImageView imvLike;

    @BindView(R.id.tv_message)
    TextView tvMessage;

    @BindView(R.id.tv_nickname)
    TextView txvNickName;

    @BindView(R.id.tv_job)
    TextView tvJob;

    @BindView(R.id.tv_job2)
    TextView tvJob2;

    @BindView(R.id.tv_address)
    TextView tvAddress;

    @BindView(R.id.tv_body_type)
    TextView tvBodyType;

    @BindView(R.id.tv_school)
    TextView tvSchool;

    @BindView(R.id.tv_major)
    TextView tvMajor;

    @BindView(R.id.tv_tall)
    TextView tvTall;

    @BindView(R.id.tv_body_type1)
    TextView tvBodyType1;

    @BindView(R.id.tv_religion)
    TextView tvReligion;

    @BindView(R.id.tv_drink)
    TextView tvDrink;

    @BindView(R.id.tv_smoke)
    TextView tvSmoke;

    @BindView(R.id.txv_manner_text)
    TextView tvCharacterText;

    @BindView(R.id.txv_manner_director)
    TextView tvCharacterDirector;

    @BindView(R.id.txv_manner_express)
    TextView tvCharacterExpress;

    @BindView(R.id.txv_manner_reason)
    TextView tvCharacterReason;

    @BindView(R.id.tv_interest)
    TextView tvInterest;

    @BindView(R.id.tv_love_style)
    TextView tvLoveStyle;

    @BindView(R.id.imv_profile_img2)
    ImageView imvProfileImage2;

    @BindView(R.id.lly_rating_bg)
    LinearLayout llyRatingBg;

    @BindView(R.id.iv_star1)
    ImageView ivStar1;

    @BindView(R.id.iv_star2)
    ImageView ivStar2;

    @BindView(R.id.iv_star3)
    ImageView ivStar3;

    @BindView(R.id.iv_star4)
    ImageView ivStar4;

    @BindView(R.id.iv_star5)
    ImageView ivStar5;

    @BindView(R.id.rate)
    ScaleRatingBar ratingBar;

    @BindView(R.id.rvImg)
    RecyclerView rvImg;

    ImgAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewDataBinding binding = DataBindingUtil.setContentView(this,R.layout.activity_profile);

        initUI();
    }

    MUser user = null;

    @Override
    protected void initUI() {
        super.initUI();

        Intent intent = getIntent();
        int usr_uid = intent.getIntExtra("usr_uid", 0);

        /*if (IS_UITEST) {
            usr_uid = 10;
        }*/

        if (usr_uid == 0) {
            finish();
        } else {
            getUserInfo(usr_uid);
        }
    }

    void updateUI() {

        txvTopNickName.setText(user.info.nickname);  //닉네임

        mTopAdapter = new ImagePagerAdapter();  //사진

        ArrayList temp = new ArrayList<String>();
        for(int i = 0; i < user.info.profile.length; i++) {
            temp.add(user.info.profile[i]);
        }

        adapter = new ImgAdapter(ProfileActivity.this, temp, new ImgAdapter.OnItemClickListener() {
            @Override
            public void onDetail(int pos) {

            }
        });

        rvImg.setAdapter(adapter);
        rvImg.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        vpBanner.setAdapter(mTopAdapter);
        ciBanner.setViewPager(vpBanner);

        for (int i = 0; i < user.info.profile.length; i++) {
            mTopAdapter.mBanner.add(user.info.profile[i]);
        }
        mTopAdapter.notifyDataSetChanged();

        String w_type = user.info.character;
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
        txvManner21.setText(sb);

        txvMannerDesc1.setText(user.info.ideal_character);
        txvMannerDesc2.setText(user.info.ideal_character);

        likeUser();

        tvMessage.setText(user.info.intro);

        txvNickName.setText(user.info.nickname);

        tvJob.setText(user.info.age + "세");
        tvJob2.setText(user.info.job);
        tvAddress.setText(" | " + user.info.address);
        tvBodyType.setText(" | " + user.info.body_type);
        tvSchool.setText(user.info.school);
        tvMajor.setText(user.info.major);
        tvTall.setText(user.info.height + "cm");
        tvBodyType1.setText(user.info.body_type);
        tvReligion.setText(user.info.religion);
        tvDrink.setText(user.info.drink);
        tvSmoke.setText(user.info.smoke);
        tvCharacterText.setText(user.info.character_info);
        tvCharacterDirector.setText("성격방향 : " + (user.info.character_director == null || user.info.character_director.equals("") ? "없음" : user.info.character_director));
        tvCharacterExpress.setText("표현 : " + (user.info.character_express == null || user.info.character_express.equals("") ? "없음" : user.info.character_express));
        tvCharacterReason.setText("동기요인 : " + (user.info.character_reason == null || user.info.character_reason.equals("") ? "없음" : user.info.character_reason));
        tvLoveStyle.setText(user.info.love_style);

        if (user.info.profile != null && user.info.profile[0] != null)
            Util.loadImage(ProfileActivity.this, imvProfileImage2, user.info.profile[0]);

        updateRating();
        getInterestList(user.info.uid);
    }

    void getInterestList(int id) {
        showProgress(this);
        Net.instance().api.get_interest(MyInfo.getInstance().uid)
                .enqueue(new Net.ResponseCallBack<MInterest>() {
                    @Override
                    public void onSuccess(MInterest response) {
                        super.onSuccess(response);
                        hideProgress();

                        ArrayList<String> parent = new ArrayList<>();
                        for (Interest item : response.interest) {
                            if (item.selectedChildCount() > 0) {
                                parent.add(item.name);
                            }
                        }
                        tvInterest.setText(TextUtils.join(",", parent));
                    }

                    @Override
                    public void onFailure(MError response) {
                        hideProgress();
                        super.onFailure(response);
                        if (response.resultcode == 500) {
                            networkErrorOccupied(response);
                        }
                    }
                });
    }

    private void likeUser() {

        if (user.info.is_liked == 0) {
            imvLike.setBackgroundResource(R.drawable.ic_heart);
        } else {
            imvLike.setBackgroundResource(R.drawable.ico_heart_eval_red);
        }
    }

    private void updateRating() {
        ratingBar.setRating(user.info.rate_byme);
        ratingBar.setOnRatingChangeListener(new ScaleRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(BaseRatingBar ratingBar, float rating) {
                user.info.rate_byme = (int)rating;

                reqEval();
            }
        });

//        ivStar1.setBackgroundResource(R.drawable.ico_eval_star_nor);
//        ivStar2.setBackgroundResource(R.drawable.ico_eval_star_nor);
//        ivStar3.setBackgroundResource(R.drawable.ico_eval_star_nor);
//        ivStar4.setBackgroundResource(R.drawable.ico_eval_star_nor);
//        ivStar5.setBackgroundResource(R.drawable.ico_eval_star_nor);
//
//        if (user.info.rate_byme > 0) {
//
//            ivStar1.setBackgroundResource(R.drawable.ico_eval_star_full);
//            if (user.info.rate_byme > 1) {
//                ivStar2.setBackgroundResource(R.drawable.ico_eval_star_full);
//
//                if (user.info.rate_byme > 2) {
//                    ivStar3.setBackgroundResource(R.drawable.ico_eval_star_full);
//
//                    if (user.info.rate_byme > 3) {
//                        ivStar4.setBackgroundResource(R.drawable.ico_eval_star_full);
//
//                        if (user.info.rate_byme > 4) {
//                            ivStar5.setBackgroundResource(R.drawable.ico_eval_star_full);
//                        }
//                    }
//                }
//            }
//        }
    }

    private void getUserInfo(final int usr_uid) {

        showProgress(this);
        Net.instance().api.get_user_profile_info(MyInfo.getInstance().uid, usr_uid)
                .enqueue(new Net.ResponseCallBack<MUser>() {
                    @Override
                    public void onSuccess(MUser response) {
                        super.onSuccess(response);
                        hideProgress();

                        user = response;

                        updateUI();
                    }

                    @Override
                    public void onFailure(MError response) {
                        super.onFailure(response);
                        hideProgress();

                        if (response.resultcode == 500) {
                            networkErrorOccupied(response);

                            if (Constant.IS_UITEST) {
                                user = new MUser();
                                user.info.uid = 10;
                                user.info.nickname = "빨간망토챠챠";
                                user.info.profile = new String[]{"http://192.168.56.1/Character/Source/Admin/temp/temp_item_1.png", "http://192.168.56.1/Character/Source/Admin/temp/temp_item_2.png"};
                                user.info.character = "DI";
                                user.info.ideal_character = "강한 주도형\n강한 사교형";

                                user.info.is_liked = 0;
                                user.info.intro = "설계는 침착하게, 개발은 여유있게, 검사는 깐깐하게,  투자는 화끈하게~!!";
                                user.info.job = "뷰티샵운영";
                                user.info.address = "부산";
                                user.info.body_type = "글래머";
                                user.info.school = "동아대";
                                user.info.major = "섬유미술";
                                user.info.height = 167;
                                user.info.religion = "무교";
                                user.info.drink = "가끔";
                                user.info.smoke = "비흡연";
                                user.info.character_text = "당신은 과정보다 결과를 중시하고 과감성을 가지고 있는 전형적인 리더타입";
                                user.info.character_director = "외향적";
                                user.info.character_express = "자기중심적, 경쟁적, 직선적";
                                user.info.character_reason = "목적, 성취";
                                user.info.interest = "공포영화, 매운 음식, 소맥, 산보다 바다, 집순이";
                                user.info.love_style = "비오는날 데이트, 스킨십자주, 낮져밤이, 빠른 진도, 톡보다 전화";
                                user.info.is_rated_byme = 0;
                                user.info.rate_byme = 0;

                                updateUI();
                            }
                        } else {
                            new AlertDialog.Builder(ProfileActivity.this).setTitle("").setMessage(response.res_msg).setPositiveButton(R.string.confirm, null).show();
                        }
                    }
                });
    }

    private void ProcLikeUser() {

        if (user.info.is_liked == 0) {
            ConfirmDialog2.show(ProfileActivity.this, String.format(getString(R.string.confirm_like_user_profile), user.info.nickname), "에너지 20개를 사용할까요?",
                    new ConfirmDialog2.ActionListener() {
                        @Override
                        public void onOk() {
                            if (MyInfo.getInstance().energy >= 20) {
                                reqLikeUser();
                            } else {

                                ConfirmDialog2.show(ProfileActivity.this, "에너지가 부족해요!!", String.format(getString(R.string.confirm_get_energy), (20 - MyInfo.getInstance().energy)),
                                        new ConfirmDialog2.ActionListener() {
                                            @Override
                                            public void onOk() {
                                                Intent goEnergy = new Intent(ProfileActivity.this, EnergyupActivity.class);
                                                goEnergy.putExtra("nickname", user.info.nickname);
                                                startActivity(goEnergy);
                                            }
                                        });

                            }
                        }
                    });
        } else {
            //reqLikeUser();
        }
    }

    private void reqLikeUser() {

        showProgress(this);
        Net.instance().api.like_user(MyInfo.getInstance().uid, user.info.uid)
                .enqueue(new Net.ResponseCallBack<MBase>() {
                    @Override
                    public void onSuccess(MBase response) {
                        super.onSuccess(response);
                        hideProgress();

                        if (response.resultcode == 0) {
                            if (user.info.is_liked == 0) {
                                user.info.is_liked = 1;
                            } else {
                                user.info.is_liked = 0;
                            }
                        }
                        MyInfo.getInstance().energy = response.count;

                        likeUser();
                    }

                    @Override
                    public void onFailure(MError response) {
                        super.onFailure(response);
                        hideProgress();

                        if (response.resultcode == 500) {
                            networkErrorOccupied(response);
                        } else {
                            new AlertDialog.Builder(ProfileActivity.this).setTitle("").setMessage(response.res_msg).setPositiveButton(R.string.confirm, null).show();
                        }
                    }
                });
    }

    private void reqBlame(String reason) {

        showProgress(this);
        Net.instance().api.report_user(MyInfo.getInstance().uid, user.info.uid, reason)
                .enqueue(new Net.ResponseCallBack<MBase>() {
                    @Override
                    public void onSuccess(MBase response) {
                        super.onSuccess(response);
                        hideProgress();

                        new AlertDialog.Builder(ProfileActivity.this).setTitle("신고하기").setMessage("정확히 신고처리되었습니다.").setPositiveButton(R.string.confirm, null).show();
                    }

                    @Override
                    public void onFailure(MError response) {
                        super.onFailure(response);
                        hideProgress();

                        if (response.resultcode == 500) {
                            networkErrorOccupied(response);
                        } else {
                            new AlertDialog.Builder(ProfileActivity.this).setTitle("").setMessage("이미 신고한 회원입니다.").setPositiveButton(R.string.confirm, null).show();
                        }
                    }
                });
    }

    private void reqEval() {

        showProgress(this);
        Net.instance().api.estimate_usr(MyInfo.getInstance().uid, user.info.uid, user.info.rate_byme)
                .enqueue(new Net.ResponseCallBack<MBase>() {
                    @Override
                    public void onSuccess(MBase response) {
                        super.onSuccess(response);
                        hideProgress();
//                        MyInfo.getInstance().energy += response.plus_energy;
//                        if (response.plus_energy > 0) {
//                            WarningDialog.show(ProfileActivity.this, String.format(getString(R.string.estimate_energy_received), response.plus_energy), null);
//                        } else {
                            WarningDialog.show(ProfileActivity.this, "정확히 평가되었습니다.", new WarningDialog.ActionListener() {
                                @Override
                                public void onOk() {
//                                    finish();
                                }
                            });
//                        }
                    }

                    @Override
                    public void onFailure(MError response) {
                        super.onFailure(response);
                        hideProgress();

                        if (response.resultcode == 500) {
                            networkErrorOccupied(response);
                        } else {
                            ToastUtils.showShort(response.res_msg);
                        }
                    }
                });
    }

    @OnClick(R.id.rlt_back)
    void goBack() {
        finish();
    }

    @OnClick(R.id.rlt_blame)
    void procBlame() {

//        new ProfileMenuDialog(this, new ProfileMenuDialog.ActionListener() {
//            @Override
//            public void onSucess(int type) {
//
//                if (type == 1) {
//                    ProcLikeUser();
//                } else if (type == 2) {
//
//                    new BlameDialog(ProfileActivity.this, new BlameDialog.ActionListener() {
//                        @Override
//                        public void onSucess(int type) {
//                            reqBlame(BLAME_REASON[type - 1]);
//                        }
//                    }).show();
//
//                } else {
//                    reqEval();
//                }
//            }
//        }).show();

        new BlameDialog(ProfileActivity.this, new BlameDialog.ActionListener() {
            @Override
            public void onSucess(int type) {
                reqBlame(BLAME_REASON[type - 1]);
            }
        }).show();
    }

    @OnClick(R.id.flt_like)
    void onLike() {
        ProcLikeUser();
    }

    @OnClick({R.id.iv_star1, R.id.iv_star2, R.id.iv_star3, R.id.iv_star4, R.id.iv_star5})
    void onRating(View view) {
        switch (view.getId()) {
            case R.id.iv_star1:
                user.info.rate_byme = 1;
                break;
            case R.id.iv_star2:
                user.info.rate_byme = 2;
                break;
            case R.id.iv_star3:
                user.info.rate_byme = 3;
                break;
            case R.id.iv_star4:
                user.info.rate_byme = 4;
                break;
            case R.id.iv_star5:
                user.info.rate_byme = 5;
                break;
        }

        updateRating();
    }

    @OnClick(R.id.btn_rate)
    void onRateUser() {
        reqEval();
    }

    @OnClick(R.id.flt_other_interest)
    void OnClickDISC() {
        Intent intent = new Intent(this, ProfileDISCActivity.class);
        intent.putExtra("usr_uid", user.info.uid);
        startActivity(intent);
    }

    class ImagePagerAdapter extends PagerAdapter {

        List<String> mBanner = new ArrayList<>();

        ImagePagerAdapter() {
        }

        @Override
        public int getCount() {
            return mBanner.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            final LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View itemView = inflater.inflate(R.layout.item_profile_banner, container, false);

            ImageView ivImage = itemView.findViewById(R.id.ivPhoto);

            Util.loadImage(ProfileActivity.this, ivImage, mBanner.get(position));

            ivImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent = new Intent(ProfileActivity.this, ProfileImageDetailViewActivity.class);
//                    intent.putExtra("profile_image", mBanner.get(position));
//                    startActivity(intent);
                    new ImageGalleryDialog(ProfileActivity.this, mBanner, 0).show();
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

}
