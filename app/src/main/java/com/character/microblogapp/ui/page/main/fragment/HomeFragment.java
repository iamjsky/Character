package com.character.microblogapp.ui.page.main.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.character.microblogapp.R;
import com.character.microblogapp.data.Common;
import com.character.microblogapp.data.MyInfo;
import com.character.microblogapp.model.MBanner;
import com.character.microblogapp.model.MBase;
import com.character.microblogapp.model.MEnergy;
import com.character.microblogapp.model.MError;
import com.character.microblogapp.model.MRefreshTodayMetting;
import com.character.microblogapp.model.MUserList;
import com.character.microblogapp.net.Net;
import com.character.microblogapp.ui.adapter.ChoosedMatchingHorizontalAdapter;
import com.character.microblogapp.ui.adapter.HistoryGridUserAdapter;
import com.character.microblogapp.ui.adapter.MatchingContactsAdapter;
import com.character.microblogapp.ui.dialog.ConfirmDialog2;
import com.character.microblogapp.ui.dialog.SearchDialog;
import com.character.microblogapp.ui.page.BaseFragment;
import com.character.microblogapp.ui.page.model.ModelFindActivity;
import com.character.microblogapp.ui.page.profile.ProfileActivity;
import com.character.microblogapp.ui.page.setting.AlarmListActivity;
import com.character.microblogapp.ui.page.setting.EnergyActivity;
import com.character.microblogapp.util.PrefMgr;
import com.character.microblogapp.util.Toaster;
import com.character.microblogapp.util.Util;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends BaseFragment {

    @BindView(R.id.iv_home_gif)
    ImageView ivHomeGif;

    @BindView(R.id.rv_contacts_list)
    RecyclerView rvContactsList;

    @BindView(R.id.cl_progress)
    ConstraintLayout clProgress;

    @BindView(R.id.rv_history_list)
    RecyclerView rvHistoryList;

    @BindView(R.id.txv_history_cnt)
    TextView txvHistoryCnt;

    @BindView(R.id.tv_title)
    TextView txvTitle;

    @BindView(R.id.tv_time)
    TextView txvTime;

    @BindView(R.id.tv_my_point)
    TextView txvMyHeart;

    @BindView(R.id.tvMatching)
    TextView tvMatching;

    @BindView(R.id.btn_meet_more)
    Button btnMeetMore;

    @BindView(R.id.btnMore)
    Button btnMore;

    @BindView(R.id.vpBanner)
    ViewPager vpBanner;

    @BindView(R.id.rlBanner)
    RelativeLayout rlBanner;

    @BindView(R.id.ciBanner)
    CirclePageIndicator ciBanner;

    // 알람 아이콘 관련
    @BindView(R.id.iv_nav_menu)
    ImageView ivNavMenu;

    @BindView(R.id.rl_noti_count)
    RelativeLayout rlNotiCnt;

    @BindView(R.id.tv_noti_cnt)
    TextView tvNotiCnt;

    @BindView(R.id.fl_content)
    FrameLayout flContent;

    @BindView(R.id.fl_start_btn)
    FrameLayout flStartBtn;

    @BindView(R.id.btn_find_model)
    Button btnFindModel;

    MatchingContactsAdapter adapter;
    private ArrayList<MUserList.User> mArrContacts = new ArrayList<>();

    HistoryGridUserAdapter hisAdapter;
    ArrayList<MUserList.User> mArrHistory = new ArrayList<>();
    ArrayList<MUserList.User> mArrMore = new ArrayList<>();

    @BindView(R.id.rv_todaymetting_list)
    RecyclerView rvChoosedMatchingList;

    ImagePagerAdapter mTopAdapter;

    ChoosedMatchingHorizontalAdapter choosedMatchingAdapter;
    private ArrayList<MUserList.User> mArrChoosedTodayMetting = new ArrayList<>();

    PrefMgr m_prefMgr;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        createView(inflater, container, R.layout.fragment_home);
        SharedPreferences prefs = getActivity().getSharedPreferences(PrefMgr.APP_PREFS,
                Context.MODE_PRIVATE);
        m_prefMgr = new PrefMgr(prefs);

        if (m_prefMgr.getInt(PrefMgr.SHOW_HOME_BTN_START, 0) == 0)
            initStartBtn();
        else
            flStartBtn.setVisibility(View.GONE);

        initData();

        loadTodayMeeting();
        loadHistory();

        return mRoot;
    }

    private void initStartBtn() {
        flContent.setVisibility(View.GONE);
        flStartBtn.setVisibility(View.VISIBLE);
        btnFindModel.setVisibility(View.GONE);
        btnMeetMore.setVisibility(View.GONE);
    }

    @OnClick(R.id.btn_start_today_metting)
    void onClickStart() {
        flContent.setVisibility(View.VISIBLE);
        flStartBtn.setVisibility(View.GONE);
//        loadTodayMeeting();
//        loadHistory();

        btnFindModel.setVisibility(View.VISIBLE);

        if (mArrChoosedTodayMetting.size() > 1) {
            rvContactsList.setVisibility(View.GONE);
            btnMeetMore.setVisibility(View.VISIBLE);
            if (getChoosableContactsCount() != 0) {
                btnMeetMore.setBackgroundResource(R.drawable.btn_main_bg);
            } else {
                btnMeetMore.setBackgroundResource(R.drawable.btn_main_disable);
            }
        } else {
            tvMatching.setVisibility(View.GONE);
            rvChoosedMatchingList.setVisibility(View.GONE);
            rvContactsList.setVisibility(View.VISIBLE);
            btnMeetMore.setVisibility(View.GONE);
        }

        if (mArrChoosedTodayMetting.size() != 0) {
            updateMettingCount();
        } else {
            updateRemainTime();
        }

        // gif 노출
        if (mArrContacts.size() > 2 && mArrChoosedTodayMetting.size() < 2)
            cardHandler.sendEmptyMessageDelayed(0, 1800);

        m_prefMgr.put(PrefMgr.SHOW_HOME_BTN_START, 1);
    }

    private void initData() {
        adapter = new MatchingContactsAdapter(getContext(), mArrContacts, new MatchingContactsAdapter.OnClickListener() {
            @Override
            public void onPickUser(int pos) {

                if (remain_seconds != 0) {
                    if (mArrContacts.get(pos).is_choose_state != 2) {

//                        mArrContacts.get(pos).is_choose_state = 2;
                        adapter.notifyItemChanged(pos);

                        boolean shouldAdd = true;
                        for (MUserList.User user : mArrChoosedTodayMetting) {
                            if (user.uid == mArrContacts.get(pos).uid) {
                                shouldAdd = false;
                                break;
                            }
                        }

                        if (shouldAdd) {
                            mArrChoosedTodayMetting.add(mArrContacts.get(pos));
                        }

                        if (mArrChoosedTodayMetting.size() == 2) {
                            reqChoosedTwoOtherUser();
                        } else {
                            if (mArrChoosedTodayMetting.size() != 0) {
                                MyInfo.getInstance().select_man = mArrChoosedTodayMetting.get(0).uid;
                            }
                            updateMettingCount();
                        }
                    }
                }
            }

            @Override
            public void onProfile(final int pos) {
                // 홈화면에서 오늘의 소개팅은 과금되지 않도록 수정함.
                //if (mArrContacts.get(pos).visit == 1 || mArrContacts.get(pos).is_pay == 1) {
                    Intent goProfile = new Intent(getActivity(), ProfileActivity.class);
                    goProfile.putExtra("usr_uid", mArrContacts.get(pos).uid);
                    startActivity(goProfile);
//                } else {
//                    ConfirmDialog2.show(getContext(), String.format(getString(R.string.confirm_view_user_profile), mArrContacts.get(pos).nickname), "에너지 10개를 사용할까요?", new ConfirmDialog2.ActionListener() {
//                        @Override
//                        public void onOk() {
//
//                            if (MyInfo.getInstance().energy >= 20) {
//
//                                mArrContacts.get(pos).visit = 1;
//                                calcEnergy(10, 1, mArrContacts.get(pos).uid);
//
//                            } else {
//
//                                ConfirmDialog2.show(getContext(), "에너지가 부족해요!!", String.format(getString(R.string.confirm_get_energy), (10 - MyInfo.getInstance().energy)),
//                                        new ConfirmDialog2.ActionListener() {
//                                            @Override
//                                            public void onOk() {
//                                                Intent goEnergy = new Intent(getActivity(), EnergyupActivity.class);
//                                                goEnergy.putExtra("nickname", mArrContacts.get(pos).nickname);
//                                                startActivity(goEnergy);
//                                            }
//                                        });
//                            }
//                        }
//                    });
//                }
            }

            @Override
            public void onSelect(final int pos) {
                if (remain_seconds != 0) {
                    reqChoosedOneOtherUser(pos);
                }
            }
        });
        rvContactsList.setAdapter(adapter);

        hisAdapter = new HistoryGridUserAdapter(getContext(), mArrHistory, new HistoryGridUserAdapter.OnClickListener() {
            @Override
            public void onPickUser(int pos) {
                onClickUser(mArrHistory.get(pos));
            }
        });
        rvHistoryList.setAdapter(hisAdapter);

        choosedMatchingAdapter = new ChoosedMatchingHorizontalAdapter(getContext(), mArrChoosedTodayMetting, new ChoosedMatchingHorizontalAdapter.OnClickListener() {
            @Override
            public void onPickUser(final int pos) {
                //if (pos == 0) {
                    Intent goProfile = new Intent(getActivity(), ProfileActivity.class);
                    goProfile.putExtra("usr_uid", mArrChoosedTodayMetting.get(pos).uid);
                    startActivity(goProfile);
//                } else {
//                    if (mArrChoosedTodayMetting.get(pos).visit == 1 || mArrContacts.get(pos).is_pay == 1) {
//                        Intent goProfile = new Intent(getActivity(), ProfileActivity.class);
//                        goProfile.putExtra("usr_uid", mArrChoosedTodayMetting.get(pos).uid);
//                        startActivity(goProfile);
//                    } else {
//                        ConfirmDialog2.show(getContext(), String.format(getString(R.string.confirm_view_user_profile), mArrChoosedTodayMetting.get(pos).nickname), "에너지 10개를 사용할까요?", new ConfirmDialog2.ActionListener() {
//                            @Override
//                            public void onOk() {
//
//                                if (MyInfo.getInstance().energy >= 20) {
//
//                                    mArrChoosedTodayMetting.get(pos).visit = 1;
//                                    calcEnergy(10, 1, mArrChoosedTodayMetting.get(pos).uid);
//
//                                } else {
//
//                                    ConfirmDialog2.show(getContext(), "에너지가 부족해요!!", String.format(getString(R.string.confirm_get_energy), (10 - MyInfo.getInstance().energy)),
//                                            new ConfirmDialog2.ActionListener() {
//                                                @Override
//                                                public void onOk() {
//                                                    Intent goEnergy = new Intent(getActivity(), EnergyupActivity.class);
//                                                    goEnergy.putExtra("nickname", mArrChoosedTodayMetting.get(pos).nickname);
//                                                    startActivity(goEnergy);
//                                                }
//                                            });
//                                }
//                            }
//                        });
//                    }
//                }
            }
        });
        rvChoosedMatchingList.setAdapter(choosedMatchingAdapter);
    }

    @Override
    protected void initUI() {
        rvHistoryList.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rvContactsList.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rvChoosedMatchingList.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));

        _remainHandler.sendEmptyMessageDelayed(0, 1000);

        updateBadgeCount();

        updateEnergyCount();

        updateMettingCount();

        initBanner();

        ////////
//        Glide.with(this)
//                .load(R.drawable.card_refresh)
//                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
//                .into(new DrawableImageViewTarget(ivHomeGif));
    }

    public void initBanner() {
        mTopAdapter = new ImagePagerAdapter();

        vpBanner.setAdapter(mTopAdapter);
        ciBanner.setViewPager(vpBanner);

        apiBanner();
    }

    public boolean resetUI() {
        if (btnMeetMore.getVisibility() != View.GONE) {
            updateEnergyCount();
            mArrChoosedTodayMetting.clear();
            updateMettingCount();
//            loadHistory();
            return true;
        } else {
            return false;
        }
    }

    private void updateEnergyCount() {
        txvTitle.setText(MyInfo.getInstance().energy + "");
    }

    private void updateBadgeCount() {
        txvMyHeart.setText(MyInfo.getInstance().energy + "");
    }

    private int getChoosableContactsCount() {

        int cnt = 0;

        for (int i = 0; i < mArrContacts.size(); i++) {
//            if (mArrContacts.get(i).is_choose_state != 1) {
//                continue;
//            } else {
            cnt++;
//            }
        }

        return cnt;
    }

    private int getContactsCount() {

        int cnt = 0;

        for (int i = 0; i < mArrContacts.size(); i++) {
//            if (mArrContacts.get(i).is_choose_state != 1) {
//                continue;
//            } else {
            cnt++;
//            }
        }

        if (cnt != 0) {
            if (mArrChoosedTodayMetting.size() == 1) {
                cnt -= 1;
            }
        }
        if (cnt % 2 != 0)
            cnt--;
        return cnt;
    }

    private void updateMettingCount() {

        txvTitle.setText("오늘의 소개팅(" + mArrChoosedTodayMetting.size() + "/2)");
        btnMeetMore.setText("더 만나보기(" + getContactsCount() + ")");

        if (mArrChoosedTodayMetting.size() > 1) {
            choosedMatchingAdapter.notifyDataSetChanged();
            rvChoosedMatchingList.setVisibility(View.VISIBLE);
            tvMatching.setVisibility(mArrChoosedTodayMetting.get(0).pay_title.equals("") ? View.GONE : View.VISIBLE);
            tvMatching.setText(mArrChoosedTodayMetting.get(0).pay_title);

            rvContactsList.setVisibility(View.GONE);
            btnMeetMore.setVisibility(View.VISIBLE);

            if (getChoosableContactsCount() != 0) {
                btnMeetMore.setBackgroundResource(R.drawable.btn_main_bg);
            } else {
                btnMeetMore.setBackgroundResource(R.drawable.btn_main_disable);
            }
        } else {
            tvMatching.setVisibility(View.GONE);
            rvChoosedMatchingList.setVisibility(View.GONE);
            rvContactsList.setVisibility(View.VISIBLE);
            btnMeetMore.setVisibility(View.GONE);
        }
    }

    int remain_seconds = -1;
    int unread_noti_cnt = 0;

    private void updateRemainTime() {

        if (remain_seconds > 0) {
            txvTime.setVisibility(View.VISIBLE);
            remain_seconds--;
            String strBuf = Util.getStringTimeFormat_00_00_00(remain_seconds);
            txvTime.setText(strBuf);

        } else if (remain_seconds == 0) {
            txvTime.setVisibility(View.VISIBLE);
            txvTime.setText("오늘의 만남이 종료되었습니다.");
        } else {
            txvTime.setVisibility(View.GONE);
        }
    }

    private void updateAlarmCnt() {

        if (unread_noti_cnt > 0) {
            rlNotiCnt.setVisibility(View.VISIBLE);
            tvNotiCnt.setText("" + unread_noti_cnt);
            ivNavMenu.setImageResource(R.drawable.ico_noti_active);
        } else {
            rlNotiCnt.setVisibility(View.GONE);
            ivNavMenu.setImageResource(R.drawable.ico_noti_normal);
        }
    }

    Handler _remainHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            updateRemainTime();

            _remainHandler.sendEmptyMessageDelayed(0, 1000);
            return false;
        }
    });

//    Handler _handler = new Handler(new Handler.Callback() {
//        @Override
//        public boolean handleMessage(Message msg) {
//
//            for (int i = 0; i < mArrContacts.size(); i++) {
//                mArrContacts.get(i).is_choose_state = 1;
//            }
//            adapter.notifyDataSetChanged();
//
//            return false;
//        }
//    });

    private void transformUserListForToday(MUserList response) {

        mArrChoosedTodayMetting.clear();
        mArrContacts.clear();

        for (int i = 0; i < response.arr_list.length; i++) {
            if (response.arr_list[i].is_choose_state == 2) {
                mArrChoosedTodayMetting.add(response.arr_list[i]);
            } else {
                mArrContacts.add(response.arr_list[i]);
                if(response.arr_list[i].is_choose_state == 1) {
                    MyInfo.getInstance().select_man = response.arr_list[i].uid;
                }

                if (MyInfo.getInstance().select_man == response.arr_list[i].uid && mArrChoosedTodayMetting.size() < 2) {
                    mArrChoosedTodayMetting.add(response.arr_list[i]);
                }
            }
        }

        adapter.notifyDataSetChanged();

        if (mArrChoosedTodayMetting.size() != 0) {
            updateMettingCount();
        } else {
            updateRemainTime();
        }

        // gif 노출
        if (flStartBtn.getVisibility() == View.GONE) {
            if (mArrContacts.size() > 2 && mArrChoosedTodayMetting.size() < 2)
                cardHandler.sendEmptyMessageDelayed(0, 1800);
        }

    }

    Handler cardHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            if (msg.what == 0) {
//                ivHomeGif.setVisibility(View.VISIBLE);
                for (int i = 0; i < mArrContacts.size(); i++) {
                    int k = (int) (Math.random() * mArrContacts.size());
                    int j = (int) (Math.random() * mArrContacts.size());

                    MUserList.User tmp = mArrContacts.get(k);
                    mArrContacts.set(k, mArrContacts.get(j));
                    mArrContacts.set(j, tmp);
                }
//                cardHandler.sendEmptyMessageDelayed(1, 2000);
//                adapter.notifyDataSetChanged();
                adapter.updateStatus(1);
            } else if (msg.what == 1) {
                //ivHomeGif.setVisibility(View.GONE);
                adapter.updateStatus(1);
            }

            return false;
        }
    });

    private MUserList makeTodayDummyDataForTest() {

        MUserList result = new MUserList();

        result.arr_list = new MUserList.User[6];
        for (int i = 0; i < 6; i++) {

            MUserList.User user = new MUserList.User();
            user.uid = i;
            user.nickname = "미팅" + i;
            user.address = "지역" + i;
            user.age = (20 + i);
            user.gender = 1;
            user.character = "CI";
            user.ideal_character = "강한 주도형\n강한 안정형";
            user.school = "서울대";
            user.job = "대학생";
            user.height = 167;
            user.profile = new String[]{"http://192.168.56.1/Character/Source/Admin/temp/temp_item_1.png", "http://192.168.56.1/Character/Source/Admin/temp/temp_item_2.png"};
            user.isPublic = true;

            result.arr_list[i] = user;
        }

        result.page_cnt = 2;

        return result;
    }

    private void transformUserList(MUserList response) {
        mArrHistory.clear();
        mArrMore.clear();
        if (response.arr_list != null) {
            for (int i = 0; i < response.arr_list.length; i++) {
                response.arr_list[i].diplay_state_home = true;
                if (i < 4) {
                    mArrHistory.add(response.arr_list[i]);
                }
                mArrMore.add(response.arr_list[i]);
            }
        }

        btnMore.setVisibility(mArrMore.size() > 4 ? View.VISIBLE : View.GONE);

        if (hisAdapter != null) {
            hisAdapter.notifyDataSetChanged();
        }

        txvHistoryCnt.setText("지난 소개(" + mArrMore.size() + ")");
        updateBadgeCount();
    }

    private MUserList makeDummyDataForTest() {

        MUserList result = new MUserList();

        result.arr_list = new MUserList.User[8];
        for (int i = 0; i < 8; i++) {

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

        return result;
    }

    public void getUnreadAlarmCnt() {
        Net.instance().api.get_unread_alarm_cnt(MyInfo.getInstance().uid)
                .enqueue(new Net.ResponseCallBack<MBase>() {
                    @Override
                    public void onSuccess(MBase response) {
                        super.onSuccess(response);
                        unread_noti_cnt = response.count;
                        updateAlarmCnt();
                    }

                    @Override
                    public void onFailure(MError response) {
                        super.onFailure(response);
                        if (response.resultcode == 500) {
                            networkErrorOccupied(response);
                        }
                    }
                });
    }

    private void loadTodayMeeting() {
        showProgress();
        Net.instance().api.get_today_meeting(MyInfo.getInstance().uid)
                .enqueue(new Net.ResponseCallBack<MUserList>() {
                    @Override
                    public void onSuccess(MUserList response) {
                        super.onSuccess(response);
                        hideProgress();
                        remain_seconds = response.remain_seconds;
                        unread_noti_cnt = response.unread_noti_cnt;
                        transformUserListForToday(response);
                        updateAlarmCnt();
                    }

                    @Override
                    public void onFailure(MError response) {
                        super.onFailure(response);
                        hideProgress();

                        if (IS_UITEST) {
                            remain_seconds = 32568;
                            transformUserListForToday(makeTodayDummyDataForTest());
                        } else {
                            if (response.resultcode == 500) {
                                networkErrorOccupied(response);
                            }
                        }
                    }
                });
    }

    public void loadHistory() {

        showProgress();
        // 이전 만남 목록
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
    }

    private void reqChoosedTwoOtherUser() {

        showProgress();
        Net.instance().api.select_two_usr_today_meeting(MyInfo.getInstance().uid, mArrChoosedTodayMetting.get(0).uid, mArrChoosedTodayMetting.get(1).uid)
                .enqueue(new Net.ResponseCallBack<MBase>() {
                    @Override
                    public void onSuccess(MBase response) {
                        super.onSuccess(response);
                        hideProgress();

                        mArrChoosedTodayMetting.get(0).is_choose_state = 1;
                        mArrChoosedTodayMetting.get(1).is_choose_state = 1;

                        for (int i = 0; i < mArrContacts.size(); i++) {
                            if (mArrContacts.get(i).uid == mArrChoosedTodayMetting.get(0).uid) {
                                mArrContacts.remove(i);
                            } else if (mArrContacts.get(i).uid == mArrChoosedTodayMetting.get(1).uid) {
                                mArrContacts.remove(i);
                            }
                        }

                        MyInfo.getInstance().select_man = 0;
                        adapter.notifyDataSetChanged();
                        updateMettingCount();
                    }

                    @Override
                    public void onFailure(MError response) {
                        super.onFailure(response);
                        hideProgress();

                        if (IS_UITEST) {
                            updateMettingCount();
                        } else {
                            if (response.resultcode == 500) {
                                networkErrorOccupied(response);
                            } else {
                                new AlertDialog.Builder(getActivity()).setTitle("").setMessage(response.res_msg).setPositiveButton(R.string.confirm, null).show();
                            }
                        }
                    }
                });
    }

    private void reqChoosedOneOtherUser(final int index) {

        showProgress();
        Net.instance().api.select_one_usr_today_meeting(MyInfo.getInstance().uid, mArrContacts.get(index).uid)
                .enqueue(new Net.ResponseCallBack<MBase>() {
                    @Override
                    public void onSuccess(MBase response) {
                        super.onSuccess(response);
                        hideProgress();

                        mArrContacts.get(index).is_choose_state = 1;

                        adapter.notifyDataSetChanged();

                        boolean shouldAdd = true;
                        for (MUserList.User user : mArrChoosedTodayMetting) {
                            if (user.uid == mArrContacts.get(index).uid) {
                                shouldAdd = false;
                                break;
                            }
                        }

                        if (shouldAdd) {
                            mArrChoosedTodayMetting.add(mArrContacts.get(index));
                        }

                        if (mArrChoosedTodayMetting.size() == 2) {
                            reqChoosedTwoOtherUser();
                        } else {
                            if (mArrChoosedTodayMetting.size() != 0) {
                                MyInfo.getInstance().select_man = mArrChoosedTodayMetting.get(0).uid;
                            }
                            updateMettingCount();
                        }

                    }

                    @Override
                    public void onFailure(MError response) {
                        super.onFailure(response);
                        hideProgress();

                        if (IS_UITEST) {
                            updateMettingCount();
                        } else {
                            if (response.resultcode == 500) {
                                networkErrorOccupied(response);
                            } else {
                                new AlertDialog.Builder(getActivity()).setTitle("").setMessage(response.res_msg).setPositiveButton(R.string.confirm, null).show();
                            }
                        }
                    }
                });
    }

    private void calcEnergy(int count, final int mType, final int id) {
        showProgress();
        Net.instance().api.calc_point(MyInfo.getInstance().uid, count, mType == 2 ? "이성 추가선택" : mType == 1 ? "프로필 확인" : "더 만나보기")
                .enqueue(new Net.ResponseCallBack<MEnergy>() {
                    @Override
                    public void onSuccess(MEnergy response) {
                        super.onSuccess(response);
                        hideProgress();

                        MyInfo.getInstance().energy = response.energy;
                        updateBadgeCount();

                        if (mType == 1) {
                            Intent goProfile = new Intent(getActivity(), ProfileActivity.class);
                            goProfile.putExtra("usr_uid", id);
                            startActivity(goProfile);
                        } else {
                            reqRefreshChoosedTodayMetting();
                        }
                    }

                    @Override
                    public void onFailure(MError response) {
                        super.onFailure(response);
                        hideProgress();
                        if (response.resultcode == 500) {
                            networkErrorOccupied(response);
                        } else {
                            new AlertDialog.Builder(getActivity()).setTitle("").setMessage(response.res_msg).setPositiveButton(R.string.confirm, null).show();
                        }
                    }
                });
    }

    public void reqRefreshChoosedTodayMetting() {

        showProgress();
        Net.instance().api.refresh_today_meeting(MyInfo.getInstance().uid)
                .enqueue(new Net.ResponseCallBack<MRefreshTodayMetting>() {
                    @Override
                    public void onSuccess(MRefreshTodayMetting response) {
                        super.onSuccess(response);
                        hideProgress();

                        MyInfo.getInstance().energy = response.energy;
                        updateEnergyCount();
                        mArrChoosedTodayMetting.clear();

                        for (int i = 0; i < mArrContacts.size(); i++) {
                            if( mArrContacts.get(i).is_choose_state > 0) {
                                mArrContacts.remove(i);
                                break;
                            }
                        }
                        adapter.notifyDataSetChanged();
                        Log.e("오늘의 소개 갯수" , "count = " + adapter.getItemCount() + "");
                        updateMettingCount();
                        loadHistory();

                        // gif 노출
                        if (mArrContacts.size() > 2 && mArrChoosedTodayMetting.size() < 2) {
                            adapter.updateStatus(0);
                            cardHandler.sendEmptyMessageDelayed(0, 3300);
                        }
                    }

                    @Override
                    public void onFailure(MError response) {
                        super.onFailure(response);
                        hideProgress();

                        if (IS_UITEST) {

                            MyInfo.getInstance().energy -= 15;
                            updateEnergyCount();
                            mArrChoosedTodayMetting.clear();
                            updateMettingCount();

                        } else {
                            if (response.resultcode == 500) {
                                networkErrorOccupied(response);
                            } else {
                                new AlertDialog.Builder(getActivity()).setTitle("").setMessage(response.res_msg).setPositiveButton(R.string.confirm, null).show();
                            }
                        }
                    }
                });
    }

    void apiBanner() {
        showProgress();
        Net.instance().api.get_banner_list()
                .enqueue(new Net.ResponseCallBack<MBanner>() {
                    @Override
                    public void onSuccess(MBanner response) {
                        super.onSuccess(response);
                        hideProgress();

                        mTopAdapter.mBanner.clear();

                        for (int i = 0; i < response.arr_list.size(); i++) {
                            mTopAdapter.mBanner.add(response.arr_list.get(i));
                        }

                        mTopAdapter.notifyDataSetChanged();

                        rlBanner.setVisibility(response.arr_list.size() == 0 ? View.GONE : View.VISIBLE);
                    }

                    @Override
                    public void onFailure(MError response) {
                        super.onFailure(response);
                        hideProgress();

                        if (response.resultcode == 500) {
                            networkErrorOccupied(response);
                        } else {
                            Toaster.showShort(getActivity(), "오류입니다.");
                        }
                    }
                });
    }

    @OnClick(R.id.btnMore)
    void onMore() {
        btnMore.setVisibility(View.GONE);

        mArrHistory.clear();
        for (int i = 0; i < mArrMore.size(); i++) {
            mArrMore.get(i).diplay_state_home = true;
            mArrHistory.add(mArrMore.get(i));
        }

        if (hisAdapter != null) {
            hisAdapter.notifyDataSetChanged();
        }
    }

    @OnClick({R.id.iv_my_point, R.id.rl_nav_menu})
    void onPoint(View view) {
        if (view.getId() == R.id.iv_my_point) {
            startActivity(new Intent(getActivity(), EnergyActivity.class));
        } else {
            startActivityForResult(new Intent(getActivity(), AlarmListActivity.class), 10022);
        }
    }

    @OnClick(R.id.btn_meet_more)
    void onClickMeetMore() {

        if (getChoosableContactsCount() < 2)
            return;

        ConfirmDialog2.show(getContext(), "추가 소개를 받습니다.", "에너지 15개를 사용할까요?", new ConfirmDialog2.ActionListener() {
            @Override
            public void onOk() {

                if (MyInfo.getInstance().energy >= 15) {
                    MyInfo.getInstance().select_man = 0;
                    calcEnergy(15, 2, 0);

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

    @OnClick(R.id.btn_find_model)
    public void onClickFindModel() {
        //이상형 찾기
        Intent goModelFind = new Intent(getContext(), ModelFindActivity.class);
        startActivityForResult(goModelFind, 1002);
    }

    public void onClickUser(final MUserList.User info) {
//        if (info.isPublic) {
//            if (info.visit == 1) {
        Intent goProfile = new Intent(getActivity(), ProfileActivity.class);
        goProfile.putExtra("usr_uid", info.uid);
        startActivity(goProfile);
//            } else {
//                ConfirmDialog2.show(getContext(), String.format(getString(R.string.confirm_view_user_profile), info.nickname), "에너지 10개를 사용할까요?", new ConfirmDialog2.ActionListener() {
//                    @Override
//                    public void onOk() {
//
//                        if (MyInfo.getInstance().energy >= 20) {
//
//                            calcEnergy(10, 1, info.uid);
//                        } else {
//
//                            ConfirmDialog2.show(getContext(), "에너지가 부족해요!!", String.format(getString(R.string.confirm_get_energy), (10 - MyInfo.getInstance().energy)),
//                                    new ConfirmDialog2.ActionListener() {
//                                        @Override
//                                        public void onOk() {
//                                            Intent goEnergy = new Intent(getActivity(), EnergyupActivity.class);
//                                            goEnergy.putExtra("nickname", info.nickname);
//                                            startActivity(goEnergy);
//                                        }
//                                    });
//                        }
//                    }
//                });
//            }
//        }
    }

    private void showProgress() {
        clProgress.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        clProgress.setVisibility(View.GONE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1002 && resultCode == RESULT_OK) {

            if (Common.g_models.size() == 2) {
                SearchDialog.show(getContext(), (Common.g_models.size() != 0 ? Common.g_models.get(0).pay_title : ""), Common.g_models, new SearchDialog.ActionListener() {
                    @Override
                    public void onOk() {
                        loadTodayMeeting();
                    }

                    @Override
                    public void onSelect(int user_id) {
                        Intent goProfile = new Intent(getActivity(), ProfileActivity.class);
                        goProfile.putExtra("usr_uid", user_id);
                        startActivity(goProfile);
                    }
                });
            } else {
                Toaster.showShort(getContext(), "이상형찾기 결과가 없습니다.");
                // 이상형 찾기 실패 - 에너지 복구
                Net.instance().api.restore_my_energy(MyInfo.getInstance().uid, Common.g_modelType)
                        .enqueue(new Net.ResponseCallBack<MEnergy>() {
                            @Override
                            public void onSuccess(MEnergy response) {
                                super.onSuccess(response);
                                hideProgress();

                                MyInfo.getInstance().energy = response.energy;
                                updateBadgeCount();
                            }

                            @Override
                            public void onFailure(MError response) {
                                super.onFailure(response);
                                hideProgress();

                                if (response.resultcode == 500) {
                                    networkErrorOccupied(response);
                                } else {
                                    Toaster.showShort(getActivity(), "오류입니다.");
                                }
                            }
                        });
            }

        } else if (requestCode == 10022) {
            getUnreadAlarmCnt();
        }
    }

    class ImagePagerAdapter extends PagerAdapter {
        List<MBanner.Result> mBanner = new ArrayList<>();

        ImagePagerAdapter() {
        }

        @Override
        public int getCount() {
            return mBanner.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            LayoutInflater inflater = (LayoutInflater) getActivity()
                    .getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
            View itemView = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.item_profile_banner, container, false).getRoot();

            ImageView ivImage = itemView.findViewById(R.id.ivPhoto);

            Util.loadImage(getActivity(), ivImage, mBanner.get(position).image);
            ivImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mBanner.get(position).url));
                    startActivity(browserIntent);
                }
            });
            container.addView(itemView, 0);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            (container).removeView((View) object);
        }
    }

}
