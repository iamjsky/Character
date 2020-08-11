package com.character.microblogapp.ui.page.setting;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.character.microblogapp.R;
import com.character.microblogapp.data.BaseInfo;
import com.character.microblogapp.data.ImageInfo;
import com.character.microblogapp.data.MyInfo;
import com.character.microblogapp.model.Interest;
import com.character.microblogapp.model.MAreaList;
import com.character.microblogapp.model.MBase;
import com.character.microblogapp.model.MError;
import com.character.microblogapp.model.MInterest;
import com.character.microblogapp.model.MUploadFiles;
import com.character.microblogapp.model.MUserCharacterInfo;
import com.character.microblogapp.net.Net;
import com.character.microblogapp.ui.adapter.ChatAdapter;
import com.character.microblogapp.ui.dialog.SelectDialog;
import com.character.microblogapp.ui.dialog.SelectRegion;
import com.character.microblogapp.ui.page.BaseActivity;
import com.character.microblogapp.ui.page.intro.SelectInterestActivity;
import com.character.microblogapp.ui.page.intro.SelectLoveActivity;
import com.character.microblogapp.ui.page.intro.adapter.ImageAdapter;
import com.character.microblogapp.util.MediaManager;
import com.character.microblogapp.util.Toaster;
import com.character.microblogapp.util.Util;
import com.google.gson.Gson;
import com.theartofdev.edmodo.cropper.CropImage;

//import org.cybergarage.util.StringUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;

public class ProfileChangeActivity extends BaseActivity {

    SelectRegion dlgRegion = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewDataBinding binding = DataBindingUtil.setContentView(this,R.layout.activity_profile_change);

        initUI();

        updateUI();
    }

    @BindView(R.id.txv_top_nickname)
    TextView txvTopNickName;
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
    @BindView(R.id.tvHobby)
    TextView tvHobby;
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

    ImageAdapter mAdapter;

    MediaManager mediaManager;

    boolean bChanged = false;

    private static final int PHOTO_MIN_COUNT = 3;
    private static final int PHOTO_MAX_COUNT = 8;

    private ArrayList<Interest> interestList = new ArrayList<>();

    @Override
    protected void initUI() {
        super.initUI();

        mediaManager = new MediaManager(this);
        mediaManager.setMediaCallback(new MediaManager.MediaCallback() {
            @Override
            public void onSelected(Boolean isVideo, File file, Bitmap bitmap, String videoPath, String thumbPath) {
                int insertPosition = mAdapter.getItemCount() - 1;

                if (insertPosition == 8){
                    Toaster.showShort(ProfileChangeActivity.this, R.string.profile_upload_guide);
                    return;
                }

                mAdapter.insert(insertPosition, new ImageInfo(file));
//                if ((mAdapter.getItemCount() - 1) > PHOTO_MAX_COUNT)
//                    mAdapter.removeLastObject();

                bChanged = true;
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(int code, String err) {

            }

            @Override
            public void onDelete() {

            }
        });

        mAdapter = new ImageAdapter(this, new ArrayList<BaseInfo>());
        mAdapter.setCallback(new ImageAdapter.Callback() {
            @Override
            public void onClick(int position, BaseInfo data) {
                onAddImage();
            }

            @Override
            public void onDelete(int position, BaseInfo data) {
                mAdapter.remove(position);
                bChanged = true;
                mAdapter.notifyDataSetChanged();

//                if ((mAdapter.getItemCount() - 1) < PHOTO_MAX_COUNT)
//                    addEmptyPhoto();

            }
        });
        rvProfile.setLayoutManager(new GridLayoutManager(this, 4));
        rvProfile.setAdapter(mAdapter);

//        addEmptyPhoto();

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
        tvHobby.addTextChangedListener(new MyTextWatcher(tvHobby));
        tvAge.addTextChangedListener(new MyTextWatcher(tvAge));
        etSchool.addTextChangedListener(new MyTextWatcher(etSchool));
        tvBody.addTextChangedListener(new MyTextWatcher(tvBody));
        tvTall.addTextChangedListener(new MyTextWatcher(tvTall));
        tvBelief.addTextChangedListener(new MyTextWatcher(tvBelief));
        tvDrinking.addTextChangedListener(new MyTextWatcher(tvDrinking));
        tvSmoking.addTextChangedListener(new MyTextWatcher(tvSmoking));
        etSelfIntroduction.addTextChangedListener(new MyTextWatcher(etSelfIntroduction));

        getAreaList("");
    }

    private void updateUI() {

        for (int i = 0; i < MyInfo.getInstance().profile.length; i++) {
            ImageInfo info = new ImageInfo(MyInfo.getInstance().profile[i]);
            mAdapter.add(info);
        }

        txvTopNickName.setText(MyInfo.getInstance().nickname);
        etNickname.setText(MyInfo.getInstance().nickname);
        tvTall.setText(MyInfo.getInstance().height + "");
        tvAge.setText(MyInfo.getInstance().age + "");
        etArea.setText(MyInfo.getInstance().address + "");
        etSchool.setText(MyInfo.getInstance().school + "");
        etJob.setText(MyInfo.getInstance().job + "");
        tvBelief.setText(MyInfo.getInstance().religion + "");
        tvDrinking.setText(MyInfo.getInstance().drink + "");
        tvSmoking.setText(MyInfo.getInstance().smoke + "");
        tvBody.setText(MyInfo.getInstance().body_type + "");
        tvHobby.setText(MyInfo.getInstance().interest.equals("[]") ? "" : MyInfo.getInstance().interest);
        tvLove.setText(MyInfo.getInstance().love_style + "");
        etSelfIntroduction.setText(MyInfo.getInstance().intro + "");
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

    void addEmptyPhoto() {
        //이미지 추가 부분
        mAdapter.add(null);
        mAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.rlt_back)
    void onBack() {
        finish();
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

            if (type.equals("hobby")) {
                interestList = new ArrayList<>();
                interestList = data.getParcelableArrayListExtra("data");

                ArrayList<String> parent = new ArrayList<>();
                for (Interest item : interestList) {
                    parent.add(item.name);
                }
                tvHobby.setText(TextUtils.join(",", parent));

                bChanged = !tvHobby.getText().toString().equals(MyInfo.getInstance().interest);
            } else {
                String selected = data.getStringExtra("data");
                tvLove.setText(selected);

                bChanged = !tvLove.getText().toString().equals(MyInfo.getInstance().love_style);
            }
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
        if (!Util.checkPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA})) {
            this.requestPermission(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, PERMISSIONS_IMAGE_REQUEST);
            return;
        }

        mediaManager.showMediaManager("", null, false);
    }

    @OnClick(R.id.etArea)
    void onArea() {
        dlgRegion = new SelectRegion(ProfileChangeActivity.this, areaList, areaDetailList, new SelectRegion.Callback() {
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
            , R.id.etNickname
            , R.id.etSchool
            , R.id.tvSmoking})
    void onClickInfo(final TextView view) {

        ArrayList<String> arrayList = new ArrayList<>();

        switch (view.getId()) {
            case R.id.tvAge:
                Toaster.showShort(this, "한 번 입력한 나이는 수정이 불가합니다.\n잘 못 기입 하셨을 경우에는 고객센터에 문의해주세요.");
//                for (int i = 1; i <= 100; i++) {
//                    arrayList.add(String.valueOf(i));
//                }
                return;
            case R.id.etNickname:
                Toaster.showShort(this, "한 번 입력한 닉네임은 수정이 불가합니다.\n잘 못 기입 하셨을 경우에는 고객센터에 문의해주세요.");
//                for (int i = 1; i <= 100; i++) {
//                    arrayList.add(String.valueOf(i));
//                }
                return;
            case R.id.etSchool:
                Toaster.showShort(this, "한 번 입력한 학교는 수정이 불가합니다.\n잘 못 기입 하셨을 경우에는 고객센터에 문의해주세요.");
//                for (int i = 1; i <= 100; i++) {
//                    arrayList.add(String.valueOf(i));
//                }
                return;
            case R.id.tvBody:
                if (MyInfo.getInstance().gender == GENDER.MALE.value) {
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
            case R.id.tvHobby:
                arrayList.addAll(Arrays.asList(hobby));
                break;
            case R.id.tvLove:
                arrayList.addAll(Arrays.asList(love_style));
                break;
        }

        new SelectDialog(this, arrayList, new SelectDialog.Callback() {
            @Override
            public void onConfirm(int position, String data) {
                view.setText(data);

                switch (view.getId()) {
                    case R.id.tvBody:
                        bChanged = !tvBody.getText().toString().equals(MyInfo.getInstance().body_type);
                        break;
                    case R.id.tvTall:
                        bChanged = !tvTall.getText().toString().equals("" + MyInfo.getInstance().height);
                        break;
                    case R.id.etArea:
                        bChanged = !etArea.getText().toString().equals(MyInfo.getInstance().address);
                        break;
                    case R.id.tvBelief:
                        bChanged = !tvBelief.getText().toString().equals(MyInfo.getInstance().religion);
                        break;
                    case R.id.tvDrinking:
                        bChanged = !tvDrinking.getText().toString().equals(MyInfo.getInstance().drink);
                        break;
                    case R.id.tvSmoking:
                        bChanged = !tvSmoking.getText().toString().equals(MyInfo.getInstance().smoke);
                        break;
                }

            }
        }).show();
    }

    @OnClick({R.id.tvHobby, R.id.tvLove})
    void onSelectMulti(TextView view) {
        Intent intent = new Intent(this, SelectLoveActivity.class);

        switch (view.getId()) {
            case R.id.tvHobby:
                intent = new Intent(this, SelectInterestActivity.class);
                intent.putExtra("type", "hobby");
                intent.putParcelableArrayListExtra("data", interestList);
                break;
            case R.id.tvLove:
                intent.putExtra("type", "love");
                intent.putExtra("data", tvLove.getText().toString());
                break;
        }

        this.startActivityForResult(intent, 0x0110);
    }

    void uploadImages() {
        final ArrayList<Integer> ids = new ArrayList<>();
        ArrayList<File> imgs = new ArrayList<>();

        for (int i = 0; i < mAdapter.getItems().size(); i++) {
            ImageInfo info = (ImageInfo)mAdapter.getItem(i);
            if (info.image != null) {
                ids.add(i);
                imgs.add(info.image);
            }
        }

        this.showProgress(this);
        MultipartBody.Part[] images = new MultipartBody.Part[imgs.size()];
        int n = 0;
        for (Object info : imgs) {
            File file = (File) info;
            RequestBody photo = RequestBody.create(MediaType.parse("image/*"), file);
            images[n++] = MultipartBody.Part.createFormData("uploadfile[]", file.getName(), photo);
        }

        Net.instance().api.uploadFiles(images)
                .enqueue(new Net.ResponseCallBack<MUploadFiles>() {
                    @Override
                    public void onSuccess(MUploadFiles response) {
                        super.onSuccess(response);
                        hideProgress();

                        if (response.arr_name != null) {
                            for (int i = 0; i < response.arr_name.length; i++) {
                                ImageInfo info = (ImageInfo) mAdapter.getItem(ids.get(i));
                                if (info != null) {
                                    info.img_url = response.arr_name[i];
                                }
                            }
                        }

                        doSignup();
                    }

                    @Override
                    public void onFailure(MError response) {
                        super.onFailure(response);

                        networkErrorOccupied(response);
                    }
                });
    }

    void getInterestList() {
        showProgress(this);
        Net.instance().api.get_interest(MyInfo.getInstance().uid)
                .enqueue(new Net.ResponseCallBack<MInterest>() {
                    @Override
                    public void onSuccess(MInterest response) {
                        super.onSuccess(response);
                        hideProgress();
                        interestList = response.interest;

                        ArrayList<String> parent = new ArrayList<>();
                        for (Interest item : interestList) {
                            if (item.selectedChildCount() > 0) {
                                parent.add(item.name);
                            }
                        }
                        tvHobby.setText(TextUtils.join(",", parent));
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

    @OnClick(R.id.btnRegistering)
    void onRegistering() {

        if (bChanged) {
            new AlertDialog.Builder(this).setTitle("변경").setMessage("수정된 프로필을 저장할까요?").setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    onConfirm();
                }
            }).setNegativeButton(R.string.cancel, null).show();
        }
    }

    void onConfirm() {
        String nickname = etNickname.getText().toString();
        String tall = tvTall.getText().toString();
        String area = etArea.getText().toString();
//        String age = tvAge.getText().toString();
        String school = etSchool.getText().toString();

        if (mAdapter.getItemCount() < PHOTO_MIN_COUNT) {
            Toaster.showShort(this, R.string.profile_upload_guide);
            return;
        }

        //if (!StringUtil.hasData(nickname)) {
        if (nickname.isEmpty()) {
            Toaster.showShort(this, R.string.input_nickname);
            return;
        }

        //if (!StringUtil.hasData(tall)) {
        if (tall.isEmpty()) {
            Toaster.showShort(this, R.string.select_tall);
            return;
        }

        //if (!StringUtil.hasData(school)) {
		if (school.isEmpty()) {
            Toaster.showShort(this, R.string.select_school);
            return;
        }

        //if (!StringUtil.hasData(area)) {
        if (area.isEmpty()) {
            Toaster.showShort(this, R.string.input_area);
            return;
        }
//        if (!StringUtil.hasData(age)) {
//            Toaster.showShort(this, R.string.select_age);
//            return;
//        }
        //if (!StringUtil.hasData(school)) {
        if (school.isEmpty()) {
            Toaster.showShort(this, R.string.select_school);
            return;
        }
		
		if (tvBelief.getText().toString().trim().equals("")) {
            Toaster.showShort(this, R.string.select_belief);
            return;
        }

        if (tvDrinking.getText().toString().trim().equals("")) {
            Toaster.showShort(this, R.string.select_drinking);
            return;
        }

        if (tvSmoking.getText().toString().trim().equals("")) {
            Toaster.showShort(this, R.string.select_smoking);
            return;
        }

        if (tvBody.getText().toString().trim().equals("")) {
            Toaster.showShort(this, R.string.select_body);
            return;
        }

        if (tvHobby.getText().toString().trim().equals("")) {
            Toaster.showShort(this, R.string.select_hobby);
            return;
        }

        if (tvLove.getText().toString().trim().equals("")) {
            Toaster.showShort(this, R.string.select_love_style);
            return;
        }

        boolean bRet = true;

        for (Object info : mAdapter.getItems()) {
            ImageInfo imageInfo = (ImageInfo) info;
            if (imageInfo.image != null) {
                bRet = false;
                break;
            }
        }

        if (bRet) {
            doSignup();
        } else {
            uploadImages();
        }
    }

    void doSignup() {

        showProgress(this);
        Net.instance().api.update_profile(
                MyInfo.getInstance().uid,
                etNickname.getText().toString(),
                Integer.parseInt(tvTall.getText().toString()),
                new Gson().toJson(mAdapter.getItems()),
                etArea.getText().toString(),
                etSchool.getText().toString(),
                etJob.getText().toString(),
                tvBelief.getText().toString(),
                tvDrinking.getText().toString(),
                tvSmoking.getText().toString(),
                tvBody.getText().toString(),
                new Gson().toJson(interestList),
                tvLove.getText().toString(),
                etSelfIntroduction.getText().toString()
        )
                .enqueue(new Net.ResponseCallBack<MBase>() {
                    @Override
                    public void onSuccess(MBase response) {
                        super.onSuccess(response);
                        hideProgress();

                        MyInfo.getInstance().nickname = etNickname.getText().toString();
                        MyInfo.getInstance().height = Integer.parseInt(tvTall.getText().toString());
                        MyInfo.getInstance().address = etArea.getText().toString();
                        MyInfo.getInstance().school = etSchool.getText().toString();
                        MyInfo.getInstance().job = etJob.getText().toString();
                        MyInfo.getInstance().religion = tvBelief.getText().toString();
                        MyInfo.getInstance().smoke = tvSmoking.getText().toString();
                        MyInfo.getInstance().body_type = tvBody.getText().toString();
                        MyInfo.getInstance().love_style = tvLove.getText().toString();
                        MyInfo.getInstance().intro = etSelfIntroduction.getText().toString();
                        MyInfo.getInstance().save(ProfileChangeActivity.this);

                        bChanged = false;
                        Toaster.showShort(ProfileChangeActivity.this, "저장되었습니다.");
                        MyInfo.getInstance().status = PRFILE_STATUS_CHECKING;
                        finish();
                    }

                    @Override
                    public void onFailure(MError response) {
                        super.onFailure(response);

                        networkErrorOccupied(response);
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
            bChanged = true;
            String string = textView.getText().toString();
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
}
