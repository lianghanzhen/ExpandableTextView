package com.lianghanzhen;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public class ExpandableTextView extends TextView {

    private static final int INVALID_HEIGHT = -1;
    private static final int DEFAULT_COLLAPSE_LINES = 3;

    private int mOriginalHeight = INVALID_HEIGHT;
    private boolean mViewExpandable = true;
    private boolean mViewExpanded;
    private int mCollapseLines = DEFAULT_COLLAPSE_LINES;
    private int mCollapseHeight = INVALID_HEIGHT;
    private boolean mRequestChangeCollapseLines;

    // listeners
    private OnClickListener mExtraOnClickListener;
    private OnExpandListener mOnExpandListener;

    private final OnClickListener mInternalOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            mViewExpanded = !mViewExpanded;
            if (mOnExpandListener != null) {
                mOnExpandListener.onExpand(ExpandableTextView.this, mViewExpanded);
            }
            requestLayout();
            if (mExtraOnClickListener != null)
                mExtraOnClickListener.onClick(view);
        }
    };

    public ExpandableTextView(Context context) {
        super(context);

        initExpandableTextView(context, null);
    }

    public ExpandableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initExpandableTextView(context, attrs);
    }

    public ExpandableTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        initExpandableTextView(context, attrs);
    }

    private void initExpandableTextView(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ExpandableTextView);
            mViewExpandable = ta.getBoolean(R.styleable.ExpandableTextView_expandable, true);
            mViewExpanded = ta.getBoolean(R.styleable.ExpandableTextView_expanded, false);
            mCollapseLines = ta.getInt(R.styleable.ExpandableTextView_collapse_lines, DEFAULT_COLLAPSE_LINES);
            ta.recycle();
        }

        setClickable(true);
        setInternalOnClickListener();
    }

    private void setInternalOnClickListener() {
        if (mViewExpandable) {
            setOnClickListener(mInternalOnClickListener);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mOriginalHeight == INVALID_HEIGHT) {
            mOriginalHeight = getMeasuredHeight();
        }
        final int originalHeightMode = MeasureSpec.getMode(heightMeasureSpec);
        final int originalWidth = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(originalWidth, mOriginalHeight);
        if (!mViewExpandable || mViewExpanded) {
            return;
        }

        if (mCollapseHeight == INVALID_HEIGHT || mRequestChangeCollapseLines) {
            setLines(mCollapseLines);
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            mCollapseHeight = getMeasuredHeight();
            setLines(Integer.MAX_VALUE);
            mRequestChangeCollapseLines = false;
        }

        if (mOriginalHeight < mCollapseHeight) {
            super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(mOriginalHeight, originalHeightMode));
            setMeasuredDimension(originalWidth, mOriginalHeight);
        } else {
            if (!mViewExpanded) {
                super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(mCollapseHeight, originalHeightMode));
                setMeasuredDimension(originalWidth, mCollapseHeight);
            }
        }
    }

    public void setCollapseLines(int collapseLines) {
        mCollapseLines = collapseLines;
        mRequestChangeCollapseLines = true;
        requestLayout();
    }

    public void setViewExpandable(boolean viewExpandable) {
        mViewExpandable = viewExpandable;
        setInternalOnClickListener();
        requestLayout();
    }

    public void collapse() {
        if (mViewExpandable && mViewExpanded) {
            performClick();
        }
    }

    public void expand() {
        if (mViewExpandable && !mViewExpanded) {
            performClick();
        }
    }

    public void toggle() {
        if (mViewExpanded) {
            collapse();
        } else {
            expand();
        }
    }

    /**
     * if you want to set View.OnClickListener, call this method instead of setOnClickListener()
     * @param extraOnClickListener extra OnClickListener
     */
    public void setExtraOnClickListener(OnClickListener extraOnClickListener) {
        mExtraOnClickListener = extraOnClickListener;
    }

    public void setOnExpandListener(OnExpandListener onExpandListener) {
        mOnExpandListener = onExpandListener;
    }

    @Override
    public void setOnClickListener(OnClickListener listener) {
        listener = mInternalOnClickListener;
        super.setOnClickListener(listener);
    }

    public interface OnExpandListener {
        void onExpand(ExpandableTextView view, boolean isExpanded);
    }

}
