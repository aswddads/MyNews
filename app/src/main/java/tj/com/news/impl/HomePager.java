package tj.com.news.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import tj.com.news.base.BasePager;

/**
 * Created by Jun on 17/4/14.  首页
 */

public class HomePager extends BasePager {

    public HomePager(Activity activity) {
        super(activity);
    }

    /**
     * 给帧布局填充对象
     */
    @Override
    public void initData() {
        TextView view = new TextView(mActivity);
        view.setText("首页");
        view.setTextColor(Color.RED);
        view.setTextSize(22);
        view.setGravity(Gravity.CENTER);
        flContent.addView(view);
        //修改界面标题
        tvTitle.setText("智慧北京");
        //隐藏菜单按钮
        btnMenu.setVisibility(View.GONE);
    }
}
