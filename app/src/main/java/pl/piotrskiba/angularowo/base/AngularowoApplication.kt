package pl.piotrskiba.angularowo.base

import android.content.Context
import android.util.Log
import androidx.multidex.MultiDex
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import io.reactivex.rxjava3.exceptions.UndeliverableException
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import pl.piotrskiba.angularowo.base.di.AppComponent
import pl.piotrskiba.angularowo.base.di.DaggerAppComponent

class AngularowoApplication : DaggerApplication() {

    private lateinit var appComponent: AppComponent

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        appComponent = DaggerAppComponent.builder()
            .application(this)
            .build()
        return appComponent
    }

    override fun onCreate() {
        super.onCreate()
        setupRxErrorHandler()
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
