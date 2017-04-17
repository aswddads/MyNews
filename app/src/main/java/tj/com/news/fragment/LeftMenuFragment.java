package tj.com.news.fragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

import tj.com.news.MainActivity;
import tj.com.news.R;
import tj.com.news.domain.NewsMenu;
import tj.com.news.impl.NewsCenterPager;

/**
 * Created by Jun on 17/4/14.   侧边栏
 */

public class LeftMenuFragment extends BaseFragment {
    @ViewInject(R.id.lv_list)

    private ListView lvList;
    private ArrayList<NewsMenu.NewsMenuData> mNewsMenuData;//侧边栏对象
    private int mCurrentPos;//当前被选中的item位置
    private LeftMenuAdapter mAdapter;

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_left_menu, null);
//        lvList = (ListView) view.findViewById(R.id.lv_list);
        ViewUtils.inject(this, view);//注入view事件
        return view;
    }

    @Override
    public void initData() {

    }

    /**
     * 给侧边栏设置数据
     */
    public void setMenuData(ArrayList<NewsMenu.NewsMenuData> data) {
        mCurrentPos=0;//当前选中位置归0
        //更新页面
        mNewsMenuData = data;
        mAdapter = new LeftMenuAdapter();
        lvList.setAdapter(mAdapter);
        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mCurrentPos = i;//更新被选中的位置
                mAdapter.notifyDataSetChanged();//刷新listview
//                收起侧边栏
                toggle();
//                侧边栏点击之后，要修改新闻中心的framlayout的内容
                setCurrentDetailPager(i);
            }
        });
    }

    /**
     * 设置当前菜单详情页
     * @param i
     */
    private void setCurrentDetailPager(int i) {
//        获取新闻中心对象
         MainActivity mainUI= (MainActivity) mActivity;
//        获取ContentFragment
        ContentFragment fragment=mainUI.getContentFragment();
//        获取NewsCenterPager
        NewsCenterPager newsCenterPager=fragment.getNewsCenterPager();
//        修改新闻中心framlayout的布局
        newsCenterPager.setCurrentDetailPager(i);
    }

    /**
     * 打开或者关闭侧边栏
     */
    private void toggle() {
        MainActivity mainUI= (MainActivity) mActivity;
        SlidingMenu slidingMenu=mainUI.getSlidingMenu();
        slidingMenu.toggle();//如果当前状态为开，调用则关，反之亦然
    }

    class LeftMenuAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mNewsMenuData.size();
        }

        @Override
        public NewsMenu.NewsMenuData getItem(int i) {
            return mNewsMenuData.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            View view = View.inflate(mActivity, R.layout.list_item_left_menu, null);
            TextView tvMenu = (TextView) view.findViewById(R.id.tv_menu);
            NewsMenu.NewsMenuData item = getItem(i);
            tvMenu.setText(item.title);
            if (i == mCurrentPos) {
                //被选中
                tvMenu.setEnabled(true);
            } else {
                //未被选中
                tvMenu.setEnabled(false);
            }
            return view;
        }
    }
}
