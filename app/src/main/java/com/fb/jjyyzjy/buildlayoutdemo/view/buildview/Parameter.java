package com.fb.jjyyzjy.buildlayoutdemo.view.buildview;

/**
 * Created by JJYYZJY on 2017/6/6.
 */
public class Parameter {

    private int pageIndex;
    private int itemPosition;

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getItemPosition() {
        return itemPosition;
    }

    public void setItemPosition(int itemPosition) {
        this.itemPosition = itemPosition;
    }

    @Override
    public String toString() {
        return "Parameter{" +
                "pageIndex=" + pageIndex +
                ", itemPosition=" + itemPosition +
                '}';
    }
}
