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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.character.microblogapp.R;
import com.character.microblogapp.model.MQna;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class QnaAdapter extends ArrayAdapter {
    public interface OnItemClickListener {
        void onDetail(int pos);
    }

    private OnItemClickListener m_listener;

    public QnaAdapter(Context context, ArrayList<MQna.Result> items, OnItemClickListener p_listener) {
        super(context, items);
        m_listener = p_listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.item_qna, parent, false).getRoot());
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ItemViewHolder) holder).m_info = (MQna.Result) getItem(position);
        ((ItemViewHolder) holder).position = position;
        ((ItemViewHolder) holder).bindItem();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvTitle)
        TextView tvTitle;

        @BindView(R.id.tvDate)
        TextView tvDate;

        @BindView(R.id.tvStatus)
        TextView tvStatus;

        @BindView(R.id.tvDetail)
        TextView tvDetail;

        @BindView(R.id.tvReply)
        TextView tvReply;

        @BindView(R.id.rlDetail)
        LinearLayout rlDetail;

        @BindView(R.id.btnStatus)
        ImageButton btnStatus;

        @BindView(R.id.imgLine)
        ImageView imgLine;

        MQna.Result m_info;
        int position = 0;

        ItemViewHolder(View p_view) {
            super(p_view);
            ButterKnife.bind(this, p_view);
        }

        void bindItem() {
            initView();

            tvTitle.setText(m_info.title);
            tvDetail.setText(m_info.content);
            tvDate.setText(m_info.reg_time);
            tvReply.setText(m_info.reply);
            imgLine.setVisibility(m_info.isSelected ? View.GONE : View.VISIBLE);
            btnStatus.setSelected(m_info.status == 1);
            if (m_info.status == 0) {
                tvStatus.setText("답변대기");
            } else {
                tvStatus.setText("답변완료");
            }
        }

        void initView() {
            rlDetail.setVisibility(m_info.isSelected ? View.VISIBLE : View.GONE);
        }

        @OnClick(R.id.rlBg)
        void onBg() {
            m_info.isSelected = !m_info.isSelected;
            initView();
            if(m_listener != null) {
                m_listener.onDetail(position);
            }
        }

        @OnClick({R.id.btnStatus})
        void onClick(View view) {

        }
    }
}
