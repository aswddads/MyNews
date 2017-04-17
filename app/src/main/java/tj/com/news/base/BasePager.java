package tj.com.news.base;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import tj.com.news.MainActivity;
import tj.com.news.R;

/**
 * Created by Jun on 17/4/14.
 */

public class BasePager {

    public Activity mActivity;
    public TextView tvTitle;
    public ImageButton btnMenu;
    public FrameLayout flContent;//空的帧布局对象，动态添加布局
    public  View mRootView;//当前页面的布局对象


    /**
     * 五个标签页的基类
     * @param activity
     */
    public BasePager(Activity activity){
        mActivity=activity;
        mRootView = initView();
    }
    /**
     * 初始化布局
     * @return
     */
    public View initView(){
        View view=View.inflate(mActivity, R.layout.base_pager,null);
        tvTitle=(TextView) view.findViewById(R.id.tv_title);
        btnMenu=(ImageButton) view.findViewById(R.id.btn_menu);
        flContent=(FrameLayout) view.findViewById(R.id.fl_content);

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });
        return view;
    }

    /**
     * 初始化数据
     */
    public void initData(){

    }
    /**
     * 打开或者关闭侧边栏
     */
    private void toggle() {
        MainActivity mainUI= (MainActivity) mActivity;
        SlidingMenu slidingMenu=mainUI.getSlidingMenu();
        slidingMenu.toggle();//如果当前状态为开，调用则关，反之亦然
    }

}
