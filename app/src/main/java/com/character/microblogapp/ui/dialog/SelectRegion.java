package com.character.microblogapp.ui.dialog;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.character.microblogapp.R;
import com.wx.wheelview.adapter.ArrayWheelAdapter;
import com.wx.wheelview.widget.WheelView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SelectRegion extends BaseDialog {

    @BindView(R.id.btnConfirm)
    Button btnConfirm;

    @BindView(R.id.whvContent1)
    WheelView whvContent1;

    @BindView(R.id.whvContent2)
    WheelView whvContent2;

    @BindView(R.id.tv_type1)
    TextView tvType1;

    @BindView(R.id.tv_type2)
    TextView tvType2;

    public List list1;
    public List list2;

    private Callback callback;

    public SelectRegion(@NonNull Context context, ArrayList list1, ArrayList list2, Callback callback) {
        super(context, android.R.style.Theme_DeviceDefault_NoActionBar);
        setContentView(R.layout.dialog_select2);

        this.list1 = list1;
        this.list2 = list2;
        this.callback = callback;

        initUI();
    }

    public void initUI() {
        tvType1.setText("시,도");
        tvType2.setText("군,구");
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
        style.holoBorderColor = mContext.getResources().getColor(R.color.color_d6d6d6);
        whvContent1.setStyle(style);
        whvContent1.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int i, Object o) {
                if (callback != null)
                    callback.onSelect((String) list1.get(i));
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

            }
        });
    }

    public void init2() {
        whvContent2.setWheelData(list2);
        whvContent2.setSelection(0);
    }

    @OnClick({R.id.rlDismiss, R.id.btnConfirm})
    void onClick(View view) {
        dismiss();

        switch (view.getId()) {
            case R.id.rlDismiss:
                break;

            case R.id.btnConfirm:
                int position1 = whvContent1.getCurrentPosition();
                int position2 = whvContent2.getCurrentPosition();
                if (callback != null)
                    callback.onConfirm((String) list1.get(position1), (String) list2.get(position2));
                break;
        }
    }

    public interface Callback {
        void onConfirm(String data1, String data2);

        void onSelect(String data);
    }
}
