package com.situm.capacitycontroltestsuite.model.usecase

import com.situm.capacitycontroltestsuite.data.base.Result
import com.situm.capacitycontroltestsuite.data.base.UseCase
import com.situm.capacitycontroltestsuite.data.services.LoginService
import com.situm.capacitycontroltestsuite.model.User

class LoginUseCase(private val loginService: LoginService) : UseCase<Any, LoginUseCase.Params>() {

    class Params(val user: User)

    override suspend fun run(params: Params): Result<Any> {
        return run {
            loginService.runService(params.user)
        }
    }
}