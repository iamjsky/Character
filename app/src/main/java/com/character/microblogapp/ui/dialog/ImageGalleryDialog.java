package com.character.microblogapp.ui.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.character.microblogapp.R;
import com.character.microblogapp.ui.widget.LockableViewPager;
import com.imagezoom.ImageViewTouch;
import com.imagezoom.ImageViewTouchBase;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ImageGalleryDialog extends BaseDialog {

    private List<String> mImageList;
    private int mCurPos;
    private Context mContext;

    @BindView(R.id.vp_pager)
    LockableViewPager m_vpImage;

    public ImageGalleryDialog(Context context, List<String> arImages, int initPos) {
        super(context, android.R.style.Theme_DeviceDefault_NoActionBar);

        setContentView(R.layout.dialog_image_gallery);
        ButterKnife.bind(this);

        mContext = context;
        mImageList = arImages;
        mCurPos = initPos;

        setDialog();
    }

    public ImageGalleryDialog(Context context, String image) {
        super(context, android.R.style.Theme_DeviceDefault_NoActionBar);

        setContentView(R.layout.dialog_image_gallery);
        ButterKnife.bind(this);

        mContext = context;
        mImageList = new ArrayList<>();
        mImageList.add(image);
        mCurPos = 0;

        setDialog();
    }

    private void setDialog() {
        m_vpImage.setAdapter(new ImagePagerAdapter());
        m_vpImage.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        showImages();
    }

    @OnClick(R.id.ib_close)
    void onClose() {
        dismiss();
    }

    private void showImages() {
        m_vpImage.getAdapter().notifyDataSetChanged();
        m_vpImage.setCurrentItem(mCurPos);
    }

    private class ImagePagerAdapter extends PagerAdapter {
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);
        }

        @Override
        public void finishUpdate(View container) {
        }

        @Override
        public int getCount() {
            if (mImageList == null)
                return 0;
            else
                return mImageList.size();
        }

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public Object instantiateItem(ViewGroup view, int position) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View itemView = inflater.inflate(R.layout.item_image_gallery, view, false);

            final ImageViewTouch ivImage = itemView.findViewById(R.id.iv_photo_detail_item);
            ivImage.setDisplayType(ImageViewTouchBase.DisplayType.FIT_TO_SCREEN);
            ivImage.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    float matrix = ivImage.getScale();

                    if (matrix > 1.0) {
                        m_vpImage.setPagingEnabled(false);
                    } else {
                        m_vpImage.setPagingEnabled(true);
                    }
                    return false;
                }
            });

            String imgUrl = mImageList.get(position);
            Glide.with(mContext).load(imgUrl).dontAnimate().into(ivImage);

            view.addView(itemView, 0);

            return itemView;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View container) {
        }
    }
}
