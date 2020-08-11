package com.character.microblogapp.ui.page.main.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.character.microblogapp.MyApplication;
import com.character.microblogapp.R;
import com.character.microblogapp.data.MyInfo;
import com.character.microblogapp.model.MEnergy;
import com.character.microblogapp.model.MError;
import com.character.microblogapp.model.MUserList;
import com.character.microblogapp.net.Net;
import com.character.microblogapp.ui.adapter.HistoryGridUserAdapter;
import com.character.microblogapp.ui.adapter.HistoryVerticalUserAdapter;
import com.character.microblogapp.ui.dialog.ConfirmDialog;
import com.character.microblogapp.ui.dialog.ConfirmDialog2;
import com.character.microblogapp.ui.page.BaseFragment;
import com.character.microblogapp.ui.page.profile.ProfileActivity;
import com.character.microblogapp.ui.page.setting.EnergyActivity;
import com.character.microblogapp.ui.page.setting.EnergyupActivity;
import com.character.microblogapp.util.Toaster;

import java.util.ArrayList;

import butterknife.BindView;

public class HistoryItemsFragment extends BaseFragment {

    public static final int TYPE_HISTORY_LIKE_RECEIVE = 0;
    public static final int TYPE_HISTORY_LIKE_SEND = 1;
    public static final int TYPE_HISTORY_HIGH_SCORE_RECEIVE = 2;
    public static final int TYPE_HISTORY_HIGH_SCORE_SEND = 3;
    public static final int TYPE_HISTORY_ALL = 4;

    public static final int GO_START_PROFILE = 5010;


    @BindView(R.id.rv_contents)
    RecyclerView rvUsers;

    @BindView(R.id.cl_progress)
    ConstraintLayout clProgress;

    @BindView(R.id.lly_sorry_msg_bg)
    LinearLayout llySorryMsgBg;

    @BindView(R.id.txv_sorry_msg)
    TextView txvSorryMsg;

//    HistoryVerticalUserAdapter verticalAdapter = null;
    HistoryGridUserAdapter gridAdapter = null;
    ArrayList<MUserList.User> mUsers;
    int viewType = TYPE_HISTORY_LIKE_RECEIVE;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        createView(inflater, container, R.layout.fragment_history_items);

        initData();

        return mRoot;
    }

    private boolean loadingEnd = false;
    private boolean lockRcv = false;
    int page_num = 1;

    @Override
    protected void initUI() {
//        if (viewType == TYPE_HISTORY_LIKE_RECEIVE || viewType == TYPE_HISTORY_LIKE_SEND) {
//            rvUsers.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
//        } else {
            rvUsers.setLayoutManager(new GridLayoutManager(getContext(), 2));
//        }

        rvUsers.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int firstVisibleItem = recyclerView.getChildAdapterPosition(recyclerView.getChildAt(0));
                int visibleCnt = recyclerView.getChildCount();
                int totalItemCnt;

//                if (viewType == TYPE_HISTORY_LIKE_RECEIVE || viewType == TYPE_HISTORY_LIKE_SEND) {
//                    totalItemCnt = verticalAdapter.getItemCount();
//                } else {
                    totalItemCnt = gridAdapter.getItemCount();
//                }

                if (loadingEnd) {
                    return;
                }

                if (totalItemCnt != 0 && firstVisibleItem >= (totalItemCnt - visibleCnt) && !lockRcv) {
                    loadUsers();
                }
            }
        });

        llySorryMsgBg.setVisibility(View.GONE);
    }

    public void setViewType(int type) {
        viewType = type;
    }

    private void initData() {

        page_num = 1;
        loadingEnd = false;
        lockRcv = false;

        mApp = (MyApplication) getActivity().getApplicationContext();
        mUsers = new ArrayList<>();
//        if (viewType == TYPE_HISTORY_LIKE_RECEIVE || viewType == TYPE_HISTORY_LIKE_SEND) {
//            verticalAdapter = new HistoryVerticalUserAdapter(getContext(), mUsers, new HistoryVerticalUserAdapter.OnClickListener() {
//                @Override
//                public void onPickUser(int pos) {
//                    onClickUser(mUsers.get(pos));
//                }
//            });
//            rvUsers.setAdapter(verticalAdapter);
//        } else {
            gridAdapter = new HistoryGridUserAdapter(getContext(), mUsers, new HistoryGridUserAdapter.OnClickListener() {
                @Override
                public void onPickUser(int pos) {
                    onClickUser(mUsers.get(pos));
                }
            });

            gridAdapter.isBlue = viewType == TYPE_HISTORY_HIGH_SCORE_RECEIVE;
            rvUsers.setAdapter(gridAdapter);
//        }

        loadUsers();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GO_START_PROFILE) {
            page_num = 1;
            loadUsers();
        }
    }

    public void onClickUser(final MUserList.User info) {
        if (viewType == TYPE_HISTORY_ALL || viewType == TYPE_HISTORY_LIKE_RECEIVE || viewType == TYPE_HISTORY_HIGH_SCORE_SEND) {
            // 지난 소개인 경우 과금없이 오픈 되도록 수정.
            Intent goProfile = new Intent(getActivity(), ProfileActivity.class);
            goProfile.putExtra("usr_uid", info.uid);
            startActivityForResult(goProfile, GO_START_PROFILE);
        } else {
            if (info.isPublic) {
                if (info.visit == 0) {
                    ConfirmDialog2.show(getContext(), String.format(getString(R.string.confirm_view_user_profile), info.nickname), "에너지 10개를 사용할까요?", new ConfirmDialog2.ActionListener() {
                        @Override
                        public void onOk() {

                            if (MyInfo.getInstance().energy >= 20) {

                                calcEnergy(10, 1, info.uid);

                            } else {

                                ConfirmDialog2.show(getContext(), "에너지가 부족해요!!", String.format(getString(R.string.confirm_get_energy), (10 - MyInfo.getInstance().energy)),
                                        new ConfirmDialog2.ActionListener() {
                                            @Override
                                            public void onOk() {
                                                Intent goEnergy = new Intent(getActivity(), EnergyupActivity.class);
                                                goEnergy.putExtra("nickname", info.nickname);
                                                startActivity(goEnergy);
                                            }
                                        });
                            }
                        }
                    });
                } else {
                    Intent goProfile = new Intent(getActivity(), ProfileActivity.class);
                    goProfile.putExtra("usr_uid", info.uid);
                    startActivityForResult(goProfile, GO_START_PROFILE);
                }
            }
        }
    }

    private void calcEnergy(int count, final int mType, final int id) {
        showProgress();
        Net.instance().api.calc_point(MyInfo.getInstance().uid, count, "프로필 확인")
                .enqueue(new Net.ResponseCallBack<MEnergy>() {
                    @Override
                    public void onSuccess(MEnergy response) {
                        super.onSuccess(response);
                        hideProgress();

                        MyInfo.getInstance().energy = response.energy;

                        if (mType == 1) {
                            Intent goProfile = new Intent(getActivity(), ProfileActivity.class);
                            goProfile.putExtra("usr_uid", id);
                            startActivityForResult(goProfile, GO_START_PROFILE);

                        } else if (mType == 2) {

                        }
                    }

                    @Override
                    public void onFailure(MError response) {
                        super.onFailure(response);
                        hideProgress();
                        if (response.resultcode == 500) {
                            networkErrorOccupied(response);
                        } else {
                            Toaster.showShort(getContext(), response.res_msg);
                        }
                    }
                });
    }

    private void transformUserList(MUserList response) {
        if (page_num == 1) {
            mUsers.clear();
        }

        if (response.arr_list != null) {
            for (int i = 0; i < response.arr_list.length; i++) {
                mUsers.add(response.arr_list[i]);
            }

            lockRcv = false;

            if (response.page_cnt <= page_num) {
                loadingEnd = true;
            } else {
                page_num++;
            }
        }

        if (mUsers.size() == 0) {

            llySorryMsgBg.setVisibility(View.VISIBLE);
            switch (viewType) {
                case TYPE_HISTORY_LIKE_RECEIVE:
                    txvSorryMsg.setText("\"아직 나를 좋아하는 이성이 없네요..힘내세요.\"");
                    break;
                case TYPE_HISTORY_LIKE_SEND:
                    txvSorryMsg.setText("\"아직 내가 좋아한 이성이 없네요.\"");
                    break;
                case TYPE_HISTORY_HIGH_SCORE_RECEIVE:
                    txvSorryMsg.setText("\"아직 나에게 높은 점수를 준 이성이 없네요..힘내세요.\"");
                    break;
                case TYPE_HISTORY_HIGH_SCORE_SEND:
                    txvSorryMsg.setText("\"아직 내가 높은 점수를 준 이성이 없네요..\"");
                    break;
                case TYPE_HISTORY_ALL:
                    txvSorryMsg.setText("\"지난만남이 없네요..\"");
                    break;
            }
        } else {
            llySorryMsgBg.setVisibility(View.GONE);
        }

//        if (viewType == TYPE_HISTORY_LIKE_RECEIVE || viewType == TYPE_HISTORY_LIKE_SEND) {
//            if (verticalAdapter != null) {
//                verticalAdapter.notifyDataSetChanged();
//            }
//        } else {
            if (gridAdapter != null) {
                gridAdapter.notifyDataSetChanged();
            }
//        }
    }

    private MUserList makeDummyDataForTest() {

        MUserList result = new MUserList();

        result.arr_list = new MUserList.User[20];
        for (int i = 0; i < 20; i++) {

            MUserList.User user = new MUserList.User();
            user.uid = i;
            user.nickname = "tester" + i;
            user.address = "address" + i;
            user.age = (20 + i);
            user.gender = 1;
            user.character = "DS";
            user.ideal_character = "강한 주도형\n강한 안정형";
            user.school = "서울대";
            user.job = "대학생";
            user.height = 167;
            user.profile = new String[]{"http://192.168.56.1/Character/Source/Admin/temp/temp_item_1.png", "http://192.168.56.1/Character/Source/Admin/temp/temp_item_2.png"};
            user.isPublic = true;

            result.arr_list[i] = user;
        }

        result.page_cnt = 2;

        //result = new MUserList();

        return result;
    }

    public void loadUsers() {

        showProgress();
        lockRcv = true;
        switch (viewType) {
            case TYPE_HISTORY_LIKE_RECEIVE:
                // 호감표시 받은 회원목록
                Net.instance().api.get_heart_receive_usr(MyInfo.getInstance().uid, page_num)
                        .enqueue(new Net.ResponseCallBack<MUserList>() {
                            @Override
                            public void onSuccess(MUserList response) {
                                super.onSuccess(response);
                                hideProgress();
                                transformUserList(response);
                            }

                            @Override
                            public void onFailure(MError response) {
                                super.onFailure(response);
                                hideProgress();

                                if (response.resultcode == 500) {

                                    if (IS_UITEST) {
                                        transformUserList(makeDummyDataForTest());
                                    } else {
                                        networkErrorOccupied(response);
                                    }
                                }
                            }
                        });
                break;
            case TYPE_HISTORY_LIKE_SEND:
                // 호감표시한 회원목록
                Net.instance().api.get_hearted_usr(MyInfo.getInstance().uid, page_num)
                        .enqueue(new Net.ResponseCallBack<MUserList>() {
                            @Override
                            public void onSuccess(MUserList response) {
                                super.onSuccess(response);
                                hideProgress();
                                transformUserList(response);
                            }

                            @Override
                            public void onFailure(MError response) {
                                super.onFailure(response);
                                hideProgress();

                                if (IS_UITEST) {
                                    transformUserList(makeDummyDataForTest());
                                } else {
                                    networkErrorOccupied(response);
                                }
                            }
                        });
                break;
            case TYPE_HISTORY_HIGH_SCORE_RECEIVE:
                // 높은점수 받은 회원목록
                Net.instance().api.get_high_score_receive_usr(MyInfo.getInstance().uid, page_num)
                        .enqueue(new Net.ResponseCallBack<MUserList>() {
                            @Override
                            public void onSuccess(MUserList response) {
                                super.onSuccess(response);
                                hideProgress();
                                transformUserList(response);
                            }

                            @Override
                            public void onFailure(MError response) {
                                super.onFailure(response);
                                hideProgress();

                                if (IS_UITEST) {
                                    transformUserList(makeDummyDataForTest());
                                } else {
                                    networkErrorOccupied(response);
                                }
                            }
                        });
                break;
            case TYPE_HISTORY_HIGH_SCORE_SEND:
                // 높은점수 보낸 회원목록
                Net.instance().api.get_high_scored_usr(MyInfo.getInstance().uid, page_num)
                        .enqueue(new Net.ResponseCallBack<MUserList>() {
                            @Override
                            public void onSuccess(MUserList response) {
                                super.onSuccess(response);
                                hideProgress();
                                transformUserList(response);
                            }

                            @Override
                            public void onFailure(MError response) {
                                super.onFailure(response);
                                hideProgress();

                                if (IS_UITEST) {
                                    transformUserList(makeDummyDataForTest());
                                } else {
                                    networkErrorOccupied(response);
                                }
                            }
                        });
                break;
            case TYPE_HISTORY_ALL:
                // 지난 만남
                Net.instance().api.get_old_meeting(MyInfo.getInstance().uid)
                        .enqueue(new Net.ResponseCallBack<MUserList>() {
                            @Override
                            public void onSuccess(MUserList response) {
                                super.onSuccess(response);
                                hideProgress();
                                transformUserList(response);
                            }

                            @Override
                            public void onFailure(MError response) {
                                super.onFailure(response);
                                hideProgress();
                                if (IS_UITEST) {
                                    transformUserList(makeDummyDataForTest());
                                } else {
                                    if (response.resultcode == 500) {
                                        networkErrorOccupied(response);
                                    }
                                }
                            }
                        });
                break;
        }
    }

    private void showProgress() {
        clProgress.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        clProgress.setVisibility(View.GONE);
    }

}
