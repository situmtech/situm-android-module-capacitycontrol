package com.situm.capacitycontroltestsuite.model.usecase

import com.situm.capacitycontroltestsuite.data.base.Result
import com.situm.capacitycontroltestsuite.data.base.UseCase
import com.situm.capacitycontroltestsuite.data.services.SaveUserService
import com.situm.capacitycontroltestsuite.model.User

class SaveUserUseCase(private val saveUserService: SaveUserService): UseCase<User, SaveUserUseCase.Params>(){

    class Params(val user: User)

    override suspend fun run(params: Params): Result<User> {
        return run{
            saveUserService.runService(params.user)
        }
    }
}