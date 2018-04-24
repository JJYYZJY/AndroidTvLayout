package com.fb.jjyyzjy.buildlayoutdemo.view.bring;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by JJYYZJY on 2017/9/7.
 */
public class BringToFrontRelative extends RelativeLayout {
    public BringToFrontRelative(Context context) {
        super(context);
        mInit();
    }

    public BringToFrontRelative(Context context, AttributeSet attrs) {
        super(context, attrs);
        mInit();
    }

    public BringToFrontRelative(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mInit();
    }

    public BringToFrontRelative(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mInit();
    }

    private BringToFrontHelper bringToFrontHelper;

    private void mInit() {
        setWillNotDraw(true);
        setChildrenDrawingOrderEnabled(true);
        bringToFrontHelper = new BringToFrontHelper();
    }


    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        return bringToFrontHelper.getChildDrawingOrder(childCount,i);
    }

    @Override
    public void bringChildToFront(View child) {
        bringToFrontHelper.bringChildToFront(this,child);
    }

}
