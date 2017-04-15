package tj.com.news.fragment;

import android.view.View;

import tj.com.news.R;

/**
 * Created by Jun on 17/4/14.   主页面
 */

public class ContentFragment extends BaseFragment {
    @Override
    public View initView() {
        View view=View.inflate(mActivity, R.layout.fragment_content,null);
        return view;
    }

    @Override
    public void initData() {

    }
}
