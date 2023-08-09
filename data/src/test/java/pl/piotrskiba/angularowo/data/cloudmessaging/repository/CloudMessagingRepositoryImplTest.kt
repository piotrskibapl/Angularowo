package pl.piotrskiba.angularowo.data.cloudmessaging.repository

import com.google.firebase.messaging.FirebaseMessaging
import io.mockk.mockk
import io.mockk.verify
import org.amshove.kluent.assertSoftly
import org.junit.jupiter.api.Test

class CloudMessagingRepositoryImplTest {

    val firebaseMessaging: FirebaseMessaging = mockk(relaxed = true)
    val tested = CloudMessagingRepositoryImpl(firebaseMessaging)

    @Test
    fun `SHOULD subscribe to app version`() {
        val result = tested.subscribeToAppVersion(123).test()

        assertSoftly {
            verify { firebaseMessaging.subscribeToTopic("version_123") }
            result.assertComplete()
        }
    }

    @Test
    fun `SHOULD unsubscribe from app version`() {
        val result = tested.unsubscribeFromAppVersion(123).test()

        assertSoftly {
            verify { firebaseMessaging.unsubscribeFromTopic("version_123") }
            result.assertComplete()
        }
    }

    @Test
    fun `SHOULD subscribe to player uuid`() {
        val result = tested.subscribeToPlayerUuid("uuid").test()

        assertSoftly {
            verify { firebaseMessaging.subscribeToTopic("player_uuid") }
            result.assertComplete()
        }
    }

    @Test
    fun `SHOULD unsubscribe from player uuid`() {
        val result = tested.unsubscribeFromPlayerUuid("uuid").test()

        assertSoftly {
            verify { firebaseMessaging.unsubscribeFromTopic("player_uuid") }
            result.assertComplete()
        }
    }

    @Test
    fun `SHOULD subscribe to player rank`() {
        val result = tested.subscribeToPlayerRank("admin").test()

        assertSoftly {
            verify { firebaseMessaging.subscribeToTopic("rank_admin") }
            result.assertComplete()
        }
    }

    @Test
    fun `SHOULD unsubscribe from player rank`() {
        val result = tested.unsubscribeFromPlayerRank("admin").test()

        assertSoftly {
            verify { firebaseMessaging.unsubscribeFromTopic("rank_admin") }
            result.assertComplete()
        }
    }

    @Test
    fun `SHOULD subscribe to new events`() {
        val result = tested.subscribeToNewEvents().test()

        assertSoftly {
            verify { firebaseMessaging.subscribeToTopic("new_event") }
            result.assertComplete()
        }
    }

    @Test
    fun `SHOULD unsubscribe from new events`() {
        val result = tested.unsubscribeFromNewEvents().test()

        assertSoftly {
            verify { firebaseMessaging.unsubscribeFromTopic("new_event") }
            result.assertComplete()
        }
    }

    @Test
    fun `SHOULD subscribe to private messages`() {
        val result = tested.subscribeToPrivateMessages().test()

        assertSoftly {
            verify { firebaseMessaging.subscribeToTopic("private_messages") }
            result.assertComplete()
        }
    }

    @Test
    fun `SHOULD unsubscribe from private messages`() {
        val result = tested.unsubscribeFromPrivateMessages().test()

        assertSoftly {
            verify { firebaseMessaging.unsubscribeFromTopic("private_messages") }
            result.assertComplete()
        }
    }

    @Test
    fun `SHOULD subscribe to account incidents`() {
        val result = tested.subscribeToAccountIncidents().test()

        assertSoftly {
            verify { firebaseMessaging.subscribeToTopic("account_incidents") }
            result.assertComplete()
        }
    }

    @Test
    fun `SHOULD unsubscribe from account incidents`() {
        val result = tested.unsubscribeFromAccountIncidents().test()

        assertSoftly {
            verify { firebaseMessaging.unsubscribeFromTopic("account_incidents") }
            result.assertComplete()
        }
    }

    @Test
    fun `SHOULD subscribe to new reports`() {
        val result = tested.subscribeToNewReports().test()

        assertSoftly {
            verify { firebaseMessaging.subscribeToTopic("new_reports") }
            result.assertComplete()
        }
    }

    @Test
    fun `SHOULD unsubscribe from new reports`() {
        val result = tested.unsubscribeFromNewReports().test()

        assertSoftly {
            verify { firebaseMessaging.unsubscribeFromTopic("new_reports") }
            result.assertComplete()
        }
    }
}
