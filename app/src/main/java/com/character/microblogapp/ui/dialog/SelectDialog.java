package com.character.microblogapp.ui.dialog;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;

import com.character.microblogapp.R;
import com.wx.wheelview.adapter.ArrayWheelAdapter;
import com.wx.wheelview.widget.WheelView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SelectDialog extends BaseDialog {

    @BindView(R.id.btnConfirm)
    Button btnConfirm;
    @BindView(R.id.whvContent)
    WheelView whvContent;

    private List list;

    private Callback callback;

    public SelectDialog(@NonNull Context context, ArrayList list, Callback callback) {
        super(context, android.R.style.Theme_DeviceDefault_NoActionBar);
        setContentView(R.layout.dialog_select);

        this.list = list;
        this.callback = callback;

        initUI();
    }

    public void initUI() {

        whvContent.setWheelAdapter(new ArrayWheelAdapter(getContext()));
        whvContent.setWheelSize(5);
        whvContent.setSkin(WheelView.Skin.Holo);
        whvContent.setWheelData(list);
        whvContent.setSelection(0);
        WheelView.WheelViewStyle style = new WheelView.WheelViewStyle();
        style.selectedTextColor = mContext.getResources().getColor(R.color.color_dark);
        style.textColor = mContext.getResources().getColor(R.color.color_hint);
        style.textSize = 16;
        style.backgroundColor = Color.TRANSPARENT;
        style.holoBorderColor = mContext.getResources().getColor(R.color.color_d6d6d6);
        whvContent.setStyle(style);
        whvContent.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int i, Object o) {

            }
        });
    }

    @OnClick({R.id.rlDismiss, R.id.btnConfirm})
    void onClick(View view) {
        dismiss();

        switch (view.getId()) {
            case R.id.rlDismiss:
                break;

            case R.id.btnConfirm:
                int position = whvContent.getCurrentPosition();
                if (callback != null)
                    callback.onConfirm(position, (String) list.get(position));
                break;
        }
    }

    public interface Callback {
        void onConfirm(int position, String data);
    }
}
