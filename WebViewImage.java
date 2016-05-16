package com.example.boulder;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

public class WebViewImage extends Activity {
	private WebView webView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d("tag","WebViewImage is called");
		// TODO 自動生成されたメソッド・スタブ
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview);
		Bundle bundle = getIntent().getExtras();
	    String uri= (String)bundle.get("uri");
	    Log.d("tag",uri);
	    
	    webView = new WebView(this);
	    webView = (WebView)findViewById(R.id.webView1);
	    webView.loadUrl(uri);
	}

}
