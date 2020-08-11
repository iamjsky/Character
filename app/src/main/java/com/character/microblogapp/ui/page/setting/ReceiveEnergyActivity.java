package com.character.microblogapp.ui.page.setting;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.character.microblogapp.R;
import com.character.microblogapp.data.MyInfo;
import com.character.microblogapp.model.MEnergy;
import com.character.microblogapp.model.MError;
import com.character.microblogapp.net.Net;
import com.character.microblogapp.ui.adapter.ReceiveEnergyAdapter;
import com.character.microblogapp.ui.adapter.SwitchAdapter;
import com.character.microblogapp.ui.page.BaseActivity;
import com.character.microblogapp.util.Toaster;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;

public class ReceiveEnergyActivity extends BaseActivity {

    @BindView(R.id.btnBack)
    ImageButton btnBack;

    @BindView(R.id.btnMenu)
    ImageButton btnMenu;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.rvContent)
    RecyclerView rvContent;

    private ReceiveEnergyAdapter adapter;
    private ArrayList<MEnergy.Result> arrContent = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewDataBinding binding = DataBindingUtil.setContentView(this,R.layout.activity_receive_energy);
    }

    @Override
    protected void initUI() {
        super.initUI();

        tvTitle.setText("보유에너지 " + getIntent().getIntExtra("count", 0) + "개");
        btnMenu.setVisibility(View.GONE);

        adapter = new ReceiveEnergyAdapter(ReceiveEnergyActivity.this, arrContent, new ReceiveEnergyAdapter.OnItemClickListener() {
            @Override
            public void onDetail(int pos) {
            }
        });

        rvContent.setAdapter(adapter);
        rvContent.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        apiList();
    }

    @OnClick(R.id.btnBack)
    void onBack() {
        finish();
    }

    void apiList() {
        showProgress(this);
        Net.instance().api.get_energy_usehis(MyInfo.getInstance().uid)
                .enqueue(new Net.ResponseCallBack<MEnergy>() {
                    @Override
                    public void onSuccess(MEnergy response) {
                        super.onSuccess(response);
                        hideProgress();

                        arrContent.clear();
                        arrContent.addAll(response.arr_list);

                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(MError response) {
                        super.onFailure(response);
                        hideProgress();

                        if (response.resultcode == 500) {
                            networkErrorOccupied(response);
                        } else {
                            Toaster.showShort(ReceiveEnergyActivity.this, "오류입니다.");
                        }
                    }
                });
    }

}
