package pl.piotrskiba.angularowo.domain.player.usecase

import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import pl.piotrskiba.angularowo.domain.base.preferences.repository.PreferencesRepository
import pl.piotrskiba.angularowo.domain.player.model.DetailedPlayerModel
import pl.piotrskiba.angularowo.domain.player.repository.PlayerRepository
import pl.piotrskiba.angularowo.domain.rank.model.RankModel
import pl.piotrskiba.angularowo.domain.rank.repository.RankRepository

class GetAppUserPlayerUseCaseTest {

    val preferencesRepository: PreferencesRepository = mockk()
    val playerRepository: PlayerRepository = mockk()
    val rankRepository: RankRepository = mockk()
    val tested = GetAppUserPlayerUseCase(preferencesRepository, playerRepository, rankRepository)

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `SHOULD get detailed player with updated rank`(ignoreCache: Boolean) {
        val rankName = "admin"
        val repoRank: RankModel = mockk {
            every { name } returns rankName
        }
        val freshPlayerWithRank: DetailedPlayerModel = mockk()
        val freshPlayer: DetailedPlayerModel = mockk {
            every { rank.name } returns rankName
            every { copy(rank = repoRank) } returns freshPlayerWithRank
        }
        every { preferencesRepository.uuid() } returns Maybe.just("uuid")
        every { playerRepository.getPlayerDetailsFromUuid("uuid") } returns Single.just(freshPlayer)
        every { rankRepository.getAllRanks() } returns Single.just(listOf(repoRank))

        val result = tested.execute(ignoreCache = ignoreCache).test()

        result.assertValue(freshPlayerWithRank)
    }

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `SHOULD get detailed player without updated rank WHEN repo doesn't contain player rank`(ignoreCache: Boolean) {
        val freshPlayer: DetailedPlayerModel = mockk {
            every { rank.name } returns "admin"
            every { copy(rank = rank) } returns this
        }
        every { preferencesRepository.uuid() } returns Maybe.just("uuid")
        every { playerRepository.getPlayerDetailsFromUuid("uuid") } returns Single.just(freshPlayer)
        every { rankRepository.getAllRanks() } returns Single.just(emptyList())

        val result = tested.execute(ignoreCache = ignoreCache).test()

        result.assertValue(freshPlayer)
    }

    @Test
    fun `SHOULD get cached detailed player with updated rank WHEN cache not ignored AND called second time`() {
        val rankName = "admin"
        val repoRank: RankModel = mockk {
            every { name } returns rankName
        }
        val cachedPlayerWithRank: DetailedPlayerModel = mockk()
        val cachedPlayer: DetailedPlayerModel = mockk {
            every { rank.name } returns rankName
            every { copy(rank = repoRank) } returns cachedPlayerWithRank
        }
        val freshDetailedPlayer: DetailedPlayerModel = mockk()
        every { preferencesRepository.uuid() } returns Maybe.just("uuid")
        every { playerRepository.getPlayerDetailsFromUuid("uuid") } returns Single.just(cachedPlayer) andThen Single.just(freshDetailedPlayer)
        every { rankRepository.getAllRanks() } returns Single.just(listOf(repoRank))

        val firstResult = tested.execute(ignoreCache = false).test()
        val secondResult = tested.execute(ignoreCache = false).test()

        firstResult.assertValue(cachedPlayerWithRank)
        secondResult.assertValue(cachedPlayerWithRank)
    }

    @Test
    fun `SHOULD get fresh detailed player with updated rank WHEN cache ignored AND called second time`() {
        val rankName = "admin"
        val repoRank: RankModel = mockk {
            every { name } returns rankName
        }
        val cachedPlayerWithRank: DetailedPlayerModel = mockk()
        val cachedPlayer: DetailedPlayerModel = mockk {
            every { rank.name } returns rankName
            every { copy(rank = repoRank) } returns cachedPlayerWithRank
        }
        val freshPlayerWithRank: DetailedPlayerModel = mockk()
        val freshPlayer: DetailedPlayerModel = mockk {
            every { rank.name } returns rankName
            every { copy(rank = repoRank) } returns freshPlayerWithRank
        }
        every { preferencesRepository.uuid() } returns Maybe.just("uuid")
        every { playerRepository.getPlayerDetailsFromUuid("uuid") } returns Single.just(cachedPlayer) andThen Single.just(freshPlayer)
        every { rankRepository.getAllRanks() } returns Single.just(listOf(repoRank))

        val firstResult = tested.execute(ignoreCache = false).test()
        val secondResult = tested.execute(ignoreCache = true).test()

        firstResult.assertValue(cachedPlayerWithRank)
        secondResult.assertValue(freshPlayerWithRank)
    }
}
