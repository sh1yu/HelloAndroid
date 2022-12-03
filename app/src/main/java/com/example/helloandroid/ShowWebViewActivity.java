package com.example.helloandroid;


import android.graphics.Bitmap;
import android.net.http.SslCertificate;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import org.apache.commons.codec.binary.Hex;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Objects;

public class ShowWebViewActivity extends AppCompatActivity {

    private WebView webView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_webview);

        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.hide();
        }


        this.webView = (WebView) findViewById(R.id.webView);
        WebSettings webSettings = this.webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);

        //加载网页链接
        webView.loadUrl("https://101.43.106.28");

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.d("cc", "开始加载");
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.d("cc", "加载结束");
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler sslErrorHandler, SslError sslError) {
                Log.d("cc", "sslError:" + sslError.getPrimaryError());
                Log.d("cc", sslError.getCertificate().toString());

//                Bundle bundle = SslCertificate.saveState(sslError.getCertificate());
//                byte[] bytes = bundle.getByteArray("x509-certificate");


                // 获取程序读取到的cert签名
                String certSig1 = null;
                try {
                    Bundle bundle = SslCertificate.saveState(sslError.getCertificate());
                    if (bundle != null) {
                        byte[] bytes = bundle.getByteArray("x509-certificate");
                        CertificateFactory cf = CertificateFactory.getInstance("X.509");
                        Certificate ca = cf.generateCertificate(new ByteArrayInputStream(bytes));
                        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
                        byte[] key = sha256.digest(ca.getEncoded());
                        certSig1 = Hex.encodeHexString(key);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.d("cc", "certSig1: "+certSig1);

                // 获取保存的证书签名
                String certSig2 = null;
                try {
                    InputStream inputStream = getAssets().open("nginx.crt");
                    CertificateFactory cf = CertificateFactory.getInstance("X.509");
                    Certificate ca = cf.generateCertificate(inputStream);
                    MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
                    byte[] key = sha256.digest(ca.getEncoded());
                    certSig2 = Hex.encodeHexString(key);
                } catch (NoSuchAlgorithmException | IOException | CertificateException e) {
                    e.printStackTrace();
                }
                Log.d("cc", "certSig2: "+certSig2);

                if (Objects.equals(certSig1, certSig2)) {
                    sslErrorHandler.proceed();
                } else {
                    Toast.makeText(getApplicationContext(), "证书有问题", Toast.LENGTH_SHORT).show();
                    sslErrorHandler.cancel();
                }

            }

            // 链接跳转都会走这个方法
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Log.d("cc", "Url：" + request.getUrl());
                view.loadUrl(request.getUrl().toString());// 强制在当前 WebView 中加载 url
                return true;
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && this.webView.canGoBack()) {
            this.webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home && this.webView.canGoBack()) {
            this.webView.goBack();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}