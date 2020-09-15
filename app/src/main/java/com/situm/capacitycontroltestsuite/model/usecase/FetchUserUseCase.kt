package com.situm.capacitycontroltestsuite.model.usecase

import com.situm.capacitycontroltestsuite.data.base.Result
import com.situm.capacitycontroltestsuite.data.base.UseCase
import com.situm.capacitycontroltestsuite.data.services.FetchUserService
import com.situm.capacitycontroltestsuite.model.User

class FetchUserUseCase(private val fetchUserService: FetchUserService) : UseCase<User, FetchUserUseCase.Params>() {

    class Params()

    override suspend fun run(params: Params): Result<User> {
        return run {
            fetchUserService.runService()
        }
    }

}