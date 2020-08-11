package com.character.microblogapp.ui.page.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.character.microblogapp.R;
import com.character.microblogapp.data.MyInfo;
import com.character.microblogapp.model.MAppAlarm;
import com.character.microblogapp.model.MBase;
import com.character.microblogapp.model.MContent;
import com.character.microblogapp.model.MError;
import com.character.microblogapp.net.Net;
import com.character.microblogapp.ui.page.BaseActivity;
import com.character.microblogapp.util.Toaster;

import butterknife.BindView;
import butterknife.OnClick;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
public class AlarmActivity extends BaseActivity {

    @BindView(R.id.btnBack)
    ImageButton btnBack;

    @BindView(R.id.btnMenu)
    ImageButton btnMenu;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.btnAll)
    ImageButton btnAll;

    @BindView(R.id.btnSound)
    ImageButton btnSound;

    @BindView(R.id.btnRecommend)
    ImageButton btnRecommend;

    @BindView(R.id.btnHigh)
    ImageButton btnHigh;

    @BindView(R.id.btnLike)
    ImageButton btnLike;

    @BindView(R.id.btnResult)
    ImageButton btnResult;

    @BindView(R.id.btnChatting)
    ImageButton btnChatting;

    @BindView(R.id.btnEvent)
    ImageButton btnEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewDataBinding binding = DataBindingUtil.setContentView(this,R.layout.activity_alarm);
    }

    @Override
    protected void initUI() {
        super.initUI();

        tvTitle.setText("알림설정");
        btnMenu.setVisibility(View.GONE);

        getAlarmSetting();
    }

    private MAppAlarm appAlarm = new MAppAlarm();

    void updateUI() {
        btnAll.setVisibility(View.VISIBLE);
        btnChatting.setVisibility(View.VISIBLE);
        btnEvent.setVisibility(View.VISIBLE);
        btnHigh.setVisibility(View.VISIBLE);
        btnLike.setVisibility(View.VISIBLE);
        btnRecommend.setVisibility(View.VISIBLE);
        btnResult.setVisibility(View.VISIBLE);
        btnSound.setVisibility(View.VISIBLE);

        int nAll = 0;

        if (appAlarm.sound_alarm == 1) {
            btnSound.setSelected(true);
            nAll++;
        } else {
            btnSound.setSelected(false);
        }

        if (appAlarm.recommend_alarm == 1) {
            btnRecommend.setSelected(true);
            nAll++;
        } else {
            btnRecommend.setSelected(false);
        }

        if (appAlarm.high_alarm == 1) {
            btnHigh.setSelected(true);
            nAll++;
        } else {
            btnHigh.setSelected(false);
        }

        if (appAlarm.like_alarm == 1) {
            btnLike.setSelected(true);
            nAll++;
        } else {
            btnLike.setSelected(false);
        }

        if (appAlarm.result_alarm == 1) {
            btnResult.setSelected(true);
            nAll++;
        } else {
            btnResult.setSelected(false);
        }

        if (appAlarm.chatting_alarm == 1) {
            btnChatting.setSelected(true);
            nAll++;
        } else {
            btnChatting.setSelected(false);
        }

        if (appAlarm.event_alarm == 1) {
            btnEvent.setSelected(true);
            nAll++;
        } else {
            btnEvent.setSelected(false);
        }

        if (nAll == 7) {
            btnAll.setSelected(true);
        } else {
            btnAll.setSelected(false);
        }
    }

    void getAlarmSetting() {

        showProgress(this);
        Net.instance().api.get_app_alarm(MyInfo.getInstance().uid)
                .enqueue(new Net.ResponseCallBack<MAppAlarm>() {
                    @Override
                    public void onSuccess(MAppAlarm response) {
                        super.onSuccess(response);
                        hideProgress();

                        appAlarm = response;

                        updateUI();
                    }

                    @Override
                    public void onFailure(MError response) {
                        super.onFailure(response);
                        hideProgress();

                        if (response.resultcode == 500) {
                            networkErrorOccupied(response);
                        } else {
                            Toaster.showShort(AlarmActivity.this, "오류입니다.");
                        }
                    }
                });
    }

    @OnClick(R.id.btnBack)
    void onBack() {
        finish();
    }

    @OnClick({R.id.btnAll, R.id.btnSound, R.id.btnRecommend, R.id.btnHigh, R.id.btnLike, R.id.btnResult, R.id.btnChatting, R.id.btnEvent})
    void onClick(View view) {
        if (view.getId() == R.id.btnAll) {
            btnSound.setSelected(!btnAll.isSelected());
            btnRecommend.setSelected(!btnAll.isSelected());
            btnHigh.setSelected(!btnAll.isSelected());
            btnLike.setSelected(!btnAll.isSelected());
            btnResult.setSelected(!btnAll.isSelected());
            btnChatting.setSelected(!btnAll.isSelected());
            btnEvent.setSelected(!btnAll.isSelected());

            btnAll.setSelected(!btnAll.isSelected());
            apiChange(0, btnAll.isSelected());

            return;
        } else if (view.getId() == R.id.btnSound) {
            btnSound.setSelected(!btnSound.isSelected());

            apiChange(1, btnSound.isSelected());
        } else if (view.getId() == R.id.btnRecommend) {
            btnRecommend.setSelected(!btnRecommend.isSelected());

            apiChange(2, btnRecommend.isSelected());
        } else if (view.getId() == R.id.btnHigh) {
            btnHigh.setSelected(!btnHigh.isSelected());

            apiChange(3, btnHigh.isSelected());
        } else if (view.getId() == R.id.btnLike) {
            btnLike.setSelected(!btnLike.isSelected());

            apiChange(4, btnLike.isSelected());
        } else if (view.getId() == R.id.btnResult) {
            btnResult.setSelected(!btnResult.isSelected());

            apiChange(5, btnResult.isSelected());
        } else if (view.getId() == R.id.btnChatting) {
            btnChatting.setSelected(!btnChatting.isSelected());

            apiChange(6, btnChatting.isSelected());
        } else if (view.getId() == R.id.btnEvent) {
            btnEvent.setSelected(!btnEvent.isSelected());

            apiChange(7, btnEvent.isSelected());
        }

        btnAll.setSelected(btnSound.isSelected() &&
                btnRecommend.isSelected() &&
                btnHigh.isSelected() &&
                btnLike.isSelected() &&
                btnResult.isSelected() &&
                btnChatting.isSelected() &&
                btnEvent.isSelected());
    }

    void apiChange(final int type, final boolean status) {
        showProgress(this);
        Net.instance().api.change_alarm_setting(MyInfo.getInstance().uid, type, status ? 1 : 0)
                .enqueue(new Net.ResponseCallBack<MBase>() {
                    @Override
                    public void onSuccess(MBase response) {
                        super.onSuccess(response);
                        hideProgress();

                        if (type == 0) {

                            if (status) {
                                appAlarm.sound_alarm = 1;
                                appAlarm.recommend_alarm = 1;
                                appAlarm.high_alarm = 1;
                                appAlarm.like_alarm = 1;
                                appAlarm.result_alarm = 1;
                                appAlarm.chatting_alarm = 1;
                                appAlarm.event_alarm = 1;
                            } else {
                                appAlarm.sound_alarm = 0;
                                appAlarm.recommend_alarm = 0;
                                appAlarm.high_alarm = 0;
                                appAlarm.like_alarm = 0;
                                appAlarm.result_alarm = 0;
                                appAlarm.chatting_alarm = 0;
                                appAlarm.event_alarm = 0;
                            }
                        } else if (type == 1) {
                            if (status) {
                                appAlarm.sound_alarm = 1;
                            } else {
                                appAlarm.sound_alarm = 0;
                            }
                        } else if (type == 2) {
                            if (status) {
                                appAlarm.recommend_alarm = 1;
                            } else {
                                appAlarm.recommend_alarm = 0;
                            }
                        } else if (type == 3) {
                            if (status) {
                                appAlarm.high_alarm = 1;
                            } else {
                                appAlarm.high_alarm = 0;
                            }
                        } else if (type == 4) {
                            if (status) {
                                appAlarm.like_alarm = 1;
                            } else {
                                appAlarm.like_alarm = 0;
                            }
                        } else if (type == 5) {
                            if (status) {
                                appAlarm.result_alarm = 1;
                            } else {
                                appAlarm.result_alarm = 0;
                            }
                        } else if (type == 6) {
                            if (status) {
                                appAlarm.chatting_alarm = 1;
                            } else {
                                appAlarm.chatting_alarm = 0;
                            }
                        } else if (type == 7) {
                            if (status) {
                                appAlarm.event_alarm = 1;
                            } else {
                                appAlarm.event_alarm = 0;
                            }
                        }
                    }

                    @Override
                    public void onFailure(MError response) {
                        super.onFailure(response);
                        hideProgress();

                        if (response.resultcode == 500) {
                            networkErrorOccupied(response);
                        } else {
                            Toaster.showShort(AlarmActivity.this, "오류입니다.");
                        }
                    }
                });
    }

}
