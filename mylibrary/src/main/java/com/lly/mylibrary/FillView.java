package com.lly.mylibrary;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;

/**
 * FillView[v 1.0.0]
 * classes:com.lly.mylibrary.FillView
 *
 * @author lileiyi
 * @date 2017/12/29
 * @time 16:21
 * @description
 */

public class FillView extends ImageView {

    private Paint mPaint;

    private int mCenterX;
    private int mCenterY;

    /**
     * 裁剪路径
     */
    private Path mPath;
    /**
     * 裁剪范围
     */
    private int mRange;

    /**
     * 透明度
     */
    private int mAlpha = 255;

    /**
     * 扩散半径
     */
    private int mRadies;

    /**
     * 最大扩散半径
     */
    private int mMaxRadies;

    /**
     * 动画持续时间
     */
    private int mDuration = 350;


    /**
     * 选中的图片
     */
    private Bitmap mCheckBitmap;

    /**
     * 是否选中
     */
    private boolean mChecked = false;

    /**
     * 是否绘制完成
     */
    private boolean isDrawComplete;

    /**
     * 是否首次
     */
    private boolean isFirst = true;

    public FillView(Context context) {
        this(context, null);
    }

    public FillView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init();
    }

    public FillView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.fill__view_styleable);
        //未选中显示的Drawable
        Drawable unCheckDrawable = typedArray.getDrawable(R.styleable.fill__view_styleable_unCheckImage);
        if (unCheckDrawable != null) {
            setImageDrawable(unCheckDrawable);
        }
        //选中显示的Drawable
        Drawable checkDrawable = typedArray.getDrawable(R.styleable.fill__view_styleable_checkImage);
        BitmapDrawable bitmapDrawable = (BitmapDrawable) checkDrawable;
        if (bitmapDrawable != null) {
            mCheckBitmap = bitmapDrawable.getBitmap();
        }
        typedArray.recycle();
    }


    /**
     * 初始化View
     */
    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.parseColor("#f2f2f2"));
        mPath = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mCenterX = getWidth() / 2;
        mCenterY = getHeight() / 2;

        mMaxRadies = getWidth() > getHeight() ? getWidth() : getHeight();

        if (mCheckBitmap == null) {
            return;
        }
        if (mRadies > 0) {
            mRadies += 2;
        }
        mPath.addCircle(mCenterX, mCenterY, Math.round(mRadies / 2f), Path.Direction.CW);
        canvas.clipPath(mPath, Region.Op.INTERSECT);
        mPaint.setAlpha(mAlpha);
        canvas.drawCircle(mCenterX, mCenterY, mCheckBitmap.getWidth() / 2, mPaint);
        canvas.drawBitmap(mCheckBitmap, mCenterX - Math.round(mCheckBitmap.getWidth() / 2f), mCenterY - Math.round(mCheckBitmap.getHeight() / 2f), mPaint);
        isDrawComplete = true;

        if (isChecked() && isFirst) {
            isFirst = false;
            refreshState();
        }
//        }
    }


    /**
     * 是否选中
     *
     * @param isCheck
     */
    public void check(boolean isCheck) {
        if (mChecked != isCheck) {
            mChecked = isCheck;
            if (isDrawComplete) {
                refreshState();
            }
        }
    }


    /**
     * 是否选中
     */
    public boolean isChecked() {
        return mChecked;
    }

    /**
     * 刷新view状态
     */
    private void refreshState() {
        if (mChecked) {
            reset();
        }
        ValueAnimator valueAnimator = mChecked ? ValueAnimator.ofInt(0, mMaxRadies) : ValueAnimator.ofInt(mAlpha, 0);
        valueAnimator.setDuration(mDuration);
        valueAnimator.setInterpolator(new AccelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (mChecked) {
                    mRadies = (int) animation.getAnimatedValue();
                } else {
                    mAlpha = (int) animation.getAnimatedValue();
                }
                invalidate();
            }
        });
        valueAnimator.start();
    }

    /**
     * 重置view状态
     */
    private void reset() {
        mAlpha = 255;
        mRadies = 0;
        mPath.reset();
    }


}
