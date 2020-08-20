package com.character.microblogapp.ui.page.intro.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.Space;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.character.microblogapp.GlideApp;
import com.character.microblogapp.R;
import com.character.microblogapp.data.BaseInfo;
import com.character.microblogapp.data.Constant;
import com.character.microblogapp.data.ImageInfo;
import com.character.microblogapp.data.MyInfo;
import com.character.microblogapp.data.NewImageInfo;
import com.character.microblogapp.model.Interest;
import com.character.microblogapp.model.MAreaList;
import com.character.microblogapp.model.MError;
import com.character.microblogapp.model.MSignup;
import com.character.microblogapp.model.MUploadFiles;
import com.character.microblogapp.net.Net;
import com.character.microblogapp.ui.adapter.ArrayAdapter;
import com.character.microblogapp.ui.dialog.SelectDialog;
import com.character.microblogapp.ui.dialog.SelectRegion;
import com.character.microblogapp.ui.page.BaseFragment;
import com.character.microblogapp.ui.page.intro.LoginActivity;
import com.character.microblogapp.ui.page.intro.SelectInterestActivity;
import com.character.microblogapp.ui.page.intro.SelectLoveActivity;
import com.character.microblogapp.ui.page.intro.SignupActivity;
import com.character.microblogapp.ui.page.intro.SignupReadyActivity;
import com.character.microblogapp.ui.page.intro.SignupSuccessActivity;
import com.character.microblogapp.ui.page.intro.adapter.ImageAdapter;
import com.character.microblogapp.ui.page.setting.ProfileChangeActivity;
import com.character.microblogapp.ui.page.setting.QuitActivity;
import com.character.microblogapp.util.MediaManager;
import com.character.microblogapp.util.Toaster;
import com.character.microblogapp.util.Util;
import com.google.gson.Gson;
import com.theartofdev.edmodo.cropper.CropImage;

//import org.cybergarage.util.StringUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;

public class SignupProfileRegisterFragment extends BaseFragment {

    @BindView(R.id.rvProfile)
    RecyclerView rvProfile;
    @BindView(R.id.etNickname)
    EditText etNickname;
    @BindView(R.id.etArea)
    TextView etArea;
    @BindView(R.id.etJob)
    EditText etJob;
    @BindView(R.id.tvLove)
    TextView tvLove;
    /*제거*/
//    @BindView(R.id.tvHobby)
//    TextView tvHobby;
    @BindView(R.id.tvAge)
    TextView tvAge;
    @BindView(R.id.etSchool)
    EditText etSchool;
    @BindView(R.id.tvBody)
    TextView tvBody;
    @BindView(R.id.tvTall)
    TextView tvTall;
    @BindView(R.id.tvBelief)
    TextView tvBelief;
    @BindView(R.id.tvDrinking)
    TextView tvDrinking;
    @BindView(R.id.tvSmoking)
    TextView tvSmoking;
    @BindView(R.id.etSelfIntroduction)
    EditText etSelfIntroduction;
    @BindView(R.id.btnRegistering)
    Button btnRegistering;

    @BindArray(R.array.belief)
    String[] belief;
    @BindArray(R.array.drinking)
    String[] drinking;
    @BindArray(R.array.smoking)
    String[] smoking;
    @BindArray(R.array.body_male)
    String[] body_male;
    @BindArray(R.array.body_female)
    String[] body_female;
    @BindArray(R.array.hobby)
    String[] hobby;
    @BindArray(R.array.love_style)
    String[] love_style;

    @BindViews({R.id.iv_profile_01, R.id.iv_profile_02, R.id.iv_profile_03, R.id.iv_profile_04
            , R.id.iv_profile_05, R.id.iv_profile_06})
    List<ImageView> iv_profile;
    @BindViews({R.id.ibDelete_01, R.id.ibDelete_02, R.id.ibDelete_03, R.id.ibDelete_04
            , R.id.ibDelete_05, R.id.ibDelete_06})
    List<ImageButton> ibDelete;




    MediaManager mediaManager;

    private static final int PHOTO_MIN_COUNT = 3;

    SelectRegion dlgRegion = null;

    //private NewArrayAdapter mAdapter;
    private int imgPosition = 0;
    private List<NewImageInfo> profileImgList = new ArrayList<>();
    private List<ImageInfo> imageInfoList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        createView(inflater, container, R.layout.fragment_signup_profile_register);

        return mRoot;
    }

    public static SignupProfileRegisterFragment newInstance() {
        return new SignupProfileRegisterFragment();
    }

    @Override
    protected void initUI() {
        super.initUI();



        mediaManager = new MediaManager(mParent);
        mediaManager.setMediaCallback(new MediaManager.MediaCallback() {
            @Override
            public void onSelected(Boolean isVideo, File file, Bitmap bitmap, String videoPath, String thumbPath) {


                if (profileImgList.size() == 6){
                    Toaster.showShort(getContext(), R.string.profile_upload_guide);
                    return;
                }


                GlideApp.with(iv_profile.get(imgPosition)).load(file).into(iv_profile.get(imgPosition));

                iv_profile.get(imgPosition).setVisibility(View.VISIBLE);
                ibDelete.get(imgPosition).setVisibility(View.VISIBLE);
                NewImageInfo imageInfo = new NewImageInfo();
                imageInfo.image = file;
                imageInfo.imgPosition = imgPosition;
                profileImgList.add(imageInfo);
                Collections.sort(profileImgList);
//                if ((mAdapter.getItemCount() - 1) >= PHOTO_MAX_COUNT)
//                    mAdapter.removeLastObject();

            }

            @Override
            public void onFailed(int code, String err) {

            }

            @Override
            public void onDelete() {

            }
        });







        etArea.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                etArea.setSelected(StringUtil.hasData(etArea.getText().toString()));
                if (etArea.getText().toString().isEmpty()) {
                    etArea.setSelected(false);
                } else {
                    etArea.setSelected(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etNickname.addTextChangedListener(new MyTextWatcher(etNickname));
//        etArea.addTextChangedListener(new MyTextWatcher(etArea));
        etJob.addTextChangedListener(new MyTextWatcher(etJob));
        tvLove.addTextChangedListener(new MyTextWatcher(tvLove));
//        tvHobby.addTextChangedListener(new MyTextWatcher(tvHobby));
        tvAge.addTextChangedListener(new MyTextWatcher(tvAge));
        etSchool.addTextChangedListener(new MyTextWatcher(etSchool));
        tvBody.addTextChangedListener(new MyTextWatcher(tvBody));
        tvTall.addTextChangedListener(new MyTextWatcher(tvTall));
        tvBelief.addTextChangedListener(new MyTextWatcher(tvBelief));
        tvDrinking.addTextChangedListener(new MyTextWatcher(tvDrinking));
        tvSmoking.addTextChangedListener(new MyTextWatcher(tvSmoking));
        etSelfIntroduction.addTextChangedListener(new MyTextWatcher(etSelfIntroduction));

        int age = ((SignupActivity)getActivity()).birth;
        tvAge.setText("" + age);
        getAreaList("");

    }

    ArrayList<String> areaList = new ArrayList<>();
    ArrayList<String> areaDetailList = new ArrayList<>();

    void getAreaList(final String town) {
        Net.instance().api.get_region(town)
                .enqueue(new Net.ResponseCallBack<MAreaList>() {
                    @Override
                    public void onSuccess(MAreaList response) {
                        super.onSuccess(response);

                        areaList.clear();
                        areaList.addAll(Arrays.asList(response.area_list));
                        areaDetailList.clear();
                        areaDetailList.addAll(Arrays.asList(response.region_list));

                        if (dlgRegion != null) {
                            dlgRegion.list2 = areaDetailList;
                            dlgRegion.init2();
                        }

                    }

                    @Override
                    public void onFailure(MError response) {
                        super.onFailure(response);
                    }
                });
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MediaManager.CROP_IMAGE
                || requestCode == MediaManager.SET_CAMERA
                || requestCode == MediaManager.SET_GALLERY
                || requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            mediaManager.onActivityResult(requestCode, resultCode, data);
        }

        if (requestCode == 0x0110 && resultCode == RESULT_OK) {
            String type = data.getStringExtra("type");

//            if (type.equals("hobby")) {
//                interestList = new ArrayList<>();
//                interestList = data.getParcelableArrayListExtra("data");
//
//                ArrayList<String> parent = new ArrayList<>();
//                for (Interest item : interestList) {
//                    parent.add(item.name);
//                }
//             //   tvHobby.setText(TextUtils.join(",", parent));
//            } else {
                String selected = data.getStringExtra("data");
                tvLove.setText(selected);
        //    }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_IMAGE_REQUEST) {
            if (permissions.length == grantResults.length) {
                for (int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                }

                onAddImage();
            }
        }
    }

    void onAddImage() {
        if (!Util.checkPermissions(mParent, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA})) {
            mParent.requestPermission(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, PERMISSIONS_IMAGE_REQUEST);
            return;
        }

        mediaManager.showMediaManager("", null, false);
    }

    @OnClick(R.id.etArea)
    void onArea() {
        dlgRegion = new SelectRegion(getContext(), areaList, areaDetailList, new SelectRegion.Callback() {
            @Override
            public void onConfirm(String data1, String data2) {
                etArea.setText(data1 + " " + data2);
            }

            @Override
            public void onSelect(String data) {
                getAreaList(data);
            }
        });

        dlgRegion.show();
    }

    @OnClick({R.id.tvAge
            , R.id.tvBody
            , R.id.tvTall
            , R.id.tvBelief
            , R.id.tvDrinking
            , R.id.tvSmoking})
    void onClickInfo(final TextView view) {
        SignupActivity signupActivity = (SignupActivity) mParent;
        if (mParent == null)
            return;

        ArrayList<String> arrayList = new ArrayList<>();

        switch (view.getId()) {
            case R.id.tvAge:
//                for (int i = 1; i <= 100; i++) {
//                    arrayList.add(String.valueOf(i));
//                }
//                break;
                return;
            case R.id.tvBody:
                if (signupActivity.sex == GENDER.MALE) {
                    arrayList.addAll(Arrays.asList(body_male));
                } else {
                    arrayList.addAll(Arrays.asList(body_female));
                }
                break;
            case R.id.tvTall:
                for (int i = 140; i <= 200; i++) {
                    arrayList.add(String.valueOf(i));
                }
                break;
            case R.id.tvBelief:
                arrayList.addAll(Arrays.asList(belief));
                break;
            case R.id.tvDrinking:
                arrayList.addAll(Arrays.asList(drinking));
                break;
            case R.id.tvSmoking:
                arrayList.addAll(Arrays.asList(smoking));
                break;
//            case R.id.tvHobby:
//                arrayList.addAll(Arrays.asList(hobby));
//                break;
            case R.id.tvLove:
                arrayList.addAll(Arrays.asList(love_style));
                break;
        }

        new SelectDialog(mParent, arrayList, new SelectDialog.Callback() {
            @Override
            public void onConfirm(int position, String data) {
                view.setText(data);
            }
        }).show();
    }

  //  private ArrayList<Interest> interestList = new ArrayList<>();


//    @OnClick({R.id.tvHobby, R.id.tvLove})
@OnClick(R.id.tvLove)
    void onSelectMulti(TextView view) {
        Intent intent = null;
//        if (view.getId() == R.id.tvHobby) {
//            // 관심사선택
//            intent = new Intent(mParent, SelectInterestActivity.class);
//            intent.putExtra("type", "hobby");
//            intent.putParcelableArrayListExtra("data", interestList);
//        } else {
            // 연애스타일선택
            intent = new Intent(mParent, SelectLoveActivity.class);
            intent.putExtra("type", "love");
            intent.putExtra("data", tvLove.getText().toString());
       // }
        mParent.startActivityForResult(intent, 0x0110);
    }

    void uploadImages() {
        mParent.showProgress(mParent);
        MultipartBody.Part[] images = new MultipartBody.Part[profileImgList.size()];
        int n = 0;

        for (NewImageInfo imageInfo : profileImgList) {
           // ImageInfo imageInfo = (ImageInfo) info;
            if (imageInfo != null && imageInfo.image.exists()) {
                RequestBody photo = RequestBody.create(MediaType.parse("image/*"), imageInfo.image);
                images[n++] = MultipartBody.Part.createFormData("uploadfile[]", imageInfo.image.getName(), photo);
            }
        }

        Net.instance().api.uploadFiles(images)
                .enqueue(new Net.ResponseCallBack<MUploadFiles>() {
                    @Override
                    public void onSuccess(MUploadFiles response) {
                        super.onSuccess(response);
                        mParent.hideProgress();

                        if (response.arr_name != null) {

                            for (int i = 0; i < response.arr_name.length; i++) {
                              profileImgList.get(i).img_url = response.arr_name[i];



                            }
                        }

                        doSignup();
                    }

                    @Override
                    public void onFailure(MError response) {
                        super.onFailure(response);

                        mParent.networkErrorOccupied(response);
                    }
                });
    }

    @OnClick(R.id.btnRegistering)
    void onRegistering() {

        SignupActivity signupActivity = (SignupActivity) mParent;
        if (signupActivity == null)
            return;

        String nickname = etNickname.getText().toString();
        String tall = tvTall.getText().toString();
        String area = etArea.getText().toString();
//        String age = tvAge.getText().toString();
        String school = etSchool.getText().toString();

        if (profileImgList.size() < PHOTO_MIN_COUNT) {
            Toaster.showShort(mParent, R.string.profile_upload_guide);
            return;
        }

        if (nickname.isEmpty()) {
            Toaster.showShort(mParent, R.string.input_nickname);
            return;
        }

        String firstNickName = nickname.substring(0, 1);
        String lastNickName = nickname.substring(nickname.length() - 1, nickname.length());
        if (firstNickName.equals(" ") || firstNickName.equals("  ") || lastNickName.equals(" ") || lastNickName.equals("  ")) {
            Toaster.showShort(mParent, "닉네임의 첫문구와 마지막문구는 스페이스가 아닌 문자로 입력해 주세요.");
            etNickname.requestFocus();
            return;
        }
        //if (!StringUtil.hasData(nickname)) {
        if (nickname.trim().isEmpty()) {
            Toaster.showShort(mParent, R.string.input_nickname);
            return;
        }

//        if (!StringUtil.hasData(tall)) {
        if (tall.isEmpty()) {
            Toaster.showShort(mParent, R.string.select_tall);
            return;
        }

//        if (!StringUtil.hasData(area)) {
        if (area.isEmpty()) {
            Toaster.showShort(mParent, R.string.input_area);
            return;
        }
//        if (!StringUtil.hasData(age)) {
//            Toaster.showShort(mParent, R.string.select_age);
//            return;
//        }
//        if (!StringUtil.hasData(school)) {

        if (school.isEmpty()) {
            Toaster.showShort(mParent, R.string.select_school);
            return;
        }
        String firstSchool = school.substring(0, 1);
        String lastSchool = school.substring(school.length() - 1, school.length());
        if (firstSchool.equals(" ") || firstSchool.equals("  ") || lastSchool.equals(" ") || lastSchool.equals("  ")) {
            Toaster.showShort(mParent, "학교의 첫문구와 마지막문구는 스페이스가 아닌 문자로 입력해 주세요.");
            etSchool.requestFocus();
            return;
        }

        if (school.trim().isEmpty() || school.equals(" ")) {
            Toaster.showShort(mParent, R.string.select_school);
            return;
        }

        if (school.isEmpty()) {
            Toaster.showShort(mParent, R.string.select_school);
            return;
        }
        String job = etJob.getText().toString();
        if (!job.isEmpty()) {
            String firstJob = job.substring(0, 1);
            String lastJob = job.substring(job.length() - 1, job.length());
            if (firstJob.equals(" ") || firstJob.equals("  ") || lastJob.equals(" ") || lastJob.equals("  ")) {
                Toaster.showShort(mParent, "직업의 첫문구와 마지막문구는 스페이스가 아닌 문자로 입력해 주세요.");
                etJob.requestFocus();
                return;
            }

            if (!job.isEmpty() && job.trim().isEmpty()) {
                Toaster.showShort(mParent, R.string.select_job_no_space);
                return;
            }
        }

        if (etJob.getText().toString().trim().equals("")) {
            Toaster.showShort(mParent, R.string.select_job);
            return;
        }

        if (tvBelief.getText().toString().trim().equals("")) {
            Toaster.showShort(mParent, R.string.select_belief);
            return;
        }

        if (tvDrinking.getText().toString().trim().equals("")) {
            Toaster.showShort(mParent, R.string.select_drinking);
            return;
        }

        if (tvSmoking.getText().toString().trim().equals("")) {
            Toaster.showShort(mParent, R.string.select_smoking);
            return;
        }

        if (tvBody.getText().toString().trim().equals("")) {
            Toaster.showShort(mParent, R.string.select_body);
            return;
        }

//        if (tvHobby.getText().toString().trim().equals("")) {
//            Toaster.showShort(mParent, R.string.select_hobby);
//            return;
//        }

        if (tvLove.getText().toString().trim().equals("")) {
            Toaster.showShort(mParent, R.string.select_love_style);
            return;
        }

        String introduction = etSelfIntroduction.getText().toString();
        if (!introduction.isEmpty()) {

            String firstIntroduction = introduction.substring(0, 1);
            String lastIntroduction = introduction.substring(introduction.length() - 1, introduction.length());
            if (firstIntroduction.equals(" ") || firstIntroduction.equals("  ") || lastIntroduction.equals(" ") || lastIntroduction.equals("  ")) {
                Toaster.showShort(mParent, "소개글의 첫문구와 마지막문구는 스페이스가 아닌 문자로 입력해 주세요.");
                etSelfIntroduction.requestFocus();
                return;
            }
            if (!etSelfIntroduction.getText().toString().isEmpty() && etSelfIntroduction.getText().toString().trim().isEmpty()) {
                Toaster.showShort(mParent, R.string.select_intro_no_space);
                return;
            }
        }

        uploadImages();
    }

    void doSignup() {

        final SignupActivity signupActivity = (SignupActivity) mParent;
        if (signupActivity == null)
            return;

        String nickname = etNickname.getText().toString().trim();
        String tall = tvTall.getText().toString();
        String area = etArea.getText().toString();
//        String age = tvAge.getText().toString();
        String school = etSchool.getText().toString().trim();

        double latitude = 0.0;
        double longitude = 0.0;

//        if ((mAdapter.getItemCount() - 1) < PHOTO_MAX_COUNT) {
//            mAdapter.removeLastObject();
//        }

        mParent.showProgress(mParent);
        Log.e("char_debug", new Gson().toJson(profileImgList));
        Log.e("char_debug", new Gson().toJson(imageInfoList));
        for(int i=0; i < profileImgList.size(); i++){
            ImageInfo info = new ImageInfo(profileImgList.get(i).img_url);
            imageInfoList.add(info);

        }
        Log.e("char_debug", new Gson().toJson(imageInfoList));
        Net.instance().api.signup(signupActivity.loginType,
                signupActivity.sns_id,
                signupActivity.email,
                signupActivity.emailType,
                signupActivity.password,
                signupActivity.sex.value,
                nickname,
                Integer.valueOf(tall),
                tvAge.getText().toString(),
                new Gson().toJson(imageInfoList),
                area,
                school,
                etJob.getText().toString().trim(),
                tvBelief.getText().toString(),
                tvDrinking.getText().toString(),
                tvSmoking.getText().toString(),
                tvBody.getText().toString(),
                "",
                tvLove.getText().toString(),
                etSelfIntroduction.getText().toString().trim(),
                signupActivity.phone,
                signupActivity.birthday.replace("-", ""),
                latitude,
                longitude,
                MyInfo.getInstance().fcm_token,
                "테스트1")
//                signupActivity.certname)
                .enqueue(new Net.ResponseCallBack<MSignup>() {
                    @Override
                    public void onSuccess(MSignup response) {
                        super.onSuccess(response);
                        mParent.hideProgress();

                        MyInfo.getInstance().uid = response.usr_uid;
                        MyInfo.getInstance().email = signupActivity.email;
                        MyInfo.getInstance().pwd = signupActivity.password;
                        MyInfo.getInstance().login_type = signupActivity.loginType;
                        MyInfo.getInstance().save(mParent);

                        ((SignupActivity)mParent).showSuccess();
                    }

                    @Override
                    public void onFailure(MError response) {
                        super.onFailure(response);

                        mParent.networkErrorOccupied(response);
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
            if (string.isEmpty()) {
                textView.setSelected(false);
            } else {
                textView.setSelected(true);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    @OnClick(R.id.iv_addPhoto_01)
    public void iv_addPhoto_01Clicked(){
       imgPosition = 0;
        onAddImage();

    }
    @OnClick(R.id.iv_addPhoto_02)
    public void iv_addPhoto_02Clicked(){
        imgPosition = 1;
        onAddImage();
    }
    @OnClick(R.id.iv_addPhoto_03)
    public void iv_addPhoto_03Clicked(){
        imgPosition = 2;
        onAddImage();
    }
    @OnClick(R.id.iv_addPhoto_04)
    public void iv_addPhoto_04Clicked(){
        imgPosition = 3;
        onAddImage();
    }
    @OnClick(R.id.iv_addPhoto_05)
    public void iv_addPhoto_05Clicked(){
        imgPosition = 4;
        onAddImage();
    }
    @OnClick(R.id.iv_addPhoto_06)
    public void iv_addPhoto_06Clicked(){
        imgPosition = 5;
        onAddImage();
    }
    @OnClick(R.id.ibDelete_01)
    public void ibDelete_01Clicked(){
        for(int i=0; i < profileImgList.size(); i++){
            if(profileImgList.get(i).imgPosition == 0){
                profileImgList.remove(i);
                Collections.sort(profileImgList);
                iv_profile.get(imgPosition).setBackgroundResource(R.color.color_transparent);
                iv_profile.get(imgPosition).setVisibility(View.GONE);
                ibDelete.get(imgPosition).setVisibility(View.GONE);
                break;
            }
        }
    }
    @OnClick(R.id.ibDelete_02)
    public void ibDelete_02Clicked(){
        for(int i=0; i < profileImgList.size(); i++){
            if(profileImgList.get(i).imgPosition == 1){
                profileImgList.remove(i);
                Collections.sort(profileImgList);
                iv_profile.get(imgPosition).setBackgroundResource(R.color.color_transparent);
                iv_profile.get(imgPosition).setVisibility(View.GONE);
                ibDelete.get(imgPosition).setVisibility(View.GONE);
                break;
            }
        }
    }
    @OnClick(R.id.ibDelete_03)
    public void ibDelete_03Clicked(){
        for(int i=0; i < profileImgList.size(); i++){
            if(profileImgList.get(i).imgPosition == 2){
                profileImgList.remove(i);
                Collections.sort(profileImgList);
                iv_profile.get(imgPosition).setBackgroundResource(R.color.color_transparent);
                iv_profile.get(imgPosition).setVisibility(View.GONE);
                ibDelete.get(imgPosition).setVisibility(View.GONE);
                break;
            }
        }

    }
    @OnClick(R.id.ibDelete_04)
    public void ibDelete_04Clicked(){
        for(int i=0; i < profileImgList.size(); i++){
            if(profileImgList.get(i).imgPosition == 3){
                profileImgList.remove(i);
                Collections.sort(profileImgList);
                iv_profile.get(imgPosition).setBackgroundResource(R.color.color_transparent);
                iv_profile.get(imgPosition).setVisibility(View.GONE);
                ibDelete.get(imgPosition).setVisibility(View.GONE);
                break;
            }
        }
    }
    @OnClick(R.id.ibDelete_05)
    public void ibDelete_05Clicked(){
        for(int i=0; i < profileImgList.size(); i++){
            if(profileImgList.get(i).imgPosition == 4){
                profileImgList.remove(i);
                Collections.sort(profileImgList);
                iv_profile.get(imgPosition).setBackgroundResource(R.color.color_transparent);
                iv_profile.get(imgPosition).setVisibility(View.GONE);
                ibDelete.get(imgPosition).setVisibility(View.GONE);
                break;
            }
        }
    }
    @OnClick(R.id.ibDelete_06)
    public void ibDelete_06Clicked(){
        for(int i=0; i < profileImgList.size(); i++){
            if(profileImgList.get(i).imgPosition == 5){
                profileImgList.remove(i);
                Collections.sort(profileImgList);
                iv_profile.get(imgPosition).setBackgroundResource(R.color.color_transparent);
                iv_profile.get(imgPosition).setVisibility(View.GONE);
                ibDelete.get(imgPosition).setVisibility(View.GONE);
                break;
            }
        }
    }
    @OnClick(R.id.tv_info)
            public void tv_infoClicked(){
    for(int i=0; i < profileImgList.size(); i++){
        Log.e("char_debug", "profileImgList" + profileImgList.get(i).imgPosition);
    }

    }




}

