package pl.piotrskiba.angularowo.network

import okhttp3.Interceptor
import okhttp3.Response
import pl.piotrskiba.angularowo.interfaces.UnauthorizedResponseListener
import java.io.IOException

class UnauthorizedInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        if (response.code() == 401) {
            unauthorizedListener.onUnauthorizedResponse()
        }
        return response
    }

    companion object {
        private lateinit var unauthorizedListener: UnauthorizedResponseListener
        @JvmStatic
        fun setUnauthorizedListener(listener: UnauthorizedResponseListener) {
            unauthorizedListener = listener
        }
    }
}