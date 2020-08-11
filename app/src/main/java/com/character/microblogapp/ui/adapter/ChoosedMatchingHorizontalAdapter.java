package com.character.microblogapp.ui.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.character.microblogapp.GlideApp;
import com.character.microblogapp.R;
import com.character.microblogapp.data.Constant;
import com.character.microblogapp.data.MyInfo;
import com.character.microblogapp.model.MUserList;
import com.character.microblogapp.util.Util;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChoosedMatchingHorizontalAdapter extends ArrayAdapter {

    public interface OnClickListener {
        void onPickUser(int pos);
    }

    private OnClickListener mListener;

    public ChoosedMatchingHorizontalAdapter(Context context, ArrayList<MUserList.User> items, OnClickListener p_listener) {
        super(context, items);
        mListener = p_listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.item_choosed_matching_user, parent, false).getRoot());
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ItemViewHolder) holder).mUser = (MUserList.User) getItem(position);
        ((ItemViewHolder) holder).position = position;
        ((ItemViewHolder) holder).bindItem();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        MUserList.User mUser;
        int position = 0;

        @BindView(R.id.iv_user)
        ImageView ivProfile;

        @BindView(R.id.txv_character1)
        TextView tvCharacter1;

        @BindView(R.id.txv_character2)
        TextView tvCharacter2;

        @BindView(R.id.txv_ideal_character)
        TextView tvIdealCharacter;

        @BindView(R.id.tv_nickname)
        TextView tvNickname;

        @BindView(R.id.tv_address)
        TextView tvAddress;

        @BindView(R.id.tv_age)
        TextView tv_age;

        @BindView(R.id.tv_school)
        TextView tv_school;

        @BindView(R.id.tv_job)
        TextView tv_job;

        public ItemViewHolder(View p_view) {
            super(p_view);
            ButterKnife.bind(this, p_view);
        }

        void bindItem() {
            if (mUser.profile.length > 0) {
                GlideApp.with(ivProfile).load(mUser.profile[0]).into(ivProfile);
            }

            ivProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onPickUser(position);
                }
            });

            String w_type = mUser.character;
            if(w_type.equals("D=")){
                w_type = "D";
            } else if(w_type.equals("I=")){
                w_type = "I";
            } else if(w_type.equals("S=")){
                w_type = "S";
            } else if(w_type.equals("C=")){
                w_type = "C";
            }

            Spannable sb = new SpannableString(w_type);
            ArrayList<String> summary = new ArrayList<>();

            for (int ind = 0; ind < w_type.length(); ind++) {
                String item = w_type.substring(ind, ind + 1);
                switch (item) {
                    case "D":
                    case "d":
                        sb.setSpan(new ForegroundColorSpan(Constant.CHARACTER_D_COLOR), ind, ind + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        break;
                    case "I":
                    case "i":
                        sb.setSpan(new ForegroundColorSpan(Constant.CHARACTER_I_COLOR), ind, ind + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        break;
                    case "S":
                    case "s":
                        sb.setSpan(new ForegroundColorSpan(Constant.CHARACTER_S_COLOR), ind, ind + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        break;
                    case "C":
                    case "c":
                        sb.setSpan(new ForegroundColorSpan(Constant.CHARACTER_C_COLOR), ind, ind + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        break;
                }
            }

            tvCharacter1.setText(sb);

            tvIdealCharacter.setText(mUser.ideal_character);

            tvNickname.setText(mUser.nickname + ", ");
            if (!(mUser.age + "").isEmpty()) { // 나이
                tv_age.setText(mUser.age + "세");
            }

            if (mUser.job != null && !mUser.job.isEmpty()) { // 직업
                tv_job.setText(mUser.job + " |");
            }

            String address = "";

            if (mUser.address.split(" ").length != 0) {
                address = mUser.address.split(" ")[0];
            }

            tvAddress.setText(" " + address + " ");

            if (mUser.body_type != null && !mUser.body_type.isEmpty()) { // 체형
                tv_school.setText("| " +  mUser.body_type);
            }


        }
    }
}
