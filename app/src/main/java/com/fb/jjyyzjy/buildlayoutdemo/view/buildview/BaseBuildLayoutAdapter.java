package com.fb.jjyyzjy.buildlayoutdemo.view.buildview;

import android.view.View;
import android.view.ViewGroup;

/**
 * 动态布局——布局中item适配器
 * Created by ZJY on 2016/5/3.
 */
public abstract class BaseBuildLayoutAdapter {

    private Parameter parameter;

    public abstract int[] getLayout(int position);

    public abstract int getCount();

    public abstract Object getItem(int position);

    public abstract long getItemId(int position);

    public int getItemViewType(int position){
        return 0;
    }

    public abstract View getView(int position, View contentView,
                                 ViewGroup parent);

    /**
     * Set some parameters
     * @param parameter
     */
    public void setParameter(Parameter parameter){
        this.parameter = parameter;
    }

    /**
     * Get some parameters
     */
    public Parameter getParameter(){
        return parameter;
    }

    private BuildAdapterDataObserver observer;

    public void registerAdapterDataObserver(BuildAdapterDataObserver observer) {
        this.observer = observer;
    }

    public void unregisterAdapterDataObserver(BuildAdapterDataObserver observer) {
        this.observer = null;
    }

    public final void notifyDataSetChanged() {
        observer.onChanged();
    }

}
