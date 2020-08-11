package com.character.microblogapp.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;

import com.character.microblogapp.R;
import com.viewpagerindicator.PageIndicator;

public class CirclePagerIndicator extends View implements PageIndicator {
    private static final String TAG = CirclePagerIndicator.class.getSimpleName();
    private final Paint pageColorPainter;
    private final Paint strokeColorPainter;
    private final Paint fillColorPainter;
    private ViewPager viewPager;
    private OnPageChangeListener pageChangeListener;
    private boolean isSnap;
    private boolean isMoveNext;
    private int gravity;
    private int currentPagePos;
    private int prevPagePos;
    private int scrollState;
    private int orientation;
    private int thresholder;
    private int touchPointerId;
    private float spacing;
    private float positionOffset;
    private float radius;
    private float touchDownX;

    static class SavedState extends BaseSavedState {
        int itemPos;

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {

            @Override
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            @Override
            public SavedState[] newArray(int i) {
                return new SavedState[i];
            }
        };

        private SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        private SavedState(Parcel parcel) {
            super(parcel);
            this.itemPos = parcel.readInt();
        }

        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeInt(this.itemPos);
        }
    }

    public CirclePagerIndicator(Context context) {
        this(context, null);
    }

    public CirclePagerIndicator(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.styleable.CirclePagerIndicator_android_gravity);
    }

    public CirclePagerIndicator(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.pageColorPainter = new Paint(1);
        this.strokeColorPainter = new Paint(1);
        this.fillColorPainter = new Paint(1);

        this.touchPointerId = -1;
        this.touchDownX = -1.0f;

        if (!isInEditMode()) {
            gravity = Gravity.CENTER;
            isSnap = true;
            orientation = LinearLayout.HORIZONTAL;

            spacing = radius * 2.0f;
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.CirclePagerIndicator, i, 0);

            float strokeWidth = 0.0f;
            int fillColor = ContextCompat.getColor(context, R.color.color_white);
            int pageColor = ContextCompat.getColor(context, R.color.color_alpha_white);
            int strokeColor = 0;

            radius = getResources().getDimension(R.dimen.page_indicator_radius);
            if (obtainStyledAttributes != null) {
                setGravity(obtainStyledAttributes.getInt(R.styleable.CirclePagerIndicator_android_gravity, Gravity.CENTER));
                setOrientation(obtainStyledAttributes.getInt(R.styleable.CirclePagerIndicator_android_orientation, LinearLayout.HORIZONTAL));
                strokeWidth = obtainStyledAttributes.getDimension(R.styleable.CirclePagerIndicator_strokeWidth, strokeWidth);
                fillColor = obtainStyledAttributes.getColor(R.styleable.CirclePagerIndicator_fillColor, fillColor);
                pageColor = obtainStyledAttributes.getColor(R.styleable.CirclePagerIndicator_pageColor, pageColor);
                radius = obtainStyledAttributes.getDimension(R.styleable.CirclePagerIndicator_radius, radius);
                setSnap(obtainStyledAttributes.getBoolean(R.styleable.CirclePagerIndicator_snap, isSnap));
                strokeColor = obtainStyledAttributes.getColor(R.styleable.CirclePagerIndicator_strokeColor, strokeColor);
                spacing = obtainStyledAttributes.getDimension(R.styleable.CirclePagerIndicator_spacing, spacing);
                obtainStyledAttributes.recycle();
            }

            setPageColor(pageColor);
            setFillColor(fillColor);
            setStrokeColor(strokeColor);
            setStrokeWidth(strokeWidth);
            thresholder = ViewConfiguration.get(context).getScaledPagingTouchSlop();
        }
    }

    public void setGravity(int gravity) {
        this.gravity = gravity;
        invalidate();
    }

    public void setPageColor(int pageColor) {
        this.pageColorPainter.setStyle(Style.FILL);
        this.pageColorPainter.setColor(pageColor);
        invalidate();
    }

    public int getPageColor() {
        return this.pageColorPainter.getColor();
    }

    public void setFillColor(int i) {
        this.fillColorPainter.setStyle(Style.FILL);
        this.fillColorPainter.setColor(i);
        invalidate();
    }

    public int getFillColor() {
        return this.fillColorPainter.getColor();
    }

    public void setOrientation(int orientation) {
        switch (orientation) {
            case LinearLayout.HORIZONTAL:
            case LinearLayout.VERTICAL:
                this.orientation = orientation;
                requestLayout();
                return;
            default:
                throw new IllegalArgumentException("Orientation must be either HORIZONTAL or VERTICAL.");
        }
    }

    public int getOrientation() {
        return this.orientation;
    }

    public void setStrokeColor(int i) {
        this.strokeColorPainter.setStyle(Style.STROKE);
        this.strokeColorPainter.setColor(i);
        invalidate();
    }

    public int getStrokeColor() {
        return this.strokeColorPainter.getColor();
    }

    public void setStrokeWidth(float f) {
        this.strokeColorPainter.setStrokeWidth(f);
        invalidate();
    }

    public float getStrokeWidth() {
        return this.strokeColorPainter.getStrokeWidth();
    }

    public void setRadius(float radius) {
        this.radius = radius;
        invalidate();
    }

    public float getRadius() {
        return this.radius;
    }

    public void setSpacing(float spacing) {
        this.spacing = spacing;
        invalidate();
    }

    public void setSnap(boolean isSnap) {
        this.isSnap = isSnap;
        invalidate();
    }

    public boolean isSnap() {
        return this.isSnap;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.viewPager != null) {
            int count = this.viewPager.getAdapter().getCount();
            if (count == 0) {
                return;
            }
            if (this.currentPagePos >= count) {
                setCurrentItem(count - 1);
                return;
            }
            int width;
            int height;
            int paddingLeft;
            int paddingRight;
            float f;
            if (this.orientation == 0) {
                width = getWidth();
                height = getHeight();
                paddingLeft = getPaddingLeft();
                paddingRight = getPaddingRight();
            } else {
                width = getHeight();
                height = getWidth();
                paddingLeft = getPaddingTop();
                paddingRight = getPaddingBottom();
            }
            float f2 = this.radius * 2.0f;
            float f3 = ((float) height) / 2.0f;
            float f4 = (((float) count) * f2) + (((float) (count - 1)) * this.spacing);
            int i = this.gravity & 112;
            switch (this.gravity & GravityCompat.RELATIVE_HORIZONTAL_GRAVITY_MASK) {
                case GravityCompat.START /*8388611*/:
                    f = ((float) paddingLeft) + this.radius;
                    break;
                case GravityCompat.END /*8388613*/:
                    f = (((float) (width - paddingRight)) - f4) + this.radius;
                    break;
                default:
                    f = (((((float) ((width - paddingLeft) - paddingRight)) - f4) / 2.0f) + ((float) paddingLeft)) + this.radius;
                    break;
            }
            float f5 = this.radius;
            if (this.strokeColorPainter.getStrokeWidth() > 0.0f) {
                f5 -= this.strokeColorPainter.getStrokeWidth() / 2.0f;
            }
            for (i = 0; i < count; i++) {
                float f6;
                f4 = (((float) i) * (this.spacing + f2)) + f;
                if (this.orientation == 0) {
                    f6 = f4;
                    f4 = f3;
                } else {
                    f6 = f3;
                }
                if (this.pageColorPainter.getAlpha() > 0) {
                    canvas.drawCircle(f6, f4, f5, this.pageColorPainter);
                }
                if (f5 != this.radius) {
                    canvas.drawCircle(f6, f4, this.radius, this.strokeColorPainter);
                }
            }
            f5 = ((float) (this.isSnap ? this.prevPagePos : this.currentPagePos)) * (this.spacing + f2);
            if (!this.isSnap) {
                f5 += this.positionOffset * (this.spacing + f2);
            }
            if (this.orientation == 0) {
                f += f5;
            } else {
                float f7 = f + f5;
                f = f3;
                f3 = f7;
            }
            canvas.drawCircle(f, f3, this.radius, this.fillColorPainter);
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        int pointerIndex = 0;
        if (super.onTouchEvent(motionEvent)) {
            return true;
        }
        if (this.viewPager == null || this.viewPager.getAdapter().getCount() == 0) {
            return false;
        }

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                this.touchPointerId = motionEvent.getPointerId(0);
                this.touchDownX = motionEvent.getX();
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (!this.isMoveNext) {
                    int count = this.viewPager.getAdapter().getCount();
                    int width = getWidth();
                    float f = ((float) width) / 2.0f;
                    float f2 = ((float) width) / 6.0f;
                    if (this.currentPagePos <= 0 || motionEvent.getX() >= f - f2) {
                        if (this.currentPagePos < count - 1 && motionEvent.getX() > f2 + f) {
                            if (motionEvent.getAction() == MotionEvent.ACTION_CANCEL) {
                                return true;
                            }
                            this.viewPager.setCurrentItem(this.currentPagePos + 1);
                            return true;
                        }
                    } else if (motionEvent.getAction() == MotionEvent.ACTION_CANCEL) {
                        return true;
                    } else {
                        this.viewPager.setCurrentItem(this.currentPagePos - 1);
                        return true;
                    }
                }
                this.isMoveNext = false;
                this.touchPointerId = -1;
                if (!this.viewPager.isFakeDragging()) {
                    return true;
                }
                this.viewPager.endFakeDrag();
                return true;
            case MotionEvent.ACTION_MOVE:
                float x = motionEvent.getX(motionEvent.findPointerIndex(this.touchPointerId));
                float delta = x - this.touchDownX;
                if (!this.isMoveNext && Math.abs(delta) > ((float) this.thresholder)) {
                    this.isMoveNext = true;
                }
                if (!this.isMoveNext) {
                    return true;
                }
                this.touchDownX = x;
                if (!this.viewPager.isFakeDragging() && !this.viewPager.beginFakeDrag()) {
                    return true;
                }
                this.viewPager.fakeDragBy(delta);
                return true;
            case MotionEvent.ACTION_POINTER_1_DOWN:
                pointerIndex = MotionEventCompat.getActionIndex(motionEvent);
                this.touchDownX = motionEvent.getX(pointerIndex);
                this.touchPointerId = motionEvent.getPointerId(pointerIndex);
                return true;
            case MotionEvent.ACTION_POINTER_1_UP:
                int action = MotionEventCompat.getActionIndex(motionEvent);
                if (motionEvent.getPointerId(action) == this.touchPointerId) {
                    if (action == MotionEvent.ACTION_DOWN) {
                        pointerIndex = 1;
                    }
                    this.touchPointerId = motionEvent.getPointerId(pointerIndex);
                }
                this.touchDownX = motionEvent.getX(motionEvent.findPointerIndex(this.touchPointerId));
                return true;
            default:
                return true;
        }
    }

    public void setViewPager(ViewPager viewPager) {
        if (this.viewPager != viewPager) {
            if (this.viewPager != null) {
                this.viewPager.removeOnPageChangeListener(this);
            }
            if (viewPager.getAdapter() == null) {
                throw new IllegalStateException("ViewPager does not have adapter instance.");
            }
            this.viewPager = viewPager;
            this.viewPager.addOnPageChangeListener(this);
            invalidate();
        }
    }

    public void setViewPager(ViewPager viewPager, int i) {
        setViewPager(viewPager);
        setCurrentItem(i);
    }

    public void setCurrentItem(int i) {
        if (this.viewPager == null) {
            throw new IllegalStateException("ViewPager has not been bound.");
        }
        this.viewPager.setCurrentItem(i);
        this.currentPagePos = i;
        invalidate();
    }

    public void notifyDataSetChanged() {
        invalidate();
    }

    public void onPageScrollStateChanged(int i) {
        this.scrollState = i;
        if (this.pageChangeListener != null) {
            this.pageChangeListener.onPageScrollStateChanged(i);
        }
    }

    public void onPageScrolled(int i, float f, int i2) {
        this.currentPagePos = i;
        this.positionOffset = f;
        invalidate();
        if (this.pageChangeListener != null) {
            this.pageChangeListener.onPageScrolled(i, f, i2);
        }
    }

    public void onPageSelected(int i) {
        if (this.isSnap || this.scrollState == 0) {
            this.currentPagePos = i;
            this.prevPagePos = i;
            invalidate();
        }
        if (this.pageChangeListener != null) {
            this.pageChangeListener.onPageSelected(i);
        }
    }

    public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        this.pageChangeListener = onPageChangeListener;
    }

    protected void onMeasure(int i, int i2) {
        if (this.orientation == 0) {
            setMeasuredDimension(m8244a(i), m8245b(i2));
        } else {
            setMeasuredDimension(m8245b(i), m8244a(i2));
        }
    }

    private int m8244a(int i) {
        int mode = MeasureSpec.getMode(i);
        int size = MeasureSpec.getSize(i);
        if (mode == 1073741824 || this.viewPager == null) {
            return size;
        }
        int count = this.viewPager.getAdapter().getCount();
        count = (int) (((((float) (count - 1)) * this.spacing) + (((float) (getPaddingLeft() + getPaddingRight())) + (((float) (count * 2)) * this.radius))) + 1.0f);
        if (mode == Integer.MIN_VALUE) {
            return Math.min(count, size);
        }
        return count;
    }

    private int m8245b(int i) {
        int mode = MeasureSpec.getMode(i);
        int size = MeasureSpec.getSize(i);
        if (mode == 1073741824) {
            return size;
        }
        int paddingTop = (int) ((((2.0f * this.radius) + ((float) getPaddingTop())) + ((float) getPaddingBottom())) + 1.0f);
        return mode == Integer.MIN_VALUE ? Math.min(paddingTop, size) : paddingTop;
    }

    public void onRestoreInstanceState(Parcelable parcelable) {
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        this.currentPagePos = savedState.itemPos;
        this.prevPagePos = savedState.itemPos;
        requestLayout();
    }

    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.itemPos = this.currentPagePos;
        return savedState;
    }
}
