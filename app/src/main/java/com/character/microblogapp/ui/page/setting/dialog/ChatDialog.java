package com.character.microblogapp.ui.page.setting.dialog;

import android.content.Context;
import android.view.View;

import com.character.microblogapp.R;
import com.character.microblogapp.ui.page.BaseDialog;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChatDialog extends BaseDialog {

    private ActionListener m_listener;

    public ChatDialog(Context context, ActionListener listener) {
        super(context, R.style.AppTheme_Dialog);

        setContentView(R.layout.dialog_chat);
        ButterKnife.bind(this);

        m_listener = listener;
    }

    @OnClick({R.id.tv1, R.id.tv2, R.id.tv3})
    void onSucess(View view) {
        dismiss();
        if (m_listener != null) {
            m_listener.onSucess(view.getId() == R.id.tv1 ? 1 : view.getId() == R.id.tv2 ? 2 : 3);
        }
    }

    @OnClick({R.id.rly_bg, R.id.btnCancel, R.id.btnExit})
    void onBg() {
        dismiss();
    }

    public interface ActionListener {
        void onSucess(int type);
    }
}
