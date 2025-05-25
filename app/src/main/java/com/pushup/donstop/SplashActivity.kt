package com.pushup.donstop

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class SplashActivity : AppCompatActivity() {

    private lateinit var bannerView: ImageView
    private lateinit var progressBar: ProgressBar
    private val prefs: SharedPreferences by lazy {
        getSharedPreferences("banner_prefs", MODE_PRIVATE)
    }

    private val handler = Handler(Looper.getMainLooper())
    private val goToMainRunnable = Runnable { goToMain() }

    private val client = OkHttpClient.Builder()
        .callTimeout(15, java.util.concurrent.TimeUnit.SECONDS)
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        bannerView = findViewById(R.id.bannerImageView)
        progressBar = findViewById(R.id.progressBar)

        if (isOnline()) {
            val cachedJson = prefs.getString("banner_json", null)

            if (cachedJson != null) {
                showBanner(JSONObject(cachedJson))
            } else {
                fetchBanner()
            }
        } else {
            goToMain()
        }
    }

    private fun fetchBanner() {
        val request = Request.Builder()
            .url("https://on.candyworld.site/areyouready") // или demo ссылка
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                goToMain()
            }

            override fun onResponse(call: Call, response: Response) {
                val json = response.body?.string()

                Log.d("BannerResponse", "Raw response: $json")

                if (json != null) {
                    val obj = JSONObject(json)
                    prefs.edit().putString("banner_json", json).apply()
                    runOnUiThread { showBanner(obj) }
                } else {
                    goToMain()
                }
            }
        })
    }

    private fun showBanner(json: JSONObject) {
        val action = json.optString("goto", null)
        val source = json.optString("isReady", null)

        Log.d("BannerURL", "Goto URL: $action")

        if (source != null) {
            Glide.with(this)
                .load(source)
                .centerCrop()
                .timeout(15000000)
                .into(object : CustomTarget<Drawable>() {
                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                        bannerView.setImageDrawable(resource)
                        bannerView.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE

                        bannerView.setOnClickListener {
                            handler.removeCallbacks(goToMainRunnable)

                            if (action != null && action.startsWith("https://")) {
                                val intent = Intent(this@SplashActivity, BannerWebActivity::class.java)
                                intent.putExtra("url", action)
                                startActivity(intent)
                            } else {
                                goToMain()
                            }
                            finish()
                        }
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {}
                    override fun onLoadFailed(errorDrawable: Drawable?) {
                        goToMain()
                    }
                })

            // Запуск управляемого таймера
            handler.postDelayed(goToMainRunnable, 15000000)
        } else {
            goToMain()
        }
    }

    private fun goToMain() {
        runOnUiThread {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun isOnline(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as android.net.ConnectivityManager
        val network = cm.activeNetworkInfo
        return network != null && network.isConnected
    }
}
