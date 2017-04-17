package tj.com.news.impl;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;

import tj.com.news.MainActivity;
import tj.com.news.base.BaseMenuDetailPager;
import tj.com.news.base.BasePager;
import tj.com.news.domain.NewsMenu;
import tj.com.news.fragment.LeftMenuFragment;
import tj.com.news.global.GlobalConstants;
import tj.com.news.menu.InterctMenuDetailPager;
import tj.com.news.menu.NewsMenuDetailPager;
import tj.com.news.menu.PhotosMenuDetailPager;
import tj.com.news.menu.TopicMenuDetailPager;
import tj.com.news.utils.CacheUtils;

/**
 * Created by Jun on 17/4/14.  新闻中心
 */

public class NewsCenterPager extends BasePager {
    private ArrayList<BaseMenuDetailPager> mMenuDetailPagers;//菜单详情页集合
    private NewsMenu mNewsdata;

    public NewsCenterPager(Activity activity) {
        super(activity);
    }

    /**
     * 给帧布局填充对象
     */
    @Override
    public void initData() {
//        TextView view = new TextView(mActivity);
//        view.setText("新闻中心");
//        view.setTextColor(Color.RED);
//        view.setTextSize(22);
//        view.setGravity(Gravity.CENTER);
//        flContent.addView(view);
        tvTitle.setText("新闻中心");
        btnMenu.setVisibility(View.VISIBLE);//显示菜单按钮
        //请求服务器，获取数据，   开源框架:XUtils
        /**
         *  先判断有没有缓存，若有就加载缓存
         */
        String cache = CacheUtils.getCache(mActivity, GlobalConstants.CATEGORY_URL);
        if (!TextUtils.isEmpty(cache)) {
            processData(cache);
        }

        getDataFromServer();//请求服务器

    }

    /**
     * 从服务器获取数据
     */
    private void getDataFromServer() {
        HttpUtils utils = new HttpUtils();
        /**
         * 在此处记得添加 android { useLibrary 'org.apache.http.legacy'}
         * 在sdk6.0以上 Android的网络请求强制使用HttpUrlConnection,并且SDK中也已经移除了HttpClient
         */
        utils.send(HttpRequest.HttpMethod.GET, GlobalConstants.CATEGORY_URL, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                processData(result);
                //写缓存
                CacheUtils.setCache(mActivity, GlobalConstants.CATEGORY_URL, result);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                e.printStackTrace();
                Toast.makeText(mActivity, "请求失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 解析数据  Gson
     */
    private void processData(String json) {
        Gson gson = new Gson();
        mNewsdata = gson.fromJson(json, NewsMenu.class);

        //获取侧边栏对象
        MainActivity mainUI= (MainActivity) mActivity;
        LeftMenuFragment fragment=mainUI.getLeftMenuFragment();

        //给侧边栏设置数据
        fragment.setMenuData(mNewsdata.data);
//        初始化四个菜单详情页
        mMenuDetailPagers=new ArrayList<>();
        mMenuDetailPagers.add(new NewsMenuDetailPager(mActivity,mNewsdata.data.get(0).children));
        mMenuDetailPagers.add(new TopicMenuDetailPager(mActivity));
        mMenuDetailPagers.add(new PhotosMenuDetailPager(mActivity));
        mMenuDetailPagers.add(new InterctMenuDetailPager(mActivity));
//        将新闻菜单详情页设置为默认页面
        setCurrentDetailPager(0);
    }

    /**
     * 设置菜单详情页
     * @param position
     */
    public void setCurrentDetailPager(int position){
//        重新添加内容
        BaseMenuDetailPager pager=mMenuDetailPagers.get(position);//获取应该显示的页面
        View view=pager.mRootView;
        flContent.removeAllViews();//清除之前布局
        flContent.addView(view);//给帧布局添加布局
//        初始化数据
        pager.initData();
//        更新标题
        tvTitle.setText(mNewsdata.data.get(position).title);
    }
}
