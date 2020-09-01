package com.character.microblogapp.ui.page.community;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.character.microblogapp.R;
import com.character.microblogapp.data.MyInfo;
import com.character.microblogapp.model.MCommunity;
import com.character.microblogapp.model.MCommunityTab;
import com.character.microblogapp.model.MError;
import com.character.microblogapp.net.Net;
import com.character.microblogapp.ui.adapter.CommunityAdapter;
import com.character.microblogapp.ui.adapter.CommunityTabAdapter;
import com.character.microblogapp.ui.page.BaseActivity;
import com.character.microblogapp.util.Toaster;

import java.util.ArrayList;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.OnClick;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
public class CommunityActivity extends BaseActivity {

    @BindView(R.id.btnMenu)
    ImageButton btnMenu;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.rcv_community)
    RecyclerView rvContent;

    @BindView(R.id.rcv_tabs)
    RecyclerView rvTabs;

    @BindView(R.id.btnBack)
            ImageButton btnBack;

    int pageNum = 1;
    int pageCount = 0;
    boolean loadingEnd = false;
    boolean lockRcv = false;

    int type = 0;
    private CommunityAdapter adapter;
    private CommunityTabAdapter tabAdapter;

    private ArrayList<MCommunity.Result> arrContent = new ArrayList<>();

    @BindArray(R.array.community_type)
    String[] communityType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewDataBinding binding = DataBindingUtil.setContentView(this,R.layout.activity_community);

    }

    @Override
    protected void initUI() {
        super.initUI();

        tvTitle.setText("커뮤니티");

        btnMenu.setImageResource(R.drawable.ico_menu);
        btnBack.setVisibility(View.VISIBLE);

        adapter = new CommunityAdapter(this, arrContent, new CommunityAdapter.OnItemClickListener() {
            @Override
            public void onDetail(int pos) {
                MCommunity.Result selected = arrContent.get(pos);
                if (selected != null) {
                    Intent it = new Intent(CommunityActivity.this, CommunityDetailActivity.class);
                    it.putExtra("id", selected.uid);
                    startActivity(it);
                }
            }
        });

        rvContent.setAdapter(adapter);
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
                    getCommunityList();
                }
            }
        });

        ArrayList<MCommunityTab> tabs = getTabList();
        tabAdapter = new CommunityTabAdapter(this, tabs, new CommunityTabAdapter.OnItemClickListener() {
            @Override
            public void onTabSelect(int pos) {
                type = pos;
                initCommunityList();
            }
        });
        rvTabs.setAdapter(tabAdapter);
        tabAdapter.notifyDataSetChanged();



        /*관리자 아이디 임시*/
        int adminUid = MyInfo.getInstance().uid;
        if(adminUid >= 6473 && adminUid <= 6475){
            btnMenu.setVisibility(View.VISIBLE);
        }else{
            btnMenu.setVisibility(View.GONE);
        }



    }

    ArrayList<MCommunityTab> getTabList() {
        ArrayList<MCommunityTab> result = new ArrayList<>();
        for (int ind = 0; ind < communityType.length; ind++) {
            MCommunityTab tab = new MCommunityTab();
            tab.type = ind;
            tab.name = communityType[ind];
            tab.selected = ind == 0;
            result.add(tab);
        }

        return result;
    }

    @Override
    protected void onResume() {
        super.onResume();
        initCommunityList();

        /*관리자 아이디 임시*/
        int adminUid = MyInfo.getInstance().uid;
        if(adminUid >= 6473 && adminUid <= 6475){
            btnMenu.setVisibility(View.VISIBLE);
        }else{
            btnMenu.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.btnMenu)
    void onMenu() {
        startActivity(this, CommunityWriteActivity.class);
    }

    @OnClick(R.id.btnBack)
    void onBack() {
        finish();
    }

    void initCommunityList() {
        pageNum = 1;
        pageCount = 0;
        loadingEnd = false;
        lockRcv = false;

        getCommunityList();
    }

    void getCommunityList() {
        showProgress(this);
        lockRcv = true;

        Net.instance().api.get_community_list(
                MyInfo.getInstance().uid,
                type,
                pageNum,
                ""
        )
                .enqueue(new Net.ResponseCallBack<MCommunity>() {
                    @Override
                    public void onSuccess(MCommunity response) {
                        super.onSuccess(response);
                        hideProgress();

                        if (response.arr_list.size() < 20) {
                            loadingEnd = true;
                        } else {
                            pageNum++;
                        }

                        if (pageNum == 1) {
                            arrContent.clear();
                        }

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
                            Toaster.showShort(CommunityActivity.this, "오류입니다.");
                        }
                    }
                });
    }

}
