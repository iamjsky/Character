package com.character.microblogapp.ui.page.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.character.microblogapp.R;
import com.character.microblogapp.data.MyInfo;
import com.character.microblogapp.model.MAreaList;
import com.character.microblogapp.model.MBase;
import com.character.microblogapp.model.MError;
import com.character.microblogapp.model.MUser;
import com.character.microblogapp.net.Net;
import com.character.microblogapp.ui.dialog.ConfirmDialog2;
import com.character.microblogapp.ui.dialog.SelectDialog;
import com.character.microblogapp.ui.dialog.SelectDialog2;
import com.character.microblogapp.ui.page.BaseActivity;
import com.character.microblogapp.ui.page.intro.SelectLoveActivity;
import com.character.microblogapp.ui.page.intro.SelectTypeActivity;
import com.character.microblogapp.ui.page.model.ModelFindActivity;
import com.character.microblogapp.util.Toaster;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.OnClick;

public class TypeActivity extends BaseActivity {

    @BindView(R.id.btnBack)
    ImageButton btnBack;

    @BindView(R.id.btnMenu)
    ImageButton btnMenu;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.tvHeight)
    TextView tvHeight;

    @BindView(R.id.tvRegion)
    TextView tvRegion;

    @BindView(R.id.tvReligion)
    TextView tvReligion;

    @BindView(R.id.tvSmoking)
    TextView tvSmoking;

    @BindView(R.id.tvDrinking)
    TextView tvDrinking;

    @BindView(R.id.tvType)
    TextView tvType;

    @BindView(R.id.tvBent)
    TextView tvBent;

    @BindView(R.id.tvStyle)
    TextView tvStyle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type);
    }

    @Override
    protected void initUI() {
        super.initUI();

        tvTitle.setText("이상형 설정");
        btnMenu.setVisibility(View.GONE);

        apiInfo();
    }

    @OnClick(R.id.btnBack)
    void onBack() {
        finish();
    }

    @OnClick({R.id.tvSave})
    void onSave(View view) {
        apiChange();
    }

    @OnClick({R.id.rlHeight, R.id.rlRegion, R.id.rlReligion, R.id.rlSmoking, R.id.rlDrinking, R.id.rlType, R.id.rlBent, R.id.rlStyle})
    void onPicker(View view) {
        ArrayList<String> arrayList = new ArrayList<>();

        if (view.getId() == R.id.rlHeight) {

            ArrayList<String> arrayList1 = new ArrayList<>();
            ArrayList<String> arrayList2 = new ArrayList<>();

            for (int i = 140; i <= 200; i++) {
                arrayList1.add(String.valueOf(i));
                arrayList2.add(String.valueOf(i));
            }

            new SelectDialog2(TypeActivity.this, arrayList1, arrayList2, 10, new SelectDialog2.Callback() {
                @Override
                public void onConfirm(String data1, String data2) {

                    start_height = Integer.parseInt(data1);
                    end_height = Integer.parseInt(data2);

                    tvHeight.setText(start_height + "cm ~ " + end_height + "cm");
                }

                @Override
                public void onCancel() {

                }
            }).show();

        } else if (view.getId() == R.id.rlRegion) {

            Intent intent = new Intent(TypeActivity.this, SelectTypeActivity.class);
            intent.putExtra("type", "area");
            intent.putExtra("data", tvRegion.getText().toString());
            intent.putExtra("max_choose", 4);
            startActivityForResult(intent, 0x0110);

        } else if (view.getId() == R.id.rlReligion) {
            Intent intent = new Intent(TypeActivity.this, SelectTypeActivity.class);
            intent.putExtra("type", "belief");
            intent.putExtra("data", tvReligion.getText().toString());
            intent.putExtra("max_choose", 3);
            startActivityForResult(intent, 0x0110);
        } else if (view.getId() == R.id.rlSmoking) {
            Intent intent = new Intent(TypeActivity.this, SelectTypeActivity.class);
            intent.putExtra("type", "smoking");
            intent.putExtra("data", tvSmoking.getText().toString());
            intent.putExtra("max_choose", 3);
            startActivityForResult(intent, 0x0110);
        } else if (view.getId() == R.id.rlDrinking) {
            Intent intent = new Intent(TypeActivity.this, SelectTypeActivity.class);
            intent.putExtra("type", "drinking");
            intent.putExtra("data", tvDrinking.getText().toString());
            intent.putExtra("max_choose", 3);
            startActivityForResult(intent, 0x0110);
        } else if (view.getId() == R.id.rlType) {
            Intent intent = new Intent(TypeActivity.this, SelectTypeActivity.class);
            if (MyInfo.getInstance().gender == 1) {
                intent.putExtra("type", "body_male");
            } else {
                intent.putExtra("type", "body_female");
            }
            intent.putExtra("data", tvType.getText().toString());
            intent.putExtra("max_choose", 3);
            startActivityForResult(intent, 0x0110);
        } else if (view.getId() == R.id.rlBent) {
            Intent intent = new Intent(TypeActivity.this, SelectTypeActivity.class);
            intent.putExtra("type", "hobby");
            intent.putExtra("data", tvBent.getText().toString());
            intent.putExtra("max_choose", 6);
            startActivityForResult(intent, 0x0110);
        } else if (view.getId() == R.id.rlStyle) {
            Intent intent = new Intent(TypeActivity.this, SelectTypeActivity.class);
            intent.putExtra("type", "love");
            intent.putExtra("data", tvStyle.getText().toString());
            intent.putExtra("max_choose", 6);
            startActivityForResult(intent, 0x0110);
        }
    }

    int start_height;
    int end_height;

    void apiChange() {
        String address = tvRegion.getText().toString();
        String religion = tvReligion.getText().toString();
        String smoke = tvSmoking.getText().toString();
        String drink = tvDrinking.getText().toString();
        String body_type = tvType.getText().toString();
        String interest = tvBent.getText().toString();
        String love_style = tvStyle.getText().toString();

        showProgress(this);
        Net.instance().api.set_ideal_setting(MyInfo.getInstance().uid,
                start_height,
                end_height,
                address,
                religion,
                smoke,
                drink,
                body_type,
                interest,
                love_style
        )
                .enqueue(new Net.ResponseCallBack<MBase>() {
                    @Override
                    public void onSuccess(MBase response) {
                        super.onSuccess(response);
                        hideProgress();

                        finish();
                    }

                    @Override
                    public void onFailure(MError response) {
                        super.onFailure(response);
                        hideProgress();

                        if (response.resultcode == 500) {
                            networkErrorOccupied(response);
                        } else {
                            Toaster.showShort(TypeActivity.this, "오류입니다.");
                        }
                    }
                });
    }

    void apiInfo() {
        showProgress(this);
        Net.instance().api.get_ideal_info(MyInfo.getInstance().uid)
                .enqueue(new Net.ResponseCallBack<MUser>() {
                    @Override
                    public void onSuccess(MUser response) {
                        super.onSuccess(response);
                        hideProgress();

                        tvRegion.setText(response.info.address);
                        tvReligion.setText(response.info.religion);
                        tvSmoking.setText(response.info.smoke);
                        tvDrinking.setText(response.info.drink);
                        tvType.setText(response.info.body_type);
                        tvBent.setText(response.info.interest);
                        tvStyle.setText(response.info.love_style);
                        start_height = response.info.start_height;
                        end_height = response.info.end_height;
                        tvHeight.setText(start_height + "cm ~ " + end_height + "cm");
                    }

                    @Override
                    public void onFailure(MError response) {
                        super.onFailure(response);
                        hideProgress();

                        if (response.resultcode == 500) {
                            networkErrorOccupied(response);
                        } else {
//                            Toaster.showShort(TypeActivity.this, "오류입니다.");
                        }
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 0x0110 && resultCode == RESULT_OK) {
            String type = data.getStringExtra("type");
            String selected = data.getStringExtra("data");

            switch (type) {
                case "hobby":
                    tvBent.setText(selected);
                    break;
                case "love":
                    tvStyle.setText(selected);
                    break;
                case "body_male":
                    tvType.setText(selected);
                    break;
                case "body_female":
                    tvType.setText(selected);
                    break;
                case "belief":
                    tvReligion.setText(selected);
                    break;
                case "area":
                    tvRegion.setText(selected);
                    break;
                case "smoking":
                    tvSmoking.setText(selected);
                    break;
                case "drinking":
                    tvDrinking.setText(selected);
                    break;
            }
        }
    }

}
