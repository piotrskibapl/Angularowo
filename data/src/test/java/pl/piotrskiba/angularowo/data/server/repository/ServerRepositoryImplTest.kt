package pl.piotrskiba.angularowo.data.server.repository

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.reactivex.rxjava3.core.Single
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import pl.piotrskiba.angularowo.data.server.ServerApiService
import pl.piotrskiba.angularowo.data.server.model.ServerStatusRemote
import pl.piotrskiba.angularowo.data.server.model.toDomain
import pl.piotrskiba.angularowo.domain.server.model.ServerStatusModel

class ServerRepositoryImplTest {

    val serverApi: ServerApiService = mockk()
    val tested = ServerRepositoryImpl(serverApi)

    @BeforeEach
    fun setup() {
        mockkStatic(ServerStatusRemote::toDomain)
    }

    @AfterEach
    fun teardown() {
        unmockkAll()
    }

    @Test
    fun `SHOULD get server status`() {
        val serverStatus: ServerStatusModel = mockk()
        val serverStatusRemote: ServerStatusRemote = mockk {
            every { toDomain() } returns serverStatus
        }
        every { serverApi.getServerStatus() } returns Single.just(serverStatusRemote)

        val result = tested.getServerStatus().test()

        result.assertValue(serverStatus)
    }
}
