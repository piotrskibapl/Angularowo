package pl.piotrskiba.angularowo.domain.login.model

import io.mockk.every
import io.mockk.mockk
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import retrofit2.HttpException
import java.io.IOException

class AccessTokenErrorTest {

    companion object {

        @JvmStatic
        fun parameters() = listOf(
            mockk<HttpException> { every { code() } returns 403 } to AccessTokenError.CodeExpiredError,
            mockk<HttpException> { every { code() } returns 404 } to AccessTokenError.CodeNotFoundError,
            mockk<HttpException> { every { code() } returns 500 } to AccessTokenError.UnknownError,
            IOException() to AccessTokenError.UnknownError,
        ).map { Arguments.of(it.first, it.second) }
    }

    @ParameterizedTest
    @MethodSource("parameters")
    fun `SHOULD map Throwable to AccessTokenError`(
        throwable: Throwable,
        expectedAccessTokenError: AccessTokenError,
    ) {
        val result = throwable.toAccessTokenError()

        result shouldBeEqualTo expectedAccessTokenError
    }
}
