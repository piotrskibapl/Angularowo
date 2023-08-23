package pl.piotrskiba.angularowo.domain.friend.usecase

import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.core.Observable
import org.amshove.kluent.assertSoftly
import org.junit.jupiter.api.Test
import pl.piotrskiba.angularowo.domain.friend.model.FriendModel
import pl.piotrskiba.angularowo.domain.friend.repository.FriendRepository

class ObserveIfPlayerIsFavoriteUseCaseTest {

    val friendRepository: FriendRepository = mockk()
    val tested = ObserveIfPlayerIsFavoriteUseCase(friendRepository)

    @Test
    fun `SHOULD observe if player is favorite`() {
        val friendModelsWithFavoritePlayer: List<FriendModel> = listOf(
            mockk { every { uuid } returns "uuid1" },
        )
        val friendModelsWithoutFavoritePlayer: List<FriendModel> = listOf(
            mockk { every { uuid } returns "uuid2" },
        )
        every { friendRepository.getAllFriends() } returns Observable.just(friendModelsWithFavoritePlayer, friendModelsWithoutFavoritePlayer)

        val result = tested.execute("uuid1").test()

        assertSoftly {
            result.assertValueAt(0, true)
            result.assertValueAt(1, false)
        }
    }
}
