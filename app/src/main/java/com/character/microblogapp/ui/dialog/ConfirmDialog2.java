package com.character.microblogapp.ui.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.character.microblogapp.R;
import com.character.microblogapp.ui.page.BaseDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ConfirmDialog2 extends BaseDialog {

    public static void show(Context context, String title, String subtitle, @NonNull ActionListener listener) {
        ConfirmDialog2 dialog = new ConfirmDialog2(context, title, subtitle, listener);
        dialog.show();
    }

    private ActionListener m_listener;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.tvSubtitle)
    TextView tvSubtitle;

    public ConfirmDialog2(Context context, String title, String subtitle, ActionListener listener) {
        super(context, R.style.AppTheme_Dialog);

        setContentView(R.layout.dialog_confirm);
        ButterKnife.bind(this);

        m_listener = listener;
        tvTitle.setText(title);
        tvSubtitle.setText(subtitle);
    }

    @OnClick(R.id.btnOk)
    void onOk() {
        dismiss();
        if (m_listener != null) {
            m_listener.onOk();
        }
    }

    @OnClick(R.id.btnCancel)
    void onCancel() {
        dismiss();
    }

    @OnClick(R.id.rly_bg)
    void onBg() {
        dismiss();
    }

    public interface ActionListener {
        void onOk();
    }
}
