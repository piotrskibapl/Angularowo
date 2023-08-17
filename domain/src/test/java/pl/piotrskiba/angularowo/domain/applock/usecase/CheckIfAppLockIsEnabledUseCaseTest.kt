package pl.piotrskiba.angularowo.domain.applock.usecase

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.reactivex.rxjava3.core.Single
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import pl.piotrskiba.angularowo.domain.applock.model.AppLockConfigModel
import pl.piotrskiba.angularowo.domain.applock.repository.AppLockRepository
import java.util.Calendar

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CheckIfAppLockIsEnabledUseCaseTest {

    val appLockRepository: AppLockRepository = mockk()
    val tested = CheckIfAppLockIsEnabledUseCase(appLockRepository)

    companion object {

        @JvmStatic
        fun parameters() =
            listOf(
                AppLockParameters(startSeconds = 499, endSeconds = 600, systemTimeMillis = 500000, expected = true),
                AppLockParameters(startSeconds = 500, endSeconds = 600, systemTimeMillis = 500000, expected = true),
                AppLockParameters(startSeconds = 501, endSeconds = 600, systemTimeMillis = 500000, expected = false),
                AppLockParameters(startSeconds = 500, endSeconds = 599, systemTimeMillis = 600000, expected = false),
                AppLockParameters(startSeconds = 500, endSeconds = 600, systemTimeMillis = 600000, expected = false),
                AppLockParameters(startSeconds = 500, endSeconds = 601, systemTimeMillis = 600000, expected = true),
            )
    }

    @BeforeAll
    fun setup() {
        mockkStatic(Calendar::class)
    }

    @AfterAll
    fun teardown() {
        unmockkAll()
    }

    @ParameterizedTest
    @MethodSource("parameters")
    fun `SHOULD check if app lock is enabled based on system time`(params: AppLockParameters) {
        val appLockConfigModel: AppLockConfigModel = mockk {
            every { startTimestamp } returns params.startSeconds
            every { endTimestamp } returns params.endSeconds
        }
        every { appLockRepository.getAppLockConfig() } returns Single.just(appLockConfigModel)
        every { Calendar.getInstance() } returns mockk {
            every { timeInMillis } returns params.systemTimeMillis
        }

        val result = tested.execute().test()

        result.assertValue(params.expected)
    }

    data class AppLockParameters(
        val startSeconds: Long,
        val endSeconds: Long,
        val systemTimeMillis: Long,
        val expected: Boolean,
    )
}
