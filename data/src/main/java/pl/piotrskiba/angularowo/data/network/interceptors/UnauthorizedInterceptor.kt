package pl.piotrskiba.angularowo.data.network.interceptors

import io.reactivex.rxjava3.subjects.PublishSubject
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

class UnauthorizedInterceptor @Inject constructor() : Interceptor {

    val unauthorizedResponses: PublishSubject<Unit> = PublishSubject.create()

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        if (response.code == 401) {
            unauthorizedResponses.onNext(Unit)
        }
        return response
    }
}
