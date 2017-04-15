package tj.com.news.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import tj.com.news.base.BasePager;

/**
 * Created by Jun on 17/4/14.  服务
 */

public class SmartServicePager extends BasePager {

    public SmartServicePager(Activity activity) {
        super(activity);
    }

    /**
     * 给帧布局填充对象
     */
    @Override
    public void initData() {
        TextView view=new TextView(mActivity);
        view.setText("服务");
        view.setTextColor(Color.RED);
        view.setTextSize(22);
        view.setGravity(Gravity.CENTER);
        flContent.addView(view);
    }
}
