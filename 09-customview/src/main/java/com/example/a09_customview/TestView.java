package com.example.a09_customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class TestView extends View {

    public static final String KEY_TEXT = "key_text";
    public static final String INSTANCE = "instance";
    private String testString = "test";
    private Paint mPaint;

    public TestView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        initPaint();

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TestView);

        ta.getBoolean(R.styleable.TestView_test_boolean, false);
        ta.getDimension(R.styleable.TestView_test_dimension, 150);
        ta.getInt(R.styleable.TestView_test_enum, 1);
        ta.getInteger(R.styleable.TestView_test_integer, -1);
        testString = ta.getString(R.styleable.TestView_test_string);

        for (int i = 0; i < ta.getIndexCount(); i++) {
            switch (ta.getIndex(i)) {
                case R.styleable.TestView_test_string:
                    testString = ta.getString(R.styleable.TestView_test_string);
                    break;
                default:
                    break;
            }
        }

        ta.recycle();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE); // 空心与实心
        mPaint.setStrokeWidth(6); // 粗细
        mPaint.setColor(0xFFFF0000); // 颜色
        mPaint.setAntiAlias(true); // ??
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int width = 0;
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            int needWidth = measureWidth() + getPaddingLeft() + getPaddingRight();

            if (widthMode == MeasureSpec.AT_MOST) {
                width = Math.min(needWidth, widthSize);
            } else {
                width = needWidth;
            }
        }

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int height = 0;
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            int needHeight = measureHeight() + getPaddingTop() + getPaddingBottom();

            if (heightMode == MeasureSpec.AT_MOST) {
                height = Math.min(needHeight, heightSize);
            } else {
                height = needHeight;
            }
        }

        setMeasuredDimension(width, height);
    }

    private int measureHeight() {
        return 0;
    }

    private int measureWidth() {
        return 0;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setTextSize(72);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(0);
        canvas.drawText(testString, 0, testString.length(), 0, getHeight(), mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        testString = "8888";
        invalidate();

        return true;
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TEXT, testString);
        bundle.putParcelable(INSTANCE, super.onSaveInstanceState());
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            Parcelable parcelable = bundle.getParcelable(INSTANCE);
            super.onRestoreInstanceState(parcelable);

            testString = bundle.getString(KEY_TEXT);
            return;
        }
        super.onRestoreInstanceState(state);
    }
}
