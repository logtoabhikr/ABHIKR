package com.abhikr.abhikr.customtab;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.transition.Explode;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.abhikr.abhikr.R;

public class WebviewActivity extends AppCompatActivity {
    public static final String EXTRA_URL = "extra.url";
    String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        getWindow().setExitTransition(new Explode());
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null) {
           url = getIntent().getStringExtra(EXTRA_URL);
        }
        else
        {
           url="http://www.dealcometrue.com/";
        }
        WebView webView =findViewById(R.id.webview);
        webView.setWebViewClient(new myWebClient());
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        //webView.setWebChromeClient(new WebChromeClient());
        WebSettings webSettings = webView.getSettings();
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(false);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true); // allow pinch to zooom
        webSettings.setDisplayZoomControls(false); // disable the default zoom controls on the page
        setTitle(url);
        // enabling action bar app icon and behaving it as toggle button
     /*
       Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarwebviewcustomtab);
        setSupportActionBar(toolbar);
       if(getSupportActionBar()!=null)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);*/
        webView.loadUrl(url);

       /* webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return true;
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                if (url.startsWith("http://www.dealcometrue.com")) {
                    //Handle Internal Link...
                } else {
                    //Open Link in a Custom Tab
                    Uri uri = Uri.parse(url);
                    CustomTabsIntent.Builder intentBuilder =
                            new CustomTabsIntent.Builder();
                    //Open the Custom Tab
                    intentBuilder.build().launchUrl(getApplicationContext(), uri);
                }
            }
        });
        */

    }
    public class myWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Respond to the action bar's Up/Home button
        if (item.getItemId() == android.R.id.home) {
            supportFinishAfterTransition();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
