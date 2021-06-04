package pl.piotrskiba.angularowo.base.model

import retrofit2.HttpException
import java.io.IOException

sealed class ViewModelState {
    object Loading : ViewModelState()
    object Loaded : ViewModelState()
    data class Error(val error: Throwable?) : ViewModelState()

    fun isLoading() = this is Loading
    fun isNetworkError() = this is Error && error is IOException
    fun isApiError() = this is Error && error is HttpException
    fun isUnknownError() = this is Error && error !is IOException && error !is HttpException
}
