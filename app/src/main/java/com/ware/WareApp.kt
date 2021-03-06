package com.ware

//import com.facebook.stetho.Stetho
import android.content.Context
import android.util.Log
import androidx.multidex.MultiDexApplication
import com.ware.common.Utils
import dagger.hilt.android.HiltAndroidApp

//import com.facebook.stetho.Stetho;
//import com.facebook.stetho.okhttp3.StethoInterceptor;
/**
 * Created by jianfeng.li on 2017/12/29.
 */
@HiltAndroidApp
class WareApp : MultiDexApplication() {
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        Log.d("WareApp", "attachBaseContext: ")
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("WareApp", "onCreate: ")
        sContext = applicationContext
        Utils.init(applicationContext)
//        Stetho.initializeWithDefaults(this)
        //        Stetho.initializeWithDefaults(this);
//        new OkHttpClient.Builder()
//                .addNetworkInterceptor(new StethoInterceptor())
//                .build();
    }

    companion object {
        lateinit var sContext: Context
    }
}