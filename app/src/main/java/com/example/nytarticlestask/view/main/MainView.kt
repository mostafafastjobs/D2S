package com.example.nytarticlestask.view.main

import androidx.appcompat.app.AppCompatActivity
import com.example.nytarticlestask.view.BaseActivityView
import com.example.nytarticlestask.view.main.listeners.OnArticlesListener


interface MainView: BaseActivityView,ResultStateView {

    fun setupViews(activity: AppCompatActivity)

    fun changeNetworkState(state: State)
    sealed class State {
        object Connected : State()
        object NoNetwork : State()
    }

    fun setArticlesListener(listener: OnArticlesListener)

}