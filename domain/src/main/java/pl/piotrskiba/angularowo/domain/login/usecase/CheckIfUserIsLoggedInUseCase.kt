package pl.piotrskiba.angularowo.domain.login.usecase

import io.reactivex.rxjava3.core.Single
import pl.piotrskiba.angularowo.domain.base.preferences.repository.PreferencesRepository
import javax.inject.Inject

class CheckIfUserIsLoggedInUseCase @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
) {

    fun execute() =
        Single.zip(
            preferencesRepository.accessToken().toSingle(),
            preferencesRepository.uuid().toSingle(), // UUID was not required before version 3.3
        ) { _, _ -> true }
            .onErrorReturn { error ->
                if (error is NoSuchElementException) {
                    false
                } else {
                    throw error
                }
            }
}
