package tj.com.news.base;

import android.app.Activity;
import android.view.View;

/**
 * Created by Jun on 17/4/16.
 * 菜单详情页的基类
 */

public abstract class BaseMenuDetailPager {
    public Activity mActivity;
    public View mRootView;//菜单详情页的帧布局

    public BaseMenuDetailPager(Activity activity) {
        mActivity = activity;
        mRootView=initView();
    }

    //初始化布局，必须子类实现
    public abstract View initView();

    //初始化数据
    public void initData() {

    }
}
