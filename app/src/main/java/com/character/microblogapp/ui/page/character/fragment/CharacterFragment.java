package com.character.microblogapp.ui.page.character.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
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
    @BindView(R.id.tv_count)
    TextView tv_count;
    @BindView(R.id.tv_totalCount)
    TextView tv_totalCount;



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

        GridLayoutManager lm1 = new GridLayoutManager(getContext(), 2);

        m_rcvCharacterQuestion.setLayoutManager(lm1);

        m_txtCharacterQuestion.setText(character.content);
        final CharacterActivity characterActivity = (CharacterActivity) mParent;

        int index = characterActivity.mViewIndex+1;
        int totalCount = characterActivity.character_list.size();
        if(index < 10){
            tv_count.setText("0"+index+".");
        }else{
            tv_count.setText(index+".");
        }

        characterActivity.setCountProgressBar();

        tv_totalCount.setText(String.format("%d/%d", index, totalCount));
        m_adapter = new CharacterTestAdapter(getContext(), character.answers, new CharacterTestAdapter.OnItemClickListener() {
            @Override
            public void onDetail(int pos) {

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
