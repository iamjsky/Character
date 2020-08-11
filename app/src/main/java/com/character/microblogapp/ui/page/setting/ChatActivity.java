package com.character.microblogapp.ui.page.setting;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.character.microblogapp.MyApplication;
import com.character.microblogapp.R;
import com.character.microblogapp.data.Constant;
import com.character.microblogapp.data.MyInfo;
import com.character.microblogapp.fcm.EventMessage;
import com.character.microblogapp.model.MBase;
import com.character.microblogapp.model.MChat;
import com.character.microblogapp.model.MEnergy;
import com.character.microblogapp.model.MError;
import com.character.microblogapp.model.MUploadFiles;
import com.character.microblogapp.net.Net;
import com.character.microblogapp.ui.adapter.ChatAdapter;
import com.character.microblogapp.ui.dialog.ConfirmDialog;
import com.character.microblogapp.ui.dialog.ConfirmDialog2;
import com.character.microblogapp.ui.dialog.ImageGalleryDialog;
import com.character.microblogapp.ui.page.BaseActivity;
import com.character.microblogapp.ui.page.character.CharacterActivity;
import com.character.microblogapp.ui.page.profile.ProfileActivity;
import com.character.microblogapp.ui.page.profile.ProfileDISCActivity;
import com.character.microblogapp.ui.page.setting.dialog.BlameDialog;
import com.character.microblogapp.ui.page.setting.dialog.ChatDialog;
import com.character.microblogapp.util.MediaManager;
import com.character.microblogapp.util.Toaster;
import com.theartofdev.edmodo.cropper.CropImage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ChatActivity extends BaseActivity {

    @BindView(R.id.btnBack)
    ImageButton btnBack;

    @BindView(R.id.btnMenu)
    ImageButton btnMenu;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.etDetail)
    EditText etDetail;

    @BindView(R.id.rvContent)
    RecyclerView rvContent;

    int pageNum = 1;
    int pageCount = 0;
    boolean loadingEnd = false;
    boolean lockRcv = false;

    private ChatAdapter adapter;
    private ArrayList<MChat.Result> arrContent = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        ((MyApplication)getApplication()).setChatActivity(this);
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdated(EventMessage event) {
        if (event.nWhat == 10007)
            apiList();
    }
    @Override
    protected void initUI() {
        super.initUI();

        tvTitle.setText(getIntent().getStringExtra("nickname") + " 님과의 대화");
        btnMenu.setVisibility(View.VISIBLE);
        btnMenu.setImageResource(R.drawable.ic_more);

        adapter = new ChatAdapter(this, arrContent, new ChatAdapter.ActionListener() {
            @Override
            public void onClickImage(int id) {
                Intent goProfile = new Intent(ChatActivity.this, ProfileActivity.class);
                goProfile.putExtra("usr_uid", id);
                startActivity(goProfile);
            }
            @Override
            public void onClickImg(String url) {

                ArrayList<String> arr = new ArrayList<>();

                arr.add(url);

                new ImageGalleryDialog(ChatActivity.this, arr, 0).show();
            }
        });

        rvContent.setAdapter(adapter);

        rvContent.setAdapter(adapter);
        rvContent.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        rvContent.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int firstVisibleItem = recyclerView.getChildAdapterPosition(recyclerView.getChildAt(0));
                int visibleCnt = recyclerView.getChildCount();
                int totalItemCnt = adapter.getItemCount();

                if (loadingEnd) {
                    return;
                }

                if (totalItemCnt != 0 && firstVisibleItem >= (totalItemCnt - visibleCnt) && !lockRcv) {
                    apiList();
                }
            }
        });

        apiList();
    }

    @OnClick(R.id.btnMenu)
    void onMenu() {
        new ChatDialog(this, new ChatDialog.ActionListener() {
            @Override
            public void onSucess(int type) {
                switch (type) {
                    case 1:
                        // 대화법 확인

                        Intent it = new Intent(ChatActivity.this, ChatMethodActivity.class);
                        it.putExtra("id", getIntent().getIntExtra("other_id", 0));
                        startActivity(it);
//                        ConfirmDialog2.show(ChatActivity.this, "대화법을 확인합니다.", "에너지 20개를 사용할까요?", new ConfirmDialog2.ActionListener() {
//                            @Override
//                            public void onOk() {
//
//                                if (MyInfo.getInstance().energy >= 20) {
//
//                                    calcEnergy(20);
//
//                                } else {
//
//                                    ConfirmDialog2.show(ChatActivity.this, "에너지가 부족해요!!", String.format(getString(R.string.confirm_get_energy), (10 - MyInfo.getInstance().energy)),
//                                            new ConfirmDialog2.ActionListener() {
//                                                @Override
//                                                public void onOk() {
//                                                    Intent goEnergy = new Intent(ChatActivity.this, EnergyupActivity.class);
//                                                    goEnergy.putExtra("nickname", getIntent().getStringExtra("nickname"));
//                                                    startActivity(goEnergy);
//                                                }
//                                            });
//                                }
//                            }
//                        });
                        break;
                    case 2:
                        // 상대방 신고
                        report();
                        break;
                    case 3:
                        // 대화종료
                        new ConfirmDialog(ChatActivity.this, "종료하시겠습니까?", new ConfirmDialog.ActionListener() {
                            @Override
                            public void onOk() {
                                removeChatRoom();
                            }
                        }).show();
                }
            }
        }).show();
    }

    private void calcEnergy(int count) {
        Net.instance().api.calc_point(MyInfo.getInstance().uid, count, "대화법 확인하기")
                .enqueue(new Net.ResponseCallBack<MEnergy>() {
                    @Override
                    public void onSuccess(MEnergy response) {
                        super.onSuccess(response);
                        hideProgress();

                        MyInfo.getInstance().energy = response.energy;

                        Intent it = new Intent(ChatActivity.this, ChatMethodActivity.class);
                        it.putExtra("id", getIntent().getIntExtra("other_id", 0));
                        startActivity(it);
                    }

                    @Override
                    public void onFailure(MError response) {
                        super.onFailure(response);
                        hideProgress();
                        if (response.resultcode == 500) {
                            networkErrorOccupied(response);
                        } else {
                            new AlertDialog.Builder(ChatActivity.this).setTitle("").setMessage(response.res_msg).setPositiveButton(R.string.confirm, null).show();
                        }
                    }
                });
    }

    void removeChatRoom() {
        showProgress(this);
        Net.instance().api.remove_chat_room(MyInfo.getInstance().uid, getIntent().getIntExtra("id", 0))
                .enqueue(new Net.ResponseCallBack<MBase>() {
                    @Override
                    public void onSuccess(MBase response) {
                        super.onSuccess(response);
                        hideProgress();
                        finish();
                    }

                    @Override
                    public void onFailure(MError response) {
                        super.onFailure(response);
                        hideProgress();

                        if (response.resultcode == 500) {
                            networkErrorOccupied(response);
                        } else {
                            new AlertDialog.Builder(ChatActivity.this).setTitle("").setMessage(response.res_msg).setPositiveButton(R.string.confirm, null).show();
                        }
                    }
                });
    }

    void report() {
        new BlameDialog(this, new BlameDialog.ActionListener() {
            @Override
            public void onSucess(int type) {
                reqBlame(BLAME_REASON[type - 1]);
            }
        }).show();
    }

    private void reqBlame(String reason) {
        int other_usr_uid = getIntent().getIntExtra("other_id", 0);

        showProgress(this);
        Net.instance().api.report_user(MyInfo.getInstance().uid, other_usr_uid, reason)
                .enqueue(new Net.ResponseCallBack<MBase>() {
                    @Override
                    public void onSuccess(MBase response) {
                        super.onSuccess(response);
                        hideProgress();

                        new AlertDialog.Builder(ChatActivity.this).setTitle("신고하기").setMessage("정확히 신고처리되었습니다.").setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        }).show();
                    }

                    @Override
                    public void onFailure(MError response) {
                        super.onFailure(response);
                        hideProgress();

                        if (response.resultcode == 500) {
                            networkErrorOccupied(response);
                        } else {
                            new AlertDialog.Builder(ChatActivity.this).setTitle("").setMessage("이미 신고한 회원입니다.").setPositiveButton(R.string.confirm, null).show();
                        }
                    }
                });
    }

    @OnClick(R.id.btnBack)
    void onBack() {
        finish();
    }

    MediaManager mediaManager;

    @OnClick(R.id.imvImageAdd)
    void onClickImage() {
        if (mediaManager == null) {
            mediaManager = new MediaManager(this);
            mediaManager.setMediaCallback(new MediaManager.MediaCallback() {
                @Override
                public void onSelected(Boolean isVideo, File file, Bitmap bitmap, String videoPath, String thumbPath) {
                    fileUpload(file);
                }

                @Override
                public void onFailed(int code, String err) {

                }

                @Override
                public void onDelete() {

                }
            });
        }

        mediaManager.showMediaManager("", null, false);
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
                            apiSend(2, response.arr_name[0]);
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
        if (requestCode == MediaManager.REQUEST_PERMISSION_CAMERA ||
                requestCode == MediaManager.REQUEST_PERMISSION_ALBUM) {
            mediaManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @OnClick(R.id.btnSend)
    void onSend() {
        apiSend(1, etDetail.getText().toString());
    }

    void apiSend(int type, String content) {
        showProgress(this);
        Net.instance().api.send_chat(
                MyInfo.getInstance().uid,
                getIntent().getIntExtra("id", 0),
                getIntent().getIntExtra("other_id", 0),
                type,
                type == 1 ? content : "",
                type == 2 ? content : ""
        )
                .enqueue(new Net.ResponseCallBack<MBase>() {
                    @Override
                    public void onSuccess(MBase response) {
                        super.onSuccess(response);
                        hideProgress();

                        etDetail.setText("");

                        pageNum = 1;
                        pageCount = 0;
                        loadingEnd = false;
                        lockRcv = false;

                        apiList();
                    }

                    @Override
                    public void onFailure(MError response) {
                        super.onFailure(response);
                        hideProgress();

                        if (response.resultcode == 500) {
                            networkErrorOccupied(response);
                        } else {
                            Toaster.showShort(ChatActivity.this, "오류입니다.");
                        }
                    }
                });
    }

    void apiList() {
//        showProgress(this);
        Net.instance().api.get_chat_list(MyInfo.getInstance().uid,
                getIntent().getIntExtra("id", 0),
                getIntent().getIntExtra("other_id", 0),
                pageNum
        )
                .enqueue(new Net.ResponseCallBack<MChat>() {
                    @Override
                    public void onSuccess(MChat response) {
                        super.onSuccess(response);
//                        hideProgress();

                        if (response.arr_list.size() < 20) {
                            loadingEnd = true;
                        } else {
                            pageNum++;
                        }

                        if (pageNum == 1) {
                            arrContent.clear();
                        }

                        for (MChat.Result item: response.arr_list) {
                            arrContent.add(0, item);
                        }
                        if (adapter.getItemCount() > 1) // modified by YJ 20200320
                            rvContent.smoothScrollToPosition(adapter.getItemCount() - 1);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(MError response) {
                        super.onFailure(response);
//                        hideProgress();

                        if (response.resultcode == 500) {
                            networkErrorOccupied(response);
                        } else {
                            Toaster.showShort(ChatActivity.this, "오류입니다.");
                        }
                    }
                });
    }

}
