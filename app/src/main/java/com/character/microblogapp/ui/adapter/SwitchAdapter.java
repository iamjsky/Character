package com.character.microblogapp.ui.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.character.microblogapp.R;
import com.character.microblogapp.data.BlockInfo;
import com.character.microblogapp.model.MBlock;
import com.character.microblogapp.ui.widget.CircleImageView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SwitchAdapter extends ArrayAdapter {
    public interface OnItemClickListener {
        void onDetail(int pos, boolean status);
    }

    private OnItemClickListener m_listener;

    public SwitchAdapter(Context context, ArrayList<BlockInfo> items, OnItemClickListener p_listener) {
        super(context, items);
        m_listener = p_listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_switch, parent, false).getRoot());
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ItemViewHolder) holder).m_info = (BlockInfo) getItem(position);
        ((ItemViewHolder) holder).position = position;
        ((ItemViewHolder) holder).bindItem();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvTitle)
        TextView tvTitle;

        @BindView(R.id.btnSwitch)
        ImageButton btnSwitch;

        BlockInfo m_info;
        int position = 0;

        ItemViewHolder(View p_view) {
            super(p_view);
            ButterKnife.bind(this, p_view);
        }

        void bindItem() {
            btnSwitch.setSelected(m_info.isBlock);
            tvTitle.setText(m_info.user_name + "(" + m_info.phone + ")");
        }

        @OnClick({R.id.btnSwitch})
        void onClick(View view) {
            btnSwitch.setSelected(!btnSwitch.isSelected());
            if (m_listener != null) {
                m_listener.onDetail(position, btnSwitch.isSelected());
            }
        }
    }
}
