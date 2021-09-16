package pl.piotrskiba.angularowo.data.login.model

import org.junit.Test
import pl.piotrskiba.angularowo.domain.login.model.AccessToken

class AccessTokenDataTest {

    @Test
    fun `SHOULD map AccessTokenData to AccessToken WHEN toDomain called`() {
        val tested = AccessTokenData(
            "uuid",
            "username",
            "accessToken",
            "message"
        )

        val result = tested.toDomain()

        assert(result == AccessToken(
            "uuid",
            "username",
            "accessToken",
            "message"
        ))
    }
}
