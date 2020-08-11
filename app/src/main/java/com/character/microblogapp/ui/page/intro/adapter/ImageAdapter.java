package com.character.microblogapp.ui.page.intro.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v4.widget.Space;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.character.microblogapp.GlideApp;
import com.character.microblogapp.R;
import com.character.microblogapp.data.BaseInfo;
import com.character.microblogapp.data.ImageInfo;
import com.character.microblogapp.ui.adapter.ArrayAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageAdapter extends ArrayAdapter {
    private Callback mCallback;
    public int PHOTO_MAX_COUNT = 8;

    public ImageAdapter(Context context, ArrayList items) {
        super(context, items);
    }

    public void setCallback(Callback mCallback) {
        this.mCallback = mCallback;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DefaultViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.row_image, parent, false).getRoot());
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        onBindViewHolders((DefaultViewHolder) holder, position);
    }

    @Override
    public int getItemCount() {
        return mItems == null ? 1 : (mItems.size() >= PHOTO_MAX_COUNT ? PHOTO_MAX_COUNT : mItems.size() + 1);
    }

    private void onBindViewHolders(DefaultViewHolder holder, final int position) {

        holder.ibAdd.setVisibility(View.GONE);
        holder.ibDelete.setVisibility(View.GONE);
        holder.ivImage.setVisibility(View.GONE);
        holder.spaceLeft.setVisibility(View.GONE);
        holder.spaceMiddle.setVisibility(View.GONE);
        holder.spaceRight.setVisibility(View.GONE);

        try {
            final ImageInfo item = (ImageInfo) mItems.get(position);

            holder.ibDelete.setVisibility(View.VISIBLE);
            holder.ivImage.setVisibility(View.VISIBLE);
            if (item.image != null) {
                GlideApp.with(holder.ivImage).load(item.image).into(holder.ivImage);
            } else {
                GlideApp.with(holder.ivImage).load(item.img_url).into(holder.ivImage);
            }

            holder.ibDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCallback != null) {
                        mCallback.onDelete(position, item);
                    }
                }
            });
        } catch (IndexOutOfBoundsException e) {
            // 추가인 경우
            holder.ibAdd.setVisibility(View.VISIBLE);
            holder.ibAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCallback != null) {
                        mCallback.onClick(position, null);
                    }
                }
            });
        } catch (NullPointerException e) {
            e.printStackTrace();
        } finally {
//            if (position == 0) {
//                holder.spaceLeft.setVisibility(View.VISIBLE);
//            }

//            if (position == getItemCount()) {
//                holder.spaceRight.setVisibility(View.VISIBLE);
//                holder.spaceMiddle.setVisibility(View.GONE);
//            }
        }
    }

    class DefaultViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivImage)
        ImageView ivImage;
        @BindView(R.id.ibAdd)
        ImageButton ibAdd;
        @BindView(R.id.ibDelete)
        ImageButton ibDelete;
        @BindView(R.id.spaceLeft)
        Space spaceLeft;
        @BindView(R.id.spaceMiddle)
        Space spaceMiddle;
        @BindView(R.id.spaceRight)
        Space spaceRight;

        DefaultViewHolder(View v) {
            super(v);

            ButterKnife.bind(this, v);
        }
    }

    public interface Callback {
        void onClick(final int position, final BaseInfo data);

        void onDelete(final int position, final BaseInfo data);
    }
}
