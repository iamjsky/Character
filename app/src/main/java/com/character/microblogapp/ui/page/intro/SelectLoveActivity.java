package com.character.microblogapp.ui.page.intro;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.TextView;

import com.character.microblogapp.R;
import com.character.microblogapp.model.MSelectLove;
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
public class SelectLoveActivity extends BaseActivity {

    @BindView(R.id.txv_title)
    TextView tvTitle;

    @BindView(R.id.txv_count)
    TextView tvCount;

    @BindView(R.id.rcv_list)
    RecyclerView rcvList;

    String type = "hobby";
    ArrayList<String> data = new ArrayList<>();
    ArrayList<MSelectLove> all = new ArrayList<>();

    @BindArray(R.array.love_style)
    String[] loveStyle;
    @BindArray(R.array.hobby_class_list)
    String[] hobbyList;

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
        String tmp_selected = getIntent().getStringExtra("data");
        if (!tmp_selected.isEmpty()) {
            String[] tmp = getIntent().getStringExtra("data").split(",");
            data = new ArrayList<>(Arrays.asList(tmp));
        }

        tvTitle.setText(type.equals("hobby") ? "관심사" : "연애스타일");
        tvCount.setText(String.format("선택 수 %d/4", data.size()));

        String[] buff = type.equals("hobby") ? hobbyList : loveStyle;
        for (int ind = 0; ind < buff.length; ind++) {
            MSelectLove item = new MSelectLove();
            item.content = buff[ind];
            item.selected = data.contains(buff[ind]);
            all.add(item);
        }

        adapter = new LoveStyleAdapter(SelectLoveActivity.this, all, new LoveStyleAdapter.OnItemClickListener() {
            @Override
            public void onClick(int pos) {
                MSelectLove itemSelected = all.get(pos);
                if (data.contains(itemSelected.content)) {
                    data.remove(itemSelected.content);
                    itemSelected.selected = false;
                } else {
                    if (data.size() < 4) {
                        data.add(itemSelected.content);
                        itemSelected.selected = true;
                    } else {
                        Toaster.showShort(SelectLoveActivity.this, "4개까지 선택가능합니다");
                    }
                }
                
                adapter.notifyItemChanged(pos);
                tvCount.setText(String.format("선택 수 %d/4", data.size()));
            }
        });

        rcvList.setAdapter(adapter);
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
