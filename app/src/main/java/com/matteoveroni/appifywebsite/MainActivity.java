package com.matteoveroni.appifywebsite;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import com.matteoveroni.appifywebsite.events.EventDisplayError;
import com.matteoveroni.appifywebsite.events.EventRedirectBrowser;
import com.matteoveroni.appifywebsite.events.EventUseBrowserFragment;
import com.matteoveroni.appifywebsite.events.EventWebError;
import com.matteoveroni.appifywebsite.fragments.BrowserFragment;
import com.matteoveroni.appifywebsite.fragments.ErrorFragment;

/**
 * @Author: Matteo Veroni
 *
 * https://developer.android.com/guide/webapps/webview.html
 */

public class MainActivity extends AppCompatActivity {

    private BrowserFragment browserFragment;
    private ErrorFragment errorFragment;

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe
    public void onUseBrowserFragment(EventUseBrowserFragment event) {
        createBrowserFragment();
    }

    @Subscribe
    public void onWebErrorOccurred(EventWebError eventWebError) {
        errorFragment = new ErrorFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_container, errorFragment, ErrorFragment.TAG)
                .commit();
        getSupportFragmentManager().executePendingTransactions();

        EventBus.getDefault().postSticky(new EventDisplayError(eventWebError.getErrorDescription()));

        Toast.makeText(MainActivity.this, getString(R.string.error) + " " + eventWebError.getErrorDescription(), Toast.LENGTH_SHORT).show();

        browserFragment = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createBrowserFragment();
    }

    @Override
    public void onBackPressed() {
        if (browserFragment != null && browserFragment.getWebView() != null) {
            WebView webView = browserFragment.getWebView();
            if (webView.canGoBack()) {
                webView.goBack();
                return;
            }
        }
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_item_starting_page:
                if (browserFragment != null) {
                    EventBus.getDefault().postSticky(new EventRedirectBrowser(getString(R.string.URL_WEBSITE)));
                } else {
                    createBrowserFragment();
                }
                return true;
            case R.id.menu_item_product_info:
                EventBus.getDefault().postSticky(new EventRedirectBrowser(getString(R.string.URL_INFO)));
                return true;
            case R.id.menu_item_submit_bug_report:
                String[] addresses = new String[1];
                addresses[0] = getString(R.string.BUG_REPORT_MAIL_ADDRESS);
                composeEmail(addresses, getString(R.string.BUG_REPORT_MAIL_SUBJECT));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void createBrowserFragment() {
        browserFragment = new BrowserFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.frame_container, browserFragment, BrowserFragment.TAG)
                .commit();
        getSupportFragmentManager().executePendingTransactions();

        errorFragment = null;
    }

    private void composeEmail(String[] addresses, String subject) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}