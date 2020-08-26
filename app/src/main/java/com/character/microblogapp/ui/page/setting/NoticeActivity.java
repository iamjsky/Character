package com.character.microblogapp.ui.page.setting;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.character.microblogapp.R;
import com.character.microblogapp.model.MContent;
import com.character.microblogapp.model.MError;
import com.character.microblogapp.net.Net;
import com.character.microblogapp.ui.page.BaseActivity;
import com.character.microblogapp.util.Toaster;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.internal.operators.maybe.MaybeCallbackObserver;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;

public class NoticeActivity extends BaseActivity {

    @BindView(R.id.btnBack)
    ImageButton btnBack;

    @BindView(R.id.btnMenu)
    ImageButton btnMenu;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.tvContent)
    TextView tvContent;

    @BindView(R.id.tv1)
    TextView tv1;

    @BindView(R.id.tv2)
    TextView tv2;

    @BindView(R.id.tv3)
    TextView tv3;

    @BindView(R.id.img1)
    ImageView img1;

    @BindView(R.id.img2)
    ImageView img2;

    @BindView(R.id.img3)
    ImageView img3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewDataBinding binding = DataBindingUtil.setContentView(this,R.layout.activity_notice);
    }

    @Override
    protected void initUI() {
        super.initUI();

        tvTitle.setText("약관");
        btnMenu.setVisibility(View.GONE);
        btnBack.setVisibility(View.VISIBLE);
        resetTap(getIntent().getIntExtra("type", 1));
    }

    @OnClick(R.id.btnBack)
    void onBack() {
        finish();
    }

    @OnClick({R.id.rl1, R.id.rl2, R.id.rl3})
    void onClick(View view) {
        if (view.getId() == R.id.rl1) {
            resetTap(1);
        } else if (view.getId() == R.id.rl2) {
            resetTap(2);
        } else if (view.getId() == R.id.rl3) {
            resetTap(3);
        }
    }

    void resetTap(int tap) {
        tv1.setTextColor(Color.parseColor("#888888"));
        img1.setVisibility(View.GONE);
        tv2.setTextColor(Color.parseColor("#888888"));
        img2.setVisibility(View.GONE);
        tv3.setTextColor(Color.parseColor("#888888"));
        img3.setVisibility(View.GONE);

        if (tap == 1) {
            tv1.setTextColor(Color.parseColor("#f3788b"));
            img1.setVisibility(View.VISIBLE);
        } else if (tap == 2) {
            tv2.setTextColor(Color.parseColor("#f3788b"));
            img2.setVisibility(View.VISIBLE);
        } else {
            tv3.setTextColor(Color.parseColor("#f3788b"));
            img3.setVisibility(View.VISIBLE);
        }

        apiContent(tap);
    }

    void apiContent(int type) {
        showProgress(this);
        Net.instance().api.get_agreement(type)
                .enqueue(new Net.ResponseCallBack<MContent>() {
                    @Override
                    public void onSuccess(MContent response) {
                        super.onSuccess(response);
                        hideProgress();

                        tvContent.setText(response.content);
                    }

                    @Override
                    public void onFailure(MError response) {
                        super.onFailure(response);
                        hideProgress();

                        if (response.resultcode == 500) {
                            networkErrorOccupied(response);
                        } else {
                            Toaster.showShort(NoticeActivity.this, "오류입니다.");
                        }
                    }
                });
    }

}
