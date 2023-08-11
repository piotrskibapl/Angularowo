package pl.piotrskiba.angularowo.data.network.interceptors

import io.mockk.every
import io.mockk.mockk
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class UnauthorizedInterceptorTest {

    val tested = UnauthorizedInterceptor()

    @Test
    fun `SHOULD signal unauthorized response WHEN response code is 401`() {
        val request: Request = mockk()
        val response: Response = mockk {
            every { code } returns 401
        }
        val chain: Interceptor.Chain = mockk {
            every { request() } returns request
            every { proceed(request) } returns response
        }

        val result = tested.unauthorizedResponses.test()
        tested.intercept(chain)

        result.assertValueCount(1)
    }

    @ParameterizedTest
    @ValueSource(ints = [200, 400, 403, 404, 500])
    fun `SHOULD NOT signal unauthorized response WHEN response code is {0}`(responseCode: Int) {
        val request: Request = mockk()
        val response: Response = mockk {
            every { code } returns responseCode
        }
        val chain: Interceptor.Chain = mockk {
            every { request() } returns request
            every { proceed(request) } returns response
        }

        val result = tested.unauthorizedResponses.test()
        tested.intercept(chain)

        result.assertValueCount(0)
    }
}
