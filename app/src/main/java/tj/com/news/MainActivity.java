package tj.com.news;

import android.os.Bundle;
import android.view.Window;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

/**
 * Created by Jun on 17/4/13.
 */

public class MainActivity extends SlidingFragmentActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        setBehindContentView(R.layout.left_menu);//设置左侧边栏 设置右侧边栏  需要指定  默认左边
        SlidingMenu slidingMenu=getSlidingMenu();
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);//设置侧边栏拉伸的触点为全屏
        slidingMenu.setBehindOffset(300);//设置拉出侧边栏剩余宽度
    }
}
