package pl.piotrskiba.angularowo.base.model

import android.content.Context
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import pl.piotrskiba.angularowo.R
import retrofit2.HttpException
import java.io.IOException

sealed class ViewModelState {
    sealed class Loading : ViewModelState() {

        object Fetch : Loading()
        object Send : Loading()
        object Refresh : Loading()
    }

    object Loaded : ViewModelState()
    data class Error(val error: Throwable?) : ViewModelState()

    fun fullscreenLoaderVisible() = isFetching() || isSending()

    fun isRefreshing() = this is Loading.Refresh

    fun isError() = this is Error

    private fun isNetworkError() = this is Error && error is IOException

    private fun isApiError() = this is Error && error is HttpException

    private fun isFetching() = this is Loading.Fetch

    private fun isSending() = this is Loading.Send

    @ColorInt
    fun loadingBackgroundColor(context: Context) =
        context.getColor(
            if (isFetching()) {
                R.color.windowBackground
            } else {
                R.color.windowBackgroundA50
            }
        )

    @DrawableRes
    fun errorImage() =
        when {
            isNetworkError() -> R.drawable.ic_no_internet
            isApiError() -> R.drawable.ic_error
            else -> R.drawable.ic_mood_bad
        }

    fun errorText(context: Context) =
        context.getString(
            when {
                isNetworkError() -> R.string.no_internet
                isApiError() -> R.string.server_error
                else -> R.string.unknown_error
            }
        )
}

