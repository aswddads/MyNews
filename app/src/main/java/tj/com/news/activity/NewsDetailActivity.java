package tj.com.news.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import tj.com.news.R;

/**
 * Created by Jun on 17/4/19.
 */

public class NewsDetailActivity extends Activity implements View.OnClickListener {

    @ViewInject(R.id.ll_control)
    private LinearLayout llControl;
    @ViewInject(R.id.btn_back)
    private ImageButton btnBack;
    @ViewInject(R.id.btn_textsize)
    private ImageButton btnTextSize;
    @ViewInject(R.id.btn_share)
    private ImageButton btnShare;
    @ViewInject(R.id.btn_menu)
    private ImageButton btnMennu;
    @ViewInject(R.id.wb_news_detail)
    private WebView mWebView;
    @ViewInject(R.id.p_loding)
    private ProgressBar pbLoding;
    private String mUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_news_detail);
        ViewUtils.inject(this);
        llControl.setVisibility(View.VISIBLE);
        btnBack.setVisibility(View.VISIBLE);
        btnMennu.setVisibility(View.GONE);

        btnBack.setOnClickListener(this);
        btnTextSize.setOnClickListener(this);
        btnShare.setOnClickListener(this);

        mUrl = getIntent().getStringExtra("url");
        mWebView.loadUrl(mUrl);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setBuiltInZoomControls(true);//显示缩放按钮(wap网页不支持)
        webSettings.setUseWideViewPort(true);//支持双击缩放(wap网页不支持)
        webSettings.setJavaScriptEnabled(true);//支持js
        mWebView.setWebViewClient(new WebViewClient() {
            //开始加载网页
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                pbLoding.setVisibility(View.VISIBLE);
            }

            //          网页加载结束
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pbLoding.setVisibility(View.INVISIBLE);
            }

            //             所有链接跳转会走此方法
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);//在跳转页面时强制加载此页面
                return true;
            }
        });
//        mWebView.goBack();//跳转到上一个页面
//        mWebView.goForward();//跳到下个页面
        mWebView.setWebChromeClient(new WebChromeClient() {
            //            进度发生变化
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }

            //          网页标题
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_textsize:
//                修改w网页字体大小
                showChooseDialog();
                break;
            case R.id.btn_share:
                break;
            default:
                break;
        }
    }

    private int mTempWhich;//记录
    private int mCurrentWhich=2;//记录当前选中的字体
    /**
     * 展示选择字体大小的弹窗
     */
    private void showChooseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("字体设置");
        String[] items = new String[]{"超大号字体", "大号字体", "正常字体", "小号字体", "超小号字体"};
        builder.setSingleChoiceItems(items, mCurrentWhich, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                mTempWhich=which;
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {

                WebSettings settings=mWebView.getSettings();
//                根据选择的字体修改网页字体大小
                switch (mTempWhich){
                    case 0:
                        settings.setTextSize(WebSettings.TextSize.LARGEST);
//                        settings.setTextZoom();
                        break;
                    case 1:
                        settings.setTextSize(WebSettings.TextSize.LARGER);
                        break;
                    case 2:
                        settings.setTextSize(WebSettings.TextSize.NORMAL);
                        break;
                    case 3:
                        settings.setTextSize(WebSettings.TextSize.SMALLER);
                        break;
                    case 4:
                        settings.setTextSize(WebSettings.TextSize.SMALLEST);
                        break;
                    default:
                        break;
                }
                mCurrentWhich=mTempWhich;
            }
        });
        builder.setNegativeButton("取消",null);
        builder.show();
    }
}
