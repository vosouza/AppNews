package com.evosouza.news.core

data class State<out T>(
    val status: Status,
    val loading: Boolean?,
    val data: T?,
    val error: Throwable?
) {
    companion object {
        fun <T> success(data: T?): State<T>{
            return State(Status.SUCCESS, false, data, null)
        }

        fun <T> error(error: Throwable?): State<T>{
            return State(Status.ERROR, false, null, error)
        }

        fun <T> loading(loading: Boolean?): State<T>{
            return State(Status.LOADING, true, null, null)
        }
    }
}