package com.character.microblogapp.ui.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.character.microblogapp.R;
import com.character.microblogapp.model.MCommunityTab;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CommunityTabAdapter extends ArrayAdapter {
    public interface OnItemClickListener {
        void onTabSelect(int pos);
    }

    private OnItemClickListener m_listener;

    public CommunityTabAdapter(Context context, ArrayList<MCommunityTab> items, OnItemClickListener p_listener) {
        super(context, items);
        m_listener = p_listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.item_community_tab, parent, false).getRoot());
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ItemViewHolder) holder).m_info = (MCommunityTab) getItem(position);
        ((ItemViewHolder) holder).position = position;
        ((ItemViewHolder) holder).bindItem();
    }

    void refresh(int type) {
        for (Object item : mItems) {
            MCommunityTab tab = (MCommunityTab) item;
            tab.selected = tab.type == type;
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txv_name)
        TextView tvContent;

        MCommunityTab m_info;
        int position = 0;

        ItemViewHolder(View p_view) {
            super(p_view);
            ButterKnife.bind(this, p_view);
        }

        void bindItem() {
            tvContent.setText(m_info.name);
            tvContent.setTextColor(Color.parseColor(m_info.selected ? "#ffffff" : "#b7b7b7"));
            tvContent.setBackgroundResource(m_info.selected ? R.drawable.bg_community_tab_active : R.drawable.bg_community_tab);
        }

        @OnClick({R.id.txv_name})
        void onClick(View view) {
            refresh(m_info.type);
            notifyDataSetChanged();

            if (m_listener != null) {
                // 대화상세
                m_listener.onTabSelect(m_info.type);
            }
        }
    }
}
