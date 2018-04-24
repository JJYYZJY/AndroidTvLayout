package com.fb.jjyyzjy.buildlayoutdemo.view.buildview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.fb.jjyyzjy.buildlayoutdemo.R;
import com.fb.jjyyzjy.buildlayoutdemo.utils.DebugLog;
import com.fb.jjyyzjy.buildlayoutdemo.view.bring.BringToFrontRelative;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JJYYZJY on 2017/9/18.
 */
public class BuildLayout extends BringToFrontRelative {
    protected static final String TAG = "MyBuildLayout";
    protected static final boolean DEBUG_CAN = false;
    private List<View> childViews;
    protected View[][] cViewTree = null;
    private View defaultGetFocusView;//默认获取焦点的View
    private View defaultGetFocusViewLB;//默认获取焦点的View
    private View defaultGetFocusViewRT;//默认获取焦点的View
    private View defaultGetFocusViewRB;//默认获取焦点的View
    private BaseBuildLayoutAdapter buildLayoutAdapter;
    private boolean isDealWithFocus;//是否处理焦点，使其左右不能离开布局
    /**
     * 单元格宽
     */
    protected int itemWidth = (int) getResources().getDimension(R.dimen.px270);
    /**
     * 单元格高
     */
    protected int itemHeight = (int) getResources().getDimension(R.dimen.px180);
    /**
     * 列数
     */
    private int hSize;
    /**
     * 行数
     */
    private int vSize;
    /**
     * 单元格间隔
     */
    protected int dividing;

    private int widthP;
    private int heightP;

    public BuildLayout(Context context) {
        this(context,null);
    }

    public BuildLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BuildLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyBuildLayout);
            this.hSize = typedArray.getInteger(R.styleable.MyBuildLayout_myHSize, 0);
            this.vSize = typedArray.getInteger(R.styleable.MyBuildLayout_myVSize, 0);
            this.itemWidth = (int) typedArray.getDimension(R.styleable.MyBuildLayout_myItemWidth, this.itemWidth);
            this.itemHeight = (int) typedArray.getDimension(R.styleable.MyBuildLayout_myItemHeight, this.itemHeight);
            this.dividing = (int) typedArray.getDimension(R.styleable.MyBuildLayout_myDividing, 0);
            typedArray.recycle();
        }
        init();
    }

    private void init() {
        childViews = new ArrayList<>();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        for (int i = 0; i < childViews.size(); i++) {
            measureChild(childViews.get(i), widthMeasureSpec, heightMeasureSpec);
        }
        if (hSize == 0 || vSize == 0) {
            hSize = 5;
            vSize = 3;
        }
        if (itemWidth == 0 || itemHeight == 0) {
            itemWidth = (int) getResources().getDimension(R.dimen.px270);
            itemHeight = (int) getResources().getDimension(R.dimen.px180);
        }

        widthP = hSize * itemWidth + (hSize - 1) * dividing;
        heightP = vSize * itemHeight + (vSize - 1) * dividing;
        setMeasuredDimension(widthP, heightP);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
//        super.onLayout(changed, l, t, r, b);
        if (changed) {
            mySetChildLayout();
        }else {
            int cCount = childViews.size();
            int cWidth = 0;
            int cHeight = 0;
            for (int i = 0; i < cCount; i++) {
                View childView = childViews.get(i);
                cWidth = childView.getMeasuredWidth();
                cHeight = childView.getMeasuredHeight();
                int cl = 0, ct = 0, cr = 0, cb = 0;
                cl = childView.getLeft();
                ct = childView.getTop();
                cr = cl + cWidth;
                cb = cHeight + ct;
                childView.layout(cl, ct, cr, cb);
            }
        }
    }

    private void mySetChildLayout() {
//		MyLog.v(TAG, "onLayout==getChildCount=="+getChildCount());
//		MyLog.v(TAG, "onLayout==childViews.size()=="+childViews.size());
        if (DEBUG_CAN) DebugLog.i("vSize = " + vSize + ",hSize = " + hSize);
        cViewTree = new View[vSize][hSize];
        for (int i = 0; i < childViews.size(); i++) {

            int[] childLayout = buildLayoutAdapter.getLayout(i);
            if (childLayout.length < 4) {
                continue;
            }

            int v_r = childLayout[0] * itemWidth + childLayout[0] * dividing;
            int v_t = childLayout[1] * itemHeight + childLayout[1] * dividing;
            int v_w = childLayout[2] * itemWidth + (childLayout[2] - 1) * dividing;
            int v_h = childLayout[3] * itemHeight + (childLayout[3] - 1) * dividing;

            if (v_r == 0 && v_t == 0) {
                defaultGetFocusView = childViews.get(i);
            }

            if (v_r == 0 && (v_t + v_h) == heightP) {
                defaultGetFocusViewLB = childViews.get(i);
            }

            if ((v_r + v_w) == widthP && v_t == 0) {
                defaultGetFocusViewRT = childViews.get(i);
            }

            if ((v_r + v_w) == widthP && (v_t + v_h) == heightP) {
                defaultGetFocusViewRB = childViews.get(i);
            }

            childViews.get(i).layout(v_r,
                    v_t,
                    v_r + v_w,
                    v_t + v_h);

			/*
            * 修改
			* 将child填充进其其所包含的所有坐标点，使查找更方便快速
			* @data 2017/4/19
			* @author zjy
			*/
            int atArrayH = childLayout[1];
            int atArrayV = childLayout[0];
            int childW = childLayout[2];
            int childH = childLayout[3];
            if (DEBUG_CAN)
                DebugLog.i("add child to array : atArrayH=" + atArrayH + ", atArrayV=" + atArrayV + ", childW=" + childW + ", childH=" + childH);
            if (atArrayH < cViewTree.length && atArrayV < cViewTree[atArrayH].length) {
                while (childH > 0) {
                    int childWCopy = childW;
                    childH--;
                    while (childWCopy > 0) {
                        --childWCopy;
                        if ((atArrayH + childH) < cViewTree.length && (atArrayV + childWCopy) < cViewTree[atArrayH + childH].length) {
                            cViewTree[atArrayH + childH][atArrayV + childWCopy] = childViews.get(i);
                            if (DEBUG_CAN)
                                DebugLog.i("add child to array : array[0]=" + (atArrayH + childH) + ", array[1]=" + (atArrayV + childWCopy));
                        } else {
							/*if (DEBUG_CAN) */
                            DebugLog.w("err ArrayIndexOutOfBounds add child to array : array[0]=" + (atArrayH + childH) + ", array[1]=" + (atArrayV + childWCopy));
                        }
                    }
                }
            }

            childViews.get(i).setTag(childLayout);
//			MyLog.v("v_r", v_r+"");
//			MyLog.v("v_t", v_t+"");
//			MyLog.v("v_w", v_w+"");
//			MyLog.v("v_h", v_h+"");
        }
        if (isDealWithFocus)
            bindFocusSearchForBound();
    }

    private void initAdapter() {
        initItem();
    }

    BuildItemPoll mItemPoll = new BuildItemPoll();
    private void initItem() {
        mItemPoll.putAllItemView(poll);
        View focusViewFlag = findFocus();
        final int childCount = buildLayoutAdapter.getCount();
        View cacheItem;
        View newItem;
        int itemType;
        for (int i = 0; i < childCount; i++) {
            int[] childLayout = buildLayoutAdapter.getLayout(i);
            if (childLayout.length < 4) {
                continue;
            }
            itemType = buildLayoutAdapter.getItemViewType(i);
            cacheItem = getCacheViewForType(itemType);

            int v_w = childLayout[2] * itemWidth + (childLayout[2] - 1) * dividing;
            int v_h = childLayout[3] * itemHeight + (childLayout[3] - 1) * dividing;
            if (cacheItem != null) {
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) cacheItem.getLayoutParams();
                lp.width = v_w;
                lp.height = v_h;
                cacheItem.setLayoutParams(lp);
                int wMeasure = View.MeasureSpec.makeMeasureSpec(v_w, View.MeasureSpec.EXACTLY);
                int hMeasure = View.MeasureSpec.makeMeasureSpec(v_h, View.MeasureSpec.EXACTLY);
                cacheItem.setTag(R.id.key_tag_build_item_w, v_w);
                cacheItem.setTag(R.id.key_tag_build_item_h, v_h);
                cacheItem.setRight(cacheItem.getLeft() + v_w);
                cacheItem.setBottom(cacheItem.getTop() + v_h);
                cacheItem.measure(wMeasure, hMeasure);
                if (cacheItem instanceof ViewGroup) {
                    ViewGroup viewGroup = (ViewGroup) cacheItem;
                    View viewChild;
                    int widthChild;
                    int heightChild;
                    for (int j = 0; j < viewGroup.getChildCount(); j++) {
                        viewChild = viewGroup.getChildAt(j);
                        widthChild = viewChild.getMeasuredWidth();
                        heightChild = viewChild.getMeasuredHeight();
                        viewChild.setRight(viewChild.getLeft() + widthChild);
                        viewChild.setBottom(viewChild.getTop() + heightChild);
                    }
                }
            }
            newItem = buildLayoutAdapter.getView(i, cacheItem, this);
            DebugLog.i(TAG + " cacheItem : " + cacheItem + ",newItem : " + newItem);
            if (cacheItem == null || (newItem != null && newItem != cacheItem)) {
                if (DEBUG_CAN) DebugLog.i("initItem newItem != cacheItem");
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(v_w, v_h);
                newItem.setLayoutParams(lp);
                if (newItem.getParent() != null) {
                    ((ViewGroup) newItem.getParent()).removeView(newItem);
                }
//                addView(newItem);
            }
            if (newItem != null) {
                if (DEBUG_CAN) DebugLog.i("newItem != null");
                poll.putBuildItemView(itemType,newItem);
                if (childViews.contains(newItem)){
                    int index = childViews.indexOf(newItem);
                    if (index != i){
                        childViews.remove(index);
                        childViews.add(i,newItem);
                    }
                }else {
                    if (i < childViews.size()){
                        removeView(childViews.remove(i));
                        childViews.add(i,newItem);
                    }else {
                        childViews.add(newItem);
                    }
                    addView(newItem);
                }
            }

        }
        if (DEBUG_CAN) {
            DebugLog.i(TAG + " mItemPoll.size() : " + mItemPoll.size() + "mItemPoll : " + mItemPoll.toString());
            if (itemPoll != null) {
                DebugLog.i(TAG + " itemPoll.size() : " + itemPoll.size() + "itemPoll : " + itemPoll.toString());
            }
            DebugLog.i(TAG + " childViews.size : " + childViews.size() + "childViews : " + childViews.toString());
            DebugLog.i(TAG + " getChildCount : " + getChildCount());
            for (int i = 0; i < getChildCount(); i++) {
                DebugLog.i(TAG + "getChild : " + i + " " + getChildAt(i));
            }
        }

        for (int i = childViews.size() - 1; i >= childCount; i--) {
            childViews.remove(i);
        }
        if (DEBUG_CAN)
            DebugLog.i(TAG + " childViews.size2 : " + childViews.size() + "childViews2 : " + childViews.toString());
        if (DEBUG_CAN) DebugLog.i(TAG + "getChildCount2 : " + getChildCount());
        View viewC;
        for (int n = 0; n < getChildCount(); n++) {
            viewC = getChildAt(n);
            if (!childViews.contains(viewC)) {
                removeView(viewC);
                n--;
            }
        }
        if (DEBUG_CAN)
            DebugLog.i(TAG + " childViews.size() : " + childViews.size() + "childViews : " + childViews.toString());
        DebugLog.i(TAG + " getChildCount : " + getChildCount());

        mySetChildLayout();
        bindEvent();
        //解决数据更新由于控件复用导致带有焦点的item位置变化而焦点框位置未更新
        if (focusViewFlag != null) {
            if (childViews != null && childViews.size() > 0) {
                if (focusViewFlag != childViews.get(0)) {
                    childViews.get(0).requestFocus();
                } else {
                    if (childViews.size() > 1) {
                        childViews.get(1).requestFocus();
                    }
                }
                if (childViews.contains(focusViewFlag)) {
                    focusViewFlag.requestFocus();
                }
            }
        }
        mItemPoll.clear();
    }

    BuildItemPoll poll = new BuildItemPoll();
    private View getCacheViewForType(int itemType) {
        View cacheItem = mItemPoll.getBuildItemView(itemType);
        if (cacheItem == null && itemPoll != null){
            cacheItem = itemPoll.getBuildItemView(itemType);
        }
        return cacheItem;
    }

    private void bindEvent() {
        View child;
        for (int i = 0; i < getChildCount(); i++) {
            child = getChildAt(i);
            if (child != null) {
                child.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(final View child, boolean focus) {
                        if (onChildSelectListener != null) {
                            onChildSelectListener.onChildSelect(child, focus);
                        }
                        if (onChildFocusChangeListener != null) {
                            onChildFocusChangeListener.onChildFocus(child, focus, false, 0F);
                        }
                    }
                });
                if (onChildClickListener != null) {
                    child.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View child) {
                            onChildClickListener.onChildClick(child);

                        }
                    });
                }
            }
        }
    }

    private void bindFocusSearchForBound() {
        View view;
        for (int i = 0; i < childViews.size(); i++) {
            view = childViews.get(i);
            FocusSearchUtils.unBindFocus(view);
        }
        if (cViewTree == null) {
            return;
        }
        if (cViewTree.length > 0) {
            for (int i = 0; i < cViewTree.length; i++) {
                if (cViewTree[i].length > 0) {
                    FocusSearchUtils.bindFocusSearch(cViewTree[i][0], cViewTree[i][cViewTree[i].length - 1]);
                }
            }
        }
    }



    /**
     * 设置单元格宽高
     *
     * @param itemWidth
     * @param itemHeight
     */
    public void mySetItemSize(int itemWidth, int itemHeight) {
        this.itemWidth = itemWidth;
        this.itemHeight = itemHeight;
        setItemLayout();
        invalidate();
    }

    private void setItemLayout() {
        View child;
        for (int i = 0; i < childViews.size(); i++) {
            child = childViews.get(i);
            if (child == null) {
                continue;
            }
            int[] childLayout = buildLayoutAdapter.getLayout(i);
            if (childLayout.length < 4) {
                continue;
            }
            int v_w = childLayout[2] * itemWidth + (childLayout[2] - 1) * dividing;
            int v_h = childLayout[3] * itemHeight + (childLayout[3] - 1) * dividing;
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) child.getLayoutParams();
            if (lp == null) {
                lp = new RelativeLayout.LayoutParams(v_w, v_h);
            } else {
                lp.width = v_w;
                lp.height = v_h;
            }
            child.setLayoutParams(lp);
        }
    }

    /**
     * 设置控件尺寸
     *
     * @param hSize 列数
     * @param vSize 行数
     */
    public void mySetLayoutSize(int hSize, int vSize) {
        if (DEBUG_CAN) DebugLog.i("mySetLayoutSize [hSize=" + hSize + ",vSize=" + vSize + "]");
        this.hSize = hSize;
        this.vSize = vSize;
        invalidate();
    }

    /**
     * 设置间隔
     */
    public void mySetDividing(int dividing) {
        this.dividing = dividing;
        invalidate();
    }

    /**
     * 处理焦点左右移动
     *
     * @param isDealWithFocus
     */
    public void setIsDealWithFocus(boolean isDealWithFocus) {
        this.isDealWithFocus = isDealWithFocus;
        if (isDealWithFocus) {
            bindFocusSearchForBound();
        }
    }

    public boolean getIsDealWithFocus() {
        return isDealWithFocus;
    }

    /**
     * 获得默认获取焦点的View
     *
     * @return
     */
    public View myGetDefaultFocusView() {
        return defaultGetFocusView;
    }

    /**
     * 获得默认获取焦点的View
     *
     * @return
     */
    public View myGetDefaultFocusViewLB() {
        return defaultGetFocusViewLB;
    }

    /**
     * 获得默认获取焦点的View
     *
     * @return
     */
    public View myGetDefaultFocusViewRT() {
        return defaultGetFocusViewRT;
    }

    /**
     * 获得默认获取焦点的View
     *
     * @return
     */
    public View myGetDefaultFocusViewRB() {
        return defaultGetFocusViewRB;
    }

    private final BuildAdapterDataObserver observer = new BuildAdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            initAdapter();
        }
    };

    /**
     * 设置布局适配器
     *
     * @param buildLayoutAdapter
     */
    public void setAdapter(BaseBuildLayoutAdapter buildLayoutAdapter) {
        if (DEBUG_CAN) DebugLog.i("setAdapter buildLayoutAdapter : " + buildLayoutAdapter);
        if (this.buildLayoutAdapter != null){
            this.buildLayoutAdapter.unregisterAdapterDataObserver(observer);
        }
        if (buildLayoutAdapter != null) {
            this.buildLayoutAdapter = buildLayoutAdapter;
            buildLayoutAdapter.registerAdapterDataObserver(observer);
            initAdapter();
        }
    }

    public BaseBuildLayoutAdapter getAdapter(){
        return buildLayoutAdapter;
    }

    private static BuildItemPoll itemPoll;
    public void setBuildItemPoll(BuildItemPoll poll){
        itemPoll = poll;
    }

    public BuildItemPoll getItemPoll(){
        return itemPoll;
    }

    public void detach(){
        if (itemPoll != null){
            itemPoll.putAllItemView(poll);
            childViews.clear();
            removeAllViews();
        }
    }

    private OnChildSelectListener onChildSelectListener;
    private OnChildClickListener onChildClickListener;
    private OnChildFocusChangeListener onChildFocusChangeListener;

    public void setOnChildSelectListener(OnChildSelectListener myListener) {
        this.onChildSelectListener = myListener;
    }

    public void setOnChildClickListener(OnChildClickListener myListener) {
        this.onChildClickListener = myListener;
    }

    public void setOnChildFocusChangeListener(OnChildFocusChangeListener myListener) {
        this.onChildFocusChangeListener = myListener;
    }

    public interface OnChildSelectListener {
        public void onChildSelect(View child, boolean isSelect);
    }

    public interface OnChildClickListener {
        public void onChildClick(View child);
    }

    public interface OnChildFocusChangeListener {
        public void onChildFocus(View child, boolean isFocus, boolean scalable, float scale);
    }
}
