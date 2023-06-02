package pl.piotrskiba.angularowo.domain.settings.usecase

import io.reactivex.rxjava3.core.Single
import pl.piotrskiba.angularowo.domain.base.preferences.repository.PreferencesRepository
import pl.piotrskiba.angularowo.domain.base.preferences.usecase.CheckIfIsStaffUserUseCase
import pl.piotrskiba.angularowo.domain.settings.model.SettingsInitialValuesModel
import javax.inject.Inject

class GetSettingsInitialValuesUseCase @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
    private val checkIfIsStaffUserUseCase: CheckIfIsStaffUserUseCase,
) {

    fun execute() =
        Single.zip(
            preferencesRepository.subscribedToFirebaseEventsTopic().defaultIfEmpty(false),
            preferencesRepository.subscribedToFirebasePrivateMessagesTopic().defaultIfEmpty(false),
            preferencesRepository.subscribedToFirebaseAccountIncidentsTopic().defaultIfEmpty(false),
            preferencesRepository.subscribedToFirebaseNewReportsTopic().defaultIfEmpty(false),
            checkIfIsStaffUserUseCase.execute(),
        ) { events, privateMessages, accountIncidents, newReports, isStaff ->
            SettingsInitialValuesModel(
                subscribedToEvents = events,
                subscribedToPrivateMessages = privateMessages,
                subscribedToAccountIncidents = accountIncidents,
                subscribedToNewReports = newReports,
                newReportsVisible = isStaff,
            )
        }
}
