package com.pushup.donstop

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.*
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

class BannerWebActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var noInternetLayout: View
    private lateinit var webContainer: FrameLayout
    private lateinit var reloadButton: ImageButton

    private val FILE_CHOOSER_REQUEST_CODE = 1000
    private var filePathCallback: ValueCallback<Array<Uri>>? = null
    private var customView: View? = null
    private var customViewCallback: WebChromeClient.CustomViewCallback? = null
    private var originalSystemUiVisibility = 0

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MusicPlayerManager.stop()

        // Иммерсивный UI
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val controller = WindowCompat.getInsetsController(window, window.decorView)
        controller?.let {
            it.hide(WindowInsetsCompat.Type.statusBars() or WindowInsetsCompat.Type.navigationBars())
            it.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT

        setContentView(R.layout.activity_banner_web)

        webView = findViewById(R.id.bannerWebView)
        noInternetLayout = findViewById(R.id.noInternetLayout)
        webContainer = findViewById(R.id.webContainer)
        reloadButton = noInternetLayout.findViewById(R.id.reloadButton)

        setupWebView()

        val url = intent.getStringExtra("url") ?: run {
            finish()
            return
        }

        if (isOnline()) {
            showWebView()
            webView.loadUrl(url)
        } else {
            showNoInternet()
        }

        reloadButton.setOnClickListener {
            if (isOnline()) {
                showWebView()
                webView.reload()
            } else {
                Toast.makeText(this, "No internet", Toast.LENGTH_SHORT).show()
            }
        }

        onBackPressedDispatcher.addCallback(this) {
            if (webView.canGoBack()) {
                webView.goBack()
            } else {
                finish()
            }
        }
    }

    private fun setupWebView() {
        with(webView.settings) {
            javaScriptEnabled = true
            domStorageEnabled = true
            cacheMode = WebSettings.LOAD_DEFAULT
            loadsImagesAutomatically = true
            useWideViewPort = true
            loadWithOverviewMode = true
            builtInZoomControls = false
            displayZoomControls = false
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            allowFileAccess = true
            allowContentAccess = true
        }

        CookieManager.getInstance().apply {
            setAcceptCookie(true)
            setAcceptThirdPartyCookies(webView, true)
        }

        webView.webChromeClient = object : WebChromeClient() {
            override fun onShowFileChooser(
                webView: WebView?,
                filePathCallback: ValueCallback<Array<Uri>>?,
                fileChooserParams: FileChooserParams?
            ): Boolean {
                this@BannerWebActivity.filePathCallback?.onReceiveValue(null)
                this@BannerWebActivity.filePathCallback = filePathCallback

                return try {
                    val intent = fileChooserParams?.createIntent()
                    if (intent != null) startActivityForResult(intent, FILE_CHOOSER_REQUEST_CODE)
                    true
                } catch (e: Exception) {
                    this@BannerWebActivity.filePathCallback = null
                    false
                }
            }

            override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
                if (customView != null) {
                    callback?.onCustomViewHidden()
                    return
                }
                customView = view
                customViewCallback = callback

                val decor = window.decorView as FrameLayout
                originalSystemUiVisibility = decor.systemUiVisibility
                decor.addView(view, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
                decor.systemUiVisibility = (
                        View.SYSTEM_UI_FLAG_FULLSCREEN or
                                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        )
                actionBar?.hide()
            }

            override fun onHideCustomView() {
                val decor = window.decorView as FrameLayout
                customView?.let { decor.removeView(it) }
                customView = null
                decor.systemUiVisibility = originalSystemUiVisibility
                customViewCallback?.onCustomViewHidden()
                customViewCallback = null
                actionBar?.show()
            }
        }

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                val url = request?.url.toString()
                Log.d("BannerWebActivity", "URL click: $url")

                return when {
                    url.startsWith("dln://") -> {
                        val newUrl = url.replace("dln://", "https://")
                        openExternalBrowser(newUrl)
                        true
                    }
                    url.startsWith("tg://") || url.startsWith("viber://") ||
                            url.startsWith("whatsapp://") || url.startsWith("privat24:") ||
                            url.startsWith("https://diia.app") -> {
                        try {
                            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                        } catch (e: ActivityNotFoundException) {
                            Toast.makeText(this@BannerWebActivity, "Приложение не найдено", Toast.LENGTH_SHORT).show()
                        }
                        true
                    }
                    else -> false
                }
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                if (request?.isForMainFrame == true) {
                    val failingUrl = request.url.toString()
                    val description = error?.description?.toString() ?: "Неизвестная ошибка"
                    val errorCode = error?.errorCode

                    Log.e("WebViewError", "Ошибка при переходе по $failingUrl: $description (код $errorCode)")

                    Toast.makeText(
                        this@BannerWebActivity,
                        "Не удалось открыть ссылку:\n$failingUrl\n\nПричина: $description",
                        Toast.LENGTH_LONG
                    ).show()

                    // Не вызываем stopLoading() и не скрываем WebView — пусть пользователь продолжает навигацию
                }
            }

            override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
                handler?.proceed() // В реальном приложении лучше показывать предупреждение!
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FILE_CHOOSER_REQUEST_CODE) {
            val results = if (resultCode == RESULT_OK && data != null) {
                WebChromeClient.FileChooserParams.parseResult(resultCode, data)
            } else null
            filePathCallback?.onReceiveValue(results)
            filePathCallback = null
        }
    }

    override fun onPause() {
        super.onPause()
        webView.onPause()
    }

    override fun onResume() {
        super.onResume()
        webView.onResume()
        if (!isOnline()) showNoInternet()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        webView.saveState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        webView.restoreState(savedInstanceState)
    }

    private fun showNoInternet() {
//        webView.visibility = View.GONE
        noInternetLayout.visibility = View.VISIBLE
    }

    private fun showWebView() {
        webView.visibility = View.VISIBLE
        noInternetLayout.visibility = View.GONE
    }

    private fun isOnline(): Boolean {
        val cm = getSystemService<ConnectivityManager>()
        val network = cm?.activeNetwork ?: return false
        val capabilities = cm.getNetworkCapabilities(network)
        return capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }

    private fun openExternalBrowser(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }
}
