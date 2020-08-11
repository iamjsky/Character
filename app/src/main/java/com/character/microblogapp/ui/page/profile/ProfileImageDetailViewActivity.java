package com.character.microblogapp.ui.page.profile;

import android.content.Intent;
import android.os.Bundle;

import com.character.microblogapp.R;
import com.character.microblogapp.ui.page.BaseActivity;
import com.character.microblogapp.ui.widget.PinchImageView;
import com.character.microblogapp.util.Util;

import butterknife.BindView;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
public class ProfileImageDetailViewActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewDataBinding binding = DataBindingUtil.setContentView(this,R.layout.activity_profile_image_detail_view);

        initUI();
    }

    @BindView(R.id.imv_profile)
    PinchImageView imvProfile;

    String img_url = "";

    @Override
    protected void initUI() {
        super.initUI();

        Intent intent = getIntent();

        img_url = intent.getStringExtra("profile_image");

        if (img_url != null)
            Util.loadImage(this, imvProfile, img_url);
    }
}
