package pl.piotrskiba.angularowo.data.login.di

import dagger.Module
import dagger.Provides
import pl.piotrskiba.angularowo.data.login.LoginApiService
import pl.piotrskiba.angularowo.data.login.mapper.AccessTokenMapper
import pl.piotrskiba.angularowo.data.login.repository.LoginRepositoryImpl
import pl.piotrskiba.angularowo.data.network.di.NetworkModule
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
        LoginRepositoryImpl(loginApiService, AccessTokenMapper())
}
