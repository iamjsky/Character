package com.character.microblogapp.ui.page.setting;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.character.microblogapp.R;
import com.character.microblogapp.data.BaseInfo;
import com.character.microblogapp.data.ImageInfo;
import com.character.microblogapp.data.MyInfo;
import com.character.microblogapp.model.MBase;
import com.character.microblogapp.model.MError;
import com.character.microblogapp.model.MQna;
import com.character.microblogapp.model.MUploadFiles;
import com.character.microblogapp.net.Net;
import com.character.microblogapp.ui.adapter.QnaAdapter;
import com.character.microblogapp.ui.dialog.SelectDialog;
import com.character.microblogapp.ui.page.BaseActivity;
import com.character.microblogapp.ui.page.intro.adapter.Image2Adapter;
import com.character.microblogapp.ui.page.intro.adapter.ImageAdapter;
import com.character.microblogapp.util.MediaManager;
import com.character.microblogapp.util.Toaster;
import com.character.microblogapp.util.Util;
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
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
public class AskActivity extends BaseActivity {

    @BindView(R.id.btnBack)
    ImageButton btnBack;

    @BindView(R.id.btnMenu)
    ImageButton btnMenu;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.tv1)
    TextView tv1;

    @BindView(R.id.tv2)
    TextView tv2;

    @BindView(R.id.img1)
    ImageView img1;

    @BindView(R.id.img2)
    ImageView img2;

    @BindView(R.id.rvContent)
    RecyclerView rvContent;

    @BindView(R.id.rvImage)
    RecyclerView rvImage;

    @BindView(R.id.svDetail)
    ScrollView svDetail;

    @BindView(R.id.rlType)
    RelativeLayout rlType;

    @BindView(R.id.tvType)
    TextView tvType;

    @BindView(R.id.etTitle)
    EditText etTitle;

    @BindView(R.id.etDetail)
    EditText etDetail;

    private QnaAdapter adapter;
    private ArrayList<MQna.Result> arrContent = new ArrayList<>();

    Image2Adapter imageAdapter;

    MediaManager mediaManager;

    private static final int PHOTO_MIN_COUNT = 3;
    private static final int PHOTO_MAX_COUNT = 8;

    int m_nIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewDataBinding binding = DataBindingUtil.setContentView(this,R.layout.activity_ask);
    }

    @Override
    protected void initUI() {
        super.initUI();

        tvTitle.setText("관리자 문의");
        btnMenu.setVisibility(View.GONE);
        btnBack.setVisibility(View.VISIBLE);
        mediaManager = new MediaManager(this);
        mediaManager.setMediaCallback(new MediaManager.MediaCallback() {
            @Override
            public void onSelected(Boolean isVideo, File file, Bitmap bitmap, String videoPath, String thumbPath) {
                int insertPosition = imageAdapter.getItemCount() - 1;
                imageAdapter.insert(insertPosition, new ImageInfo(file));
//                if ((imageAdapter.getItemCount() - 1) > PHOTO_MAX_COUNT)
//                    imageAdapter.removeLastObject();

                imageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(int code, String err) {

            }

            @Override
            public void onDelete() {

            }
        });

        imageAdapter = new Image2Adapter(this, new ArrayList<BaseInfo>());
        imageAdapter.setCallback(new Image2Adapter.Callback() {
            @Override
            public void onClick(int position, BaseInfo data) {
                onAddImage();
            }

            @Override
            public void onDelete(int position, BaseInfo data) {
                imageAdapter.remove(position);
                imageAdapter.notifyDataSetChanged();

//                if ((imageAdapter.getItemCount() - 1) < PHOTO_MAX_COUNT)
//                    addEmptyPhoto();

            }
        });
        rvImage.setAdapter(imageAdapter);

//        addEmptyPhoto();

        adapter = new QnaAdapter(AskActivity.this, arrContent, new QnaAdapter.OnItemClickListener() {
            @Override
            public void onDetail(int pos) {
                adapter.notifyDataSetChanged();
            }
        });

        rvContent.setAdapter(adapter);
        rvContent.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        resetTap(1);
    }

    void addEmptyPhoto() {
        //이미지 추가 부분
        imageAdapter.add(null);
        imageAdapter.notifyDataSetChanged();
    }

    void uploadImages() {
        if (imageAdapter.getItemCount() - 1 < 1) {
            apiAsk("");
            return;
        }

        showProgress(this);
        MultipartBody.Part[] images = new MultipartBody.Part[imageAdapter.getItems().size()];
        int n = 0;
        for (Object info : imageAdapter.getItems()) {
            ImageInfo imageInfo = (ImageInfo) info;
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
                        hideProgress();

                        if (response.arr_name != null) {
//                            for (int i = 0; i < response.arr_name.length; i++) {
//                                ImageInfo info = (ImageInfo) imageAdapter.getItem(i);
//                                if (info != null) {
//                                    info.img_url = response.arr_name[i];
//                                }
//                            }
                            apiAsk(TextUtils.join(",", response.arr_name));
                        } else {
                            apiAsk("");
                        }
                    }

                    @Override
                    public void onFailure(MError response) {
                        super.onFailure(response);

                        networkErrorOccupied(response);
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
            requestPermission(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, PERMISSIONS_IMAGE_REQUEST);
            return;
        }

        mediaManager.showMediaManager("", null, false);
    }

    @OnClick(R.id.btnBack)
    void onBack() {
        finish();
    }

    @OnClick({R.id.rl1, R.id.rl2})
    void onClick(View view) {
        if (view.getId() == R.id.rl1) {
            resetTap(1);
        } else if (view.getId() == R.id.rl2) {
            resetTap(2);
        }
    }

    @BindArray(R.array.ask_type)
    String[] ask_type;

    @OnClick(R.id.rlType)
    void onType() {

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.addAll(Arrays.asList(ask_type));

        new SelectDialog(this, arrayList, new SelectDialog.Callback() {
            @Override
            public void onConfirm(int position, String data) {
                tvType.setText(data);
            }
        }).show();
    }

    @OnClick(R.id.tvAsk)
    void onAsk(View view) {
        uploadImages();
    }

    void resetTap(int tap) {
        tv1.setTextColor(Color.parseColor("#888888"));
        img1.setVisibility(View.GONE);
        tv2.setTextColor(Color.parseColor("#888888"));
        img2.setVisibility(View.GONE);
        svDetail.setVisibility(View.GONE);
        rvContent.setVisibility(View.GONE);

        if (tap == 1) {
            tv1.setTextColor(Color.parseColor("#f3788b"));
            img1.setVisibility(View.VISIBLE);
            svDetail.setVisibility(View.VISIBLE);

            etDetail.setText("");
            etTitle.setText("");
        } else if (tap == 2) {
            tv2.setTextColor(Color.parseColor("#f3788b"));
            img2.setVisibility(View.VISIBLE);
            rvContent.setVisibility(View.VISIBLE);

            apiList();
        }
    }

    void apiAsk(String files) {
        String title = etTitle.getText().toString();
        String content = etDetail.getText().toString();

        //if (!StringUtil.hasData(title)) {
        if (title.isEmpty()) {
            Toaster.showShort(this, "제목을 입력해주세요.");
            return;
        }

        //if (!StringUtil.hasData(content)) {
        if (content.isEmpty()) {
            Toaster.showShort(this, "내용을 입력해주세요.");
            return;
        }

        showProgress(this);
        Net.instance().api.add_qna(MyInfo.getInstance().uid, tvType.getText().toString(), title, content, files)
                .enqueue(new Net.ResponseCallBack<MBase>() {
                    @Override
                    public void onSuccess(MBase response) {
                        super.onSuccess(response);
                        hideProgress();

                        resetTap(2);
                    }

                    @Override
                    public void onFailure(MError response) {
                        super.onFailure(response);
                        hideProgress();

                        if (response.resultcode == 500) {
                            networkErrorOccupied(response);
                        } else {
                            Toaster.showShort(AskActivity.this, "오류입니다.");
                        }
                    }
                });
    }

    void apiList() {
        showProgress(this);
        Net.instance().api.get_qna_list(MyInfo.getInstance().uid)
                .enqueue(new Net.ResponseCallBack<MQna>() {
                    @Override
                    public void onSuccess(MQna response) {
                        super.onSuccess(response);
                        hideProgress();

                        arrContent.clear();
                        arrContent.addAll(response.arr_list);

                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(MError response) {
                        super.onFailure(response);
                        hideProgress();

                        if (response.resultcode == 500) {
                            networkErrorOccupied(response);
                        } else {
                            Toaster.showShort(AskActivity.this, "오류입니다.");
                        }
                    }
                });
    }

}
