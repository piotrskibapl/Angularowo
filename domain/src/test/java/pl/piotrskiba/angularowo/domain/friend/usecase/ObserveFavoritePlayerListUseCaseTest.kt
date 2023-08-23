package pl.piotrskiba.angularowo.domain.friend.usecase

import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.core.Observable
import org.junit.jupiter.api.Test
import pl.piotrskiba.angularowo.domain.friend.model.FriendModel
import pl.piotrskiba.angularowo.domain.friend.repository.FriendRepository

class ObserveFavoritePlayerListUseCaseTest {

    val friendRepository: FriendRepository = mockk()
    val tested = ObserveFavoritePlayerListUseCase(friendRepository)

    @Test
    fun `SHOULD observe all friends`() {
        val friendModels: List<FriendModel> = mockk()
        every { friendRepository.getAllFriends() } returns Observable.just(friendModels)

        val result = tested.execute().test()

        result.assertValue(friendModels)
    }
}
