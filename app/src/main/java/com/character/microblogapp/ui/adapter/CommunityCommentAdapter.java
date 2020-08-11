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
import com.character.microblogapp.data.MyInfo;
import com.character.microblogapp.model.MComment;
import com.character.microblogapp.ui.widget.CircleImageView;
import com.character.microblogapp.util.GlideUtil;
import com.character.microblogapp.util.Util;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CommunityCommentAdapter extends ArrayAdapter {
    public interface OnItemClickListener {
        void onDelete(int pos);
    }

    private OnItemClickListener m_listener;

    public CommunityCommentAdapter(Context context, ArrayList<MComment.Comment> items, OnItemClickListener p_listener) {
        super(context, items);
        m_listener = p_listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.item_community_comment, parent, false).getRoot());
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ItemViewHolder) holder).m_info = (MComment.Comment) getItem(position);
        ((ItemViewHolder) holder).position = position;
        ((ItemViewHolder) holder).bindItem();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvNickname)
        TextView tvName;

        @BindView(R.id.tvContent)
        TextView tvDetail;

        @BindView(R.id.tvTime)
        TextView tvTime;

        @BindView(R.id.btnDelete)
        TextView btnDelete;

        @BindView(R.id.imgPhoto)
        ImageView imgPhoto;

        MComment.Comment m_info;
        int position = 0;

        ItemViewHolder(View p_view) {
            super(p_view);
            ButterKnife.bind(this, p_view);
        }

        void bindItem() {
//            tvName.setText(m_info.nickname);
            tvDetail.setText(m_info.content);
            tvTime.setText(m_info.reg_time);
            GlideUtil.loadRoundImage(imgPhoto, m_info.profile, Util.dp2px(mContext, 17));
            btnDelete.setVisibility(m_info.usr_uid == MyInfo.getInstance().uid ? View.VISIBLE : View.GONE);

            String[] result = Util.getSplitCharArrayFromString(m_info.nickname);

            String nick = "";
            for (int i = 0; i < result.length; i++) {
                nick = i > 1 ? nick + "*" : nick + result[i];
            }
            tvName.setText(nick);
        }

        @OnClick({R.id.btnDelete})
        void onClick(View view) {
            if (m_listener != null) {
                switch (view.getId()) {
                    case R.id.btnDelete:
                        // 취소
                        m_listener.onDelete(position);
                        break;

                }
            }
        }
    }
}
