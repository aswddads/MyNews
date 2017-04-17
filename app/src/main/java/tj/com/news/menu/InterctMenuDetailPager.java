package tj.com.news.menu;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import tj.com.news.base.BaseMenuDetailPager;

/**
 * Created by Jun on 17/4/16.
 * 菜单详情页－互动
 */

public class InterctMenuDetailPager extends BaseMenuDetailPager {
    public InterctMenuDetailPager(Activity activity) {
        super(activity);
    }

    @Override
    public View initView() {
        TextView view=new TextView(mActivity);
        view.setText("互动");
        view.setTextColor(Color.RED);
        view.setTextSize(22);
        view.setGravity(Gravity.CENTER);
        return view;
    }
}
