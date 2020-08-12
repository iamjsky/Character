package com.character.microblogapp.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.character.microblogapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomTextView extends LinearLayout {
    static final String TAG = CustomTextView.class.getSimpleName();

    @BindView(R.id.textview)
    TextView textView;

    private String mText;

    public CustomTextView(Context context) {
        this(context, null);
    }

    public CustomTextView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public CustomTextView(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);

        TypedArray a = context.obtainStyledAttributes(attributeSet, R.styleable.CustomTextView, defStyle, 0);

        mText = a.getString(R.styleable.CustomTextView_text);

        a.recycle();

        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.layout_custom_textview, this);

        ButterKnife.bind(this);

        initUI();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    private void initUI() {
        textView.setText(mText);

    }
}
