package pl.piotrskiba.angularowo.data.network.interceptors

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.amshove.kluent.assertSoftly
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test
import pl.piotrskiba.angularowo.data.BuildConfig
import pl.piotrskiba.angularowo.domain.base.preferences.repository.PreferencesRepository

class AuthInterceptorTest {

    val preferencesRepository: PreferencesRepository = mockk()
    val tested = AuthInterceptor(preferencesRepository)

    @Test
    fun `SHOULD add api_key query parameter WHEN domain matches`() {
        every { preferencesRepository.accessToken().blockingGet() } returns null
        val newUrl: HttpUrl = mockk()
        val newUrlBuilder: HttpUrl.Builder = mockk {
            every { addQueryParameter("api_key", BuildConfig.API_KEY) } returns this
            every { build() } returns newUrl
        }
        val newRequest: Request = mockk()
        val newRequestBuilder: Request.Builder = mockk {
            every { url(newUrl) } returns this
            every { build() } returns newRequest
        }
        val newRequestResponse: Response = mockk()
        val chain: Interceptor.Chain = mockk {
            every { request() } returns mockk {
                every { url.host } returns "api.angularowo.pl"
                every { url.newBuilder() } returns newUrlBuilder
                every { newBuilder() } returns newRequestBuilder
            }
            every { proceed(newRequest) } returns newRequestResponse
        }

        val result = tested.intercept(chain)

        assertSoftly {
            verify { newUrlBuilder.addQueryParameter("api_key", BuildConfig.API_KEY) }
            verify { newRequestBuilder.url(newUrl) }
            result shouldBeEqualTo newRequestResponse
        }
    }

    @Test
    fun `SHOULD add access_token query parameter WHEN domain matches AND access token is not null`() {
        every { preferencesRepository.accessToken().blockingGet() } returns "access_token"
        val newUrl: HttpUrl = mockk()
        val newUrlBuilder: HttpUrl.Builder = mockk {
            every { addQueryParameter("api_key", any()) } returns this
            every { addQueryParameter("access_token", "access_token") } returns this
            every { build() } returns newUrl
        }
        val newRequest: Request = mockk()
        val newRequestBuilder: Request.Builder = mockk {
            every { url(newUrl) } returns this
            every { build() } returns newRequest
        }
        val newRequestResponse: Response = mockk()
        val chain: Interceptor.Chain = mockk {
            every { request() } returns mockk {
                every { url.host } returns "api.angularowo.pl"
                every { url.newBuilder() } returns newUrlBuilder
                every { newBuilder() } returns newRequestBuilder
            }
            every { proceed(newRequest) } returns newRequestResponse
        }

        val result = tested.intercept(chain)

        assertSoftly {
            verify { newUrlBuilder.addQueryParameter("access_token", "access_token") }
            verify { newRequestBuilder.url(newUrl) }
            result shouldBeEqualTo newRequestResponse
        }
    }

    @Test
    fun `SHOULD NOT add access_token query parameter WHEN domain matches AND access_token is null`() {
        every { preferencesRepository.accessToken().blockingGet() } returns null
        val newUrl: HttpUrl = mockk()
        val newUrlBuilder: HttpUrl.Builder = mockk {
            every { addQueryParameter("api_key", any()) } returns this
            every { build() } returns newUrl
        }
        val newRequest: Request = mockk()
        val newRequestBuilder: Request.Builder = mockk {
            every { url(newUrl) } returns this
            every { build() } returns newRequest
        }
        val newRequestResponse: Response = mockk()
        val chain: Interceptor.Chain = mockk {
            every { request() } returns mockk {
                every { url.host } returns "api.angularowo.pl"
                every { url.newBuilder() } returns newUrlBuilder
                every { newBuilder() } returns newRequestBuilder
            }
            every { proceed(newRequest) } returns newRequestResponse
        }

        val result = tested.intercept(chain)

        assertSoftly {
            verify(exactly = 0) { newUrlBuilder.addQueryParameter("access_token", any()) }
            result shouldBeEqualTo newRequestResponse
        }
    }

    @Test
    fun `SHOULD NOT modify query parameters WHEN domain does not match`() {
        val request: Request = mockk {
            every { url.host } returns "google.com"
        }
        val requestResponse: Response = mockk()
        val chain: Interceptor.Chain = mockk {
            every { request() } returns request
            every { proceed(request) } returns requestResponse
        }

        val result = tested.intercept(chain)

        assertSoftly {
            result shouldBeEqualTo requestResponse
        }
    }
}
