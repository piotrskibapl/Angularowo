package pl.piotrskiba.angularowo.data.login.model

import org.junit.Test
import pl.piotrskiba.angularowo.domain.login.model.AccessToken

class AccessTokenRemoteTest {

    @Test
    fun `SHOULD map AccessTokenData to AccessToken WHEN toDomain called`() {
        val tested = AccessTokenRemote(
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
