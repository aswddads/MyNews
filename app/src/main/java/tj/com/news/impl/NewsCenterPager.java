package tj.com.news.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import tj.com.news.base.BasePager;

/**
 * Created by Jun on 17/4/14.  新闻中心
 */

public class NewsCenterPager extends BasePager {

    public NewsCenterPager(Activity activity) {
        super(activity);
    }

    /**
     * 给帧布局填充对象
     */
    @Override
    public void initData() {
        TextView view=new TextView(mActivity);
        view.setText("新闻中心");
        view.setTextColor(Color.RED);
        view.setTextSize(22);
        view.setGravity(Gravity.CENTER);
        flContent.addView(view);
    }
}
