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
import android.util.Log;
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

    //中心X和Y坐标
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


    private int mMaxRadies;


    private Bitmap mCheckBitmp;

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
            mCheckBitmp = bitmapDrawable.getBitmap();
//            mMaxRadies = mCheckBitmp.getWidth() / 2;
//            Log.v("test", "mCheckBitmp:=" + mMaxRadies);
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


//        mCheckBitmp = BitmapFactory.decodeResource(getResources(),R.mipmap.)
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mCenterX = getWidth() / 2;
        mCenterY = getHeight() / 2;

        mMaxRadies = getWidth() > getHeight() ? getWidth() : getHeight();
        Log.v("test", "mMaxRadies" + mMaxRadies);

        if (mCheckBitmp == null) {
            return;
        }
        mPath.addCircle(mCenterX, mCenterY, mRadies / 2 + 2, Path.Direction.CW);
        canvas.clipPath(mPath, Region.Op.INTERSECT);
        mPaint.setAlpha(mAlpha);
        canvas.drawCircle(mCenterX, mCenterY, mCheckBitmp.getWidth() / 2, mPaint);
        canvas.drawBitmap(mCheckBitmp, mCenterX - mCheckBitmp.getWidth() / 2, mCenterY - mCheckBitmp.getHeight() / 2, mPaint);
    }

    /**
     * 是否选中
     *
     * @param isCheck
     */
    public void check(boolean isCheck) {
        if (isCheck) {
            mAlpha = 255;
            mRadies = 0;
            mPath.reset();
            Log.v("test", "mMaxRadies:=" + mMaxRadies);
            ValueAnimator valueAnimator = ValueAnimator.ofInt(0, mMaxRadies);
            valueAnimator.setDuration(300);
            valueAnimator.setInterpolator(new AccelerateInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mRadies = (int) animation.getAnimatedValue();
                    invalidate();
                }
            });
            valueAnimator.start();
        } else {
            ValueAnimator valueAnimator = ValueAnimator.ofInt(mAlpha, 0);
            valueAnimator.setDuration(300);
            valueAnimator.setInterpolator(new AccelerateInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mAlpha = (int) animation.getAnimatedValue();
                    invalidate();
                }
            });
            valueAnimator.start();
        }
    }
}
