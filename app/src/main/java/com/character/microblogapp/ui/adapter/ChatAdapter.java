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
import com.character.microblogapp.model.MChat;
import com.character.microblogapp.ui.widget.RoundImageView;
import com.character.microblogapp.util.GlideUtil;
import com.character.microblogapp.util.Util;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChatAdapter extends ArrayAdapter {
    public ActionListener listener = null;

    private static int MSG_SEND = 1;
    private static int MSG_RECEIVE = 2;

    public ChatAdapter(Context context, ArrayList<MChat.Result> items, ActionListener actionListener) {
        super(context, items);
        listener = actionListener;
    }

    @Override
    public int getItemViewType(int p_nPosition) {
        MChat.Result item = (MChat.Result) mItems.get(p_nPosition);
        return item.sender_id == MyInfo.getInstance().uid ? MSG_SEND : MSG_RECEIVE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == MSG_SEND) {
            return new ItemSendViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.item_balloon_send, parent, false).getRoot());
        } else {
            return new ItemReceiveViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.item_balloon_receive, parent, false).getRoot());
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == MSG_SEND) {
            ((ItemSendViewHolder) holder).position = position;
            ((ItemSendViewHolder) holder).info = (MChat.Result) getItem(position);
            ((ItemSendViewHolder) holder).bindItem();
        } else {
            ((ItemReceiveViewHolder) holder).position = position;
            ((ItemReceiveViewHolder) holder).info = (MChat.Result) getItem(position);
            ((ItemReceiveViewHolder) holder).bindItem();
        }
    }

    class ItemSendViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvDetail)
        TextView tvDetail;

        @BindView(R.id.tvDate)
        TextView tvDate;

        @BindView(R.id.imgPhoto)
        ImageView imgPhoto;

        int position = 0;
        MChat.Result info;

        ItemSendViewHolder(View p_view) {
            super(p_view);
            ButterKnife.bind(this, p_view);
        }

        void bindItem() {
            tvDate.setText(info.reg_time);

            if (info.type == 1) {
                // 본문
                tvDetail.setText(info.content);
                imgPhoto.setVisibility(View.GONE);
            } else {
                tvDetail.setVisibility(View.GONE);
                GlideUtil.loadImage(imgPhoto, info.content);
                imgPhoto.setVisibility(View.VISIBLE);
            }
        }

        @OnClick({R.id.imgPhoto})
        void onPhoto(View view) {
            if (listener != null) {
                listener.onClickImg(info.content);
            }
        }
    }

    class ItemReceiveViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvDetail)
        TextView tvDetail;

        @BindView(R.id.tvDate)
        TextView tvDate;

        @BindView(R.id.tvName)
        TextView tvName;

        @BindView(R.id.imgPhoto)
        ImageView imgPhoto;

        @BindView(R.id.imgProfile)
        RoundImageView imgProfile;

        int position = 0;
        MChat.Result info;

        ItemReceiveViewHolder(View p_view) {
            super(p_view);
            ButterKnife.bind(this, p_view);
        }

        void bindItem() {
            tvDate.setText(info.reg_time);
            tvName.setText(info.nickname);

            if (info.type == 1) {
                // 본문
                tvDetail.setText(info.content);
                imgPhoto.setVisibility(View.GONE);
            } else {
                tvDetail.setVisibility(View.GONE);
                GlideUtil.loadImage(imgPhoto, info.content);
                imgPhoto.setVisibility(View.VISIBLE);
            }

            Util.loadImage(mContext, imgProfile, info.profile_url);
        }

        @OnClick({R.id.imgProfile})
        void onClick(View view) {
            if (listener != null) {
                listener.onClickImage(info.sender_id);
            }
        }

        @OnClick({R.id.imgPhoto})
        void onPhoto(View view) {
            if (listener != null) {
                listener.onClickImg(info.content);
            }
        }
    }

    public interface ActionListener {
        void onClickImage(int id);
        void onClickImg(String url);
    }
}
