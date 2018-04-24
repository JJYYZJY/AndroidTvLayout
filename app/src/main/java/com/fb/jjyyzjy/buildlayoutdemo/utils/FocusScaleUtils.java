package com.fb.jjyyzjy.buildlayoutdemo.utils;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import java.util.HashMap;
import java.util.Map;

/**
 * 焦点缩放工具类
 * Created by ZJY on 2016/6/7.
 * update on 2016/8/17 解决还原未执行完时放大失败问题
 */
public class FocusScaleUtils {
    private View oldView;
    /**
     * 放大用时
     */
    private int durationLarge = 300;
    /**
     * 缩小用时
     */
    private int durationSmall = 500;
    private float scale = 1.1f;
    private AnimatorSet animatorSet;
    private Interpolator interpolatorLarge;
    private Interpolator interpolatorSmall;
    private Map<View,Animator> animatorSetMap;

    public FocusScaleUtils() {
        this.interpolatorLarge = new AccelerateInterpolator(1.5f);
        this.interpolatorSmall = new DecelerateInterpolator(1.5f);
        init();
    }

    public FocusScaleUtils(int duration, float scale, Interpolator interpolator) {
        this(duration, duration, scale, interpolator, interpolator);
    }

    public FocusScaleUtils(int durationLarge, int durationSmall, float scale, Interpolator interpolatorLarge, Interpolator interpolatorSmall) {
        this.durationLarge = durationLarge;
        this.durationSmall = durationSmall;
        this.scale = scale;
        this.interpolatorLarge = interpolatorLarge;
        this.interpolatorSmall = interpolatorSmall;
        init();
    }

    private void init() {
        animatorSetMap = new HashMap<>();
    }

    /**
     * 放大指定view（view必须拥有焦点拥有焦点）
     * @param item
     */
    public void scaleToLarge(View item) {
        if (!item.isFocused()) {
            return;
        }
        scaleToLargeNotFocus(item);
    }

    /**
     * 放大指定view（不需要view拥有焦点）
     * @param item
     */
    public void scaleToLargeNotFocus(View item) {
        DebugLog.e("FocusScaleUtils__scaleToLargeNotFocus=="+item);
        DebugLog.e("FocusScaleUtils__animator_map.size=="+animatorSetMap.size());

        Animator animatorOld = animatorSetMap.get(item);
        if (animatorOld != null && animatorOld.isRunning()){
            animatorOld.cancel();
        }

        animatorSet = new AnimatorSet();

        ObjectAnimator largeX = ObjectAnimator.ofFloat(item, "ScaleX", 1f, scale);
        ObjectAnimator largeY = ObjectAnimator.ofFloat(item, "ScaleY", 1f,
                scale);
        animatorSet.setDuration(durationLarge);
        animatorSet.setInterpolator(interpolatorLarge);
        animatorSet.play(largeX).with(largeY);
        animatorSet.start();

        oldView = item;
    }

    /**
     * 还原指定view
     * @param item
     */
    public void scaleToNormal(final View item) {
        DebugLog.e("FocusScaleUtils__scaleToNormal=="+item);
        if (animatorSet == null || item == null) {
            return;
        }

        if (animatorSet.isRunning()) {
            animatorSet.cancel();
        }

        AnimatorSet animatorSetForNormal = new AnimatorSet();

        ObjectAnimator oa = ObjectAnimator.ofFloat(item, "ScaleX", 1f);
        ObjectAnimator oa2 = ObjectAnimator.ofFloat(item, "ScaleY", 1f);
        animatorSetForNormal.setDuration(durationSmall);
        animatorSetForNormal.setInterpolator(interpolatorSmall);
        animatorSetForNormal.play(oa).with(oa2);
        animatorSetForNormal.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                animatorSetMap.put(item,animation);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animatorSetMap.remove(item);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                animatorSetMap.remove(item);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSetForNormal.start();
        oldView = null;
    }

    /**
     * 还原上一次放大的view
     */
    public void scaleToNormal() {
        scaleToNormal(oldView);
    }
}