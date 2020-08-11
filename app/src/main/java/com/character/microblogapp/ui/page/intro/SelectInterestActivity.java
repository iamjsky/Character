package com.character.microblogapp.ui.page.intro;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.character.microblogapp.R;
import com.character.microblogapp.model.Interest;
import com.character.microblogapp.model.MError;
import com.character.microblogapp.model.MInterest;
import com.character.microblogapp.net.Net;
import com.character.microblogapp.ui.adapter.InterestAdapter;
import com.character.microblogapp.ui.page.BaseActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
public class SelectInterestActivity extends BaseActivity {

    @BindView(R.id.txv_title)
    TextView tvTitle;

    @BindView(R.id.rcv_list)
    RecyclerView rcvList;

    String type = "hobby";

    ArrayList<Interest> selected = new ArrayList<>();
    ArrayList<Interest> all = new ArrayList<>();

    private InterestAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewDataBinding binding = DataBindingUtil.setContentView(this,R.layout.activity_interest_select);
    }

    @SuppressLint("DefaultLocale")
    @Override
    protected void initUI() {
        super.initUI();

        type = getIntent().getStringExtra("type");
        selected = getIntent().getParcelableArrayListExtra("data");

        tvTitle.setText(type.equals("hobby") ? "관심사" : "연애스타일");

        adapter = new InterestAdapter(SelectInterestActivity.this, new ArrayList<Interest>(), new InterestAdapter.OnItemClickListener() {
            @Override
            public void onClick(int pos) {
            }
        });

        rcvList.setAdapter(adapter);
        getInterestList();
    }

    void getInterestList() {
        Net.instance().api.get_interest(0)
                .enqueue(new Net.ResponseCallBack<MInterest>() {
                    @Override
                    public void onSuccess(MInterest response) {
                        super.onSuccess(response);
                        all = response.interest;

                        ArrayList<Integer> selectedChildId = new ArrayList<>();
                        for (Interest chose : selected) {
                            for (Interest.Child chosenChild : chose.child) {
                                if (chosenChild.selected) {
                                    selectedChildId.add(chosenChild.uid);
                                }
                            }
                        }

                        for (Interest item : all) {
                            for (Interest.Child child : item.child) {
                                if (selectedChildId.contains(child.uid)) {
                                    child.selected = true;
                                }
                            }
                        }

                        adapter.clear();
                        adapter.addAll(all);
                        adapter.notifyDataSetChanged();
                        new Handler(getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                rcvList.smoothScrollToPosition(0);
                            }
                        }, 500);

                    }

                    @Override
                    public void onFailure(MError response) {
                        super.onFailure(response);
                        if (response.resultcode == 500) {
                            networkErrorOccupied(response);
                        }
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
        intent.putExtra("data", adapter.getSelected());

        setResult(RESULT_OK, intent);
        finish();
    }

}
