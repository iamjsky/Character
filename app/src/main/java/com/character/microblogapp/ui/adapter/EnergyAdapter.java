package com.character.microblogapp.ui.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.character.microblogapp.R;
import com.character.microblogapp.ui.widget.CircleImageView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EnergyAdapter extends ArrayAdapter {
    public interface OnItemClickListener {
        void onDetail(int pos);
    }

    private OnItemClickListener m_listener;

    public EnergyAdapter(Context context, ArrayList<String> items, OnItemClickListener p_listener) {
        super(context, items);
        m_listener = p_listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.item_energy, parent, false).getRoot());
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ItemViewHolder) holder).m_info = (String) getItem(position);
        ((ItemViewHolder) holder).position = position;
        ((ItemViewHolder) holder).bindItem();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvTop)
        TextView tvTop;

        @BindView(R.id.tvBottom)
        TextView tvBottom;

        String m_info;
        int position = 0;

        ItemViewHolder(View p_view) {
            super(p_view);
            ButterKnife.bind(this, p_view);
        }

        void bindItem() {
            tvTop.setText(position == 0 ? "50" : position == 1 ? "100" : position == 2 ? "200" : position == 3 ? "300" : position == 4 ? "500" : position == 5 ? "1,000" : "3,000");
            tvBottom.setText(position == 0 ? "7,500" : position == 1 ? "14,700" : position == 2 ? "28,500" : position == 3 ? "40,500" : position == 4 ? "63,750" : position == 5 ? "120,000" : "315,000");
        }

        @OnClick({R.id.rlBg})
        void onClick(View view) {
            if(m_listener != null) {
                m_listener.onDetail(position);
            }
        }
    }
}
