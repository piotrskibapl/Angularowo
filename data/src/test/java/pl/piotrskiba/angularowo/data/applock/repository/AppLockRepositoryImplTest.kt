package pl.piotrskiba.angularowo.data.applock.repository

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import pl.piotrskiba.angularowo.domain.applock.model.AppLockConfigModel

class AppLockRepositoryImplTest {

    val firebaseRemoteConfig: FirebaseRemoteConfig = mockk()
    val tested = AppLockRepositoryImpl(firebaseRemoteConfig)

    @Test
    fun `SHOULD get app lock config`() {
        every { firebaseRemoteConfig.getString("app_lock_title") } returns "title"
        every { firebaseRemoteConfig.getString("app_lock_body") } returns "body"
        every { firebaseRemoteConfig.getLong("app_lock_start_timestamp") } returns 123L
        every { firebaseRemoteConfig.getLong("app_lock_end_timestamp") } returns 456L

        val result = tested.getAppLockConfig().test()

        result.assertValue(
            AppLockConfigModel(
                title = "title",
                body = "body",
                startTimestamp = 123L,
                endTimestamp = 456L,
            ),
        )
    }
}
