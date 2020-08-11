package com.character.microblogapp.ui.dialog;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.character.microblogapp.R;
import com.wx.wheelview.adapter.ArrayWheelAdapter;
import com.wx.wheelview.widget.WheelView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SelectDialog2 extends BaseDialog {

    @BindView(R.id.btnConfirm)
    Button btnConfirm;

    @BindView(R.id.whvContent1)
    WheelView whvContent1;

    @BindView(R.id.whvContent2)
    WheelView whvContent2;

    @BindView(R.id.tv_split)
    TextView tvSplit;

    @BindView(R.id.tv_type1)
    TextView tvType1;

    @BindView(R.id.tv_type2)
    TextView tvType2;

    private List list1;
    private List list2;

    private Callback callback;

    private int selIndex1 = -1;
    private int selIndex2 = -1;

    private String selStr1 = "";
    private String selStr2 = "";

    private int type = 0;

    private boolean initSelected = false;

    public SelectDialog2(@NonNull Context context, ArrayList list1, ArrayList list2, int type, Callback callback) {
        super(context, android.R.style.Theme_DeviceDefault_NoActionBar);
        setContentView(R.layout.dialog_select2);

        this.list1 = list1;
        this.list2 = list2;
        this.callback = callback;
        this.type = type;
        initUI();
    }

    public void initUI() {

        if (type == 10) {
            tvSplit.setVisibility(View.VISIBLE);
            tvType1.setText("최소");
            tvType2.setText("최대");
            tvType1.setVisibility(View.VISIBLE);
            tvType2.setVisibility(View.VISIBLE);
        } else if (type == 3){
            tvSplit.setVisibility(View.GONE);
            tvType1.setText("1차 유형");
            tvType2.setText("2차 유형");
            tvType1.setVisibility(View.VISIBLE);
            tvType2.setVisibility(View.VISIBLE);
        } else {
            tvSplit.setVisibility(View.GONE);
            tvType1.setVisibility(View.GONE);
            tvType2.setVisibility(View.GONE);
        }
        whvContent1.setWheelAdapter(new ArrayWheelAdapter(getContext()));
        whvContent1.setWheelSize(3);
        whvContent1.setSkin(WheelView.Skin.Holo);
        whvContent1.setWheelData(list1);
        whvContent1.setSelection(0);
        WheelView.WheelViewStyle style = new WheelView.WheelViewStyle();
        style.selectedTextColor = mContext.getResources().getColor(R.color.color_dark);
        style.textColor = mContext.getResources().getColor(R.color.color_hint);
        style.textSize = 16;
        style.backgroundColor = Color.TRANSPARENT;
//        style.holoBorderColor = mContext.getResources().getColor(R.color.color_d6d6d6);
        whvContent1.setStyle(style);
        whvContent1.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int i, Object o) {
                if (!initSelected) {
                    initSelected = true;
                    selIndex1 = -1;
                } else {
                    whvContent2.setVisibility(View.VISIBLE);
                }
                selIndex1 = i;
                selStr1 = (String)o;
            }
        });

        whvContent2.setWheelAdapter(new ArrayWheelAdapter(getContext()));
        whvContent2.setWheelSize(3);
        whvContent2.setSkin(WheelView.Skin.Holo);
        whvContent2.setWheelData(list2);
        whvContent2.setSelection(0);
        WheelView.WheelViewStyle style2 = new WheelView.WheelViewStyle();
        style2.selectedTextColor = mContext.getResources().getColor(R.color.color_dark);
        style2.textColor = mContext.getResources().getColor(R.color.color_hint);
        style2.textSize = 16;
        style2.backgroundColor = Color.TRANSPARENT;
        style2.holoBorderColor = mContext.getResources().getColor(R.color.color_d6d6d6);
        whvContent2.setStyle(style2);
        whvContent2.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int i, Object o) {
                selIndex2 = i;
                selStr2 = (String)o;
            }
        });
    }

    @OnClick({R.id.rlDismiss, R.id.btnConfirm})
    void onClick(View view) {
        if (selIndex1 == -1 || selIndex2 == -1) {
            Toast.makeText(getContext(), "이상형정보를 선택해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (type == 10 && selIndex1 > selIndex2) {
            Toast.makeText(getContext(), "키 설정을 다시 제대로 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (type == 3 && selStr1.equals(selStr2)) {
            Toast.makeText(getContext(), "중복된 유형입니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        dismiss();
        int position1 = whvContent1.getCurrentPosition();
        int position2 = whvContent2.getCurrentPosition();
        if (callback != null)
            callback.onConfirm((String) list1.get(position1), (String) list2.get(position2));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        callback.onCancel();
    }

    public interface Callback {
        void onConfirm(String data1, String data2);
        void onCancel();
    }
}
