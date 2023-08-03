package pl.piotrskiba.angularowo.data.login.model

import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test
import pl.piotrskiba.angularowo.domain.login.model.AccessTokenModel

class AccessTokenRemoteTest {

    @Test
    fun `SHOULD map remote object to domain`() {
        val tested = AccessTokenRemote(
            uuid = "uuid",
            username = "username",
            access_token = "accessToken",
            message = "message",
        )

        tested.toDomain() shouldBeEqualTo AccessTokenModel(
            uuid = "uuid",
            username = "username",
            accessToken = "accessToken",
            message = "message",
        )
    }
}
