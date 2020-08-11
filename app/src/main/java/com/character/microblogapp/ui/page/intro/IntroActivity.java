package com.character.microblogapp.ui.page.intro;

import android.content.Context;
import android.content.res.TypedArray;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.antonyt.infiniteviewpager.InfinitePagerAdapter;
import com.antonyt.infiniteviewpager.InfiniteViewPager;
import com.character.microblogapp.R;
import com.character.microblogapp.ui.page.BaseActivity;

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

    TypedArray pageRes;

    IntroPagerAdapter introPagerAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewDataBinding binding = DataBindingUtil.setContentView(this,R.layout.activity_intro);

        setFinishAppWhenPressedBackKey();
        pageRes = getResources().obtainTypedArray(R.array.intro_xml_array);

        viewPager.setOffscreenPageLimit(pageRes.length());
        introPagerAdapter = new IntroPagerAdapter();
        introPagerAdapter.res = pageRes;
        PagerAdapter wrappedAdapter = new InfinitePagerAdapter(introPagerAdapter);
        viewPager.setAdapter(wrappedAdapter);
        pageIndicator.setViewPager(viewPager);

    }

    @OnClick(R.id.llyLogin)
    void onLogin() {
        startActivity(IntroActivity.this, LoginActivity.class);
    }

    @OnClick(R.id.llySignup)
    void onSignup() {
        startActivity(IntroActivity.this, SignupReadyActivity.class);
    }

    class IntroPagerAdapter extends PagerAdapter {
        TypedArray res;

        IntroPagerAdapter() {
        }

        @Override
        public int getCount() {
            return res.length();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            LayoutInflater inflater = (LayoutInflater) container.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View itemView = inflater.inflate(res.getResourceId(position, -1), container, false);



            container.addView(itemView, 0);
            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            (container).removeView((View) object);
        }
    }

}
