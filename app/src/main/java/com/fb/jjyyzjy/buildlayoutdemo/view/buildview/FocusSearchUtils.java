package com.fb.jjyyzjy.buildlayoutdemo.view.buildview;

import android.view.KeyEvent;
import android.view.View;

import com.fb.jjyyzjy.buildlayoutdemo.R;
import com.fb.jjyyzjy.buildlayoutdemo.utils.DebugLog;


/**
 * Created by JJYYZJY on 2017/5/3.
 */
public class FocusSearchUtils {

    private static final boolean DEBUG = true;

    public static void bindFocusSearch(View viewLeft, View viewRight){
        if (DEBUG) DebugLog.i("bindFocusSearch viewLeft : "+viewLeft+", viewRight : "+viewRight);
        if (viewLeft == null || viewRight == null){
            return;
        }
        viewLeft.setTag(R.id.key_tag_focus_has_next_left,viewRight);
        viewRight.setTag(R.id.key_tag_focus_has_next_right,viewLeft);
    }

    public static void bindFocusSearch(View[] viewLeft, View[] viewRight){
        if (DEBUG) DebugLog.i("bindFocusSearch viewLeft[] : "+viewLeft+", viewRight[] : "+viewRight);
        if (viewLeft == null || viewRight == null){
            return;
        }
        int count = viewLeft.length > viewRight.length ? viewLeft.length : viewRight.length;
        for (int i = 0 ; i < count ; i++){
            bindFocusSearch(viewLeft[i >= viewLeft.length ? viewLeft.length-1 : i]
                    ,viewRight[i >= viewRight.length ? viewRight.length-1 : i]);
        }
    }

    public static void unBindFocus(View viewLeft, View viewRight){
        if (viewLeft == null || viewRight == null){
            return;
        }
        viewLeft.setTag(R.id.key_tag_focus_has_next_left,null);
        viewRight.setTag(R.id.key_tag_focus_has_next_right,null);
    }

    public static void unBindFocus(View view){
        if (view == null){
            return;
        }
        view.setTag(R.id.key_tag_focus_has_next_left,null);
        view.setTag(R.id.key_tag_focus_has_next_right,null);
    }

    public static boolean requestFocusSearch(View focus, int direction){
        if (DEBUG) DebugLog.i("requestFocusSearch direction : "+direction+",focus : "+focus);
        if (focus == null){
            return false;
        }
        if (direction == View.FOCUS_LEFT){
            final View tag = (View) focus.getTag(R.id.key_tag_focus_has_next_left);
            if (tag != null){
                final View focusNext = tag.focusSearch(View.FOCUS_UP);
                if (DEBUG) DebugLog.i("FOCUS_LEFT focusNext : "+focusNext + "\n tag : "+tag);
                if (focusNext != null && focusNext != tag){
                    boolean is = focusNext.requestFocus();
                    DebugLog.w("FOCUS_LEFT isRequest : " + is);
                }
                return true;
            }
        }else if (direction == View.FOCUS_RIGHT){
            final View tag = (View) focus.getTag(R.id.key_tag_focus_has_next_right);
            if (tag != null){
                final View focusNext = tag.focusSearch(View.FOCUS_DOWN);
                if (DEBUG) DebugLog.i("FOCUS_RIGHT focusNext : "+focusNext + "\n tag : "+tag);
                if (focusNext != null && focusNext != tag) {
                    boolean is = focusNext.requestFocus();
                    DebugLog.w("FOCUS_RIGHT isRequest : " + is);
                }
                return true;
            }
        }
        return false;
    }

    public static boolean dispatchKeyEvent(View mRootView, KeyEvent event){
        if (DEBUG) DebugLog.i("dispatchKeyEvent event : " + event);
        if (event.getAction() == KeyEvent.ACTION_DOWN){
            if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT){
                final View focus = mRootView != null ? mRootView.findFocus() : null;
                if (focus != null){
                    if (requestFocusSearch(focus, View.FOCUS_LEFT)){
                        return true;
                    }
                }
            }else if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT){
                final View focus = mRootView != null ? mRootView.findFocus() : null;
                if (focus != null){
                    if (requestFocusSearch(focus, View.FOCUS_RIGHT)){
                        return true;
                    }
                }
            }
        }
        return false;
    }


}
