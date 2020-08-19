package com.character.microblogapp.ui.page.main.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.antonyt.infiniteviewpager.InfinitePagerAdapter;
import com.antonyt.infiniteviewpager.InfiniteViewPager;
import com.character.microblogapp.R;
import com.character.microblogapp.ui.page.BaseFragment;
import com.character.microblogapp.ui.page.intro.IntroActivity;
import com.character.microblogapp.ui.page.main.MainActivity;
import com.character.microblogapp.util.PrefMgr;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.relex.circleindicator.CircleIndicator;

/**
 * 프로필 심사중 페이지
 */
public class InspectingUserFragment extends BaseFragment {
    @BindView(R.id.viewPager)
    InfiniteViewPager viewPager;

    @BindView(R.id.pageIndicator)
    CircleIndicator pageIndicator;

    TypedArray pageRes;

    InpectingUserPagerAdapter inpectingUserPagerAdapter = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        createView(inflater, container, R.layout.fragment_inspecting);

        return mRoot;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initVC();
    }

    void initVC() {
        pageRes = getResources().obtainTypedArray(R.array.inspecting_xml_array);

        viewPager.setOffscreenPageLimit(pageRes.length());
        inpectingUserPagerAdapter = new InpectingUserPagerAdapter();
        inpectingUserPagerAdapter.res = pageRes;
        PagerAdapter wrappedAdapter = new InfinitePagerAdapter(inpectingUserPagerAdapter);
        viewPager.setAdapter(wrappedAdapter);
        pageIndicator.setViewPager(viewPager);

    }

    class InpectingUserPagerAdapter extends PagerAdapter {
        TypedArray res;

        InpectingUserPagerAdapter() {
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
