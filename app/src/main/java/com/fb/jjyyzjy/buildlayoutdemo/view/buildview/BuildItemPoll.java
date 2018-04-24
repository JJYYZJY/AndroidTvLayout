package com.fb.jjyyzjy.buildlayoutdemo.view.buildview;

import android.support.annotation.NonNull;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by JJYYZJY on 2017/9/18.
 */
public class BuildItemPoll/* extends RecyclerView.RecycledViewPool */{

    private SparseArray<ArrayList<View>> mScrap =
            new SparseArray<ArrayList<View>>();
    private SparseIntArray mMaxScrap = new SparseIntArray();
    private static final int DEFAULT_MAX_SCRAP = 5;

    public void clear() {
        mScrap.clear();
    }

    public void setMaxRecycledViews(int viewType, int max) {
        mMaxScrap.put(viewType, max);
        final ArrayList<View> scrapHeap = mScrap.get(viewType);
        if (scrapHeap != null) {
            while (scrapHeap.size() > max) {
                scrapHeap.remove(scrapHeap.size() - 1);
            }
        }
    }

    public View getBuildItemView(int viewType) {
        final ArrayList<View> scrapHeap = mScrap.get(viewType);
        if (scrapHeap != null && !scrapHeap.isEmpty()) {
            final int index = scrapHeap.size() - 1;
            final View scrap = scrapHeap.get(index);
            scrapHeap.remove(index);
            return scrap;
        }
        return null;
    }

    public void putBuildItemView(int viewType, View scrap) {
        getScrapHeapForType(viewType).add(scrap);
    }

    private ArrayList<View> getScrapHeapForType(int viewType) {
        ArrayList<View> scrap = mScrap.get(viewType);
        if (scrap == null) {
            scrap = new ArrayList<>();
            mScrap.put(viewType, scrap);
        }
        return scrap;
    }

    int size() {
        int count = 0;
        for (int i = 0; i < mScrap.size(); i ++) {
            ArrayList<View> items = mScrap.valueAt(i);
            if (items != null) {
                count += items.size();
            }
        }
        return count;
    }

    public void putAllItemView(@NonNull BuildItemPoll poll){

        for (int i = 0; i < poll.mScrap.size(); i ++) {
            final int key = poll.mScrap.keyAt(i);
            mScrap.append(key,poll.mScrap.get(key));
        }

        poll.clear();

    }

}
