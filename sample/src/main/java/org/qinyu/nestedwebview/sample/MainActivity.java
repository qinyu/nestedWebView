package org.qinyu.nestedwebview.sample;

import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(org.qinyu.nestedwebview.sample.R.layout.activity_main);
        WebView viewById = (WebView) findViewById(org.qinyu.nestedwebview.R.id.nested_web);
        WebSettings settings = viewById.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        settings.setSupportZoom(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);

        settings.setAppCacheEnabled(true);
        settings.setAppCachePath(getCacheDir().getAbsolutePath());
        settings.setDatabaseEnabled(true);
        settings.setSupportMultipleWindows(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setDomStorageEnabled(true);
        settings.setAllowContentAccess(true);
        settings.setAllowFileAccess(true);
        settings.setSaveFormData(true);

        viewById.loadUrl("http://nba.hupu.com");
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
}
