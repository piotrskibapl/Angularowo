package pl.piotrskiba.angularowo.applock.viewmodel

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.reactivex.rxjava3.core.Single
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import pl.piotrskiba.angularowo.applock.model.AppLockData
import pl.piotrskiba.angularowo.applock.model.toUi
import pl.piotrskiba.angularowo.base.InstantTestExecutor
import pl.piotrskiba.angularowo.base.model.ViewModelState.Loaded
import pl.piotrskiba.angularowo.base.model.ViewModelState.Loading
import pl.piotrskiba.angularowo.base.rx.TestSchedulersFacade
import pl.piotrskiba.angularowo.domain.applock.model.AppLockConfigModel
import pl.piotrskiba.angularowo.domain.applock.model.AppLockDataModel
import pl.piotrskiba.angularowo.domain.applock.usecase.GetAppLockDataUseCase

@ExtendWith(InstantTestExecutor::class)
class AppLockViewModelTest {

    val getAppLockDataUseCase: GetAppLockDataUseCase = mockk()
    val schedulers = TestSchedulersFacade()
    val tested = AppLockViewModel(getAppLockDataUseCase, schedulers)

    @BeforeEach
    fun setup() {
        mockkStatic(AppLockConfigModel::toUi)
    }

    @AfterEach
    fun teardown() {
        unmockkAll()
    }

    @Test
    fun `SHOULD set state to loading WHEN created`() {
        tested.state.value shouldBeEqualTo Loading.Fetch
    }

    @Test
    fun `SHOULD update appLockData WHEN onFirstCreate called AND data loaded`() {
        val appLockData: AppLockData = mockk()
        val appLockDataModel: AppLockDataModel = mockk {
            every { config.toUi() } returns appLockData
        }
        every { getAppLockDataUseCase.execute() } returns Single.just(appLockDataModel)

        tested.onFirstCreate()

        tested.appLockData.value shouldBeEqualTo appLockData
    }

    @Test
    fun `SHOULD set state to loaded WHEN onFirstCreate called AND data loaded`() {
        val appLockDataModel: AppLockDataModel = mockk(relaxed = true)
        every { getAppLockDataUseCase.execute() } returns Single.just(appLockDataModel)

        tested.onFirstCreate()

        tested.state.value shouldBeEqualTo Loaded
    }
}
