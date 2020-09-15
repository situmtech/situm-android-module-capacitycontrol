package com.situm.capacitycontroltestsuite.model.usecase

import com.situm.capacitycontroltestsuite.data.base.Result
import com.situm.capacitycontroltestsuite.data.base.UseCase
import com.situm.capacitycontroltestsuite.data.services.LogoutService

class LogoutUseCase(private val logoutService: LogoutService) : UseCase<Boolean, LogoutUseCase.Params>() {
    class Params

    override suspend fun run(params: Params): Result<Boolean> {
        return run {
            logoutService.runService()
        }
    }
}