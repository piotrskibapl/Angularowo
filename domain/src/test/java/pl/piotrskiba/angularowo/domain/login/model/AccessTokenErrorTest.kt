package pl.piotrskiba.angularowo.domain.login.model

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import retrofit2.HttpException
import java.io.IOException

class AccessTokenErrorTest {

    companion object {

        @JvmStatic
        fun parameters() = listOf(
            Pair(
                mockk<HttpException> { every { code() } returns 403 },
                AccessTokenError.CodeExpiredError,
            ),
            Pair(
                mockk<HttpException> { every { code() } returns 404 },
                AccessTokenError.CodeNotFoundError,
            ),
            Pair(
                mockk<HttpException> { every { code() } returns 500 },
                AccessTokenError.UnknownError,
            ),
            Pair(
                IOException(),
                AccessTokenError.UnknownError,
            ),
        ).map { Arguments.of(it.first, it.second) }
    }

    @ParameterizedTest
    @MethodSource("parameters")
    fun `SHOULD map Throwable to AccessTokenError WHEN toAccessTokenError called`(
        throwable: Throwable,
        expectedAccessTokenError: AccessTokenError,
    ) {
        val result = throwable.toAccessTokenError()

        assert(result == expectedAccessTokenError)
    }
}
