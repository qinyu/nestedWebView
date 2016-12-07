package org.qinyu.nestedwebview;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.View;

import org.qinyu.nestedwebview.log.LogUtils;

import static org.qinyu.nestedwebview.log.LogUtils.TAG;


public class NestedWebViewContainer extends NestedScrollView {
    private View nestedScrollChild;

    public NestedWebViewContainer(Context context) {
        super(context);
    }

    public NestedWebViewContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NestedWebViewContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        nestedScrollChild = findViewById(R.id.nested_web);
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        if (shouldPreScroll() && canScrollVertically(dy)) {
            int consumeY = 0;
            int oldScrollY = getScrollY();
            preScrollBy(0, dy);
            consumeY = getScrollY() - oldScrollY;
            if (consumed != null && consumed.length > 1) {
                consumed[1] = consumeY;
            }
            int unconsumedY = dy - consumeY;
            int[] parentConsumed = new int[2];
            if (dispatchNestedPreScroll(dx, unconsumedY, parentConsumed, null)) {
                if (consumed != null && consumed.length > 1) {
                    consumed[0] += parentConsumed[0];
                    consumed[1] += parentConsumed[1];
                }
            }
        } else {
            super.onNestedPreScroll(target, dx, dy, consumed);
        }
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        if (canScrollVertically(dyUnconsumed)) {
            final int oldScrollY = getScrollY();
            scrollBy(0, dyUnconsumed);

            final int myConsumed = getScrollY() - oldScrollY;
            final int myUnconsumed = dyUnconsumed - myConsumed;
            dispatchNestedScroll(0, myConsumed, 0, myUnconsumed, null);
            LogUtils.d(TAG, "onNestedScroll:myConsumed:" + myConsumed);
            LogUtils.d(TAG, "onNestedScroll:myUnconsumed:" + myUnconsumed);
        } else {
            dispatchNestedScroll(0, 0, 0, dyUnconsumed, null);
        }
    }

    private void preScrollBy(int x, int y) {
        if (y == 0) {
            return;
        }

        int oldScrollY = getScrollY();
        int top = getRelativeTop(nestedScrollChild);
        int bottom = getRelativeBottom(nestedScrollChild);
        int height = getHeight();

        int tryScrollY = oldScrollY + y;
        if (y > 0 && tryScrollY >= top) {
            tryScrollY = top;
        } else {
            if (y < 0 && bottom >= tryScrollY + height) {
                tryScrollY = bottom - height;
            }
        }

        super.scrollBy(x, tryScrollY - oldScrollY);
    }


    public boolean shouldPreScroll() {
        int top = getRelativeTop(nestedScrollChild);
        int bottom = getRelativeBottom(nestedScrollChild);
        int scrollY = getScrollY();

        int height = getHeight();
        boolean result = scrollY < top || bottom < scrollY + height;
        LogUtils.d(TAG, "shouldPreScroll:" + result +
                ",top:" + top + ",bottom:" + bottom + ",scrollY:" + scrollY + ",height:" + height);
        return result;
    }

    private int getRelativeTop(View childView) {
        if (childView.getParent() == this) {
            return childView.getTop();
        } else {
            return childView.getTop() + getRelativeTop((View) childView.getParent());
        }
    }

    private int getRelativeBottom(View childView) {
        if (childView.getParent() == this) {
            return childView.getBottom();
        } else {
            return childView.getBottom() + getRelativeTop((View) childView.getParent());
        }
    }
}
