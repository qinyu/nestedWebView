package org.qinyu.nestedwebview.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class MainActivity extends AppCompatActivity {

    private WebView viewById;

    private int index;
    private final static String[] URLS = {
            "file:///android_asset/index.html",
            "file:///android_asset/origin_index.html",
            "file:///android_asset/test_mail.html",
            "http://nba.hupu.com"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(org.qinyu.nestedwebview.sample.R.layout.activity_main);
        viewById = (WebView) findViewById(org.qinyu.nestedwebview.R.id.nested_web);
        WebSettings settings = viewById.getSettings();
//        settings.setJavaScriptEnabled(true);
//        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        settings.setSupportZoom(true);
//        settings.setJavaScriptCanOpenWindowsAutomatically(true);
//        settings.setBuiltInZoomControls(true);
//        settings.setDisplayZoomControls(false);
//
//        settings.setAppCacheEnabled(true);
//        settings.setAppCachePath(getCacheDir().getAbsolutePath());
//        settings.setDatabaseEnabled(true);
//        settings.setSupportMultipleWindows(true);
        settings.setLoadWithOverviewMode(true);
//        settings.setUseWideViewPort(true);
//        settings.setDomStorageEnabled(true);
//        settings.setAllowContentAccess(true);
//        settings.setAllowFileAccess(true);
//        settings.setSaveFormData(true);

        settings.setJavaScriptEnabled(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setUseWideViewPort(true);
        viewById.setInitialScale(1);

        loadNextUrl();
    }

    public void switchHeader(View view) {
        View viewById = findViewById(R.id.header2);
        if (viewById.getVisibility() == View.GONE) {
            viewById.setVisibility(View.VISIBLE);
        } else if (viewById.getVisibility() == View.VISIBLE) {
            viewById.setVisibility(View.GONE);
        }
    }


    public void switchFooter(View view) {
        View viewById = findViewById(R.id.footer1);
        if (viewById.getVisibility() == View.GONE) {
            viewById.setVisibility(View.VISIBLE);
        } else if (viewById.getVisibility() == View.VISIBLE) {
            viewById.setVisibility(View.GONE);
        }
    }

    public void switchHtml(View view) {
        loadNextUrl();
    }

    private void loadNextUrl() {
        String url = URLS[(index++) % URLS.length];
        viewById.loadUrl(url);
        setTitle(url);
    }
}
