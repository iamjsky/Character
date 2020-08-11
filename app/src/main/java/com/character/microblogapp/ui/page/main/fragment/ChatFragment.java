package com.character.microblogapp.ui.page.main.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.character.microblogapp.MyApplication;
import com.character.microblogapp.R;
import com.character.microblogapp.data.MyInfo;
import com.character.microblogapp.model.MBase;
import com.character.microblogapp.model.MChatRoom;
import com.character.microblogapp.model.MError;
import com.character.microblogapp.model.MLeftEnergy;
import com.character.microblogapp.net.Net;
import com.character.microblogapp.ui.adapter.ChartAdapter;
import com.character.microblogapp.ui.dialog.ConfirmDialog2;
import com.character.microblogapp.ui.page.BaseFragment;
import com.character.microblogapp.ui.page.community.CommunityActivity;
import com.character.microblogapp.ui.page.setting.ChatActivity;
import com.character.microblogapp.ui.page.setting.EnergyActivity;
import com.character.microblogapp.util.Toaster;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class ChatFragment extends BaseFragment {

    @BindView(R.id.btnBack)
    ImageButton btnBack;

    @BindView(R.id.btnMenu)
    ImageButton btnMenu;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.rvContent)
    RecyclerView rvContent;

    private ChartAdapter adapter;
    private ArrayList<MChatRoom.Result> arrContent = new ArrayList<>();

    MyApplication mApp = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        createView(inflater, container, R.layout.fragment_chat);

        initData();
        return mRoot;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initVC();
    }

    private void initData() {
        mApp = (MyApplication) getActivity().getApplicationContext();
    }

    private void initUI(View p_view) {
    }

    @OnClick(R.id.btnMenu)
    void onClickMenu() {
        // 커뮤니티페이지로 이동
        Intent intent = new Intent(getContext(), CommunityActivity.class);
        startActivity(intent);
    }

    void initVC() {
        tvTitle.setText("채팅");

        adapter = new ChartAdapter(mApp, arrContent, new ChartAdapter.OnItemClickListener() {
            @Override
            public void onDetail(int pos) {
                if (arrContent.get(pos).status != 1) {
                    return;
                }

                Intent intent = new Intent(getContext(), ChatActivity.class);
                intent.putExtra("id", arrContent.get(pos).uid);
                intent.putExtra("other_id", arrContent.get(pos).other_usr_uid);
                intent.putExtra("nickname", arrContent.get(pos).nickname);
                startActivity(intent);
            }

            @Override
            public void onDeny(int pos) {
                onDoSth(pos, "d");
            }

            @Override
            public void onAllow(int pos) {

                onDoSth(pos, "a");
            }

            @Override
            public void onStartTalk(final int pos) {
                final MChatRoom.Result item = arrContent.get(pos);

                ConfirmDialog2.show(getContext(), item.nickname + "님과 대화를 시작합니다.", "대화는 에너지 20개를 사용합니다.", new ConfirmDialog2.ActionListener() {
                    @Override
                    public void onOk() {

                        if (MyInfo.getInstance().energy >= 20) {

                            onStartChat(pos);

                        } else {

                            ConfirmDialog2.show(getContext(), "에너지가 부족해요!!", String.format(getString(R.string.confirm_get_energy), (10 - MyInfo.getInstance().energy)),
                                    new ConfirmDialog2.ActionListener() {
                                        @Override
                                        public void onOk() {
                                            Intent goEnergy = new Intent(getActivity(), EnergyActivity.class);
                                            startActivity(goEnergy);
                                        }
                                    });
                        }
                    }
                });
            }

            @Override
            public void onCancel(int pos) {
                onDoSth(pos, "c");
            }
        });

        rvContent.setAdapter(adapter);
        rvContent.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        apiList();
    }

    void onStartChat(final int ind) {
        final MChatRoom.Result item = arrContent.get(ind);

        if (item != null) {
            Net.instance().api.open_chat_room(MyInfo.getInstance().uid, item.uid, item.other_usr_uid)
                    .enqueue(new Net.ResponseCallBack<MLeftEnergy>() {
                        @Override
                        public void onSuccess(MLeftEnergy response) {
                            super.onSuccess(response);

                            MyInfo.getInstance().energy = response.left_usr_energy;
                            arrContent.get(ind).status = 1;

                            Intent intent = new Intent(getContext(), ChatActivity.class);
                            intent.putExtra("id", item.uid);
                            intent.putExtra("other_id", item.other_usr_uid);
                            intent.putExtra("nickname", item.nickname);
                            startActivity(intent);
                        }

                        @Override
                        public void onFailure(MError response) {
                            super.onFailure(response);

                            if (response.resultcode == 500) {
                                mParent.networkErrorOccupied(response);
                            } else if (response.resultcode == 10) {
                                Toaster.showShort(mParent, "보유에너지가 부족합니다.");
                            }
                        }
                    });
        }
    }

    void onDoSth(final int ind, final String action) {
        final MChatRoom.Result item = arrContent.get(ind);

        if (item != null) {
            Net.instance().api.process_chat_room(MyInfo.getInstance().uid, item.uid, action)
                    .enqueue(new Net.ResponseCallBack<MBase>() {
                        @Override
                        public void onSuccess(MBase response) {
                            super.onSuccess(response);
                            Toaster.showShort(mParent, "조작이 성공하였습니다");

                            switch (action) {
                                case "a"://승인
                                    item.status = 1;
                                    adapter.notifyItemChanged(ind);
                                    break;
                                case "d"://거절
                                case "c"://취소
                                    arrContent.remove(ind);
                                    adapter.notifyItemRemoved(ind);
                                    break;
                            }
                        }

                        @Override
                        public void onFailure(MError response) {
                            super.onFailure(response);

                            if (response.resultcode == 500) {
                                mParent.networkErrorOccupied(response);
                            }
                        }
                    });
        }
    }

    void apiList() {
        Net.instance().api.get_chat_room_list(MyInfo.getInstance().uid)
                .enqueue(new Net.ResponseCallBack<MChatRoom>() {
                    @Override
                    public void onSuccess(MChatRoom response) {
                        super.onSuccess(response);

                        arrContent.clear();
                        arrContent.addAll(response.arr_list);

                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(MError response) {
                        super.onFailure(response);

                        if (response.resultcode == 500) {
                            mParent.networkErrorOccupied(response);
                        } else {
                            Toaster.showShort(getContext(), "오류입니다.");
                        }
                    }
                });
    }

}
