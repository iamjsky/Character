package com.character.microblogapp.ui.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.character.microblogapp.R;
import com.character.microblogapp.model.MSelectLove;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoveStyleAdapter extends ArrayAdapter {
    public interface OnItemClickListener {
        void onClick(int pos);
    }

    private OnItemClickListener m_listener;

    public LoveStyleAdapter(Context context, ArrayList<MSelectLove> items, OnItemClickListener p_listener) {
        super(context, items);
        m_listener = p_listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.item_love_style, parent, false).getRoot());
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ItemViewHolder) holder).m_info = (MSelectLove) getItem(position);
        ((ItemViewHolder) holder).position = position;
        ((ItemViewHolder) holder).bindItem();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvTitle)
        TextView tvTitle;
        @BindView(R.id.rlyTitle)
        ConstraintLayout rlyTitle;

        MSelectLove m_info;
        int position = 0;

        ItemViewHolder(View p_view) {
            super(p_view);
            ButterKnife.bind(this, p_view);
        }

        void bindItem() {
            tvTitle.setText(m_info.content);
            rlyTitle.setBackgroundResource(m_info.selected ? R.drawable.btn_bg_test_answer : R.drawable.btn_bg_test_answer_black);
            tvTitle.setTextColor(m_info.selected ? Color.parseColor("#f13c4c") : Color.parseColor("#000000"));
        }

        @OnClick({R.id.rlBg})
        void onClick(View view) {
//            m_info.selected = !m_info.selected;
//            notifyItemChanged(getAdapterPosition());
            if (m_listener != null) {
                m_listener.onClick(position);
            }
        }
    }
}
