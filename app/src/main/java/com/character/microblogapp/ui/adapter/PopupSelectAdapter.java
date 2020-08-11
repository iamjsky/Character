package com.character.microblogapp.ui.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.character.microblogapp.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Crazy on 5/14/2019.
 */

public class PopupSelectAdapter extends ArrayAdapter {

    OnItemClickListener onItemClickListener;
    String selectedString;
    int textAlignment;
    int itemWidth = -1;

    public PopupSelectAdapter(Context context, ArrayList<String> items, String selectedString, int textAlignment, OnItemClickListener onItemClickListener) {
        super(context, items);
        this.selectedString = selectedString;
        this.onItemClickListener = onItemClickListener;
        this.textAlignment = textAlignment;
    }

    public PopupSelectAdapter setItemWidth(int width) {
        itemWidth = width;
        return this;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.item_popup, parent, false).getRoot());
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ItemViewHolder) holder).title = (String) getItem(position);
        ((ItemViewHolder) holder).bindItem();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        @BindView((R.id.tvTitle))
        TextView tvTitle;

        String title;

        ItemViewHolder(View p_view) {
            super(p_view);
            ButterKnife.bind(this, p_view);
        }

        void bindItem() {
            tvTitle.setTextAlignment(textAlignment);
            tvTitle.setText(title);
            tvTitle.setSelected(selectedString.equals(title));
            if (itemWidth != -1) {
                ViewGroup.LayoutParams layoutParams = tvTitle.getLayoutParams();
                layoutParams.width = itemWidth;
                tvTitle.setLayoutParams(layoutParams);
            }
        }

        @OnClick(R.id.tvTitle)
        void onTitle() {
            if (onItemClickListener != null)
                onItemClickListener.onItemClick(title, getLayoutPosition());
        }
    }

    public interface OnItemClickListener {
        void onItemClick(String title, int position);
    }
}
