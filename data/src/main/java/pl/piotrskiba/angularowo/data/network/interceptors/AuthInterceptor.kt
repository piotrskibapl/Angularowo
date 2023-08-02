package pl.piotrskiba.angularowo.data.network.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import pl.piotrskiba.angularowo.data.BuildConfig

private const val API_HOST = "piotrskiba.pl"

class AuthInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        if (request.url.host == API_HOST) {
            val newUrl = request.url.newBuilder()
                .addQueryParameter("api_key", BuildConfig.API_KEY)
                .build()
            val newRequest = request.newBuilder()
                .url(newUrl)
                .build()
            return chain.proceed(newRequest)
        }
        return chain.proceed(request)
    }
}
