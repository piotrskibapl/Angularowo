package pl.piotrskiba.angularowo.domain.settings.usecase

import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import org.junit.jupiter.api.Test
import pl.piotrskiba.angularowo.domain.base.preferences.repository.PreferencesRepository
import pl.piotrskiba.angularowo.domain.base.preferences.usecase.CheckIfIsStaffUserUseCase
import pl.piotrskiba.angularowo.domain.settings.model.SettingsInitialValuesModel

class GetSettingsInitialValuesUseCaseTest {

    val preferencesRepository: PreferencesRepository = mockk()
    val checkIfIsStaffUserUseCase: CheckIfIsStaffUserUseCase = mockk()
    val tested = GetSettingsInitialValuesUseCase(preferencesRepository, checkIfIsStaffUserUseCase)

    @Test
    fun `SHOULD get initial settings values`() {
        every { preferencesRepository.subscribedToFirebaseEventsTopic() } returns Maybe.just(true)
        every { preferencesRepository.subscribedToFirebasePrivateMessagesTopic() } returns Maybe.just(true)
        every { preferencesRepository.subscribedToFirebaseAccountIncidentsTopic() } returns Maybe.just(true)
        every { preferencesRepository.subscribedToFirebaseNewReportsTopic() } returns Maybe.just(true)
        every { checkIfIsStaffUserUseCase.execute() } returns Single.just(true)

        val result = tested.execute().test()

        result.assertValue(
            SettingsInitialValuesModel(
                subscribedToEvents = true,
                subscribedToPrivateMessages = true,
                subscribedToAccountIncidents = true,
                subscribedToNewReports = true,
                newReportsVisible = true,
            ),
        )
    }

    @Test
    fun `SHOULD get initial settings values WHEN they are empty`() {
        every { preferencesRepository.subscribedToFirebaseEventsTopic() } returns Maybe.empty()
        every { preferencesRepository.subscribedToFirebasePrivateMessagesTopic() } returns Maybe.empty()
        every { preferencesRepository.subscribedToFirebaseAccountIncidentsTopic() } returns Maybe.empty()
        every { preferencesRepository.subscribedToFirebaseNewReportsTopic() } returns Maybe.empty()
        every { checkIfIsStaffUserUseCase.execute() } returns Single.just(true)

        val result = tested.execute().test()

        result.assertValue(
            SettingsInitialValuesModel(
                subscribedToEvents = false,
                subscribedToPrivateMessages = false,
                subscribedToAccountIncidents = false,
                subscribedToNewReports = false,
                newReportsVisible = true,
            ),
        )
    }
}
