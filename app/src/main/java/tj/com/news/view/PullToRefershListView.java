package tj.com.news.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Date;

import tj.com.news.R;

/**
 * Created by Jun on 17/4/18.
 */

public class PullToRefershListView extends ListView {

    private static final int STATE_PULL_TO_REFRESH = 1;
    private static final int STATE_RELEASE_TO_REFRESH = 2;
    private static final int STATE_REFRESHING = 3;

    private int mCurrentState = STATE_PULL_TO_REFRESH;//当前状态

    private View mHeaderView;
    private int mHeaderViewMeasuredHeight;
    private int startY = -1;
    private TextView tvTitle;
    private TextView tvTime;
    private ImageView ivArrow;
    private RotateAnimation animUp;
    private RotateAnimation animDown;
    private ProgressBar progressBar;

    public PullToRefershListView(Context context) {
        super(context);
        initHeaderView();
    }

    public PullToRefershListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHeaderView();
    }

    public PullToRefershListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeaderView();
    }

    public PullToRefershListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initHeaderView();
    }

    /**
     * 初始化头布局
     */
    private void initHeaderView() {
        mHeaderView = View.inflate(getContext(), R.layout.pull_to_refresh, null);
        this.addHeaderView(mHeaderView);

        tvTitle = (TextView) mHeaderView.findViewById(R.id.tv_title);
        tvTime = (TextView) mHeaderView.findViewById(R.id.tv_time);
        ivArrow = (ImageView) mHeaderView.findViewById(R.id.iv_arrow);
        progressBar = (ProgressBar) mHeaderView.findViewById(R.id.pb_loding);

//        平时隐藏头布局
        mHeaderView.measure(0, 0);
        mHeaderViewMeasuredHeight = mHeaderView.getMeasuredHeight();
        mHeaderView.setPadding(0, -mHeaderViewMeasuredHeight, 0, 0);
        initAnim();
        setCurrentTime();
    }

    /**
     * 希望下拉刷新显示出头布局
     *
     * @param ev
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (startY == -1) {//当用户按住头条viewpager下拉时，action_down会被viewpager消费  ，starty不会被赋值
                    startY = (int) ev.getY();
                }

                if (mCurrentState == STATE_REFRESHING) {//如果正在刷新，跳出
                    break;
                }
                int endY = (int) ev.getY();
                int dY = endY - startY;

                int firstVisiblePosition = getFirstVisiblePosition();//当前显示的第一个item的位置
//                必须下拉并且显示的必须第一个item
                if (dY > 0 && firstVisiblePosition == 0) {
                    int padding = dY - mHeaderViewMeasuredHeight;//计算当前控件下拉padding值
                    mHeaderView.setPadding(0, padding, 0, 0);

                    if (padding > 0 && mCurrentState != STATE_RELEASE_TO_REFRESH) {
//                        改为松开刷新
                        mCurrentState = STATE_RELEASE_TO_REFRESH;
                        refreshState();
                    } else if (padding < 0 && mCurrentState != STATE_PULL_TO_REFRESH) {
//                        改为下拉刷新
                        mCurrentState = STATE_PULL_TO_REFRESH;
                        refreshState();
                    }
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                startY = -1;
                if (mCurrentState == STATE_RELEASE_TO_REFRESH) {
                    mCurrentState = STATE_REFRESHING;
                    refreshState();
//                    完整展示头布局
                    mHeaderView.setPadding(0, 0, 0, 0);
                    /**
                     *                      4.  进行回调
                     */
                    if (mListener != null) {
                        mListener.onRefresh();
                    }
                } else if (mCurrentState == STATE_PULL_TO_REFRESH) {
//                    隐藏头布局
                    mHeaderView.setPadding(0, -mHeaderViewMeasuredHeight, 0, 0);
                }
                break;
            default:
                break;
        }


        return super.onTouchEvent(ev);
    }

    /**
     * 初始化箭头动画
     */
    private void initAnim() {
        animUp = new RotateAnimation(0, -180,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animUp.setDuration(200);
        animUp.setFillAfter(true);

        animDown = new RotateAnimation(-180, 0,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animDown.setDuration(200);
        animDown.setFillAfter(true);
    }


    /**
     * 根据当前状态进行刷新界面
     */
    private void refreshState() {
        switch (mCurrentState) {
            case STATE_PULL_TO_REFRESH:
                tvTitle.setText("下拉刷新");
                progressBar.setVisibility(INVISIBLE);
                ivArrow.setVisibility(VISIBLE);
                ivArrow.startAnimation(animDown);
                break;
            case STATE_RELEASE_TO_REFRESH:
                tvTitle.setText("松开刷新");
                progressBar.setVisibility(INVISIBLE);
                ivArrow.setVisibility(VISIBLE);
                ivArrow.startAnimation(animUp);
                break;
            case STATE_REFRESHING:
                tvTitle.setText("正在刷新...");
                ivArrow.clearAnimation();//清除动画，否则无法隐藏
                progressBar.setVisibility(VISIBLE);
                ivArrow.setVisibility(INVISIBLE);
                break;
            default:
                break;
        }
    }

    /**
     * 设置刷新时间
     */
    private void setCurrentTime() {
//        时间api调用java
        java.text.SimpleDateFormat format=new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = format.format(new Date());
            tvTime.setText(time);
    }


    /**
     * 3.定义成员变量接收监听的对象
     */
    private OnRefreshListener mListener;

    /**
     * 2.暴露接口，设置监听
     */
    public void setOnRefreshListener(OnRefreshListener listener) {
        mListener = listener;
    }

    /**
     * 回调步骤
     * 1.下拉刷新的回调接口
     */
    public interface OnRefreshListener {
        public void onRefresh();
    }

    /**
     * 刷新结束，收起控件
     */
    public void onRefreshComplete(boolean success) {
        mHeaderView.setPadding(0, -mHeaderViewMeasuredHeight, 0, 0);
        mCurrentState = STATE_PULL_TO_REFRESH;
        tvTitle.setText("下拉刷新");
        progressBar.setVisibility(INVISIBLE);
        ivArrow.setVisibility(VISIBLE);
        if (success) {//成功才更新时间
            setCurrentTime();
        }

    }

}
