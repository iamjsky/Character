package com.character.microblogapp.ui.dialog;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.character.microblogapp.R;
import com.character.microblogapp.data.MyInfo;
import com.character.microblogapp.model.MUserList;
import com.character.microblogapp.ui.adapter.MatchingContactsAdapter;
import com.character.microblogapp.ui.adapter.MatchingContactsFindAdapter;
import com.character.microblogapp.ui.page.BaseDialog;
import com.character.microblogapp.ui.page.profile.ProfileActivity;
import com.character.microblogapp.ui.page.setting.EnergyupActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchDialog extends BaseDialog {

    public static void show(Context context, String title, ArrayList<MUserList.User> list, @NonNull ActionListener listener) {
        SearchDialog dialog = new SearchDialog(context, title, list, listener);
        dialog.show();
    }

    private ActionListener m_listener;

    MatchingContactsFindAdapter adapter;
    private ArrayList<MUserList.User> mArrContacts = new ArrayList<>();

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.rvContent)
    RecyclerView rvContent;

    public SearchDialog(Context context, String title, ArrayList<MUserList.User> list, ActionListener listener) {
        super(context, R.style.AppTheme_Dialog);

        setContentView(R.layout.dialog_search);
        ButterKnife.bind(this);

        m_listener = listener;
        tvTitle.setText(title);

        rvContent.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mArrContacts.addAll(list);
        adapter = new MatchingContactsFindAdapter(getContext(), mArrContacts, new MatchingContactsFindAdapter.OnClickListener() {
            @Override
            public void onPickUser(int pos) {
                if (m_listener != null) {
                    m_listener.onSelect(mArrContacts.get(pos).uid);
                }
            }
            @Override
            public void onProfile(final int pos) {
                if (m_listener != null) {
                    m_listener.onSelect(mArrContacts.get(pos).uid);
                }
            }
            @Override
            public void onSelect(final int pos) {
                if (m_listener != null) {
                    m_listener.onSelect(mArrContacts.get(pos).uid);
                }
            }
        });
        adapter.setShowPick(false);

        rvContent.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @OnClick(R.id.btnOk)
    void onOk() {
        dismiss();
        if (m_listener != null) {
            m_listener.onOk();
        }
    }

    @OnClick(R.id.rly_bg)
    void onBg() {
//        dismiss();
    }

    public interface ActionListener {
        void onOk();
        void onSelect(int user_id);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onOk();
    }
}
