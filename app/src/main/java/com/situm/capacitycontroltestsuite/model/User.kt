package com.situm.capacitycontroltestsuite.model

import java.io.Serializable

data class User(var username: String, var password: String, var dashboard: String) : Serializable