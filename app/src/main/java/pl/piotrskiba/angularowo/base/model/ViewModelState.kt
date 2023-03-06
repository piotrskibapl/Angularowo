package pl.piotrskiba.angularowo.base.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import pl.piotrskiba.angularowo.R
import retrofit2.HttpException
import java.io.IOException

sealed class ViewModelState {
    // TODO: separate fetch/send loading states
    object Loading : ViewModelState()
    object Loaded : ViewModelState()
    data class Error(val error: Throwable?) : ViewModelState()

    fun isLoading() = this is Loading

    fun isAnyError() = this is Error

    fun isNetworkError() = this is Error && error is IOException

    fun isApiError() = this is Error && error is HttpException

    @DrawableRes
    fun errorImage() = when {
        isNetworkError() -> R.drawable.ic_no_internet
        isApiError() -> R.drawable.ic_error
        else -> R.drawable.ic_mood_bad
    }

    @StringRes
    fun errorText() = when {
        isNetworkError() -> R.string.no_internet
        isApiError() -> R.string.server_error
        else -> R.string.unknown_error
    }
}

