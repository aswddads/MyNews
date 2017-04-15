package tj.com.news;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.Window;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import tj.com.news.fragment.ContentFragment;
import tj.com.news.fragment.LeftMenuFragment;

/**
 * Created by Jun on 17/4/13.
 */

public class MainActivity extends SlidingFragmentActivity {
    private static final String TAG_LEFT_MENU="TAG_LEFT_MENU";
    private static final String TAG_CONTENT="TAG_CONTENT";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        setBehindContentView(R.layout.left_menu);//设置左侧边栏 设置右侧边栏  需要指定  默认左边
        SlidingMenu slidingMenu=getSlidingMenu();
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);//设置侧边栏拉伸的触点为全屏
        slidingMenu.setBehindOffset(400);//设置拉出侧边栏剩余宽度
        initFragment();
    }

    /**
     * 初始化fragment
     */
    private void initFragment(){
        FragmentManager fm=getSupportFragmentManager();//fragment的管理者
        android.support.v4.app.FragmentTransaction transaction=fm.beginTransaction();//开始事物
//        用fragment替换帧布局，第一个参数为帧布局容器的id，参数二为要替换的fragment，第三个参数为标记
        transaction.replace(R.id.fl_main,new ContentFragment(),TAG_CONTENT);
        transaction.replace(R.id.fl_left_menu,new LeftMenuFragment(),TAG_LEFT_MENU);
        transaction.commit();//提交事物
//        Fragment fragment=fm.findFragmentByTag(TAG_CONTENT);根据标记寻找fragment
    }
}
