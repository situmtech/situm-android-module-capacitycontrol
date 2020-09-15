package com.situm.capacitycontroltestsuite.presentation.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.situm.capacitycontroltestsuite.data.base.Event
import com.situm.capacitycontroltestsuite.data.base.Result
import com.situm.capacitycontroltestsuite.model.User
import com.situm.capacitycontroltestsuite.model.usecase.FetchUserUseCase
import com.situm.capacitycontroltestsuite.model.usecase.LoginUseCase
import com.situm.capacitycontroltestsuite.model.usecase.SaveUserUseCase

class LoginViewModel(
    val loginUseCase: LoginUseCase,
    val saveUserUseCase: SaveUserUseCase,
    val fetchUserUseCase: FetchUserUseCase
) : ViewModel() {
    val username: MutableLiveData<String> = MutableLiveData()
    val password: MutableLiveData<String> = MutableLiveData()
    val dashboard: MutableLiveData<String> = MutableLiveData()

    val navigationEvent = MutableLiveData<Event<Boolean>>()

    init {
        fetchUserUseCase.invoke(FetchUserUseCase.Params()) { result ->
            when (result) {
                is Result.Response -> {
                    login(result.data)
                }
                is Result.Error -> {
                    dashboard.value = "https://dashboard.situm.es"
                }

            }
        }
    }

    fun btLoginClick() {
        //Check credential validity
        if (username.value.isNullOrEmpty()
            || password.value.isNullOrEmpty()
            || dashboard.value.isNullOrEmpty()
        ) {
            //TODO: Show error
            return
        }

        val user = User(
            username = username.value!!,
            password = password.value!!,
            dashboard = dashboard.value!!
        )

        login(user)
    }

    fun login(user: User) {

        //try login
        loginUseCase.invoke(LoginUseCase.Params(user)) { result ->
            when (result) {
                is Result.Response -> {
                    saveUserUseCase.invoke(SaveUserUseCase.Params(user)) { result ->
                        navigationEvent.postValue(Event(true))
                    }
                }
                is Result.Error -> {
                    //TODO: SHOW LOGIN ERROR
                }
            }
        }
    }
}