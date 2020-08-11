package com.character.microblogapp.ui.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.character.microblogapp.R;
import com.character.microblogapp.model.MEnergy;
import com.character.microblogapp.model.MEvent;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReceiveEnergyAdapter extends ArrayAdapter {
    public interface OnItemClickListener {
        void onDetail(int pos);
    }

    private OnItemClickListener m_listener;

    public ReceiveEnergyAdapter(Context context, ArrayList<MEnergy.Result> items, OnItemClickListener p_listener) {
        super(context, items);
        m_listener = p_listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.item_receive_energy, parent, false).getRoot());
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ItemViewHolder) holder).m_info = (MEnergy.Result) getItem(position);
        ((ItemViewHolder) holder).position = position;
        ((ItemViewHolder) holder).bindItem();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvTitle)
        TextView tvTitle;

        @BindView(R.id.tvDate)
        TextView tvDate;

        MEnergy.Result m_info;
        int position = 0;

        ItemViewHolder(View p_view) {
            super(p_view);
            ButterKnife.bind(this, p_view);
        }

        void bindItem() {
            tvDate.setText(m_info.reg_time.replace("-", "."));
            tvTitle.setText((m_info.type == 1 ? "+" : "-") + m_info.energy + " " + m_info.reason);
            tvTitle.setTextColor(Color.parseColor(m_info.type == 1 ? "#f13c4c" : "#000000"));
        }

        @OnClick({R.id.rlBg})
        void onClick(View view) {
            if(m_listener != null) {
                m_listener.onDetail(position);
            }
        }
    }
}
