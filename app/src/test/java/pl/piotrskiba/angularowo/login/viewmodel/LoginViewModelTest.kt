package pl.piotrskiba.angularowo.login.viewmodel

import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkAll
import io.mockk.verify
import io.reactivex.rxjava3.core.Single
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import pl.piotrskiba.angularowo.base.InstantTestExecutor
import pl.piotrskiba.angularowo.base.rx.TestSchedulersFacade
import pl.piotrskiba.angularowo.domain.login.model.AccessTokenError
import pl.piotrskiba.angularowo.domain.login.model.AccessTokenModel
import pl.piotrskiba.angularowo.domain.login.usecase.RegisterDeviceUseCase
import pl.piotrskiba.angularowo.login.model.LoginState
import pl.piotrskiba.angularowo.utils.AnalyticsUtils

@ExtendWith(InstantTestExecutor::class)
class LoginViewModelTest {

    val registerDeviceUseCase: RegisterDeviceUseCase = mockk()
    val schedulers = TestSchedulersFacade()
    val tested = LoginViewModel(registerDeviceUseCase, schedulers)

    @BeforeEach
    fun setup() {
        mockkObject(AnalyticsUtils)
    }

    @AfterEach
    fun teardown() {
        unmockkAll()
    }

    @Test
    fun `SHOULD set loginState to unknown WHEN created`() {
        tested.loginState.value shouldBeEqualTo LoginState.Unknown
    }

    @Test
    fun `SHOULD set loginState to loading WHEN onPinEntered called`() {
        every { registerDeviceUseCase.execute("123456") } returns Single.never()

        tested.onPinEntered("123456")

        tested.loginState.value shouldBeEqualTo LoginState.Loading
    }

    @Test
    fun `SHOULD set loginState to success WHEN onPinEntered called AND logging in succeeded`() {
        val accessTokenModel: AccessTokenModel = mockk(relaxed = true)
        every { registerDeviceUseCase.execute("123456") } returns Single.just(accessTokenModel)
        every { AnalyticsUtils.logLogin(any(), any()) } just Runs

        tested.onPinEntered("123456")

        tested.loginState.value shouldBeEqualTo LoginState.Success
    }

    @Test
    fun `SHOULD log login event WHEN onPinEntered called AND logging in succeeded`() {
        val accessTokenUuid = "uuid"
        val accessTokenUsername = "username"
        val accessTokenModel: AccessTokenModel = mockk {
            every { uuid } returns accessTokenUuid
            every { username } returns accessTokenUsername
        }
        every { registerDeviceUseCase.execute("123456") } returns Single.just(accessTokenModel)
        every { AnalyticsUtils.logLogin(accessTokenUuid, accessTokenUsername) } just Runs

        tested.onPinEntered("123456")

        verify { AnalyticsUtils.logLogin(accessTokenUuid, accessTokenUsername) }
    }

    @Test
    fun `SHOULD set loginState to error WHEN onPinEntered called AND logging in failed`() {
        val error = AccessTokenError.CodeNotFoundError
        every { registerDeviceUseCase.execute("123456") } returns Single.error(error)
        every { AnalyticsUtils.logLoginError(any()) } just Runs

        tested.onPinEntered("123456")

        tested.loginState.value shouldBeEqualTo LoginState.Error(error)
    }

    @Test
    fun `SHOULD log login error event WHEN onPinEntered called AND logging in failed`() {
        val error = AccessTokenError.CodeNotFoundError
        every { registerDeviceUseCase.execute("123456") } returns Single.error(error)
        every { AnalyticsUtils.logLoginError(error::class.simpleName) } just Runs

        tested.onPinEntered("123456")

        verify { AnalyticsUtils.logLoginError(error::class.simpleName) }
    }
}
