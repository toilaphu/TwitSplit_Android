package com.phunguyen.twitsplit.presenter

import android.content.Context
import com.phunguyen.twitsplit.view.MainView

interface MainViewPresenter {

    fun attachView(mainView: MainView)

    fun displayMessage(messages:List<String>)

    fun verifyMessage(context: Context, messages: String, limitCharacters: Int)

    fun detachView()

}