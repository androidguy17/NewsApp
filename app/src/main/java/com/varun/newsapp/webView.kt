package com.varun.newsapp

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.*
import android.widget.Toast
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.activity_web_view.*

class webView : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        var bundle:Bundle?= intent.extras
        var url = intent.getStringExtra("URL")


     webview.webViewClient = WebViewClient()
     webview.webChromeClient = WebChromeClient()

        webview.settings.javaScriptEnabled = true

       webview.loadUrl(url)

        webview.webViewClient =object :WebViewClient(){

            override fun onPageFinished(view: WebView?, url: String?) {

                progress1.isVisible= false
                webview.isVisible= true

                super.onPageFinished(view, url)
            }

        }



    }

    override fun onBackPressed() {
        if(webview.canGoBack()){
            webview.goBack()
        }
        else {
            super.onBackPressed()
        }
    }

    override fun onPause() {
        finish()

        super.onPause()
    }



}
