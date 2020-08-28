package com.character.microblogapp.ui.page.main.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.antonyt.infiniteviewpager.InfinitePagerAdapter;
import com.antonyt.infiniteviewpager.InfiniteViewPager;
import com.character.microblogapp.R;
import com.character.microblogapp.ui.page.BaseFragment;
import com.character.microblogapp.ui.page.intro.IntroActivity;
import com.character.microblogapp.ui.page.main.MainActivity;
import com.character.microblogapp.ui.widget.customindicator.MyPageIndicator;
import com.character.microblogapp.util.PrefMgr;
import com.character.microblogapp.util.Toaster;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.relex.circleindicator.CircleIndicator;

/**
 * 프로필 심사중 페이지
 */
public class InspectingUserFragment extends BaseFragment {
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @BindView(R.id.pagesContainer)
    LinearLayout pagesContainer;

    MyPageIndicator pageIndicator;

    TypedArray pageRes;

    InpectingUserPagerAdapter inpectingUserPagerAdapter = null;
    private boolean mFinish = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        createView(inflater, container, R.layout.fragment_inspecting);
        pageRes = getResources().obtainTypedArray(R.array.inspecting_xml_array);

        viewPager.setOffscreenPageLimit(pageRes.length());
        inpectingUserPagerAdapter = new InpectingUserPagerAdapter(pageRes.length());
        inpectingUserPagerAdapter.res = pageRes;
        viewPager.setAdapter(inpectingUserPagerAdapter);
        pageIndicator = new MyPageIndicator(getContext(), pagesContainer, viewPager, R.drawable.indicator_circle);
        pageIndicator.setPageCount(pageRes.length());
        pageIndicator.show();
        mRoot.setFocusableInTouchMode(true);
        mRoot.requestFocus();
        mRoot.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (event.getAction() == KeyEvent.ACTION_UP) {
                        if (!mFinish) {
                            mFinish = true;
                            Toaster.showShort(getContext(), R.string.app_finish_message);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mFinish = false;
                                }
                            }, 2000);
                        } else {
                            ActivityCompat.finishAffinity(getActivity());
                            System.runFinalizersOnExit(true);
                            System.exit(0);

                        }
                        return true;
                    }
                }

                return false;
            }
        });

        return mRoot;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initVC();
    }

    void initVC() {


    }

    class InpectingUserPagerAdapter extends PagerAdapter {
        TypedArray res;
        public int mCount;

        InpectingUserPagerAdapter(int count) {
            mCount = count;
        }

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            LayoutInflater inflater = (LayoutInflater) container.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            int index = getRealPosition(position);
            View itemView = inflater.inflate(res.getResourceId(index, -1), container, false);



            container.addView(itemView, 0);
            return itemView;
        }
        public int getRealPosition(int position) {
            return position % mCount;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            (container).removeView((View) object);
        }
    }


}
