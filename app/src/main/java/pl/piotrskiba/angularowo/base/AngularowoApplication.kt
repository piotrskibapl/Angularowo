package pl.piotrskiba.angularowo.base

import android.content.Context
import androidx.multidex.MultiDex
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import pl.piotrskiba.angularowo.base.di.AppComponent
import pl.piotrskiba.angularowo.base.di.DaggerAppComponent

class AngularowoApplication: DaggerApplication() {

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
}