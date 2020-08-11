package com.character.microblogapp.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.character.microblogapp.R;
import com.character.microblogapp.model.Interest;
import com.character.microblogapp.model.MInterest;
import com.character.microblogapp.util.Toaster;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InterestAdapter extends ArrayAdapter {
    public interface OnItemClickListener {
        void onClick(int pos);
    }

    private Context _context;
    private OnItemClickListener m_listener;

    public InterestAdapter(Context context, ArrayList<Interest> all, OnItemClickListener p_listener) {
        super(context, all);
        _context = context;
        m_listener = p_listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.item_interest, parent, false).getRoot());
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ItemViewHolder) holder).m_info = (Interest) getItem(position);
        ((ItemViewHolder) holder).position = position;
        ((ItemViewHolder) holder).bindItem();
    }

    private int selectedParentCount() {
        int count = 0;
        for (Object item : mItems) {
            if (((Interest) item).selectedChildCount() > 0) {
                count++;
            }
        }
        return count;
    }

    public ArrayList<Interest> getSelected() {
        ArrayList<Interest> result = new ArrayList<>();
        for (Object item : mItems) {
            if (((Interest) item).selectedChildCount() > 0) {
                result.add((Interest) item);
            }
        }
        return result;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rcvChild)
        RecyclerView rcvChild;

        @BindView(R.id.rlyTitle)
        ConstraintLayout rlyTitle;

        @BindView(R.id.tvTitle)
        TextView tvTitle;

        Interest m_info;
        int position = 0;

        InterestChildAdapter adapter;

        ItemViewHolder(View p_view) {
            super(p_view);
            ButterKnife.bind(this, p_view);
        }

        @SuppressLint("DefaultLocale")
        void bindItem() {
            tvTitle.setText(String.format("%s(%d/%d)", m_info.name, m_info.selectedChildCount(), 3));
            tvTitle.setTextColor(m_info.selectedChildCount() > 0 ? Color.parseColor("#f13c4c") : Color.parseColor("#000000"));
            rlyTitle.setBackgroundResource(m_info.selectedChildCount() > 0 ? R.drawable.btn_bg_test_answer : R.drawable.btn_bg_test_answer_black);

            adapter = new InterestChildAdapter(_context, m_info.child, new InterestChildAdapter.OnItemClickListener() {
                @Override
                public void onClick(int pos) {
                    Interest.Child child = m_info.child.get(pos);

                    if (child.selected) {
                        child.selected = false;
                        adapter.notifyItemChanged(pos);
                    } else {
                        if (m_info.selectedChildCount() < 1 && selectedParentCount() >= 5) {
                            Toaster.showShort(_context, "상위관심사는 최대 5개까지 등록하실 수 있습니다.");
                            return;
                        }

                        if (m_info.selectedChildCount() < 3) {
                            child.selected = true;
                            adapter.notifyItemChanged(pos);
                        } else {
                            Toaster.showShort(_context, "하위관심사는 최대 3개까지 등록하실 수 있습니다.");
                        }
                    }

                    rlyTitle.setBackgroundResource(m_info.selectedChildCount() > 0 ? R.drawable.btn_bg_test_answer : R.drawable.btn_bg_test_answer_black);
                    tvTitle.setTextColor(m_info.selectedChildCount() > 0 ? Color.parseColor("#f13c4c") : Color.parseColor("#000000"));
                    tvTitle.setText(String.format("%s(%d/%d)", m_info.name, m_info.selectedChildCount(), 3));
                }
            });

            rcvChild.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

        @OnClick({R.id.rlBg})
        void onClick() {
            if (m_listener != null) {
                m_listener.onClick(position);
            }
        }
    }
}
