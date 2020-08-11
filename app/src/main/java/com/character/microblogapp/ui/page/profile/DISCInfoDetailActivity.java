package com.character.microblogapp.ui.page.profile;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.widget.TextView;

import com.character.microblogapp.R;
import com.character.microblogapp.data.MyInfo;
import com.character.microblogapp.model.MCharacterInfo;
import com.character.microblogapp.model.MError;
import com.character.microblogapp.model.MVersion;
import com.character.microblogapp.net.Net;
import com.character.microblogapp.ui.page.BaseActivity;
import com.character.microblogapp.ui.page.character.CharacterConfirmActivity;
import com.character.microblogapp.util.Toaster;

import butterknife.BindView;
import butterknife.OnClick;

public class DISCInfoDetailActivity extends BaseActivity {

    @BindView(R.id.txv_top_nickname)
    TextView txvTopNickName;

    @BindView(R.id.tv_info)
    TextView tvInfo;

    String character = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewDataBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_disc_info_detail);

        initUI();
        getInfo();
    }

    @Override
    protected void initUI() {
        super.initUI();
        updateUI();
    }

    void updateUI() {
        character = getIntent().getStringExtra("type");
        if (character != null) {
            txvTopNickName.setText(character + "형");  //타이틀
        }
    }

    @OnClick(R.id.rlt_back)
    void goBack() {
        finish();
    }

    private void getInfo() {
        showProgress(this);
        Net.instance().api.get_character_info_style(character)
                .enqueue(new Net.ResponseCallBack<MVersion>() {
                    @Override
                    public void onSuccess(MVersion response) {
                        hideProgress();
                        if (response.info != null) {
                            String text = response.info;
                            tvInfo.setText(text);
                        }
                    }

                    @Override
                    public void onFailure(MError response) {
                        hideProgress();
                        Toaster.showShort(DISCInfoDetailActivity.this, "등록된 데이터가 없습니다.");
                    }
                });
    }

}
