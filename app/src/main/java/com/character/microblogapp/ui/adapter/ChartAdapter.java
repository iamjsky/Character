package com.character.microblogapp.ui.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.character.microblogapp.R;
import com.character.microblogapp.data.MyInfo;
import com.character.microblogapp.model.MChatRoom;
import com.character.microblogapp.ui.widget.CircleImageView;
import com.character.microblogapp.util.Util;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChartAdapter extends ArrayAdapter {
    public interface OnItemClickListener {
        void onDetail(int pos);

        void onDeny(int pos);

        void onAllow(int pos);

        void onStartTalk(int pos);

        void onCancel(int pos);
    }

    private OnItemClickListener m_listener;

    public ChartAdapter(Context context, ArrayList<MChatRoom.Result> items, OnItemClickListener p_listener) {
        super(context, items);
        m_listener = p_listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.item_chart, parent, false).getRoot());
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ItemViewHolder) holder).m_info = (MChatRoom.Result) getItem(position);
        ((ItemViewHolder) holder).position = position;
        ((ItemViewHolder) holder).bindItem();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvName)
        TextView tvName;

        @BindView(R.id.tvDetail)
        TextView tvDetail;

        @BindView(R.id.tvCount)
        TextView tvCount;

        @BindView(R.id.imgPhoto)
        CircleImageView imgPhoto;

        @BindView(R.id.llRequest)
        LinearLayout llRequest;//승인/거절

        @BindView(R.id.llCancel)
        LinearLayout llCancel;//대화취소

        @BindView(R.id.llTalk)
        RelativeLayout llTalk;//대화신청

        MChatRoom.Result m_info;
        int position = 0;

        ItemViewHolder(View p_view) {
            super(p_view);
            ButterKnife.bind(this, p_view);
        }

        void bindItem() {
            tvName.setText(m_info.nickname);
            Util.loadImage(mContext, imgPhoto, m_info.profile.get(0));
            tvDetail.setText(m_info.last_chat_type == 1 ? m_info.last_chat_content : "이미지");

            llTalk.setVisibility(View.GONE);
            llRequest.setVisibility(View.GONE);
            llCancel.setVisibility(View.GONE);
            tvCount.setVisibility(m_info.unread_count == 0 ? View.GONE : View.VISIBLE);
            tvCount.setText("" + m_info.unread_count);

            llTalk.setVisibility(m_info.status == 1 ? View.GONE : View.VISIBLE);
//            switch (m_info.status) {
//                case 0:// open 안됨
//                    if (MyInfo.getInstance().uid == m_info.open_usr_uid) {
//                        // 취소버튼현시
//                        llCancel.setVisibility(View.VISIBLE);
//                    } else {
//                        // 승인,취소버튼현시
//                        llRequest.setVisibility(View.VISIBLE);
//                    }
//                    break;
//                case 1:// open
//                    if (m_info.last_chat_time.isEmpty()) {
//                        // 첫 대화일시
//                        llTalk.setVisibility(View.VISIBLE);
//                    }
//                    break;
//            }
        }

        @OnClick({R.id.btnRequest, R.id.btnClose, R.id.btnCancel, R.id.btnTalk, R.id.rlBg})
        void onClick(View view) {
            if (m_listener != null) {
                switch (view.getId()) {
                    case R.id.btnRequest:
                        // 승인
                        m_listener.onAllow(position);
                        break;
                    case R.id.btnClose:
                        // 거절
                        m_listener.onDeny(position);
                        break;
                    case R.id.btnCancel:
                        // 취소
                        m_listener.onCancel(position);
                        break;
                    case R.id.btnTalk:
                        // 대화신청
                        m_listener.onStartTalk(position);
                        break;
                    case R.id.rlBg:
                        // 대화상세
                        m_listener.onDetail(position);
                        break;
                }
            }
        }
    }
}
