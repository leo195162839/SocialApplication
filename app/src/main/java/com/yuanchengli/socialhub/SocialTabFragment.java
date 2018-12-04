package com.yuanchengli.socialhub;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebBackForwardList;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class SocialTabFragment extends android.support.v4.app.Fragment {

    public String url;
    private WebView mWebView;
    private SwipeRefreshLayout swipeContainer;
    private View progressBar;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case 1: {
                    webViewGoBack();
                }
                break;
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.social_tab, container, false);

        mWebView = rootView.findViewById(R.id.wvFacebook);
        swipeContainer = rootView.findViewById(R.id.swipeContainer);
        progressBar = rootView.findViewById(R.id.progressBar);


        final WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(false);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setBuiltInZoomControls(false);

        mWebView.setInitialScale(1);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mWebView.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == MotionEvent.ACTION_UP && mWebView.canGoBack()) {
                    handler.sendEmptyMessage(1);
                    return true;
                }

                return false;
            }

        });
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
                swipeContainer.setRefreshing(false);
            }
        });

        if (url != null) {
            mWebView.loadUrl(url);
        } else {
            Toast.makeText(getContext(), "URL Dead", Toast.LENGTH_SHORT).show();
        }

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mWebView.loadUrl(mWebView.getUrl());
            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        return rootView;
    }


    private void webViewGoBack() {

        WebBackForwardList history = mWebView.copyBackForwardList();
        int index = -1;
        String url = null;

        while (mWebView.canGoBackOrForward(index)) {
            if (!history.getItemAtIndex(history.getCurrentIndex() + index).getUrl().equals("about:blank")) {
                mWebView.goBackOrForward(index);
                url = history.getItemAtIndex(-index).getUrl();
                Log.e("tag", "first non empty" + url);
                break;
            }
            index--;

        }
    }

    public void fbNotificationQuickLink() {
        Log.d("fragment2", "reloading");
        mWebView.loadUrl("https://m.facebook.com/notifications");
    }

    public void momentQuickLink() {
        Log.d("fragment2", "reloading");
        mWebView.loadUrl("https://twitter.com/i/moments");
    }

    public void notificationQuickLink() {
        Log.d("fragment2", "reloading");
        mWebView.loadUrl("https://twitter.com/i/notifications");
    }

    public void dmQuickLink() {
        Log.d("fragment2", "reloading");
        mWebView.loadUrl("https://twitter.com/messages");
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        // enable synchronization for account information
        CookieManager.getInstance().flush();
    }
}

