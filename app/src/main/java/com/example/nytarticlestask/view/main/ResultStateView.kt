package com.example.nytarticlestask.view.main



interface ResultStateView {

    fun changeState(state: State)
    sealed class State {
        object Loading : State()
        object NoData : State()
        class Results(val list: MutableList<com.example.nytarticlestask.network.model.Results>) : State()
        class Error(val exception: Throwable) : State()

    }
}