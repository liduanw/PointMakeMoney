package com.pwyql.pointmakemoney.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class CustomViewPager extends android.support.v4.view.ViewPager {
    public CustomViewPager(Context context) {
	super(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
	super(context, attrs);
    }
    
    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        // TODO Auto-generated method stub
        return false;//super.onInterceptTouchEvent(arg0);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
	// TODO Auto-generated method stub

	return super.dispatchTouchEvent(ev);
    }

//    @Override
//    protected boolean canScroll(View arg0, boolean arg1, int arg2, int arg3, int arg4) {
//        // TODO Auto-generated method stub
//        return false;//super.canScroll(arg0, arg1, arg2, arg3, arg4);
//    }
    
    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        // TODO Auto-generated method stub
        return false;//super.onTouchEvent(arg0);
    }

}
