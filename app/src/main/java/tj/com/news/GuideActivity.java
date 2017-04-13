package tj.com.news;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import tj.com.news.utils.SpUtils;

/**
 * Created by Jun on 17/4/13.  新手引导页面
 */

public class GuideActivity extends Activity {

    private ViewPager mViewPager;
    //    引导页图片id数组
    private int[] mImageIds = new int[]{R.drawable.guide_1,
            R.drawable.guide_2, R.drawable.guide_3};
    private ArrayList<ImageView> mImageViewList;//引导页imageview的集合
    private LinearLayout llContainer;
    private ImageView ivRedPoint;
    private int mPointDes;//小红点移动距离
    private Button btnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_guide);
        mViewPager = (ViewPager) findViewById(R.id.vp_guide);
        llContainer = (LinearLayout) findViewById(R.id.ll_container);
        ivRedPoint = (ImageView) findViewById(R.id.iv_red_point);
        btnStart = (Button) findViewById(R.id.btn_start);
        initData();
        mViewPager.setAdapter(new GuideAdapter());

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                  当页面滑动过程中回调（当前位置，移动偏移百分比，）    更新小红点距离，
                int leftMargin = (int) (mPointDes * positionOffset) + position * mPointDes;//计算当前小红点左边距
                RelativeLayout.LayoutParams ivRedPointLayoutParams = (RelativeLayout.LayoutParams) ivRedPoint.getLayoutParams();
                ivRedPointLayoutParams.leftMargin = leftMargin;//修改左边距
                ivRedPoint.setLayoutParams(ivRedPointLayoutParams);//更新布局参数
            }

            @Override
            public void onPageSelected(int position) {
//                某个页面被选中
                if (position == mImageViewList.size() - 1) {//最后一个页面显示开始
                    btnStart.setVisibility(View.VISIBLE);
                } else {
                    btnStart.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
//                  页面状态发送变化回调
            }
        });
//        计算两个圆点的距离  移动距离＝（第二个圆点的left值）－（第一个圆点left值）
//        measure（测量）-->layout（确定位置）-->draw(activity的onCreate方法执行结束后才会走此流程)
//        mPointDes = llContainer.getChildAt(1).getLeft()-llContainer.getChildAt(0).getLeft();


//        监听layout方法结束的事件，位置确定好再获取圆点距离       视图树
        ivRedPoint.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            //       layout方法执行结束回调
            @Override
            public void onGlobalLayout() {
                //移除监听，避免重复回调
                ivRedPoint.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mPointDes = llContainer.getChildAt(1).getLeft() - llContainer.getChildAt(0).getLeft();
            }
        });
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                跳到主页面   更新sp
                SpUtils.putBoolean(getApplicationContext(),"is_first_enter",false);
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        });
    }


    /**
     * 初始化数据
     */
    private void initData() {
        mImageViewList = new ArrayList<>();
        for (int i = 0; i < mImageIds.length; i++) {
            ImageView view = new ImageView(this);
            view.setBackgroundResource(mImageIds[i]);//通过设置背景，可用让宽高填充布局
//            view.setImageResource();  根据图片宽高
            mImageViewList.add(view);
//            初始化小圆点
            ImageView point = new ImageView(this);
            point.setImageResource(R.drawable.shape_point_gray);//设置图片（shape形状）
//            初始化布局参数，父控件是谁，就是谁声明的参数
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            if (i > 0) {
//                设置左边距
                params.leftMargin = 10;
            }
            point.setLayoutParams(params);
            llContainer.addView(point);//给容器添加小圆点
        }
    }

    class GuideAdapter extends PagerAdapter {

        @Override
        public int getCount() {//item的个数
            return mImageViewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        /**
         * 初始化item布局
         *
         * @param container
         * @param position
         * @return
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView view = mImageViewList.get(position);
            container.addView(view);
            return view;
        }

        /**
         * 销毁item
         *
         * @param container
         * @param position
         * @param object
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
