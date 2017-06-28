package fr.pomp.adfuell.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import butterknife.BindView;
import fr.pomp.adfuell.R;

/**
 * Created by Mandimbisoa on 07/06/2017.
 */

public class FaqFragment extends BaseFragment{

    @BindView(R.id.id_webview)
    public WebView _webview;

    public FaqFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_faq, container, false);
        _edButterKniff.view(this,v);
        _mainActivity.headerShow(true);
        _mainActivity.headerRightBtnShow(false);
        _mainActivity.changeHeaderTitle(getStringRes(R.string.text_faq));

        _webview.loadUrl(getStringRes(R.string.url_faq));
        WebSettings webSettings = _webview.getSettings();
        webSettings.setJavaScriptEnabled(true);

        _webview.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                _mainActivity.loadingShow(true);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                _mainActivity.loadingShow(false);
            }
        });
        return v;
    }
}
