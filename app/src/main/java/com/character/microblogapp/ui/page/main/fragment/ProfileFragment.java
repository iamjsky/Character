package com.character.microblogapp.ui.page.main.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.character.microblogapp.GlideApp;
import com.character.microblogapp.MyApplication;
import com.character.microblogapp.R;
import com.character.microblogapp.data.MyInfo;
import com.character.microblogapp.model.MError;
import com.character.microblogapp.model.MUser;
import com.character.microblogapp.net.Net;
import com.character.microblogapp.ui.adapter.InterestAdapter;
import com.character.microblogapp.ui.dialog.ImageGalleryDialog;
import com.character.microblogapp.ui.dialog.SelectRegion;
import com.character.microblogapp.ui.page.BaseFragment;
import com.character.microblogapp.ui.page.profile.ProfileActivity;
import com.character.microblogapp.ui.page.profile.ProfileDISCActivity;
import com.character.microblogapp.ui.page.profile.ProfileImageDetailViewActivity;
import com.character.microblogapp.ui.page.setting.AskActivity;
import com.character.microblogapp.ui.page.setting.EnergyActivity;
import com.character.microblogapp.ui.page.setting.ProfileChangeActivity;
import com.character.microblogapp.ui.page.setting.SettingActivity;
import com.character.microblogapp.ui.page.setting.TypeActivity;
import com.character.microblogapp.util.Toaster;
import com.character.microblogapp.util.Util;
import com.jackandphantom.circularimageview.CircleImage;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileFragment extends BaseFragment {

    @BindView(R.id.btnBack)
    ImageButton btnBack;

    @BindView(R.id.btnMenu)
    ImageButton btnMenu;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.cl_progress)
    ConstraintLayout clProgress;

    @BindView(R.id.txv_manner1)
    TextView txvManner1;

    @BindView(R.id.txv_manner2)
    TextView txvManner2;

    @BindView(R.id.txv_manner_desc1)
    TextView txvMannerDesc1;
    @BindView(R.id.txv_manner_desc2)
    TextView txvMannerDesc2;

    @BindView(R.id.tv_nickname)
    TextView tvNickname;

    @BindView(R.id.tv_address)
    TextView tvAddress;

    @BindView(R.id.tv_age)
    TextView tv_age;



    @BindView(R.id.tv_job)
    TextView tv_job;

    @BindView(R.id.tv_body_type)
    TextView tv_body_type;

    @BindView(R.id.tvHint)
    TextView tvHint;

    @BindView(R.id.rlProfile)
    RelativeLayout rlProfile;

    @BindView(R.id.rlEnergy)
    RelativeLayout rlEnergy;

    @BindView(R.id.rlType)
    RelativeLayout rlType;

    @BindView(R.id.rlAsk)
    RelativeLayout rlAsk;

    @BindView(R.id.rlSetting)
    RelativeLayout rlSetting;

    @BindView(R.id.iv_userProfileImg)
    CircleImage iv_userProfileImg;


    MyApplication mApp = null;
    String myChar = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        createView(inflater, container, R.layout.fragment_profile);

        initData();
        apiInfo();
//        initUI(container);
        return mRoot;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    private void initData() {
        mApp = (MyApplication) getActivity().getApplicationContext();
    }

    void setUI() {

        btnBack.setVisibility(View.GONE);

//        tvTitle.setText(MyInfo.getInstance().nickname);  //닉네임
        tvTitle.setText(R.string.tab_profile);

        if(MyInfo.getInstance().profile != null){
            Glide.with(iv_userProfileImg).load(MyInfo.getInstance().profile[0]).diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(iv_userProfileImg);
        }




        if (IS_UITEST) {
            MyInfo.getInstance().character = "DS";
            MyInfo.getInstance().ideal_character = "강한 주도형\n강한 안정형";
        }

        String w_type = MyInfo.getInstance().character;
        if (w_type.equals("D=")) {
            w_type = "D";
        } else if (w_type.equals("I=")) {
            w_type = "I";
        } else if (w_type.equals("S=")) {
            w_type = "S";
        } else if (w_type.equals("C=")) {
            w_type = "C";
        } else if(w_type.equals("d")){
            w_type = "D";
        } else if(w_type.equals("i")){
            w_type = "I";
        } else if(w_type.equals("s")){
            w_type = "S";
        } else if(w_type.equals("c")){
            w_type = "C";
        }
        w_type = w_type.replace("d","D");
        w_type = w_type.replace("i","I");
        w_type = w_type.replace("s","S");
        w_type = w_type.replace("c","C");
        if(w_type.length() > 2){
            w_type = w_type.substring(0,2);
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
        String idelCharacter = myChar;
        String[] charText = idelCharacter.split(",");

        if(charText.length > 1){
            txvMannerDesc1.setText(charText[0]+"");
            txvMannerDesc2.setText(charText[1]+"");
            Log.e("char_debug", idelCharacter + " / " + charText[0] + charText[1]);
        }else{
            txvMannerDesc1.setText(charText[0]+"");
            Log.e("char_debug", idelCharacter + " / " + charText[0]);
        }


        String strName = MyInfo.getInstance().nickname+"";
        String strAge = MyInfo.getInstance().age+"";
        String strJob = MyInfo.getInstance().job+"";
        String strAddr = MyInfo.getInstance().address+"";
        String strBody = MyInfo.getInstance().body_type+"";


        tvNickname.setText(checkTextLength(0,strName));
        tv_age.setText(checkTextLength(1,strAge));
        tv_job.setText(checkTextLength(1,strJob));
        tvAddress.setText(checkTextLength(1, strAddr));
        tv_body_type.setText(checkTextLength(1, strBody));




    }

    private String checkTextLength(int type, String text){
        String result = text;
        switch (type){
            case 0:
                if(text.length()>=8){
                    result = text.substring(0,8) + "...";
                }
                break;

            case 1:
                if(text.length()>=6){
                    result = text.substring(0,6) + "...";
                }
                break;

        }

        return result;
    }
    void initVC() {
        btnMenu.setVisibility(View.GONE);

        setUI();
//        if (IS_UITEST) {
//            MyInfo.getInstance().status = 2;
//            MyInfo.getInstance().release_date = 34;
//        }

        if (MyInfo.getInstance().status == 2) {

            tvHint.setText(String.format("다수의 신고로 계정이 정지중입니다. 남은 정지기간 %d일", MyInfo.getInstance().release_date));

            rlAsk.setVisibility(View.VISIBLE);
            tvHint.setVisibility(View.VISIBLE);
            rlProfile.setVisibility(View.GONE);
            rlEnergy.setVisibility(View.GONE);
            rlType.setVisibility(View.GONE);
            rlSetting.setVisibility(View.GONE);
        } else {
            rlAsk.setVisibility(View.VISIBLE);
            tvHint.setVisibility(View.GONE);
            rlProfile.setVisibility(View.VISIBLE);
            rlEnergy.setVisibility(View.VISIBLE);
            rlType.setVisibility(View.VISIBLE);
            rlSetting.setVisibility(View.VISIBLE);
        }


    }

//    @OnClick({R.id.rlProfile, R.id.rlEnergy, R.id.rlType, R.id.rlAsk, R.id.rlSetting, R.id.flt_other_interest})
@OnClick({R.id.rlProfile, R.id.rlEnergy, R.id.rlType, R.id.rlAsk, R.id.rlSetting})
    void onClick(View view) {
        if (view.getId() == R.id.rlProfile) {
            getActivity().startActivity(new Intent(getActivity(), ProfileChangeActivity.class));
        } else if (view.getId() == R.id.rlEnergy) {
            getActivity().startActivity(new Intent(getActivity(), EnergyActivity.class));
        } else if (view.getId() == R.id.rlType) {
            getActivity().startActivity(new Intent(getActivity(), TypeActivity.class));
        } else if (view.getId() == R.id.rlAsk) {
            getActivity().startActivity(new Intent(getActivity(), AskActivity.class));
        } else if (view.getId() == R.id.rlSetting) {
            getActivity().startActivity(new Intent(getActivity(), SettingActivity.class));
        }

//        else if (view.getId() == R.id.flt_other_interest) {
//            Intent intent = new Intent(getActivity(), ProfileDISCActivity.class);
//            intent.putExtra("usr_uid", MyInfo.getInstance().uid);
//            startActivity(intent);
//        }
    }

    void apiInfo() {
        showProgress();
        Net.instance().api.get_my_profile_info(MyInfo.getInstance().uid)
                .enqueue(new Net.ResponseCallBack<MUser>() {
                    @Override
                    public void onSuccess(MUser response) {
                        super.onSuccess(response);
                        hideProgress();

                        MyInfo.getInstance().status = response.info.status;
                        MyInfo.getInstance().uid = response.info.uid;
                        MyInfo.getInstance().transformData(response);
                        myChar = response.info.ideal_character;

                        initVC();
                    }

                    @Override
                    public void onFailure(MError response) {
                        super.onFailure(response);
                        hideProgress();

                        if (response.resultcode == 500) {
                            networkErrorOccupied(response);
                        } else {
                            Toaster.showShort(getActivity(), "오류입니다.");
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
            LayoutInflater inflater = (LayoutInflater) getActivity()
                    .getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
//            View itemView = inflater.inflate(R.layout.item_profile_banner, container, false);
            View itemView = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.item_profile_banner, container, false).getRoot();

            ImageView ivImage = itemView.findViewById(R.id.ivPhoto);

            Util.loadImage(getActivity(), ivImage, mBanner.get(position));
            ivImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent = new Intent(getActivity(), ProfileImageDetailViewActivity.class);
//                    intent.putExtra("profile_image", mBanner.get(position));
//                    startActivity(intent);
                    new ImageGalleryDialog(getContext(), mBanner, 0).show();
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
