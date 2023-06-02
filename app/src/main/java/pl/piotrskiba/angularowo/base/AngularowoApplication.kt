package pl.piotrskiba.angularowo.base

import android.content.Context
import android.util.Log
import androidx.multidex.MultiDex
import com.google.android.gms.ads.MobileAds
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import io.reactivex.rxjava3.exceptions.UndeliverableException
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.base.di.DaggerAppComponent
import pl.piotrskiba.angularowo.utils.NotificationUtils
import javax.inject.Inject

class AngularowoApplication : DaggerApplication() {

    @Inject
    lateinit var remoteConfig: FirebaseRemoteConfig

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
        DaggerAppComponent.builder()
            .application(this)
            .build()

    override fun onCreate() {
        super.onCreate()
        setupRxErrorHandler()
        remoteConfig.setDefaultsAsync(R.xml.remote_config_default_values)
        remoteConfig.fetchAndActivate()
        MobileAds.initialize(applicationContext)
        NotificationUtils(applicationContext).createNotificationChannels()
    }

    private fun setupRxErrorHandler() {
        RxJavaPlugins.setErrorHandler { error ->
            if (error !is UndeliverableException) {
                Thread.currentThread().uncaughtExceptionHandler?.uncaughtException(Thread.currentThread(), error)
            } else {
                Log.w(javaClass.name, "Undeliverable exception received: $error")
            }
        }
    }
}
