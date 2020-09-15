package com.situm.capacitycontroltestsuite.presentation

import com.situm.capacitycontroltestsuite.data.PreferenceProvider
import com.situm.capacitycontroltestsuite.data.services.FetchUserService
import com.situm.capacitycontroltestsuite.data.services.LoginService
import com.situm.capacitycontroltestsuite.data.services.LogoutService
import com.situm.capacitycontroltestsuite.data.services.SaveUserService
import com.situm.capacitycontroltestsuite.model.usecase.FetchUserUseCase
import com.situm.capacitycontroltestsuite.model.usecase.LoginUseCase
import com.situm.capacitycontroltestsuite.model.usecase.LogoutUseCase
import com.situm.capacitycontroltestsuite.model.usecase.SaveUserUseCase
import com.situm.capacitycontroltestsuite.presentation.login.LoginViewModel
import com.situm.capacitycontroltestsuite.presentation.main.DebugViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {

    //Providers
    factory { PreferenceProviderImpl(androidContext()) as PreferenceProvider }

    //Services
    single { LoginService() }
    single { SaveUserService(preferenceProvider = get()) }
    single { FetchUserService(preferenceProvider = get()) }
    single { LogoutService(preferenceProvider = get()) }

    //UseCase
    single { LoginUseCase(loginService = get()) }
    single { SaveUserUseCase(saveUserService = get()) }
    single { FetchUserUseCase(fetchUserService = get()) }
    single { LogoutUseCase(logoutService = get()) }

    //ViewModels
    viewModel { LoginViewModel(loginUseCase = get(), saveUserUseCase = get(), fetchUserUseCase = get()) }
    viewModel { DebugViewModel(logoutUseCase = get()) }
}
