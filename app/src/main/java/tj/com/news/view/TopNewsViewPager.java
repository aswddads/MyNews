package tj.com.news.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Jun on 17/4/17.
 * 头条新闻自定义viewpager
 */

public class TopNewsViewPager extends ViewPager {

    private int startX;
    private int startY;

    public TopNewsViewPager(Context context) {
        super(context);
    }

    public TopNewsViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 上下滑动需要拦截
     * 向右滑动并且是第一个页面需要拦截
     * 向左滑动并且当前是最后一个页面需要拦截
     *
     * @param event
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        getParent().requestDisallowInterceptTouchEvent(true);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = (int) event.getX();
                startY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int endX = (int) event.getX();
                int endY = (int) event.getY();
                int dx = endX - startX;
                int dy = endY - startY;
                if (Math.abs(dy) < Math.abs(dx)) {
                    int currentItem = getCurrentItem();
//                  左右滑动，拦截
                    if (dx > 0) {
//                        向右滑动
                        if (currentItem == 0) {
//                            第一个页面，需要拦截
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }
                    } else {
                        int count=getAdapter().getCount();//item的总数
                        if (currentItem==count-1){
//                            最后一个页面,拦截
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }
                    }
                } else {
//                  上下滑动，拦截
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(event);
    }
}
