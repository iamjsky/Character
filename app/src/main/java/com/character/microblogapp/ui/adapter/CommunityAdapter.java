package com.character.microblogapp.ui.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.character.microblogapp.R;
import com.character.microblogapp.model.MCommunity;
import com.character.microblogapp.util.GlideUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CommunityAdapter extends ArrayAdapter {
    public interface OnItemClickListener {
        void onDetail(int pos);
    }

    private OnItemClickListener m_listener;

    public CommunityAdapter(Context context, ArrayList<MCommunity.Result> items, OnItemClickListener p_listener) {
        super(context, items);
        m_listener = p_listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.item_community, parent, false).getRoot());
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ItemViewHolder) holder).m_info = (MCommunity.Result) getItem(position);
        ((ItemViewHolder) holder).position = position;
        ((ItemViewHolder) holder).bindItem();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txv_content)
        TextView tvContent;

        @BindView(R.id.txv_time)
        TextView txvTime;

        @BindView(R.id.imv_image)
        ImageView imvImage;

        MCommunity.Result m_info;
        int position = 0;

        ItemViewHolder(View p_view) {
            super(p_view);
            ButterKnife.bind(this, p_view);
        }

        void bindItem() {
            tvContent.setText(m_info.title);
            txvTime.setText(m_info.reg_time);
            if (m_info.profile.size() > 0) {
                imvImage.setVisibility(View.VISIBLE);
                GlideUtil.loadImage(imvImage, m_info.add_file.size() > 0 ? m_info.add_file.get(0) : "");
            } else {
                imvImage.setVisibility(View.GONE);
            }
        }

        @OnClick({R.id.rlBg})
        void onClick(View view) {
            if (m_listener != null) {
                // 대화상세
                m_listener.onDetail(position);
            }
        }
    }
}
