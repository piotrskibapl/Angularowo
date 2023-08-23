package pl.piotrskiba.angularowo.domain.friend.usecase

import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.core.Completable
import org.junit.jupiter.api.Test
import pl.piotrskiba.angularowo.domain.friend.model.FriendModel
import pl.piotrskiba.angularowo.domain.friend.repository.FriendRepository

class MarkPlayerAsFavoriteUseCaseTest {

    val friendRepository: FriendRepository = mockk()
    val tested = MarkPlayerAsFavoriteUseCase(friendRepository)

    @Test
    fun `SHOULD insert model to database`() {
        val uuid = "uuid"
        every { friendRepository.insert(FriendModel(uuid)) } returns Completable.complete()

        val result = tested.execute(uuid).test()

        result.assertComplete()
    }
}
