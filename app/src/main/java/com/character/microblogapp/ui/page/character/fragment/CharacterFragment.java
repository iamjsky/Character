package com.character.microblogapp.ui.page.character.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.character.microblogapp.R;
import com.character.microblogapp.model.MCharacter;
import com.character.microblogapp.ui.adapter.CharacterTestAdapter;
import com.character.microblogapp.ui.page.BaseFragment;
import com.character.microblogapp.ui.page.character.CharacterActivity;

import butterknife.BindView;

public class CharacterFragment extends BaseFragment {

    @BindView(R.id.rcv_character_question)
    RecyclerView m_rcvCharacterQuestion;

    @BindView(R.id.txt_character_question)
    TextView m_txtCharacterQuestion;

    private CharacterTestAdapter m_adapter;
    private MCharacter.Character character = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        createView(inflater, container, R.layout.fragment_character);
        return mRoot;
    }

    public static CharacterFragment newInstance() {
        return new CharacterFragment();
    }

    @Override
    protected void initUI() {
        super.initUI();

        LinearLayoutManager lm1 = new LinearLayoutManager(getContext());
        lm1.setOrientation(LinearLayoutManager.VERTICAL);
        m_rcvCharacterQuestion.setLayoutManager(lm1);

        m_txtCharacterQuestion.setText(character.content);

        m_adapter = new CharacterTestAdapter(getContext(), character.answers, new CharacterTestAdapter.OnItemClickListener() {
            @Override
            public void onDetail(int pos) {
                CharacterActivity characterActivity = (CharacterActivity) mParent;
                if (characterActivity != null) {
                    characterActivity.showCharacterNext(character.answers.get(pos));
                }
            }
        });

        m_rcvCharacterQuestion.setAdapter(m_adapter);
        m_adapter.notifyDataSetChanged();
    }

    public void setData(MCharacter.Character data) {
        character = data;
    }
}
