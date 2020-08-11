package com.character.microblogapp.ui.page.intro;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.TextView;

import com.character.microblogapp.R;
import com.character.microblogapp.data.MyInfo;
import com.character.microblogapp.model.MAreaList;
import com.character.microblogapp.model.MError;
import com.character.microblogapp.model.MSelectLove;
import com.character.microblogapp.net.Net;
import com.character.microblogapp.ui.adapter.LoveStyleAdapter;
import com.character.microblogapp.ui.page.BaseActivity;
import com.character.microblogapp.util.Toaster;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.OnClick;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
public class SelectTypeActivity extends BaseActivity {

    @BindView(R.id.txv_title)
    TextView tvTitle;

    @BindView(R.id.txv_count)
    TextView tvCount;

    @BindView(R.id.rcv_list)
    RecyclerView rcvList;

    String type = "";
    Integer max_choose = 0;
    ArrayList<String> data = new ArrayList<>();
    ArrayList<MSelectLove> all = new ArrayList<>();

    @BindArray(R.array.belief)
    String[] belief;
    @BindArray(R.array.drinking)
    String[] drinking;
    @BindArray(R.array.smoking)
    String[] smoking;
    @BindArray(R.array.body_male)
    String[] body_male;
    @BindArray(R.array.body_female)
    String[] body_female;
    @BindArray(R.array.love_style)
    String[] loveStyle;
    @BindArray(R.array.hobby_class_list)
    String[] hobbyList;
    @BindArray(R.array.job)
    String[] jobList;

    String[] areaList;

    private LoveStyleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewDataBinding binding = DataBindingUtil.setContentView(this,R.layout.activity_love_select);
    }

    @SuppressLint("DefaultLocale")
    @Override
    protected void initUI() {
        super.initUI();

        type = getIntent().getStringExtra("type");
        max_choose = getIntent().getIntExtra("max_choose", 0);

        if (type.equals("area")) {
            getAreaList();
        } else {
            initInfo();
        }
    }

    void initInfo() {
        String tmp_selected = getIntent().getStringExtra("data");
        if (!tmp_selected.equals("")) {
            String[] tmp = getIntent().getStringExtra("data").split(",");
            data = new ArrayList<>(Arrays.asList(tmp));
        }

        tvTitle.setText(type.equals("belief") ? "종교" : type.equals("smoking") ? "흡연" : type.equals("drinking") ? "음주" :
                type.equals("hobby") ? "취향" : type.equals("love") ? "연애스타일" : type.equals("area") ? "지역" : type.equals("job") ? "직업" : "체형");
        tvCount.setText(String.format("선택 수 %d/%d", data.size(), max_choose));

        String[] buff = type.equals("job") ? jobList : type.equals("belief") ? belief : type.equals("smoking") ? smoking : type.equals("drinking") ? drinking :
                type.equals("hobby") ? hobbyList : type.equals("love") ? loveStyle : type.equals("area") ? areaList : MyInfo.getInstance().gender == 1 ? body_female : body_male;
        for (int ind = 0; ind < buff.length; ind++) {
            MSelectLove item = new MSelectLove();
            item.content = buff[ind];
            item.selected = data.contains(buff[ind]);
            all.add(item);
        }

        adapter = new LoveStyleAdapter(SelectTypeActivity.this, all, new LoveStyleAdapter.OnItemClickListener() {
            @Override
            public void onClick(int pos) {
                MSelectLove itemSelected = all.get(pos);
                if (data.contains(itemSelected.content)) {
                    data.remove(itemSelected.content);
                    itemSelected.selected = false;
                } else {
                    if (data.size() < max_choose) {
                        data.add(itemSelected.content);
                        itemSelected.selected = true;
                    } else {
                        Toaster.showShort(SelectTypeActivity.this, max_choose + "개까지 선택가능합니다");
                    }
                }

                adapter.notifyItemChanged(pos);
                tvCount.setText(String.format("선택 수 %d/%d", data.size(), max_choose));
            }
        });

        rcvList.setAdapter(adapter);
    }

    void getAreaList() {
        showProgress(this);
        Net.instance().api.get_area()
                .enqueue(new Net.ResponseCallBack<MAreaList>() {
                    @Override
                    public void onSuccess(MAreaList response) {
                        super.onSuccess(response);
                        hideProgress();
                        areaList = response.area_list;

                        initInfo();
                    }

                    @Override
                    public void onFailure(MError response) {
                        super.onFailure(response);
                        hideProgress();
                    }
                });
    }

    @OnClick(R.id.rlt_back)
    void goBack() {
        setResult(RESULT_CANCELED);
        finish();
    }

    @OnClick(R.id.btnSave)
    void onSave() {
        Intent intent = new Intent();
        intent.putExtra("type", type);
        intent.putExtra("data", TextUtils.join(",", data));

        setResult(RESULT_OK, intent);
        finish();
    }
}
