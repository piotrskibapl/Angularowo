package pl.piotrskiba.angularowo.base.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.createViewModelLazy
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.CreationExtras
import dagger.android.support.AndroidSupportInjection
import pl.piotrskiba.angularowo.base.viewmodel.LifecycleViewModel
import pl.piotrskiba.angularowo.utils.AnalyticsUtils
import javax.inject.Inject
import kotlin.reflect.KClass

private const val FIRST_CREATED_KEY = "firstCreated"

open class BaseFragment<out VM : LifecycleViewModel>(viewModelClass: KClass<VM>) : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    val viewModel by viewModel(viewModelClass) { viewModelFactory }
    private var firstCreated = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firstCreated = savedInstanceState?.getBoolean(FIRST_CREATED_KEY) ?: false
        if (!firstCreated) {
            viewModel.onFirstCreate()
            firstCreated = true
        }
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onResume() {
        super.onResume()

        if (context != null && this::class.qualifiedName != null && this::class.simpleName != null) {
            AnalyticsUtils.logScreenView(
                this::class.qualifiedName!!,
                this::class.simpleName!!
            )
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(FIRST_CREATED_KEY, firstCreated)
    }

    private fun Fragment.viewModel(
        clazz: KClass<VM>,
        ownerProducer: () -> ViewModelStoreOwner = { this },
        extrasProducer: () -> CreationExtras = { CreationExtras.Empty },
        factoryProducer: (() -> ViewModelProvider.Factory)? = null,
    ) = createViewModelLazy(clazz, { ownerProducer().viewModelStore }, extrasProducer, factoryProducer)
}
