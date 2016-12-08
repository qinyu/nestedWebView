package org.qinyu.nestedwebview;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;


public class NestedWebView extends WebView implements NestedScrollingChild {
    private int lastY;
    private final int[] scrollOffset = new int[2];
    private final int[] scrollConsumed = new int[2];
    private int nestedOffsetY;
    private NestedScrollingChildHelper scrollingChildHelper;

    public NestedWebView(Context context) {
        this(context, null);
    }

    public NestedWebView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.webViewStyle);
    }

    public NestedWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        scrollingChildHelper = new NestedScrollingChildHelper(this);
        setNestedScrollingEnabled(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        boolean returnValue = false;

        if (ev.getPointerCount() > 1) {
            return super.onTouchEvent(ev);
        }

        MotionEvent event = MotionEvent.obtain(ev);
        final int action = MotionEventCompat.getActionMasked(event);
        if (action == MotionEvent.ACTION_DOWN) {
            nestedOffsetY = 0;
        }
        int eventY = (int) event.getY();
        event.offsetLocation(0, nestedOffsetY);
        switch (action) {
            case MotionEvent.ACTION_MOVE:
                // NestedPreScroll
                int deltaY = lastY - eventY;
//                LogUtils.d(TAG, "onTouchEvent:beforeDispatchNestedPreScroll:lastY" + lastY +
//                        ",eventY:" + event.getY() + ",deltaY:" + deltaY + ",nestedOffsetY:" + nestedOffsetY);

                if (dispatchNestedPreScroll(0, deltaY, scrollConsumed, scrollOffset)) {
                    deltaY -= scrollConsumed[1];
                    lastY = eventY - scrollOffset[1];
                    event.offsetLocation(0, scrollOffset[1]);
                    nestedOffsetY += scrollOffset[1];
//                    LogUtils.d(TAG, "onTouchEvent:afterDispatchNestedPreScroll:lastY" + lastY +
//                            ",eventY:" + event.getY() + ",deltaY:" + deltaY + ",nestedOffsetY:" + nestedOffsetY);
                }

                returnValue = consumeEvent(event, deltaY);
                break;
            case MotionEvent.ACTION_DOWN:
                returnValue = super.onTouchEvent(event);
                lastY = eventY;
                // start NestedScroll
                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                returnValue = super.onTouchEvent(event);
                // end NestedScroll
                stopNestedScroll();
                break;
        }
        return returnValue;
    }

    private boolean consumeEvent(MotionEvent event, int deltaY) {
        boolean returnValue;
        int oldScrollY = getScrollY();

        int tryScrollToY = oldScrollY + deltaY;
        int maxScrollY = computeVerticalScrollRange() - computeVerticalScrollExtent();
        if (tryScrollToY < 0) {
            tryScrollToY = 0;
        } else if (tryScrollToY > maxScrollY) {
            tryScrollToY = maxScrollY;
        }

        returnValue = super.onTouchEvent(event);

        final int dyConsumed = tryScrollToY - oldScrollY;
        final int dyUnconsumed = deltaY - dyConsumed;

//        LogUtils.d(TAG, "dispatchNestedScroll:deltaY:" + deltaY);
//        LogUtils.d(TAG, "dispatchNestedScroll:dyConsumed:" + deltaY);
//        LogUtils.d(TAG, "dispatchNestedScroll:scrollConsumed:[" + scrollConsumed[0] + ", " + scrollConsumed[1] + "]");
//        LogUtils.d(TAG, "dispatchNestedScroll:scrollOffset:[" + scrollOffset[0] + ", " + scrollOffset[1] + "]");
        // NestedScroll
        if (dispatchNestedScroll(0, dyConsumed, 0, dyUnconsumed, scrollOffset)) {
            event.offsetLocation(0, scrollOffset[1]);
            nestedOffsetY += scrollOffset[1];
            lastY -= scrollOffset[1];
        }
        return returnValue;
    }


    // Nested Scroll implements
    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        scrollingChildHelper.setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        return scrollingChildHelper.isNestedScrollingEnabled();
    }

    @Override
    public boolean startNestedScroll(int axes) {
        return scrollingChildHelper.startNestedScroll(axes);
    }

    @Override
    public void stopNestedScroll() {
        scrollingChildHelper.stopNestedScroll();
    }

    @Override
    public boolean hasNestedScrollingParent() {
        return scrollingChildHelper.hasNestedScrollingParent();
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed,
                                        int[] offsetInWindow) {
        return scrollingChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return scrollingChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return scrollingChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return scrollingChildHelper.dispatchNestedPreFling(velocityX, velocityY);
    }
}
