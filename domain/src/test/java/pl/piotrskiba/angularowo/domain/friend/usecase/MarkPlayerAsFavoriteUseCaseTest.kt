package pl.piotrskiba.angularowo.domain.friend.usecase

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import org.amshove.kluent.assertSoftly
import org.junit.jupiter.api.Test
import pl.piotrskiba.angularowo.domain.analytics.repository.AnalyticsRepository
import pl.piotrskiba.angularowo.domain.friend.model.FriendModel
import pl.piotrskiba.angularowo.domain.friend.repository.FriendRepository
import pl.piotrskiba.angularowo.domain.player.model.DetailedPlayerModel
import pl.piotrskiba.angularowo.domain.player.usecase.GetAppUserPlayerUseCase

class MarkPlayerAsFavoriteUseCaseTest {

    val friendRepository: FriendRepository = mockk()
    private val analyticsRepository: AnalyticsRepository = mockk()
    private val getAppUserPlayerUseCase: GetAppUserPlayerUseCase = mockk()
    val tested = MarkPlayerAsFavoriteUseCase(friendRepository, analyticsRepository, getAppUserPlayerUseCase)

    @Test
    fun `SHOULD insert model to database AND log analytics event`() {
        val targetUuid = "uuid"
        val targetUsername = "username"
        val playerUuid = "playerUuid"
        val playerUsername = "playerUsername"
        val detailedPlayerModel: DetailedPlayerModel = mockk {
            every { uuid } returns playerUuid
            every { username } returns playerUsername
        }
        every { friendRepository.insert(FriendModel(targetUuid)) } returns Completable.complete()
        every { getAppUserPlayerUseCase.execute(ignoreCache = false) } returns Single.just(detailedPlayerModel)
        every { analyticsRepository.logFavorite(playerUuid, playerUsername, targetUuid, targetUsername) } returns Completable.complete()

        val result = tested.execute(targetUuid, targetUsername).test()

        assertSoftly {
            result.assertComplete()
            verify { friendRepository.insert(FriendModel(targetUuid)) }
            verify { analyticsRepository.logFavorite(playerUuid, playerUsername, targetUuid, targetUsername) }
        }
    }
}
