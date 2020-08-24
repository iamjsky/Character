package com.character.microblogapp.ui.page.intro;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.antonyt.infiniteviewpager.InfinitePagerAdapter;
import com.antonyt.infiniteviewpager.InfiniteViewPager;
import com.character.microblogapp.R;
import com.character.microblogapp.ui.page.BaseActivity;
import com.character.microblogapp.ui.page.intro.fragment.IntroPageItem01Fragment;
import com.character.microblogapp.ui.page.intro.fragment.IntroPageItem02Fragment;
import com.character.microblogapp.ui.page.intro.fragment.IntroPageItem03Fragment;
import com.character.microblogapp.ui.page.main.fragment.HistoryFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.relex.circleindicator.CircleIndicator;

public class IntroActivity extends BaseActivity {
    @BindView(R.id.viewPager)
    InfiniteViewPager viewPager;

    @BindView(R.id.pageIndicator)
    CircleIndicator pageIndicator;

    IntroPagerAdapter introPagerAdapter = null;
    IntroPageItem02Fragment fragment;
    private int numPage = 3;
    private int nowPage = 0;


    Animation fadeInAnimation;
    int nowAnim = 0;
    FragmentManager manager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewDataBinding binding = DataBindingUtil.setContentView(this,R.layout.activity_intro);

        setFinishAppWhenPressedBackKey();
        manager = getSupportFragmentManager();
        viewPager.setOffscreenPageLimit(numPage);
        introPagerAdapter = new IntroPagerAdapter(manager, numPage);

        viewPager.setAdapter(introPagerAdapter);
        pageIndicator.setViewPager(viewPager);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                Log.e("char_debug", "i : " + i);

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });



    }

    @OnClick(R.id.llyLogin)
    void onLogin() {
        startActivity(IntroActivity.this, LoginActivity.class);
    }

    @OnClick(R.id.llySignup)
    void onSignup() {
        startActivity(IntroActivity.this, SignupReadyActivity.class);
    }

    class IntroPagerAdapter extends FragmentStatePagerAdapter {
        public int mCount;

        @Override
        public Fragment getItem(int position) {
            int index = getRealPosition(position);


            switch (index) {
                case 0:
                    return IntroPageItem01Fragment.newInstance(index + 1);
                case 1:
                    return IntroPageItem02Fragment.newInstance(index + 1);
                case 2:
                    return IntroPageItem03Fragment.newInstance(index + 1);
                default:
                    return null;
            }

        }




        @Override
        public int getCount() {
            return mCount;
        }

        public IntroPagerAdapter(FragmentManager manager, int count) {
            super(manager);
            mCount = count;
        }



        public int getRealPosition(int position) {
            return position % mCount;
        }

    }

}
