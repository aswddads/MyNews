package tj.com.news.menu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;

import tj.com.news.R;
import tj.com.news.activity.NewsDetailActivity;
import tj.com.news.base.BaseMenuDetailPager;
import tj.com.news.domain.NewsMenu;
import tj.com.news.domain.NewsTabBeam;
import tj.com.news.global.GlobalConstants;
import tj.com.news.utils.CacheUtils;
import tj.com.news.utils.SpUtils;
import tj.com.news.view.PullToRefershListView;
import tj.com.news.view.TopNewsViewPager;

/**
 * Created by Jun on 17/4/16.
 * 页签页面对象
 */

public class TabDetailPager extends BaseMenuDetailPager {
    private NewsMenu.NewsTabData mTabData;//单个页签网络数据
    //    private TextView view;
    @ViewInject(R.id.vp_top_news)
    private TopNewsViewPager mViewPager;
    @ViewInject(R.id.tv_title)
    private TextView tvTitle;
    @ViewInject(R.id.indicator)
    private CirclePageIndicator mIndicator;
    @ViewInject(R.id.lv_list)
    private PullToRefershListView lvList;
    private final String mUrl;
    private ArrayList<NewsTabBeam.TopNews> mTopnews;
    private ArrayList<NewsTabBeam.NewsData> mNewsList;
    private NewsAdapter mNewsAdapter;
    private String mMoreUrl;//下一页数据链接

    public TabDetailPager(Activity activity, NewsMenu.NewsTabData newsTabData) {
        super(activity);
        mTabData = newsTabData;
        mUrl = GlobalConstants.SERVER_URL + mTabData.url;
    }

    @Override
    public View initView() {
//        view = new TextView(mActivity);
////        view.setText(mTabData.title);   此处空指针
//        view.setTextColor(Color.RED);
//        view.setTextSize(22);
//        view.setGravity(Gravity.CENTER);

        View view = View.inflate(mActivity, R.layout.pager_tab_detail, null);
        ViewUtils.inject(this, view);

        //给listview添加头布局
        View mHeaderView=View.inflate(mActivity,R.layout.list_item_header,null);
        ViewUtils.inject(this,mHeaderView);//头布局注入
        lvList.addHeaderView(mHeaderView);

/**
 *            5.   设置回调监听
 */
        lvList.setOnRefreshListener(new PullToRefershListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                刷新数据
                getDataFromServer();
            }

            @Override
            public void onLoadMore() {
//                加载下一页数据
                if (mMoreUrl!=null){
//                    有下一页数据
                    getMoreDataFromServer();
                }else {
//                   没有数据
                    Toast.makeText(mActivity,"没有更多信息了...",Toast.LENGTH_SHORT).show();
                    lvList.onRefreshComplete(false);
                }
            }
        });
        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                int headerViewCount=lvList.getHeaderViewsCount();//获取头布局数量
                position=position-headerViewCount;//需要减去头布局的占位
                NewsTabBeam.NewsData news=mNewsList.get(position);
//                read_id:1101，1002
                String readIds=SpUtils.getString(mActivity,"read_ids","");
                if (readIds.contains(news.id+"")){//不包含当前id才添加
                    readIds=readIds+news.id+",";
                    SpUtils.putString(mActivity,"read_ids",readIds);
                }
//                要将被点击的item颜色变为灰色
                TextView tvTitle= (TextView) view.findViewById(R.id.tv_title);
                tvTitle.setTextColor(Color.GRAY);
//                mNewsAdapter.notifyDataSetChanged();  全局刷新，浪费性能
//                点击跳转到新闻详情页面
                Intent intent=new Intent(mActivity, NewsDetailActivity.class);
                intent.putExtra("url",news.url);
                mActivity.startActivity(intent);
            }
        });
        return view;
    }

    /**
     * 加载下一页数据
     */
    private void getMoreDataFromServer() {
        HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, mMoreUrl, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                processData(result,true);
//                CacheUtils.setCache(mActivity, mUrl, result);

//                收起下拉刷新控件
                lvList.onRefreshComplete(true);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                e.printStackTrace();
                Toast.makeText(mActivity, s, Toast.LENGTH_SHORT).show();
                //                收起下拉刷新控件
               lvList.onRefreshComplete(false);
            }
        });
    }

    @Override
    public void initData() {
//        view.setText(mTabData.title);
        String cache = CacheUtils.getCache(mActivity, mUrl);
        if (!TextUtils.isEmpty(cache)) {
            processData(cache,false);
        }
        getDataFromServer();
    }

    private void getDataFromServer() {
        HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, mUrl, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                processData(result,false);
                CacheUtils.setCache(mActivity, mUrl, result);

//                收起下拉刷新控件
                lvList.onRefreshComplete(true);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                e.printStackTrace();
                Toast.makeText(mActivity, s, Toast.LENGTH_SHORT).show();
                //                收起下拉刷新控件
                lvList.onRefreshComplete(false);
            }
        });
    }

    private void processData(String result,boolean isMore) {
        Gson gson = new Gson();
        NewsTabBeam newsTabBeam = gson.fromJson(result, NewsTabBeam.class);
        mTopnews = newsTabBeam.data.topnews;
        String moreUrl=newsTabBeam.data.more;
        if (!TextUtils.isEmpty(moreUrl)){
            mMoreUrl = GlobalConstants.SERVER_URL+moreUrl;
        }else {
            mMoreUrl=null;
        }
        if (!isMore){
            //头条新闻填充数据
            if (mTopnews != null) {
                mViewPager.setAdapter(new TopNewsAdapter());

                mIndicator.setViewPager(mViewPager);
                mIndicator.setSnap(true);//快照显示展示

//            事件要设置给mIndicator
                mIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
//                    跟新头条新闻的标题
                        NewsTabBeam.TopNews topNews = mTopnews.get(position);
                        tvTitle.setText(topNews.title);
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
                tvTitle.setText(mTopnews.get(0).title);//手动添加第一页的新闻标题
                mIndicator.onPageSelected(0);//默认回到0页面   解决销毁后  回到上次保留的状态 页面确是第一页
            }

//        列表新闻
            mNewsList = newsTabBeam.data.news;
            if (mNewsList != null) {
                mNewsAdapter = new NewsAdapter();
                lvList.setAdapter(mNewsAdapter);
            }

        }else {
            //加载更多
            ArrayList<NewsTabBeam.NewsData> moreNews=newsTabBeam.data.news;
            mNewsList.addAll(moreNews);//将数据追加在原来的集合后
//            刷新listview
            mNewsAdapter.notifyDataSetChanged();

        }


    }

    /**
     * 头条新闻适配器
     */
    class TopNewsAdapter extends PagerAdapter {

        private final BitmapUtils mBitmapUtils;

        public TopNewsAdapter() {
            mBitmapUtils = new BitmapUtils(mActivity);
            mBitmapUtils.configDefaultLoadingImage(R.drawable.topnews_item_default);//设置加载中的默认图片
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView view = new ImageView(mActivity);
            view.setScaleType(ImageView.ScaleType.FIT_XY);//设置图片缩放方式，宽高填充父窗体

//            下载图片，将图片设置给imageView--避免溢出－－－缓存   BitmapUtils-XUtils
            String imageUrl = mTopnews.get(position).topimage;//图片下载链接
            mBitmapUtils.display(view, imageUrl);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return mTopnews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    class NewsAdapter extends BaseAdapter {
        private final BitmapUtils mBitmapUtils;

        public NewsAdapter(){
            mBitmapUtils= new BitmapUtils(mActivity);
            mBitmapUtils.configDefaultLoadingImage(R.drawable.pic_item_list_default);
        }

        @Override
        public int getCount() {
            return mNewsList.size();
        }

        @Override
        public Object getItem(int i) {
            return mNewsList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mActivity, R.layout.list_item_news, null);
                holder = new ViewHolder();
                holder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
                holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
                holder.tvDate = (TextView) convertView.findViewById(R.id.tv_date);
                convertView.setTag(holder);
            }else {
                holder= (ViewHolder) convertView.getTag();
            }
            NewsTabBeam.NewsData news= (NewsTabBeam.NewsData) getItem(i);
            holder.tvTitle.setText(news.title);
            holder.tvDate.setText(news.pubdate);
            //根据记录标记已读未读
            String readIds=SpUtils.getString(mActivity,"read_ids","");
            if (readIds.contains(news.id+"")){
                holder.tvTitle.setTextColor(Color.GRAY);
            }else {
                holder.tvTitle.setTextColor(Color.BLACK);
            }
            mBitmapUtils.display(holder.ivIcon,news.listimage);
            return convertView;
        }
    }

    static class ViewHolder {
        public ImageView ivIcon;
        public TextView tvTitle;
        public TextView tvDate;
    }
}
