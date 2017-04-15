package tj.com.news.fragment;

import android.view.View;

import tj.com.news.R;

/**
 * Created by Jun on 17/4/14.   侧边栏
 */

public class LeftMenuFragment extends BaseFragment {
    @Override
    public View initView() {
        View view=View.inflate(mActivity, R.layout.fragment_left_menu,null);
        return view;
    }

    @Override
    public void initData() {

    }
}
