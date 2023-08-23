package pl.piotrskiba.angularowo.domain.friend.usecase

import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.core.Completable
import org.junit.jupiter.api.Test
import pl.piotrskiba.angularowo.domain.friend.model.FriendModel
import pl.piotrskiba.angularowo.domain.friend.repository.FriendRepository

class UnmarkPlayerAsFavoriteUseCaseTest {

    val friendRepository: FriendRepository = mockk()
    val tested = UnmarkPlayerAsFavoriteUseCase(friendRepository)

    @Test
    fun `SHOULD delete model from database`() {
        val uuid = "uuid"
        every { friendRepository.delete(FriendModel(uuid)) } returns Completable.complete()

        val result = tested.execute(uuid).test()

        result.assertComplete()
    }
}
