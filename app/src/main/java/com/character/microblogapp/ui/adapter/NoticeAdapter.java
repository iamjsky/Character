package com.character.microblogapp.ui.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.character.microblogapp.R;
import com.character.microblogapp.model.MEvent;
import com.character.microblogapp.util.Util;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NoticeAdapter extends ArrayAdapter {
    public interface OnItemClickListener {
        void onDetail(int pos);
    }

    private OnItemClickListener m_listener;

    public NoticeAdapter(Context context, ArrayList<MEvent.Result> items, OnItemClickListener p_listener) {
        super(context, items);
        m_listener = p_listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.item_notice, parent, false).getRoot());
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ItemViewHolder) holder).m_info = (MEvent.Result) getItem(position);
        ((ItemViewHolder) holder).position = position;
        ((ItemViewHolder) holder).bindItem();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvTitle)
        TextView tvTitle;

        @BindView(R.id.tvDate)
        TextView tvDate;

        @BindView(R.id.imgPhoto)
        ImageView imgPhoto;

        @BindView(R.id.tvDetail)
        TextView tvDetail;

        @BindView(R.id.rlBg)
        LinearLayout rlBg;

        @BindView(R.id.llDetail)
        LinearLayout llDetail;

        MEvent.Result m_info;
        int position = 0;

        ItemViewHolder(View p_view) {
            super(p_view);
            ButterKnife.bind(this, p_view);
        }

        void bindItem() {
            tvTitle.setText(m_info.title);
            tvDetail.setText(m_info.content);
            tvDate.setText(m_info.reg_time);

            Util.loadImage(mContext, imgPhoto, m_info.image);

            rlBg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    m_info.isSelected = !m_info.isSelected;
                    initUI();
                    if (m_listener != null) {
                        m_listener.onDetail(position);
                    }
                }
            });
        }

        void initUI() {
            llDetail.setVisibility(m_info.isSelected ? View.VISIBLE : View.GONE);
        }

    }
}
