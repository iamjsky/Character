package com.character.microblogapp.ui.page.setting;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.character.microblogapp.R;
import com.character.microblogapp.data.MyInfo;
import com.character.microblogapp.fcm.EventMessage;
import com.character.microblogapp.model.MAlarm;
import com.character.microblogapp.model.MBase;
import com.character.microblogapp.model.MError;
import com.character.microblogapp.net.Net;
import com.character.microblogapp.ui.adapter.AlarmAdapter;
import com.character.microblogapp.ui.adapter.ReceiveEnergyAdapter;
import com.character.microblogapp.ui.page.BaseActivity;
import com.character.microblogapp.ui.page.profile.ProfileActivity;
import com.character.microblogapp.util.Toaster;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class AlarmListActivity extends BaseActivity {

    @BindView(R.id.btnBack)
    ImageButton btnBack;

    @BindView(R.id.btnMenu)
    ImageButton btnMenu;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.rvContent)
    RecyclerView rvContent;

    private AlarmAdapter adapter;
    private ArrayList<MAlarm.Result> arrContent = new ArrayList<>();

    int pageNum = 1;
    int pageCount = 0;
    boolean loadingEnd = false;
    boolean lockRcv = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_list);
    }

    @Override
    protected void initUI() {
        super.initUI();

        tvTitle.setText("알림");
        btnMenu.setVisibility(View.GONE);

        adapter = new AlarmAdapter(AlarmListActivity.this, arrContent, new AlarmAdapter.OnItemClickListener() {
            @Override
            public void onDetail(int pos) {
                MAlarm.Result info = arrContent.get(pos);
                if (info.type == 5) { // 호감표시 - 이력페이지 이동
                    finish();
                    EventBus.getDefault().post(new EventMessage(11000, null));
                    return;
                }

                if (info.type == 6) { // 호감표시 - 회원프로필페이지로 이동
                    finish();
                    EventBus.getDefault().post(new EventMessage(11300, null));
                    return;
                }

                if (info.type == 7) {
                    Intent intent = new Intent(AlarmListActivity.this, ChatActivity.class);
                    intent.putExtra("id", info.other_uid);
                    intent.putExtra("other_id", info.usr_uid);
                    intent.putExtra("nickname", info.nickname);
                    startActivity(intent);
                    return;
                }

                if (info.type == 8 ) {
                    finish();
                    EventBus.getDefault().post(new EventMessage(13000, null));
                    return;
                }

                if (info.type == 3) {
                    Intent intent = new Intent(AlarmListActivity.this, NoticeListActivity.class);
                    startActivity(intent);
                }
            }
        });

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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdated(EventMessage event) {
        if (event.nWhat == 10005)
            new AlertDialog.Builder(this).setTitle("변경").setMessage("좋아요를 승인하실래요?").setPositiveButton("승인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    apiCancel();
                }
            }).show();
    }

    @OnClick(R.id.btnBack)
    void onBack() {
        finish();
    }

    void apiCancel() {
        showProgress(this);
        Net.instance().api.cancel_like(MyInfo.getInstance().uid, getIntent().getIntExtra("other_id", 0))
                .enqueue(new Net.ResponseCallBack<MBase>() {
                    @Override
                    public void onSuccess(MBase response) {
                        super.onSuccess(response);
                        hideProgress();

                        apiList();
                    }

                    @Override
                    public void onFailure(MError response) {
                        super.onFailure(response);
                        hideProgress();

                        if (response.resultcode == 500) {
                            networkErrorOccupied(response);
                        } else {
                            Toaster.showShort(AlarmListActivity.this, "오류입니다.");
                        }
                    }
                });
    }

    void apiList() {
        showProgress(this);
        Net.instance().api.get_alarm_list(MyInfo.getInstance().uid, pageNum)
                .enqueue(new Net.ResponseCallBack<MAlarm>() {
                    @Override
                    public void onSuccess(MAlarm response) {
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
                            Toaster.showShort(AlarmListActivity.this, "오류입니다.");
                        }
                    }
                });
    }

}
