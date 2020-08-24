package com.character.microblogapp.ui.page.intro.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.character.microblogapp.R;
import com.character.microblogapp.ui.page.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class IntroPageItem02Fragment extends BaseFragment {
    private int frag_num;
    public static IntroPageItem02Fragment instance;


    @BindView(R.id.iv_talk)
    ImageView iv_talk;
    @BindView(R.id.iv_chat_00)
    ImageView iv_chat_00;
    @BindView(R.id.tv_chat_01)
    TextView tv_chat_01;
    @BindView(R.id.layout_chat_02)
    LinearLayout layout_chat_02;
    @BindView(R.id.tv_chat_03)
    TextView tv_chat_03;
    @BindView(R.id.tv_chat_04)
    TextView tv_chat_04;
    @BindView(R.id.layout_chat_05)
    LinearLayout layout_chat_05;
    @BindView(R.id.layout_chat_06)
    LinearLayout layout_chat_06;
    @BindView(R.id.layout_result)
    LinearLayout layout_result;




    public IntroPageItem02Fragment() {
    }

    // newInstance constructor for creating fragment with arguments
    public static IntroPageItem02Fragment newInstance(int num) {
        IntroPageItem02Fragment fragment = new IntroPageItem02Fragment();
        Bundle args = new Bundle();
        args.putInt("num", num);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        frag_num = getArguments().getInt("num", 0);

    }   //  onCreate

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.item_intro_page_02, container, false);
        ButterKnife.bind(this, view);
        instance = this;





        return view;

    }   //  onCreateView


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstancdState) {
        super.onViewCreated(view, savedInstancdState);
    }   //  onViewCreated

    @Override
    public void onResume() {
        super.onResume();
        Log.e("char_debug", "onResume");
        if (getView() == null) {
            return;
        }


    }   //  onResume

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser && nowAnim == 0) {
            Log.e("char_debug", "isVisibleToUser : " + isVisibleToUser);

            startAnim();
        }

        super.setUserVisibleHint(isVisibleToUser);


    }
    int animCount = 9;
    Animation[] fadeInAnimation = new Animation[animCount];
    int nowAnim = 0;
    public void startAnim() {


        iv_chat_00.setVisibility(View.INVISIBLE);
        tv_chat_01.setVisibility(View.INVISIBLE);
        layout_chat_02.setVisibility(View.INVISIBLE);
        tv_chat_03.setVisibility(View.INVISIBLE);
        tv_chat_04.setVisibility(View.INVISIBLE);
        layout_chat_05.setVisibility(View.INVISIBLE);
        layout_chat_06.setVisibility(View.INVISIBLE);
        iv_talk.setVisibility(View.INVISIBLE);
        layout_result.setVisibility(View.INVISIBLE);
        nowAnim = 0;
        /*페이드 인 애니메이션*/
        for (int i = 0; i < animCount; i++) {
            fadeInAnimation[i] = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in_chat);
            Animation.AnimationListener animationListener = new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    nowAnim++;
                    switch (nowAnim) {
                        case 1:
                            iv_chat_00.setVisibility(View.VISIBLE);
                            tv_chat_01.startAnimation(fadeInAnimation[nowAnim]);
                            break;
                        case 2:
                            tv_chat_01.setVisibility(View.VISIBLE);
                            layout_chat_02.startAnimation(fadeInAnimation[nowAnim]);
                            break;

                        case 3:
                            layout_chat_02.setVisibility(View.VISIBLE);
                            tv_chat_03.startAnimation(fadeInAnimation[nowAnim]);
                            break;

                        case 4:
                            tv_chat_03.setVisibility(View.VISIBLE);
                            tv_chat_04.startAnimation(fadeInAnimation[nowAnim]);
                            break;

                        case 5:
                            tv_chat_04.setVisibility(View.VISIBLE);
                            layout_chat_05.startAnimation(fadeInAnimation[nowAnim]);
                            break;

                        case 6:
                            layout_chat_05.setVisibility(View.VISIBLE);
                            layout_chat_06.startAnimation(fadeInAnimation[nowAnim]);
                            break;
                        case 7:
                            layout_chat_06.setVisibility(View.VISIBLE);
                            iv_talk.startAnimation(fadeInAnimation[nowAnim]);
                            break;
                        case 8:
                            iv_talk.setVisibility(View.VISIBLE);
                            layout_result.startAnimation(fadeInAnimation[nowAnim]);
                            break;
                        case 9:
                            layout_result.setVisibility(View.VISIBLE);
                            break;
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            };

            fadeInAnimation[i].setAnimationListener(animationListener);
        }

        iv_chat_00.startAnimation(fadeInAnimation[nowAnim]);
    }

}
