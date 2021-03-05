package pl.piotrskiba.angularowo.data.login.di

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import pl.piotrskiba.angularowo.data.base.preferences.repository.PreferencesRepositoryImpl
import pl.piotrskiba.angularowo.data.login.LoginApiService
import pl.piotrskiba.angularowo.data.login.repository.LoginRepositoryImpl
import pl.piotrskiba.angularowo.data.network.di.NetworkModule
import pl.piotrskiba.angularowo.domain.base.preferences.repository.PreferencesRepository
import pl.piotrskiba.angularowo.domain.login.repository.LoginRepository
import pl.piotrskiba.angularowo.domain.login.usecase.RegisterDeviceUseCase
import retrofit2.Retrofit

@Module(includes = [NetworkModule::class])
class LoginModule {

    @Provides
    fun provideLoginApiService(retrofit: Retrofit) =
        retrofit.create(LoginApiService::class.java)

    @Provides
    fun provideRegisterDeviceUseCase(loginRepository: LoginRepository) =
        RegisterDeviceUseCase(loginRepository)

    @Provides
    fun provideLoginRepository(loginApiService: LoginApiService): LoginRepository =
        LoginRepositoryImpl(loginApiService)

    @Provides
    fun providePreferencesRepository(sharedPreferences: SharedPreferences): PreferencesRepository =
        PreferencesRepositoryImpl(sharedPreferences)

    @Provides
    fun provideSharedPreferences(context: Context): SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)
}
