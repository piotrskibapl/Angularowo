package pl.piotrskiba.angularowo.data.punishment.di

import dagger.Module
import dagger.Provides
import pl.piotrskiba.angularowo.data.network.di.NetworkModule
import pl.piotrskiba.angularowo.data.punishment.PunishmentApiService
import pl.piotrskiba.angularowo.data.punishment.repository.PunishmentRepositoryImpl
import pl.piotrskiba.angularowo.domain.punishment.repository.PunishmentRepository
import pl.piotrskiba.angularowo.domain.punishment.usecase.GetActivePlayerPunishmentsUseCase
import retrofit2.Retrofit

@Module(includes = [NetworkModule::class])
class PunishmentModule {

    @Provides
    fun providePunishmentApiService(retrofit: Retrofit): PunishmentApiService =
        retrofit.create(PunishmentApiService::class.java)

    @Provides
    fun providePunishmentRepository(punishmentApiService: PunishmentApiService): PunishmentRepository =
        PunishmentRepositoryImpl(punishmentApiService)

    @Provides
    fun provideGetActivePlayerPunishmentsUseCase(punishmentRepository: PunishmentRepository) =
        GetActivePlayerPunishmentsUseCase(punishmentRepository)
}
