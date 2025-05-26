package com.pushup.donstop

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.CookieManager
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class BannerWebActivity : AppCompatActivity() {

    private lateinit var webView: WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MusicPlayerManager.stop()

        setContentView(R.layout.activity_banner_web)

        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                )

        val url = intent.getStringExtra("url") ?: run {
            finish()
            return
        }

        Log.d("Banners", "Url: $url")

        webView = findViewById(R.id.bannerWebView)

        with(webView.settings) {
            javaScriptEnabled = true
            domStorageEnabled = true
            cacheMode = WebSettings.LOAD_DEFAULT
            loadsImagesAutomatically = true
            useWideViewPort = true
            loadWithOverviewMode = true
            builtInZoomControls = false
            displayZoomControls = false
        }

        CookieManager.getInstance().apply {
            setAcceptCookie(true)
            setAcceptThirdPartyCookies(webView, true)
        }

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: android.webkit.WebResourceRequest?): Boolean {
                val clickedUrl = request?.url.toString()
                Log.d("BannerWebActivity", "Клик по ссылке: $clickedUrl")

                // Если нужно открыть в новом окне — делаем это
                val intent = intent
                intent.putExtra("url", clickedUrl)
                finish() // Закрываем текущую
                startActivity(intent) // Открываем новую

                return true // Мы сами обработали переход
            }

            // (опционально) для старых версий API
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                url?.let {
                    Log.d("BannerWebActivity", "Клик по ссылке (old API): $url")

                    val intent = intent
                    intent.putExtra("url", url)
                    finish()
                    startActivity(intent)
                }
                return true
            }
        }
        webView.webChromeClient = WebChromeClient()

        webView.loadUrl(url)
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}
