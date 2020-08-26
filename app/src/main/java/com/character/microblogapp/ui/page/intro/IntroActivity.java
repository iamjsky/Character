package com.character.microblogapp.ui.page.intro;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.LinearLayout;

import com.character.microblogapp.R;
import com.character.microblogapp.ui.page.BaseActivity;
import com.character.microblogapp.ui.page.intro.fragment.IntroPageItem01Fragment;
import com.character.microblogapp.ui.page.intro.fragment.IntroPageItem02Fragment;
import com.character.microblogapp.ui.page.intro.fragment.IntroPageItem03Fragment;
import com.character.microblogapp.ui.widget.customindicator.MyPageIndicator;

import butterknife.BindView;
import butterknife.OnClick;

public class IntroActivity extends BaseActivity {
    @BindView(R.id.viewPager)
    ViewPager viewPager;


    @BindView(R.id.pagesContainer)
    LinearLayout pagesContainer;

    MyPageIndicator pageIndicator;

    IntroPagerAdapter introPagerAdapter = null;
    private int numPage = 3;
    private int nowPage = 0;


    FragmentManager manager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewDataBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_intro);

        setFinishAppWhenPressedBackKey();
        manager = getSupportFragmentManager();
        viewPager.setOffscreenPageLimit(numPage);
        introPagerAdapter = new IntroPagerAdapter(manager, numPage);

        viewPager.setAdapter(introPagerAdapter);

        pageIndicator = new MyPageIndicator(this, pagesContainer, viewPager, R.drawable.indicator_circle);
        pageIndicator.setPageCount(numPage);
        pageIndicator.show();


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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        pageIndicator.cleanup();
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
            return Integer.MAX_VALUE;
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
