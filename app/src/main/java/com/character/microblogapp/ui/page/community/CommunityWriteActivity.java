package com.character.microblogapp.ui.page.community;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.character.microblogapp.R;
import com.character.microblogapp.data.BaseInfo;
import com.character.microblogapp.data.ImageInfo;
import com.character.microblogapp.data.MyInfo;
import com.character.microblogapp.model.MBase;
import com.character.microblogapp.model.MError;
import com.character.microblogapp.model.MUploadFiles;
import com.character.microblogapp.net.Net;
import com.character.microblogapp.ui.dialog.SelectDialog;
import com.character.microblogapp.ui.page.BaseActivity;
import com.character.microblogapp.ui.page.intro.adapter.Image2Adapter;
import com.character.microblogapp.ui.page.intro.adapter.ImageAdapter;
import com.character.microblogapp.util.MediaManager;
import com.character.microblogapp.util.Toaster;
import com.character.microblogapp.util.Util;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
public class CommunityWriteActivity extends BaseActivity {

    @BindView(R.id.btnBack)
    ImageButton btnBack;

    @BindView(R.id.btnMenu)
    ImageButton btnMenu;

    @BindView(R.id.txvMenu)
    TextView txvMenu;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.rlType)
    RelativeLayout rlType;

    @BindView(R.id.tvType)
    TextView tvType;

    @BindView(R.id.etTitle)
    EditText etTitle;

    @BindView(R.id.etDetail)
    EditText etDetail;

    @BindView(R.id.rvImage)
    RecyclerView rvImage;

    @BindView(R.id.txv_content_count)
    TextView txvCount;

    Image2Adapter imageAdapter;
    MediaManager mediaManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewDataBinding binding = DataBindingUtil.setContentView(this,R.layout.activity_communi_write);

//        setFinishAppWhenPressedBackKey();
    }

    @Override
    protected void initUI() {
        super.initUI();

        tvTitle.setText("커뮤니티");
        btnMenu.setVisibility(View.GONE);
        txvMenu.setVisibility(View.VISIBLE);
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

        imageAdapter.PHOTO_MAX_COUNT = 3;
        imageAdapter.setCallback(new Image2Adapter.Callback() {
            @Override
            public void onClick(int position, BaseInfo data) {
                onAddImage();
            }

            @Override
            public void onDelete(int position, BaseInfo data) {
                imageAdapter.remove(position);
                imageAdapter.notifyDataSetChanged();

//                if ((imageAdapter.getItemCount() - 1) < 3)
//                    addEmptyPhoto();

            }
        });
        rvImage.setAdapter(imageAdapter);

        etDetail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                txvCount.setText(String.format("%d/%,d", etDetail.getText().toString().length(), 1000));
            }
        });
    }

    @BindArray(R.array.community_type)
    String[] ask_type;

    int type = 0;

    @OnClick(R.id.rlType)
    void onType() {

        ArrayList<String> arrayList = new ArrayList<>();
        for (int ind = 1; ind < ask_type.length; ind++) {
            arrayList.add(ask_type[ind]);
        }

        new SelectDialog(this, arrayList, new SelectDialog.Callback() {
            @Override
            public void onConfirm(int position, String data) {
                type = position + 1;
                tvType.setText(data);
            }
        }).show();
    }

    void addCommunity(String uploadedFiles) {
        Net.instance().api.add_community(MyInfo.getInstance().uid, 0, 1, etTitle.getText().toString(),
                etDetail.getText().toString(), uploadedFiles)
                .enqueue(new Net.ResponseCallBack<MBase>() {
                    @Override
                    public void onSuccess(MBase response) {
                        super.onSuccess(response);
                        hideProgress();
                        Toaster.showShort(CommunityWriteActivity.this, "저장되었습니다.");
                        finish();
                    }

                    @Override
                    public void onFailure(MError response) {
                        super.onFailure(response);
                        networkErrorOccupied(response);
                    }
                });
    }

    void uploadImages() {
        if (imageAdapter.getItemCount() - 1 < 1) {
            addCommunity("");
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
                            addCommunity(TextUtils.join(",", response.arr_name));
                        } else {
                            addCommunity("");
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

    @OnClick(R.id.txvMenu)
    void onSave() {
//        if (type == 0) {
//            Toaster.showShort(this, "게시판을 선택해주세요");
//            return;
//        }
        if (etTitle.getText().toString().isEmpty()) {
            Toaster.showShort(this, "제목을 입력해주세요");
            return;
        }
        if (etTitle.getText().toString().length() < 2 || etTitle.getText().toString().length() > 30) {
            Toaster.showShort(this, "제목을 확인해주세요.");
            return;
        }
        if (etDetail.getText().toString().isEmpty()) {
            Toaster.showShort(this, "글을 입력해주세요");
            return;
        }
        if (etDetail.getText().toString().length() < 2 || etDetail.getText().toString().length() > 1000) {
            Toaster.showShort(this, "글을 확인해주세요.");
            return;
        }

        uploadImages();
    }

    @OnClick(R.id.btnBack)
    void onBack() {
        finish();
    }

    void fileUpload(File file) {
        MultipartBody.Part[] images = new MultipartBody.Part[1];
        RequestBody photo = RequestBody.create(MediaType.parse("image/*"), file);
        images[0] = MultipartBody.Part.createFormData("uploadfile[]", file.getName(), photo);

        showProgress(this);
        Net.instance().api.uploadFiles(images)
                .enqueue(new Net.ResponseCallBack<MUploadFiles>() {
                    @Override
                    public void onSuccess(MUploadFiles response) {
                        super.onSuccess(response);
                        hideProgress();

                        if (response.arr_name != null) {

                        }
                    }

                    @Override
                    public void onFailure(MError response) {
                        super.onFailure(response);
                        hideProgress();
                        networkErrorOccupied(response);
                    }
                });
    }

}
