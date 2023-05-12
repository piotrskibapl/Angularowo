package pl.piotrskiba.angularowo.base.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import dagger.android.AndroidInjection
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import pl.piotrskiba.angularowo.base.viewmodel.LifecycleViewModel
import pl.piotrskiba.angularowo.utils.AnalyticsUtils
import javax.inject.Inject
import kotlin.reflect.KClass

private const val FIRST_CREATED_KEY = "firstCreated"

open class BaseActivity<out VM : LifecycleViewModel>(viewModelClass: KClass<VM>) : AppCompatActivity(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    val viewModel by viewModel(viewModelClass) { viewModelFactory }
    private var firstCreated = false

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        firstCreated = savedInstanceState?.getBoolean(FIRST_CREATED_KEY) ?: false
        if (!firstCreated) {
            viewModel.intent = intent
            firstCreated = true
            viewModel.onFirstCreate()
        }
    }

    override fun onResume() {
        super.onResume()
        if (this::class.qualifiedName != null && this::class.simpleName != null) {
            AnalyticsUtils().logScreenView(
                this::class.qualifiedName!!,
                this::class.simpleName!!
            )
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(FIRST_CREATED_KEY, firstCreated)
    }

    override fun androidInjector() = dispatchingAndroidInjector

    private fun <VM : ViewModel> AppCompatActivity.viewModel(
        clazz: KClass<VM>,
        ownerProducer: () -> ViewModelStoreOwner = { this },
        factoryProducer: (() -> ViewModelProvider.Factory)? = null,
    ): Lazy<VM> {
        val factoryPromise = factoryProducer ?: {
            defaultViewModelProviderFactory
        }
        return ViewModelLazy(clazz, { ownerProducer().viewModelStore }, factoryPromise)
    }
}
