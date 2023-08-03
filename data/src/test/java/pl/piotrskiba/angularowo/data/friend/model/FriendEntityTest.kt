package pl.piotrskiba.angularowo.data.friend.model

import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test
import pl.piotrskiba.angularowo.domain.friend.model.FriendModel

class FriendEntityTest {

    @Test
    fun `SHOULD map remote object to domain`() {
        val tested = FriendEntity("uuid")

        tested.toDomain() shouldBeEqualTo FriendModel("uuid")
    }

    @Test
    fun `SHOULD map list of remote objects to domain`() {
        val tested = listOf(FriendEntity("uuid"))

        tested.toDomain() shouldBeEqualTo listOf(FriendModel("uuid"))
    }

    @Test
    fun `SHOULD map domain object to remote`() {
        val tested = FriendModel("uuid")

        tested.toRemote() shouldBeEqualTo FriendEntity("uuid")
    }
}
