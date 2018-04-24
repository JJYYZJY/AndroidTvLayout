package com.fb.jjyyzjy.buildlayoutdemo.view.buildview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;

import com.fb.jjyyzjy.buildlayoutdemo.utils.DebugLog;

/**
 * 动态表格布局
 * @author JJYYZJY
 */
public class MyBuildLayout extends BuildLayout {

    public MyBuildLayout(Context context) {
        super(context);
        init();
    }

    public MyBuildLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyBuildLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        windowWidth = windowManager.getDefaultDisplay().getWidth();
    }

    /**
     * 焦点出界监听
     */
    private OnDismissFocusListener2 dismissListener;
    private OnOutOfBoundListener outOfBoundListener;
    private int windowWidth;

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (DEBUG_CAN) DebugLog.i("dispatchKeyEvent ACTION_DOWN");
            View focus = findFocus();
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    if (focus.getBottom() == getHeight()) {
                        if (dismissListener != null) {
                            if (dismissListener.onDismissForBot(this)) {
                                return true;
                            }
                        }
                    } else {
                        if (focusSearchWithTag(focus.getTag(), FOCUS_DOWN)) {
                            return true;
                        }
                    }
                    break;
                case KeyEvent.KEYCODE_DPAD_UP:
                    if (focus.getTop() == 0) {
                        if (dismissListener != null) {
                            if (dismissListener.onDismissForTop(this)) {
                                return true;
                            }
                        }
                    } else {
                        if (focusSearchWithTag(focus.getTag(), FOCUS_UP)) {
                            return true;
                        }
                    }
                    break;
                case KeyEvent.KEYCODE_DPAD_LEFT:
                    if (focus.getLeft() == 0) {
                        if (dismissListener != null) {
                            if (dismissListener.onDismissForLeft(this)) {
                                return true;
                            }
                        }
                    } else {
                        if (outOfBoundListener != null) {
                            int[] location = new int[2];
                            focus.getLocationInWindow(location);
//							 MyLog.v("focus.getLeft()",""+focus.getLeft());
//							 MyLog.v("location[0]",""+location[0]);
//							 MyLog.v("itemWidth",""+itemWidth);
//							 MyLog.v("dividing",""+dividing);
                            if (focus.getLeft() > location[0] && location[0] < itemWidth + dividing) {
                                if (DEBUG_CAN) DebugLog.i("outOfBoundListener ok");
                                outOfBoundListener.onOutForLeft(focus);
                            }
                        }

                        if (focusSearchWithTag(focus.getTag(), FOCUS_LEFT)) {
                            return true;
                        }

                    }
                    break;
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    if (focus.getRight() == getWidth()) {
                        if (dismissListener != null) {
                            if (dismissListener.onDismissForRight(this)) {
                                return true;
                            }
                        }
                    } else {
                        if (outOfBoundListener != null) {

                            int[] location = new int[2];
                            focus.getLocationInWindow(location);

//							 MyLog.v("location[0] + itemWidth + dividing + itemWidth",""+ (location[0] + itemWidth + dividing + itemWidth));

                            if (location[0] + itemWidth + dividing + itemWidth > windowWidth) {
                                if (DEBUG_CAN) DebugLog.i("outOfBoundListener ok");
                                outOfBoundListener.onOutForRight(focus);
                            }
                        }

                        if (focusSearchWithTag(focus.getTag(), FOCUS_RIGHT)) {
                            return true;
                        }

                    }
                    break;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    private boolean focusSearchWithTag(Object tag, int direction) {
        if (DEBUG_CAN) DebugLog.i(TAG + " focusSearchWithTag");
        if (tag instanceof int[]) {
            int[] layout = (int[]) tag;
            if (DEBUG_CAN)
                DebugLog.i(TAG + " focusSearchWithTag tag(layout) : [" + layout[0] + "," + layout[1] + "," + layout[2] + "," + layout[3] + "] direction : " + direction);
            switch (direction) {
                case FOCUS_UP:
                    if (layout[2] >= 1) {
                        View nextFocus = findViewWithFuzzyLayout(layout[0], layout[1] - 1, layout[3], FOCUS_UP);
                        if (nextFocus != null) {
                            return nextFocus.requestFocus();
                        }
                    }
                    break;
                case FOCUS_DOWN:
                    if (layout[2] >= 1) {
                        View nextFocus = findViewWithFuzzyLayout(layout[0], layout[1] + layout[3], layout[2], FOCUS_DOWN);
                        if (nextFocus != null) {
                            return nextFocus.requestFocus();
                        }
                    }
                    break;
                case FOCUS_LEFT:
                    if (layout[3] >= 1) {
                        View nextFocus = findViewWithFuzzyLayout(layout[0] - 1, layout[1], layout[3], FOCUS_LEFT);
                        if (nextFocus != null) {
                            return nextFocus.requestFocus();
                        }
                    }
                    break;
                case FOCUS_RIGHT:
                    if (layout[3] >= 1) {
                        View nextFocus = findViewWithFuzzyLayout(layout[0] + layout[2], layout[1], layout[3], FOCUS_RIGHT);
                        if (nextFocus != null) {
                            return nextFocus.requestFocus();
                        }
                    }
                    break;
            }
        }
        if (DEBUG_CAN) DebugLog.i(TAG + " focusSearchWithTag false");
        return false;
    }

    private View findViewWithFuzzyLayout(int x, int y, int range, int direction) {
        if (DEBUG_CAN)
            DebugLog.i(TAG + " findViewWithFuzzyLayout [x=" + x + ",y=" + y + ",range=" + range + ",direction=" + direction + "]");
        if (cViewTree == null || x < 0 || y < 0 || y >= cViewTree.length || x >= cViewTree[y].length) {
            if (DEBUG_CAN) DebugLog.w(TAG + " findViewWithFuzzyLayout parameter err");
            return null;
        }
        View nextView = null;
        switch (direction) {
            case FOCUS_UP:
                while (nextView == null && y >= 0) {
                    int flag = 0;
                    while (nextView == null && range > flag) {
                        nextView = cViewTree[y][x + flag];
                        if (DEBUG_CAN) {
                            if (nextView != null)
                                DebugLog.i("cViewTree[" + y + "][" + (x + flag) + "]");
                        }
                        flag++;
                    }
                    y--;
                }
                break;
            case FOCUS_DOWN:
                while (nextView == null && y >= 0 && y < cViewTree.length) {
                    int flag = 0;
                    while (nextView == null && range > flag) {
                        nextView = cViewTree[y][x + flag];
                        if (DEBUG_CAN) {
                            if (nextView != null)
                                DebugLog.i("cViewTree[" + y + "][" + (x + flag) + "]");
                        }
                        flag++;
                    }
                    y++;
                }
                break;
            case FOCUS_LEFT:
                while (nextView == null && x >= 0) {
                    int flag = 0;
                    while (nextView == null && range > flag) {
                        nextView = cViewTree[y + flag][x];
                        if (DEBUG_CAN) {
                            if (nextView != null)
                                DebugLog.i("cViewTree[" + (y + flag) + "][" + x + "]");
                        }
                        flag++;
                    }
                    x--;
                }
                break;
            case FOCUS_RIGHT:
                while (nextView == null && x >= 0 && x < cViewTree[y].length) {
                    int flag = 0;
                    while (nextView == null && range > flag) {
                        nextView = cViewTree[y + flag][x];
                        if (DEBUG_CAN) {
                            if (nextView != null)
                                DebugLog.i("cViewTree[" + (y + flag) + "][" + x + "]");
                        }
                        flag++;
                    }
                    x++;
                }
                break;
        }
        if (DEBUG_CAN) DebugLog.i(TAG + " findViewWithFuzzyLayout nextView : " + nextView);
        return nextView;
    }


    public View[] getLeftChildTree() {
        if (cViewTree != null) {
            View[] views = new View[cViewTree.length];
            for (int i = 0; i < cViewTree.length; i++) {
                if (cViewTree[i].length > 0) {
                    views[i] = cViewTree[i][0];
                }
            }
            return views;
        }
        return null;
    }

    public View[] getRightChildTree() {
        DebugLog.i("getRightChildTree cViewTree : " + cViewTree.toString() + ",cViewTree.length : " + cViewTree.length);
        if (cViewTree != null) {
            View[] views = new View[cViewTree.length];
            for (int i = 0; i < cViewTree.length; i++) {
                if (cViewTree[i].length > 0) {
                    views[i] = cViewTree[i][cViewTree[i].length - 1];
                    DebugLog.i("views[" + i + "] : " + views[i]);
                }
            }
            return views;
        }
        return null;
    }

    public void setOnDismissFocusListener(OnDismissFocusListener2 dismissListener) {
        this.dismissListener = dismissListener;
    }

    public void setOnOutOfBoundListener(OnOutOfBoundListener outOfBoundListener) {
        this.outOfBoundListener = outOfBoundListener;
    }

}
