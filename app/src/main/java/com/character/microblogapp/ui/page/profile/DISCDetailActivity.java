package com.character.microblogapp.ui.page.profile;

import android.app.AlertDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.character.microblogapp.R;
import com.character.microblogapp.data.Constant;
import com.character.microblogapp.data.MyInfo;
import com.character.microblogapp.model.MError;
import com.character.microblogapp.model.MUser;
import com.character.microblogapp.net.Net;
import com.character.microblogapp.ui.page.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class DISCDetailActivity extends BaseActivity {

    @BindView(R.id.txv_top_nickname)
    TextView txvTopNickName;

    @BindView(R.id.tvTitle)
    TextView tvTItle;

    @BindView(R.id.tvDetail)
    TextView tvDetail;

    @BindView(R.id.tvBottom)
    TextView tvBottom;

    String type = "D";
    int status = 1; // 1 ? 유형별 직업정보 2 ? 유형별 선호 직업환경 3 ? 유형별 선호 성격

    MUser user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewDataBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_disc_detail);

        initUI();
    }

    @Override
    protected void initUI() {
        super.initUI();
    }

    @Override
    protected void onResume() {
        super.onResume();

        type = getIntent().getStringExtra("type");
        status = getIntent().getIntExtra("status", 0);

        apiInfo();
    }

    void updateUI() {
        String w_type = type;
        if(w_type.equals("D=")){
            w_type = "D";
        } else if(w_type.equals("I=")){
            w_type = "I";
        } else if(w_type.equals("S=")){
            w_type = "S";
        } else if(w_type.equals("C=")){
            w_type = "C";
        }


        tvTItle.setText(w_type);
        txvTopNickName.setText(user.info.nickname);  //타이틀
        tvDetail.setText(user.info.character_detail);

        switch (w_type) {
            case "D":
            case "d":
                tvTItle.setTextColor(CHARACTER_D_COLOR);
                break;
            case "I":
            case "i":
                tvTItle.setTextColor(CHARACTER_I_COLOR);
                break;
            case "C":
            case "c":
                tvTItle.setTextColor(CHARACTER_C_COLOR);
                break;
            case "S":
            case "s":
                tvTItle.setTextColor(CHARACTER_S_COLOR);
                break;
        }

        tvBottom.setText(status == 1 ? "유형별 직업정보" : status == 2 ? "유형별 선호 직업환경" : "유형별 선호 성격");
    }

    void apiInfo() {
        showProgress(this);
        Net.instance().api.get_user_character_info(MyInfo.getInstance().uid, type, status)
                .enqueue(new Net.ResponseCallBack<MUser>() {
                    @Override
                    public void onSuccess(MUser response) {
                        super.onSuccess(response);
                        hideProgress();

                        user = response;

                        updateUI();
                    }

                    @Override
                    public void onFailure(MError response) {
                        super.onFailure(response);
                        hideProgress();

                        if (response.resultcode == 500) {
                            networkErrorOccupied(response);

                        } else {
                            new AlertDialog.Builder(DISCDetailActivity.this).setTitle("").setMessage(response.res_msg).setPositiveButton(R.string.confirm, null).show();
                        }
                    }
                });
    }

    @OnClick(R.id.rlt_back)
    void goBack() {
        finish();
    }

    @OnClick(R.id.rlt_info)
    void goInfo() {
        Intent intent = new Intent(this, ProfileDISCInfoActivity.class);
        startActivity(intent);
    }
}
