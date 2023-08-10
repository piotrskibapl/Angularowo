package pl.piotrskiba.angularowo.data.server.model

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import pl.piotrskiba.angularowo.domain.server.model.MotdModel
import pl.piotrskiba.angularowo.domain.server.model.ServerStatusModel

class ServerStatusRemoteTest {

    @BeforeEach
    fun setup() {
        mockkStatic(MotdRemote::toDomain)
    }

    @AfterEach
    fun teardown() {
        unmockkAll()
    }

    @Test
    fun `SHOULD map remote object to domain`() {
        val motd: MotdModel = mockk()
        val motdRemote: MotdRemote = mockk {
            every { toDomain() } returns motd
        }
        val tested = ServerStatusRemote(
            player_count = 10,
            tps_5 = 5.0,
            motd = motdRemote,
        )

        tested.toDomain() shouldBeEqualTo ServerStatusModel(
            playerCount = 10,
            tps = 5.0,
            motd = motd,
        )
    }
}
