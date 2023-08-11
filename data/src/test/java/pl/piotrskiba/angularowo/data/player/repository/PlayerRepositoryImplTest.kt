package pl.piotrskiba.angularowo.data.player.repository

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.reactivex.rxjava3.core.Single
import org.amshove.kluent.assertSoftly
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import pl.piotrskiba.angularowo.data.player.PlayerApiService
import pl.piotrskiba.angularowo.data.player.model.DetailedPlayerRemote
import pl.piotrskiba.angularowo.data.player.model.PlayerRemote
import pl.piotrskiba.angularowo.data.player.model.toDomain
import pl.piotrskiba.angularowo.domain.player.model.DetailedPlayerModel
import pl.piotrskiba.angularowo.domain.player.model.PlayerModel

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PlayerRepositoryImplTest {

    val playerApi: PlayerApiService = mockk()
    val tested = PlayerRepositoryImpl(playerApi)

    @BeforeAll
    fun setup() {
        mockkStatic(
            DetailedPlayerRemote::toDomain,
            List<PlayerRemote>::toDomain,
        )
    }

    @AfterAll
    fun teardown() {
        unmockkAll()
    }

    @Test
    fun `SHOULD get player details from username`() {
        val detailedPlayer: DetailedPlayerModel = mockk()
        val detailedPlayerRemote: DetailedPlayerRemote = mockk {
            every { toDomain() } returns detailedPlayer
        }
        every { playerApi.getPlayerInfoFromUsername("username") } returns Single.just(detailedPlayerRemote)

        val result = tested.getPlayerDetailsFromUsername("username").test()

        result.assertValue(detailedPlayer)
    }

    @Test
    fun `SHOULD get player details from uuid`() {
        val detailedPlayer: DetailedPlayerModel = mockk()
        val detailedPlayerRemote: DetailedPlayerRemote = mockk {
            every { toDomain() } returns detailedPlayer
        }
        every { playerApi.getPlayerInfoFromUuid("uuid") } returns Single.just(detailedPlayerRemote)

        val result = tested.getPlayerDetailsFromUuid("uuid").test()

        result.assertValue(detailedPlayer)
    }

    @Test
    fun `SHOULD observe AND refresh online player list`() {
        val players1: List<PlayerModel> = mockk()
        val players2: List<PlayerModel> = mockk()
        val playersRemote1: List<PlayerRemote> = mockk {
            every { toDomain() } returns players1
        }
        val playersRemote2: List<PlayerRemote> = mockk {
            every { toDomain() } returns players2
        }
        every { playerApi.getOnlinePlayerList() } returns Single.just(playersRemote1) andThen Single.just(playersRemote2)

        val result = tested.observeOnlinePlayerList().test()
        tested.refreshOnlinePlayerList().blockingAwait()
        tested.refreshOnlinePlayerList().blockingAwait()

        assertSoftly {
            result.assertValueCount(2)
            result.assertValueAt(0, players1)
            result.assertValueAt(1, players2)
            result.assertNotComplete()
        }
    }
}
