package tj.com.news;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import tj.com.news.utils.SpUtils;

public class SplashActivity extends Activity {

    private RelativeLayout rlRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        rlRoot = (RelativeLayout) findViewById(R.id.rl_root);
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);//旋转动画
        rotateAnimation.setDuration(1000);
        rotateAnimation.setFillAfter(true);//保持动画结束状态
        ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);//缩放动画
        scaleAnimation.setDuration(1000);
        scaleAnimation.setFillAfter(true);
//        渐变动画
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(2000);
        alphaAnimation.setFillAfter(true);
//        动画集合
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(rotateAnimation);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);
//        启动动画
        rlRoot.startAnimation(animationSet);
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
//
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //                动画结束，跳转页面
//                第一次进入，跳入新手引导页面，否则跳到主页面
                boolean isFirstEnter = SpUtils.getBoolean(getApplicationContext(), "is_first_enter", true);
                Intent intent;
                if (isFirstEnter) {
//                     新手引导
//                    SpUtils.putBoolean(getApplicationContext(),"is_first_enter",false);
                    intent = new Intent(getApplicationContext(), GuideActivity.class);
                } else {
//                      主页面
                    intent = new Intent(getApplicationContext(), MainActivity.class);
                }
                startActivity(intent);
                finish();//结束splashActivity

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
