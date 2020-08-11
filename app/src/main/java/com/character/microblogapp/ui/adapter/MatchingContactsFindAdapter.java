package com.character.microblogapp.ui.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;
import com.character.microblogapp.R;
import com.character.microblogapp.data.Constant;
import com.character.microblogapp.model.MUserList;
import com.character.microblogapp.ui.widget.RoundImageView;
import com.character.microblogapp.util.GlideUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MatchingContactsFindAdapter extends ArrayAdapter {

    public interface OnClickListener {
        void onPickUser(int pos);

        void onSelect(int pos);

        void onProfile(int pos);
    }

    private OnClickListener mListener;
    private Context _context;
    private int status = 0;
    private boolean is_show_pick = true;

    public MatchingContactsFindAdapter(Context context, ArrayList<MUserList.User> items, OnClickListener p_listener) {
        super(context, items);
        _context = context;
        mListener = p_listener;
    }

    public void setShowPick(boolean show) {
        is_show_pick = show;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_matching_contacts, parent, false).getRoot());
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ItemViewHolder) holder).mUser = (MUserList.User) getItem(position);
        ((ItemViewHolder) holder).position = position;
        ((ItemViewHolder) holder).bindItem();
    }

    public void updateStatus(int status) {
        this.status = status;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        MUserList.User mUser;
        int position = 0;

        @BindView(R.id.lly_userinfo_bg)
        LinearLayout lly_userinfo_bg;

        @BindView(R.id.iv_profile)
        RoundImageView ivProfile;

        @BindView(R.id.tv_profile)
        TextView tv_profile;

        @BindView(R.id.tv_detail)
        TextView tv_detail;

        @BindView(R.id.tv_height)
        TextView tv_height;

        @BindView(R.id.btn_pick)
        TextView btn_pick;

        @BindView(R.id.tvType)
        TextView tvType;

        @BindView(R.id.fly_cover)
        FrameLayout fly_cover;

        @BindView(R.id.imv_blur)
        ImageView imv_blur;

        @BindView(R.id.imv_backside)
        ImageView imv_backside;

        @BindView(R.id.txv_character1)
        TextView tvCharacter1;

        @BindView(R.id.txv_character2)
        TextView tvCharacter2;

        @BindView(R.id.txv_ideal_character)
        TextView tvIdealCharacter;

        public ItemViewHolder(View p_view) {
            super(p_view);
            ButterKnife.bind(this, p_view);
        }

        void bindItem() {
            String w_type = mUser.character;
            if (w_type.equals("D=")) {
                w_type = "D";
            } else if (w_type.equals("I=")) {
                w_type = "I";
            } else if (w_type.equals("S=")) {
                w_type = "S";
            } else if (w_type.equals("C=")) {
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

            if (mUser.profile.length > 0) {
                GlideUtil.loadRoundImage(ivProfile, mUser.profile[0], ConvertUtils.dp2px(4));
            }

//            if (!is_show_pick)
//                btn_pick.setVisibility(View.GONE);
//            else
//                btn_pick.setVisibility(View.VISIBLE);

            btn_pick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onPickUser(position);
                }
            });

            ivProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onProfile(position);
                }
            });


            String address = "";

            if (mUser.address.split(" ").length != 0) {
                address = mUser.address.split(" ")[0];
            }

            if (mUser.is_choose_state == 0) {
                tv_profile.setText(mUser.nickname + ", " + mUser.age + "세");
                tv_height.setText(mUser.job + " | " + address + " | " + mUser.body_type);
            } else {
                tv_profile.setText(mUser.nickname);
                tv_height.setText(mUser.age + "세 | " + address + " | " + mUser.body_type);
            }

            tv_detail.setText(mUser.ideal_character);

            ivProfile.setVisibility(View.VISIBLE);
            imv_blur.setVisibility(View.GONE);
            imv_backside.setVisibility(View.GONE);
            fly_cover.setVisibility(View.GONE);

        }

        Handler cardPlayHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                fly_cover.setVisibility(View.VISIBLE);
                Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.scale_out);
                imv_backside.startAnimation(animation);

                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if (mUser.is_choose_state == 0) {
                            fly_cover.setVisibility(View.VISIBLE);
                            imv_backside.setVisibility(View.VISIBLE);
                        } else {
                            fly_cover.setVisibility(View.GONE);
                            imv_backside.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                return false;
            }
        });
    }
}

