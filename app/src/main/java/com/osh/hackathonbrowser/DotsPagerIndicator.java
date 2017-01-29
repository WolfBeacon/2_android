package com.osh.hackathonbrowser;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * A custom View that enables indicators for a ViewPager. Remember to call
 * {@linkplain DotsPagerIndicator#attachViewPager(ViewPager)}.
 */
public class DotsPagerIndicator extends View implements ViewPager.OnPageChangeListener {
    private Paint lightPaint;
    private Paint darkPaint;
    private ViewPager pager;

    private int circleWidth;
    private int gapWidth;

    public void init(){
        lightPaint = new Paint();
        lightPaint.setAntiAlias(true);
        lightPaint.setColor(getResources().getColor(R.color.white));
        lightPaint.setStyle(Paint.Style.FILL);
        lightPaint.setAlpha(127);

        darkPaint = new Paint();
        darkPaint.setAntiAlias(true);
        darkPaint.setColor(getResources().getColor(R.color.white));
        darkPaint.setStyle(Paint.Style.FILL);
        darkPaint.setAlpha(255);

        circleWidth = (int) getResources().getDimension(R.dimen.indicator_circle_width);
        gapWidth = (int) getResources().getDimension(R.dimen.indicator_circle_gap_width);
    }

    public DotsPagerIndicator(Context context) {
        super(context);
        init();
    }

    public DotsPagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DotsPagerIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(!hasPager()) return;

        //Total draw space is [width of view / (number of elements * 1.5)]
        int numCircles = pager.getChildCount();
        int currentCircle = pager.getCurrentItem();

        int totalWidth = numCircles == 1 ? circleWidth : (circleWidth * numCircles) + (gapWidth * (numCircles - 1));
        int startX = (getWidth() - totalWidth) / 2;
        for(int i = 0; i < numCircles; i++){
            canvas.drawCircle(startX + (circleWidth / 2), circleWidth / 2, circleWidth / 2,
                    i == currentCircle ? darkPaint : lightPaint);
            startX += circleWidth;
            startX += gapWidth;
        }
    }

    private boolean hasPager(){
        return pager != null;
    }

    /**
     * Attach the ViewPager to enable the dots view.
     * @param pager A ViewPager.
     */
    public void attachViewPager(ViewPager pager){
        this.pager = pager;
        pager.addOnPageChangeListener(this);
        invalidate();
    }

    public void detachViewPager(){
        pager.removeOnPageChangeListener(this);
        this.pager = null;
        invalidate();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        invalidate();
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
}
