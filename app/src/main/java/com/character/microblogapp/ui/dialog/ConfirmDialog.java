package com.character.microblogapp.ui.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Button;
import android.widget.TextView;

import com.character.microblogapp.R;
import com.character.microblogapp.ui.page.BaseDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ConfirmDialog extends BaseDialog {

    public static void show(Context context, String title, @NonNull ActionListener listener) {
        ConfirmDialog dialog = new ConfirmDialog(context, title, listener);
        dialog.show();
    }

    private ActionListener m_listener;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    public ConfirmDialog(Context context, String title, ActionListener listener) {
        super(context, R.style.AppTheme_Dialog);

        setContentView(R.layout.dialog_alert);
        ButterKnife.bind(this);

        m_listener = listener;
        tvTitle.setText(title);
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
