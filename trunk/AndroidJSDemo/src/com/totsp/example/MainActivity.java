package com.totsp.example;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

public class MainActivity extends Activity {

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      setContentView(R.layout.main);

      final WebView webView = (WebView) findViewById(R.id.webview);
      WebSettings webSettings = webView.getSettings();
      webSettings.setJavaScriptEnabled(true);
      webView.addJavascriptInterface(new AndroidJSBridge(this), "AndroidJSBridge");
      webView.loadUrl("file:///android_asset/test.html");

      Button button = (Button) findViewById(R.id.button);
      button.setOnClickListener(new OnClickListener() {
         @Override
         public void onClick(View v) {
            webView.loadUrl("javascript:showJsToast('Hello via JS')");
         }
      });

   }
}