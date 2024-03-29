package com.character.microblogapp.ui.page.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.character.microblogapp.R;
import com.character.microblogapp.data.Constant;
import com.character.microblogapp.data.MyInfo;
import com.character.microblogapp.fcm.EventMessage;
import com.character.microblogapp.model.MBase;
import com.character.microblogapp.model.MError;
import com.character.microblogapp.net.Net;
import com.character.microblogapp.ui.page.BaseActivity;
import com.character.microblogapp.ui.page.main.fragment.CharInfoFragment;
import com.character.microblogapp.ui.page.main.fragment.CharWorldFragment;
import com.character.microblogapp.ui.page.main.fragment.ChatFragment;
import com.character.microblogapp.ui.page.main.fragment.ChemistryFragment;
import com.character.microblogapp.ui.page.main.fragment.DISCCharListFragment;
import com.character.microblogapp.ui.page.main.fragment.DISCInfoMainFragment;
import com.character.microblogapp.ui.page.main.fragment.DISCInfoTypeFragment;
import com.character.microblogapp.ui.page.main.fragment.DISCMyLoverFragment;
import com.character.microblogapp.ui.page.main.fragment.EvaluationFragment;
import com.character.microblogapp.ui.page.main.fragment.HistoryFragment;
import com.character.microblogapp.ui.page.main.fragment.HomeFragment;
import com.character.microblogapp.ui.page.main.fragment.InspectingUserFragment;
import com.character.microblogapp.ui.page.main.fragment.MyCharInfoFragment;
import com.character.microblogapp.ui.page.main.fragment.PassedInspectionFragment;
import com.character.microblogapp.ui.page.main.fragment.ProfileFragment;
import com.character.microblogapp.ui.page.main.fragment.RejectFragment;
import com.character.microblogapp.util.PrefMgr;
import com.character.microblogapp.util.Toaster;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
public class MainActivity extends BaseActivity {

    private final static int MAIN_TAB_HOME = 0;
    private final static int MAIN_TAB_HISTORY = 1;
    private final static int MAIN_TAB_EVAL = 2;
    private final static int MAIN_TAB_CHAT = 3;
    private final static int MAIN_TAB_PROFILE = 4;
    private final static int PROFILE_CHECKING = 5;
    private final static int PROFILE_ACCEPT = 6;
    private final static int PROFILE_REJECT = 7;
    /*200721, add*/
    private final static int MAIN_CHAR_INFO = 8;
    /*200818, add*/
    private final static int MAIN_DISC_INFO_MAIN = 9;
    private final static int MAIN_DISC_INFO_TYPE_D = 10;
    private final static int MAIN_DISC_INFO_TYPE_I = 11;
    private final static int MAIN_DISC_INFO_TYPE_S = 12;
    private final static int MAIN_DISC_INFO_TYPE_C = 13;

    private final static int MAIN_CHAR_INFO_MY_CHAR = 14;
    private final static int MAIN_CHAR_INFO_LOVER = 15;
    private final static int MAIN_CHAR_LIST = 16;
    private final static int MAIN_CHEMISTRY = 17;

    public String parentFrag = "";
    public String charListType = "";
    public int charInfoPageNum = 0;



    RadioGroup btTabGroup;

    ImageView img1;
    ImageView img2;
    ImageView img3;
    ImageView img4;
    ImageView img5;

    Fragment fragment = null;

    @BindView(R.id.tvChat)
    TextView tvBadge;

    int mCurrentTabIdx = MAIN_TAB_HOME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewDataBinding binding = DataBindingUtil.setContentView(this,R.layout.activity_main);

        setFinishAppWhenPressedBackKey();
        EventBus.getDefault().register(this);

        int tab = getIntent().getIntExtra("tab", 0);
        if (tab != 0) {
            if (tab == 11000 || tab == 11300) {
                mCurrentTabIdx = MAIN_TAB_HISTORY;
            } else if (tab == 13000) {
                mCurrentTabIdx = MAIN_TAB_CHAT;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (MyInfo.getInstance().status == 0) {
            MyInfo.getInstance().profile_status = Constant.PRFILE_STATUS_CHECKING;

            RadioButton rbHome = findViewById(R.id.navigation_home);
            rbHome.setChecked(true);

            selectLine(0);

            initVC();
        }

        if (mCurrentTabIdx == MAIN_TAB_EVAL) {
            selectLine(MAIN_TAB_EVAL);
            selectTab(MAIN_TAB_EVAL);
        } else if (mCurrentTabIdx == MAIN_TAB_PROFILE) {
            selectLine(MAIN_TAB_PROFILE);
            selectTab(MAIN_TAB_PROFILE);
        } else if (mCurrentTabIdx == MAIN_TAB_HOME) {
         //   ((HomeFragment)fragment).loadHistory();
        } else if (mCurrentTabIdx == MAIN_TAB_CHAT) {
            selectLine(MAIN_TAB_CHAT);
            selectTab(MAIN_TAB_CHAT);
        }
    }

    @Override
    protected void initUI() {
        super.initUI();
        btTabGroup = findViewById(R.id.navigation_group);
        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);
        img3 = findViewById(R.id.img3);
        img4 = findViewById(R.id.img4);
        img5 = findViewById(R.id.img5);

        initVC();
    }

    @Override
    public void onBackPressed() {
//        if (mCurrentTabIdx == MAIN_TAB_HOME || mCurrentTabIdx == PROFILE_REJECT) {
//            if (!((HomeFragment)fragment).resetUI()) {
//                super.onBackPressed();
//            }
//        } else {
//            super.onBackPressed();
//        }
        if(mCurrentTabIdx == MAIN_CHAR_INFO){
            fragment = new CharWorldFragment();
            selectLine(0);
            selectTab(0);
        }else if(mCurrentTabIdx == MAIN_DISC_INFO_MAIN){
            fragment = new CharInfoFragment();
            selectLine(0);
            selectTab(MAIN_CHAR_INFO);

        }else if(mCurrentTabIdx == MAIN_TAB_HOME){
            super.onBackPressed();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdated(EventMessage event) {
        if (event.nWhat == 10017) {
            MyInfo.getInstance().profile_status = Constant.PRFILE_STATUS_ACCEPT;

            initVC();
        } else if (event.nWhat == 11000) {
            tvBadge.postDelayed(new Runnable() {
                @Override
                public void run() {
                    selectLine(MAIN_TAB_HISTORY);
                    selectTab(MAIN_TAB_HISTORY);
                    ((HistoryFragment)fragment).selectTab(0);
                }
            }, 10);

        } else if (event.nWhat == 11300) {

            tvBadge.postDelayed(new Runnable() {
                @Override
                public void run() {
                    selectLine(MAIN_TAB_HISTORY);
                    selectTab(MAIN_TAB_HISTORY);
                    ((HistoryFragment)fragment).selectTab(2);
                }
            }, 10);
        } else if (event.nWhat == 13000) {

            tvBadge.postDelayed(new Runnable() {
                @Override
                public void run() {
                    selectLine(MAIN_TAB_CHAT);
                    selectTab(MAIN_TAB_CHAT);
                }
            }, 10);
        } else if (event.nWhat == 20000) { // 알람 업데이트
            if (mCurrentTabIdx == MAIN_TAB_HOME) {
              //  ((HomeFragment)fragment).getUnreadAlarmCnt();
            }
        }
    }

    void initVC() {
        SharedPreferences prefs = getSharedPreferences(PrefMgr.APP_PREFS,
                Context.MODE_PRIVATE);
        PrefMgr prefMgr = new PrefMgr(prefs);

        if (MyInfo.getInstance().status == 2) {
            prefMgr.put(PrefMgr.WATCHED_PROFILE_ACCEPTED, false);

            selectLine(MAIN_TAB_PROFILE);
            selectTab(MAIN_TAB_PROFILE);

            RadioButton rbProfile = findViewById(R.id.navigation_profile);
            rbProfile.setChecked(true);
            return;
        } else if (MyInfo.getInstance().profile_status == Constant.PRFILE_STATUS_CHECKING) {
            selectTab(PROFILE_CHECKING);
            return;
        } else  if (MyInfo.getInstance().profile_status == Constant.PRFILE_STATUS_REJECT) {
            selectTab(PROFILE_REJECT);
            return;
        } else if (MyInfo.getInstance().profile_status == Constant.PRFILE_STATUS_ACCEPT) {
            if (!prefMgr.getBoolean(PrefMgr.WATCHED_PROFILE_ACCEPTED, false)) {
                selectTab(PROFILE_ACCEPT);
                return;
            }
        }

        if (prefMgr.getInt(PrefMgr.PUSH_ACCEPTED, 0) > 0) {
            int tab = prefMgr.getInt(PrefMgr.PUSH_ACCEPTED, 0);
            if (tab == 13000) {
                prefMgr.put(PrefMgr.PUSH_ACCEPTED, 0);
                selectLine(MAIN_TAB_CHAT);
                selectTab(MAIN_TAB_CHAT);
                return;
            } else {
                prefMgr.put(PrefMgr.PUSH_ACCEPTED, 0);
                selectLine(MAIN_TAB_HISTORY);
                selectTab(MAIN_TAB_HISTORY);
                return;
            }
        }
        selectTab(MAIN_TAB_HOME);
    }

    @OnClick({R.id.navigation_home, R.id.navigation_history, R.id.navigation_eval, R.id.navigation_chat, R.id.navigation_profile})
    void onClickTabBtn() {
        // 프로필 심사에 통과되지 못했거나 심사통과확인페이지 현시내역이 없으면
        // 심사통과확인페이지 현시 및 다른 페이지로 이동 불가
        SharedPreferences prefs = getSharedPreferences(PrefMgr.APP_PREFS,
                Context.MODE_PRIVATE);
        PrefMgr prefMgr = new PrefMgr(prefs);
        if (MyInfo.getInstance().profile_status != Constant.PRFILE_STATUS_ACCEPT || !prefMgr.getBoolean(PrefMgr.WATCHED_PROFILE_ACCEPTED, false)) {
            if (MyInfo.getInstance().status == 2) {
                RadioButton rbProfile = findViewById(R.id.navigation_profile);
                rbProfile.setChecked(true);
            } else {
                RadioButton rbHome = findViewById(R.id.navigation_home);
                rbHome.setChecked(true);
            }
            return;
        }

        switch (btTabGroup.getCheckedRadioButtonId()) {
            case R.id.navigation_home:
                selectLine(MAIN_TAB_HOME);
                selectTab(MAIN_TAB_HOME);
                break;
            case R.id.navigation_history:
                selectLine(MAIN_TAB_HISTORY);
                selectTab(MAIN_TAB_HISTORY);
                break;
            case R.id.navigation_eval:
                selectLine(MAIN_TAB_EVAL);
                selectTab(MAIN_TAB_EVAL);
                break;
            case R.id.navigation_chat:
                selectLine(MAIN_TAB_CHAT);
                selectTab(MAIN_TAB_CHAT);
                break;
            case R.id.navigation_profile:
                selectLine(MAIN_TAB_PROFILE);
                selectTab(MAIN_TAB_PROFILE);
                break;
        }
    }

    public void selectLine(int idx) {
        img1.setVisibility(View.INVISIBLE);
        img2.setVisibility(View.GONE);
        img3.setVisibility(View.INVISIBLE);
        img4.setVisibility(View.GONE);
        img5.setVisibility(View.INVISIBLE);

        switch (idx) {
            case MAIN_TAB_HOME:
                img1.setVisibility(View.VISIBLE);
                break;
            case MAIN_TAB_HISTORY:
                img2.setVisibility(View.VISIBLE);
                break;
            case MAIN_TAB_EVAL:
                img3.setVisibility(View.VISIBLE);
                break;
            case MAIN_TAB_CHAT:
                img4.setVisibility(View.VISIBLE);
                break;
            case MAIN_TAB_PROFILE:
                img5.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void selectTab(int idx) {
        String tag = "";
        switch (idx) {
            case MAIN_TAB_HOME:
                fragment = new CharWorldFragment();
                tag = getString(R.string.tab_home);
                break;
            case MAIN_TAB_HISTORY:
                fragment = new HistoryFragment();
                tag = getString(R.string.tab_history);
                break;
            case MAIN_TAB_EVAL:
                if (mCurrentTabIdx != MAIN_TAB_EVAL) {
                    fragment = new EvaluationFragment();
                }
                tag = getString(R.string.tab_eval);
                break;
            case MAIN_TAB_CHAT:
                fragment = new ChatFragment();
                tag = getString(R.string.tab_chat);
                break;
            case MAIN_TAB_PROFILE:
                fragment = new ProfileFragment();
                tag = getString(R.string.tab_profile);
                break;
            case PROFILE_CHECKING:
                fragment = new InspectingUserFragment();
                tag = getString(R.string.inspecting_profile_processing);
                break;
            case PROFILE_ACCEPT:
                fragment = new PassedInspectionFragment();
                tag = getString(R.string.passed_inspection_part_1);
                break;
            case PROFILE_REJECT:
                fragment = new RejectFragment();
                tag = getString(R.string.passed_inspection_part_2);
                break;
            case MAIN_CHAR_INFO:
                fragment = new CharInfoFragment();
                tag = "성격정보";
                break;
            case MAIN_DISC_INFO_MAIN:
                fragment = new DISCInfoMainFragment();
                tag = "성격정보2";
                break;
            case MAIN_DISC_INFO_TYPE_D:
                fragment = new DISCInfoTypeFragment();
                tag = "D";
                break;
            case MAIN_DISC_INFO_TYPE_I:
                fragment = new DISCInfoTypeFragment();
                tag = "I";
                break;
            case MAIN_DISC_INFO_TYPE_S:
                fragment = new DISCInfoTypeFragment();
                tag = "S";
                break;
            case MAIN_DISC_INFO_TYPE_C:
                fragment = new DISCInfoTypeFragment();
                tag = "C";
                break;
            case MAIN_CHAR_INFO_MY_CHAR:
                fragment = new MyCharInfoFragment();

                if(charInfoPageNum == 0){
                    tag = "myInfo";
                }else if(charInfoPageNum == 1){
                    tag = charListType;
                    charListType = "";
                }else if(charInfoPageNum == 2){
                    tag = charListType;
                    charListType = "";
                }
                break;
            case MAIN_CHAR_INFO_LOVER:
                fragment = new DISCMyLoverFragment();
                tag = "나와 잘 맞는 성격";
                break;
            case MAIN_CHAR_LIST:
                fragment = new DISCCharListFragment();
                if(parentFrag.equals("CharInfoFragment")){
                    tag = "CharInfoFragment";
                }else if(parentFrag.equals("DISCMyLoverFragment")){
                    tag = "DISCMyLoverFragment";
                }

                break;
            case MAIN_CHEMISTRY:
                fragment = new ChemistryFragment();
                    tag = "ChemistryFragment";



                break;

        }

        mCurrentTabIdx = idx;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fm_container, fragment, tag);
        transaction.commitAllowingStateLoss();
        apiCount();
    }

    public void goHome() {
        MyInfo.getInstance().profile_status = Constant.PRFILE_STATUS_ACCEPT;
        RadioButton rbHome = findViewById(R.id.navigation_home);
        rbHome.setChecked(true);

        selectLine(MAIN_TAB_HOME);
        selectTab(MAIN_TAB_HOME);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        FragmentManager manager = getSupportFragmentManager();
        if (manager != null) {
            for (Fragment fragment : manager.getFragments()) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    void apiCount() {
        Net.instance().api.get_count(MyInfo.getInstance().uid)
                .enqueue(new Net.ResponseCallBack<MBase>() {
                    @Override
                    public void onSuccess(MBase response) {
                        super.onSuccess(response);

                        tvBadge.setVisibility(response.count == 0 ? View.GONE : View.VISIBLE);
                        tvBadge.setText("" + response.count);
                    }

                    @Override
                    public void onFailure(MError response) {
                        super.onFailure(response);

                        if (response.resultcode == 500) {
                            networkErrorOccupied(response);
                        } else {
                            Toaster.showShort(MainActivity.this, "오류입니다.");
                        }
                    }
                });
    }
}