package com.character.microblogapp.ui.page.profile;

import android.app.AlertDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.character.microblogapp.R;
import com.character.microblogapp.data.Constant;
import com.character.microblogapp.data.MyInfo;
import com.character.microblogapp.model.MBase;
import com.character.microblogapp.model.MEnergy;
import com.character.microblogapp.model.MError;
import com.character.microblogapp.model.MUser;
import com.character.microblogapp.model.MVersion;
import com.character.microblogapp.net.Net;
import com.character.microblogapp.ui.dialog.ConfirmDialog2;
import com.character.microblogapp.ui.page.BaseActivity;
import com.character.microblogapp.ui.page.character.CharacterActivity;
import com.character.microblogapp.ui.page.setting.EnergyupActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class ProfileDISCInfoActivity extends BaseActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewDataBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_disc_info);

        initUI();
    }

    MUser user = null;





    @OnClick(R.id.rlt_back)
    void goBack() {
        finish();
    }

    @OnClick(R.id.btn_style_d)
    void goStyleD() {
        Intent intent = new Intent(this, DISCInfoDetailActivity.class);
        intent.putExtra("type", "D");
        startActivity(intent);
    }

    @OnClick(R.id.btn_style_i)
    void goStyleI() {
        Intent intent = new Intent(this, DISCInfoDetailActivity.class);
        intent.putExtra("type", "I");
        startActivity(intent);
    }

    @OnClick(R.id.btn_style_s)
    void goStyleS() {
        Intent intent = new Intent(this, DISCInfoDetailActivity.class);
        intent.putExtra("type", "S");
        startActivity(intent);
    }

    @OnClick(R.id.btn_style_c)
    void goStyleC() {
        Intent intent = new Intent(this, DISCInfoDetailActivity.class);
        intent.putExtra("type", "C");
        startActivity(intent);
    }
}
