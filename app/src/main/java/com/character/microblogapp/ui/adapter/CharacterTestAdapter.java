package com.character.microblogapp.ui.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.character.microblogapp.R;
import com.character.microblogapp.model.MCharacter;
import com.character.microblogapp.ui.widget.BaseTextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.facebook.FacebookSdk.getApplicationContext;

public class CharacterTestAdapter extends ArrayAdapter {
    public interface OnItemClickListener {
        void onDetail(int pos);
    }

    private OnItemClickListener m_listener;

    public CharacterTestAdapter(Context context, ArrayList<MCharacter.Answer> items, OnItemClickListener p_listener) {
        super(context, items);
        m_listener = p_listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_character_test, parent, false).getRoot());
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ItemViewHolder) holder).m_info = (MCharacter.Answer) getItem(position);
        ((ItemViewHolder) holder).position = position;
        ((ItemViewHolder) holder).bindItem();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        int selectedColor = ContextCompat.getColor(getApplicationContext(), R.color.color_white);
        @BindView(R.id.tv_question)
        TextView m_tvQuestion;
        @BindView(R.id.iv_default)
        ImageView iv_default;
        @BindView(R.id.iv_selected)
        ImageView iv_selected;

        MCharacter.Answer m_info;
        int position = 0;

        public ItemViewHolder(View p_view) {
            super(p_view);
            ButterKnife.bind(this, p_view);
        }

        void bindItem() {
            m_tvQuestion.setText(m_info.content);
        }

        @OnClick(R.id.tv_question)
        void onDetail(View v) {
            if (m_listener != null) {

                TextView tv = (TextView) v;
                tv.setTextColor(selectedColor);
                iv_default.setVisibility(View.INVISIBLE);
                iv_selected.setVisibility(View.VISIBLE);
                waitSec(m_tvQuestion);

            }
        }
        public void waitSec(final View v){

            v.setClickable(false);

            Handler mHandler = new Handler();
            mHandler.postDelayed(new Runnable()  {
                public void run() {
                    v.setClickable(true);
                    m_listener.onDetail(position);

                }
            }, 100); // 1
        }
    }
}
