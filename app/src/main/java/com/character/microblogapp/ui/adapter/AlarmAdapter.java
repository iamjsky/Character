package com.character.microblogapp.ui.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.character.microblogapp.R;
import com.character.microblogapp.databinding.ItemAlarmBinding;
import com.character.microblogapp.model.MAlarm;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AlarmAdapter extends ArrayAdapter {
    public interface OnItemClickListener {
        void onDetail(int pos);
    }

    private OnItemClickListener m_listener;

    public AlarmAdapter(Context context, ArrayList<MAlarm.Result> items, OnItemClickListener p_listener) {
        super(context, items);
        m_listener = p_listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.item_alarm, parent, false).getRoot());
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ItemViewHolder) holder).m_info = (MAlarm.Result) getItem(position);
        ((ItemViewHolder) holder).position = position;
        ((ItemViewHolder) holder).bindItem();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvTitle)
        TextView tvTitle;

        @BindView(R.id.tvDate)
        TextView tvDate;

        MAlarm.Result m_info;
        int position = 0;

        ItemViewHolder(View p_view) {
            super(p_view);
            ButterKnife.bind(this, p_view);
        }

        void bindItem() {
            tvDate.setText(m_info.reg_time);
            switch (m_info.type) {
                case 1:
                    tvTitle.setText("오늘의 만남이 도착했습니다.");
                    break;
                case 2:
                    tvTitle.setText("이상형 소개가 도착했습니다.");
                    break;
                case 3:
                    tvTitle.setText("공지사항이 도착했습니다.");
                    break;
                case 4:
                    tvTitle.setText("이벤트가 도착했습니다.");
                    break;
                case 5:
                    tvTitle.setText("나를 좋아요한 이성이 있습니다.");
                    break;
                case 6:
                    tvTitle.setText("이성으로부터 높은점수를 받으셨습니다.");
                    break;
                case 7:
                    tvTitle.setText(m_info.nickname + "님으로부터 채팅이 도착했습니다.");
                    break;
                case 8:
                    tvTitle.setText(m_info.nickname + "회원님과 채팅방이 오픈되었습니다.");
                    break;
                case 9:
                    tvTitle.setText("신고를 받았습니다.");
                    break;
                case 10:
                    tvTitle.setText("포인트를 사용하였습니다.");
                    break;
                case 11:
                    tvTitle.setText("포인트를 획득하였습니다.");
                    break;
                case 12:
                    tvTitle.setText("포인트를 구매하였습니다.");
                    break;
                case 13:
                    tvTitle.setText("약관이 변경되었습니다.");
                    break;
                default:
                    break;
            }
        }

        @OnClick({R.id.rlBg})
        void onClick(View view) {
            if(m_listener != null) {
                m_listener.onDetail(position);
            }
        }
    }
}
