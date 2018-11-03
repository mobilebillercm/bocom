package cm.siplus2018.bocom;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import cm.siplus2018.bocom.utils.Util;

public class TermsOfUse extends AppCompatActivity {

    private WebView remote_privacy_policy;
    private ProgressBar load_policy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_of_use);
        remote_privacy_policy = findViewById(R.id.remote_privacy_policy);
        load_policy = findViewById(R.id.load_policy);
        remote_privacy_policy.getSettings().setJavaScriptEnabled(true);
        remote_privacy_policy.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (load_policy.getVisibility() == View.GONE){
                    load_policy.setVisibility(View.VISIBLE);
                }

            }
            @Override
            public void onPageFinished(WebView view, String url) {

                if (load_policy.getVisibility() == View.VISIBLE){
                    load_policy.setVisibility(View.GONE);
                }
            }
        });
        remote_privacy_policy.loadUrl(Util.BOCOM_PRIVACY_POLICY);
    }
}
