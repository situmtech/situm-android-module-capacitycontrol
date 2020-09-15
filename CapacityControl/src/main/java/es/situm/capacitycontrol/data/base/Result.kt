package es.situm.capacitycontrol.data.base

sealed class Result<out T : Any> {

    class Response<out T : Any>(val data: T) : Result<T>()

    class Error() : Result<Nothing>()
}