package com.animgames.cave

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import com.animgames.cave.data.AdsHelper
import com.animgames.cave.data.SharPreferences
import com.animgames.cave.data.WebCallback
import com.animgames.cave.receive.MyNotification
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import com.appsflyer.AppsFlyerLibCore
import kotlinx.coroutines.*
import java.lang.Runnable
import java.util.*

class MenuActivity : AppCompatActivity(), WebCallback {

    var pref : SharPreferences? = null
    var job : Job? = null
    var run = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        window.statusBarColor = Color.BLACK
        initAppSflyaer()
        val helper = AdsHelper(this)
        helper.attachWeb(this)
        pref = SharPreferences(this).apply { getSp("url") }
        val url = pref!!.getStr("url")
        if(url != null && url != "") chrome(url, false)
        //if(helper.url != null && helper.url != "") chrome(helper.url!!, false)
    }

    fun initAppSflyaer(){
        val conversionListener: AppsFlyerConversionListener = object : AppsFlyerConversionListener {
            override fun onConversionDataSuccess(conversionData: Map<String, Any>) {
                for (attrName in conversionData.keys) Log.e(
                    AppsFlyerLibCore.LOG_TAG,
                    "Conversion attribute: " + attrName + " = " + conversionData[attrName]
                )
                //TODO - remove this
                //TODO - remove this
                val status: String =
                    Objects.requireNonNull(conversionData["af_status"]).toString()
                if (status == "Non-organic") {
                    if (Objects.requireNonNull(conversionData["is_first_launch"]).toString()
                            .equals("true")
                    ) {
                        Log.e(AppsFlyerLibCore.LOG_TAG, "Conversion: First Launch")
                        if (conversionData.containsKey("af_adset")) {
                            MyNotification().scheduleMsg(this@MenuActivity)
                            chrome(conversionData["af_adset"] as String, true)
                        }
                    } else {
                        Log.e(AppsFlyerLibCore.LOG_TAG, "Conversion: Not First Launch")
                        val handler = Handler(mainLooper)
                        handler.post(Runnable { goToGame() })
                    }
                } else {
                    Log.e(AppsFlyerLibCore.LOG_TAG, "Conversion: This is an organic install.")
                    val handler = Handler(mainLooper)
                    if (conversionData.containsKey("af_adset")) {
                        MyNotification().scheduleMsg(this@MenuActivity)
                        chrome(conversionData["af_adset"] as String, true)
                    }
                    handler.post(Runnable { goToGame() })
                }
            }

            override fun onConversionDataFail(errorMessage: String) {
                Log.d("LOG_TAG", "error getting conversion data: $errorMessage")
                val handler = Handler(mainLooper)
                handler.post(Runnable { goToGame() })
            }

            override fun onAppOpenAttribution(attributionData: Map<String, String>) {
                for (attrName in attributionData.keys) {
                    Log.d(
                        "LOG_TAG",
                        "attribute: " + attrName + " = " + attributionData[attrName]
                    )
                }
                attributionData["af_adset"]?.let { chrome(it) }
            }

            override fun onAttributionFailure(errorMessage: String) {
                Log.d("LOG_TAG", "error onAttributionFailure : $errorMessage")
                val handler = Handler(mainLooper)
                handler.post(Runnable { goToGame() })
            }
        }
        AppsFlyerLib.getInstance().init(App.AF_DEV_KEY, conversionListener, applicationContext)
        AppsFlyerLib.getInstance().startTracking(this)
    }

    fun goToGame(){
        job = GlobalScope.launch(Dispatchers.Main) {
            delay(3000)
            if(!run){
                startActivity(Intent(this@MenuActivity, MainActivity::class.java))
            }
        }
    }

    override fun chrome(url : String, first : Boolean){
        run = true
        Log.e("Deep", url)
        if(first) pref?.putStr("url", url)
        val builder = CustomTabsIntent.Builder()
        builder.setToolbarColor(ContextCompat.getColor(this, R.color.black))
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(url))
        job?.cancel()
        finish()
    }
}