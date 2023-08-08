package pl.piotrskiba.angularowo.data.friend.repository

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import org.amshove.kluent.assertSoftly
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import pl.piotrskiba.angularowo.data.friend.dao.FriendDao
import pl.piotrskiba.angularowo.data.friend.model.FriendEntity
import pl.piotrskiba.angularowo.data.friend.model.toDomain
import pl.piotrskiba.angularowo.data.friend.model.toRemote
import pl.piotrskiba.angularowo.domain.friend.model.FriendModel

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FriendRepositoryImplTest {

    val friendDao: FriendDao = mockk()
    val tested = FriendRepositoryImpl(friendDao)

    @BeforeAll
    fun setup() {
        mockkStatic(
            List<FriendEntity>::toDomain,
            FriendModel::toRemote,
        )
    }

    @AfterAll
    fun teardown() {
        unmockkAll()
    }

    @Test
    fun `SHOULD return all friends`() {
        val friendModels: List<FriendModel> = listOf(mockk())
        val friendEntities: List<FriendEntity> = listOf(mockk())
        every { friendEntities.toDomain() } returns friendModels
        every { friendDao.getAllFriends() } returns Observable.just(friendEntities)

        val result = tested.getAllFriends().test()

        result.assertValue(friendModels)
    }

    @Test
    fun `SHOULD insert`() {
        val friendEntity: FriendEntity = mockk()
        val friendModel: FriendModel = mockk {
            every { toRemote() } returns friendEntity
        }
        every { friendDao.insert(friendEntity) } returns Completable.complete()

        val result = tested.insert(friendModel).test()

        assertSoftly {
            verify { friendDao.insert(friendEntity) }
            result.assertComplete()
        }
    }

    @Test
    fun `SHOULD delete`() {
        val friendEntity: FriendEntity = mockk()
        val friendModel: FriendModel = mockk {
            every { toRemote() } returns friendEntity
        }
        every { friendDao.delete(friendEntity) } returns Completable.complete()

        val result = tested.delete(friendModel).test()

        assertSoftly {
            verify { friendDao.delete(friendEntity) }
            result.assertComplete()
        }
    }
}
