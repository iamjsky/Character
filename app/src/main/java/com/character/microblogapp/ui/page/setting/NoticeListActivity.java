package com.character.microblogapp.ui.page.setting;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.character.microblogapp.R;
import com.character.microblogapp.data.MyInfo;
import com.character.microblogapp.model.MBase;
import com.character.microblogapp.model.MError;
import com.character.microblogapp.model.MEvent;
import com.character.microblogapp.net.Net;
import com.character.microblogapp.ui.adapter.AlarmAdapter;
import com.character.microblogapp.ui.adapter.NoticeAdapter;
import com.character.microblogapp.ui.page.BaseActivity;
import com.character.microblogapp.util.Toaster;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;

public class NoticeListActivity extends BaseActivity {

    @BindView(R.id.btnBack)
    ImageButton btnBack;

    @BindView(R.id.btnMenu)
    ImageButton btnMenu;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.rvContent)
    RecyclerView rvContent;

    private NoticeAdapter adapter;
    private ArrayList<MEvent.Result> arrContent = new ArrayList<>();

    int pageNum = 1;
    int pageCount = 0;
    boolean loadingEnd = false;
    boolean lockRcv = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewDataBinding binding = DataBindingUtil.setContentView(this,R.layout.activity_notice_list);
    }

    @Override
    protected void initUI() {
        super.initUI();

        tvTitle.setText("공지사항");
        btnMenu.setVisibility(View.GONE);

        adapter = new NoticeAdapter(NoticeListActivity.this, arrContent, new NoticeAdapter.OnItemClickListener() {
            @Override
            public void onDetail(int pos) {
                MEvent.Result info = arrContent.get(pos);
                if (info.isSelected) {
                    showNotice(info.uid);
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

    @OnClick(R.id.btnBack)
    void onBack() {
        finish();
    }

    void apiList() {
        showProgress(this);
        Net.instance().api.get_notice_Event_list(pageNum)
                .enqueue(new Net.ResponseCallBack<MEvent>() {
                    @Override
                    public void onSuccess(MEvent response) {
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
                            Toaster.showShort(NoticeListActivity.this, "오류입니다.");
                        }
                    }
                });
    }


    void showNotice(int notice_id) {
        showProgress(this);
        Net.instance().api.show_notice(MyInfo.getInstance().uid, notice_id)
                .enqueue(new Net.ResponseCallBack<MBase>() {
                    @Override
                    public void onSuccess(MBase response) {
                        super.onSuccess(response);
                        hideProgress();
                    }

                    @Override
                    public void onFailure(MError response) {
                        super.onFailure(response);
                        hideProgress();
                    }
                });
    }

}
